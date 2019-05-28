package com.ibay.tea.cache;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.dao.TbActivityCouponsRecordMapper;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.dao.TbCouponsMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TodayActivityBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityCache implements InitializingBean{

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private TbActivityCouponsRecordMapper tbActivityCouponsRecordMapper;

    private List<TbActivity> activityListCache;

    private TodayActivityBean todayActivityBean;

    private List<TbCoupons> couponsListCache;
    //通用券
    private TbCoupons generalCoupons;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordsCache;



    private void initActivityCache(){
        activityListCache = tbActivityMapper.findAll();
        couponsListCache = tbCouponsMapper.findAll();
        tbActivityCouponsRecordsCache = tbActivityCouponsRecordMapper.findAll();
        initGeneralCoupons();
    }

    private void initGeneralCoupons() {
        for (TbCoupons tbCoupons : couponsListCache) {
            if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_GENERAL){
                generalCoupons = tbCoupons;
                break;
            }
        }
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

    public void buildTodayActivityInfoBean(){

        TbActivity activityInfo = getActivityInfo();
        if (activityInfo != null){
            todayActivityBean = new TodayActivityBean();
            todayActivityBean.setTbActivity(activityInfo);
            setTodayActivityCouponsRecordList(todayActivityBean,activityInfo);
            setTodayActivityCouponsMap(todayActivityBean);

        }
    }

    private void setTodayActivityCouponsMap(TodayActivityBean todayActivityBean) {
        Map<Integer,TbCoupons> tbCouponsMap = new HashMap<>();
        List<TbActivityCouponsRecord> tbActivityCouponsRecordList = todayActivityBean.getTbActivityCouponsRecordList();
        for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordList) {
            for (TbCoupons tbCoupons : couponsListCache) {
                if (tbActivityCouponsRecord.getCouponsId() == tbCoupons.getId()){
                    tbCouponsMap.put(tbCoupons.getId(),tbCoupons);
                }
            }
        }
    }

    /**
     * 组装今日活动信息，将今日活动下关联的优惠券放入今日活动下
     * @param todayActivityBean 今日活动信息
     * @param activityInfo 活动描述
     */
    private void setTodayActivityCouponsRecordList(TodayActivityBean todayActivityBean, TbActivity activityInfo) {
        List<TbActivityCouponsRecord> tbActivityCouponsRecordList = new ArrayList<>();
        for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordsCache) {
            if (tbActivityCouponsRecord.getActivityId() == activityInfo.getId()){
                tbActivityCouponsRecordList.add(tbActivityCouponsRecord);
            }
        }
        todayActivityBean.setTbActivityCouponsRecordList(tbActivityCouponsRecordList);
    }

    /**
     * 获取今日抽奖活动信息
     * @return 返回今日活动信息
     */
    public TodayActivityBean getTodayActivityBean() {
        return todayActivityBean;
    }

    public TbCoupons getGeneralCoupons() {
        return generalCoupons;
    }
}
