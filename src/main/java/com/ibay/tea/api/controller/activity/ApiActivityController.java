package com.ibay.tea.api.controller.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbApiUser;
import com.ibay.tea.entity.TbUserCoupons;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/activity")
public class ApiActivityController {

    @Resource
    private ApiActivityService apiActivityService;

    @Resource
    private ApiCouponsService apiCouponsService;

    @Resource
    private ApiUserService apiUserService;

    //查询活动，如果当前时间没有活动在进行中查询最近的活动开始时间，点击查看活动奖品
    // 如果在开奖时间内且还有奖品提示用户参与抽奖，若奖品已经发放完毕且用户没有获奖提示用户明天继续参与
    // 若用户已经在当天参与抽奖并获得优惠券提示用户立即使用

    @GetMapping("getActivityInfo/{oppenId}")
    public ResultInfo getActivityInfo(@PathVariable("oppenId") String oppenId){
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            Map<String,Object> result = new HashMap<>();
            TbActivity activityInfo = apiActivityService.getTodayActivity();
            if (activityInfo == null){
                return ResultInfo.newEmptyResultInfo();
            }
            int activityStatus = apiActivityService.checkActivityStatus(activityInfo);
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_NOT_START){
                result.put("type",1);
                result.put("data",activityInfo);
                resultInfo.setData(result);
                return resultInfo;
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_STARTING){
                //活动正在进行中，查询用户是否有领取过奖品，如果有返回优惠券信息，如果优惠券用户已经使用提示用户明天继续参加抽奖
                // 没有优惠券让用户参与抢优惠券
                Map<String,Object> condition = new HashMap<>();
                condition.put("oppenId",oppenId);
                condition.put("receiveDate", DateUtil.getDateYyyyMMdd());
                TbUserCoupons tbUserCoupons = apiCouponsService.findCouponsByCondition(condition);
                if (tbUserCoupons == null){
                    result.put("type",2);
                    result.put("data",activityInfo);
                    resultInfo.setData(result);
                    return resultInfo;
                }
                if (tbUserCoupons.getStatus() == ApiConstant.USER_COUPONS_STATUS_NO_USE){
                    result.put("type",3);
                    result.put("data",tbUserCoupons);
                    resultInfo.setData(result);
                    return resultInfo;
                }else {
                    result.put("type",4);
                    resultInfo.setData(result);
                }
            }
            if (activityStatus == ApiConstant.ACTIVITY_STATUS_END){
                //活动已经结束，如果有优惠券返回优惠券 没有提示用户明天继续参与抽奖
                TbUserCoupons tbUserCoupons = apiCouponsService.findOneCouponsByOppenId(oppenId);
                if (tbUserCoupons == null){
                    result.put("type",4);
                    result.put("data",tbUserCoupons);
                    resultInfo.setData(result);
                    return resultInfo;
                }
            }
            return resultInfo;
        }catch (Exception e){
            return resultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/extractPrize/{oppenId}")
    public ResultInfo extractPrize(@PathVariable("oppenId") String oppenId){
        //判断oppenId是否有效
        ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        try {
            TbApiUser tbApiUser = apiUserService.findApiUserByOppenId(oppenId);
            if (tbApiUser == null){
                return ResultInfo.newEmptyResultInfo();
            }
            //判断通过执行抽奖过程
            TbActivityCouponsRecord record = apiActivityService.extractPrize(oppenId);
            if (record != null){
                //将用户的优惠券存入数据库
                TbUserCoupons tbUserCoupons = apiActivityService.buildUserCoupons(oppenId,record);
                apiActivityService.saveUserCouponsToDb(tbUserCoupons);
                resultInfo.setData(tbUserCoupons);
                return resultInfo;
            }
            return ResultInfo.newEmptyResultInfo();
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
