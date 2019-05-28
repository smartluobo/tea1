package com.ibay.tea.cache;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityCouponsRecordMapper;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbCoupons;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityCache implements InitializingBean{

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbActivityCouponsRecordMapper tbActivityCouponsRecordMapper;

    private List<TbActivity> activityListCache;

    private List<TbCoupons> couponsListCache;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordsCache;

    private void initActivityCache(){
        activityListCache = tbActivityMapper.findAll();
        couponsListCache = tbCouponsMapper.findAll();
        tbActivityCouponsRecordsCache = tbActivityCouponsRecordMapper.findAll();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initActivityCache();
    }

    public TbActivity getActivityInfo() {
        TbActivity tbActivity = null;
        String dateYyyyMMdd = DateUtil.getDateYyyyMMdd();
        for (TbActivity activity : activityListCache) {
            if (activity.getStartDate() >= Integer.valueOf(dateYyyyMMdd) && activity.getEndDate() <= Integer.valueOf(dateYyyyMMdd)){
                if (activity.getActivityType() == ApiConstant.ACTIVITY_TYPE_HOLIDAY){
                    //如果存在节假日活动优先返回节假日活动
                    tbActivity = activity;
                    break;
                }
                if (activity.getActivityType() == ApiConstant.ACTIVITY_TYPE_NORMAL){
                    tbActivity = activity;
                }

            }
        }
        if (tbActivity != null){
            //如果有活动判断活动是否开始
            int hour = DateUtil.getHour();
            if (tbActivity.getStartHour() < hour){
                //活动未开始
                tbActivity.setStatus(ApiConstant.ACTIVITY_STATUS_NOT_START);
            }
            if (tbActivity.getStartHour() < hour && tbActivity.getEndHour() > hour){
                //活动正在进行中
                tbActivity.setStatus(ApiConstant.ACTIVITY_STATUS_STARTING);
            }
            if (tbActivity.getEndHour() < hour){
                tbActivity.setStatus(ApiConstant.ACTIVITY_STATUS_END);
            }
        }
        return tbActivity;
    }
}
