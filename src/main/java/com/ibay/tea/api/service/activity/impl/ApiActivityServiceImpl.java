package com.ibay.tea.api.service.activity.impl;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ApiActivityServiceImpl implements ApiActivityService {

    private static final String LOCK_STRING = "LOCK_STRING";

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Override
    public TbActivity getTodayActivity(int storeId) {
        TbActivity activity = activityCache.getTodayActivity(storeId);
        if (activity != null){
            return activity.copy();
        }
        return null;
    }

    @Override
    public TbActivityCouponsRecord extractPrize(String oppenId,int storeId) {
        TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(storeId);
        TbActivityCouponsRecord record = null;
        if (todayActivityBean == null || todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
            return null;
        }
        List<TbActivityCouponsRecord> tbActivityCouponsRecordList = todayActivityBean.getTbActivityCouponsRecordList();
        synchronized (LOCK_STRING){
            for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordList) {
                if (tbActivityCouponsRecord.getCouponsCount() > 0){
                    tbActivityCouponsRecord.setCouponsCount(tbActivityCouponsRecord.getCouponsCount()-1);
                    record = tbActivityCouponsRecord;
                    break;
                }
            }
        }
        if (record != null){
            //表示已经抽取到优惠券
            return record;
        }else{
            //查看是否定义了通用券，如果又直接返回通用券
            TbCoupons generalCoupons = activityCache.getGeneralCoupons();
            if (generalCoupons != null){
                return buildRecord(todayActivityBean.getTbActivity(),generalCoupons);
            }
        }
        return null;
    }

    @Override
    public void saveUserCouponsToDb(TbUserCoupons tbUserCoupons) {
        tbUserCouponsMapper.insert(tbUserCoupons);
    }

    @Override
    public TbUserCoupons buildUserCoupons(String oppenId, TbActivityCouponsRecord record) {
        TbUserCoupons tbUserCoupons = new TbUserCoupons();
        tbUserCoupons.setOppenId(oppenId);
        tbUserCoupons.setCouponsId(record.getCouponsId());
        tbUserCoupons.setCouponsName(record.getCouponsName());
        tbUserCoupons.setCreateTime(new Date());
        tbUserCoupons.setReceiveDate(Integer.valueOf(DateUtil.getDateYyyyMMdd()));
        tbUserCoupons.setStatus(0);
        return tbUserCoupons;
    }

    private TbActivityCouponsRecord buildRecord(TbActivity tbActivity, TbCoupons generalCoupons) {

        TbActivityCouponsRecord record = new TbActivityCouponsRecord();
        record.setActivityId(tbActivity.getId());
        record.setActivityName(tbActivity.getActivityName());
        record.setCouponsId(generalCoupons.getId());
        record.setCouponsName(generalCoupons.getCouponsName());
        return record;
    }

    @Override
    public int checkActivityStatus(TbActivity tbActivity) {
        int hour = DateUtil.getHour();
        if (tbActivity.getStartHour() > hour){
            //活动未开始
            return ApiConstant.ACTIVITY_STATUS_NOT_START;
        }
        if (tbActivity.getStartHour() < hour && tbActivity.getEndHour() > hour){
            //活动正在进行中
            return ApiConstant.ACTIVITY_STATUS_STARTING;
        }

        return ApiConstant.ACTIVITY_STATUS_END;
    }
}
