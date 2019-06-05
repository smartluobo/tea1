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

    //当日活动
    private TbActivity todayActivity;

    private TbActivity todayFullActivity;


    //今日活动对象 包含了活动关联的优惠券信息
    private TodayActivityBean todayActivityBean;

    private List<TbCoupons> couponsListCache;

    //通用券，当用户参与活动为抢到优惠券时返回通用优惠券
    private TbCoupons generalCoupons;

    private List<TbActivityCouponsRecord> tbActivityCouponsRecordsCache;



    private void initActivityCache(){
        //如果有及节日活动优先选择当天活动为节假日活动
        todayActivity = tbActivityMapper.findHolidayActivity(DateUtil.getDateYyyyMMdd());
        if(todayActivity == null){
            //没有节假日活动查询常规活动
            todayActivity = tbActivityMapper.findRegularActivity(DateUtil.getDateYyyyMMdd());
        }
        couponsListCache = tbCouponsMapper.findAll();
        tbActivityCouponsRecordsCache = tbActivityCouponsRecordMapper.findAll();
        if (todayActivity != null){
            buildTodayActivityInfoBean();
        }

        initGeneralCoupons();
        initTodayFullActivity();
    }

    private void initTodayFullActivity(){
        todayFullActivity = tbActivityMapper.findFullActivity(DateUtil.getDateYyyyMMdd());
    }

    /**
     * 从缓存中获取今日的全场活动，初始化商品缓存的说好需要使用。因此初始化商品缓存需要在改任务之后执行
     * @return 返回全场折扣活动
     */
    public TbActivity getTodayFullActivity() {
        return todayFullActivity;
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

    public TbActivity getTodayActivity() {
        return todayActivity;
    }

    public void buildTodayActivityInfoBean(){

        if (todayActivity != null){
            todayActivityBean = new TodayActivityBean();
            todayActivityBean.setTbActivity(todayActivity);
            setTodayActivityCouponsRecordList(todayActivityBean,todayActivity);
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
        List<TbActivityCouponsRecord> currentActivityCouponsRecords = getActivityCouponsRecordsByActivityId(activityInfo.getId());
        todayActivityBean.setTbActivityCouponsRecordList(currentActivityCouponsRecords);
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

    /**
     * 通过活动id 查询该活动下绑定的所有优惠券
     * @param activityId 活动id
     * @return 返回活动绑定的优惠券
     */
    public List<TbActivityCouponsRecord> getActivityCouponsRecordsByActivityId(long activityId){
        List<TbActivityCouponsRecord> result = new ArrayList<>();
        for (TbActivityCouponsRecord tbActivityCouponsRecord : tbActivityCouponsRecordsCache) {
            if (tbActivityCouponsRecord.getActivityId() == activityId){
                result.add(tbActivityCouponsRecord);
            }
        }
        return result;
    }

    public TbCoupons getTbCouponsById(long couponsId){
        for (TbCoupons tbCoupons : couponsListCache) {
            if (tbCoupons.getId() == couponsId){
                return tbCoupons;
            }
        }
        return null;
    }
}
