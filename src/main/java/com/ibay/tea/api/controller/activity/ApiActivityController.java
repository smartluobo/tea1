package com.ibay.tea.api.controller.activity;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbCoupons;
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

    //查询活动，如果当前时间没有活动在进行中查询最近的活动开始时间，点击查看活动奖品
    // 如果在开奖时间内且还有奖品提示用户参与抽奖，若奖品已经发放完毕且用户没有获奖提示用户明天继续参与
    // 若用户已经在当天参与抽奖并获得优惠券提示用户立即使用

    @GetMapping("getActivityInfo")
    public Object getActivityInfo(String oppenId){
        try {
            Map<String,Object> resultInfo = new HashMap<>();
            resultInfo.put("code",200);

            TbActivity activityInfo = apiActivityService.getActivityInfo();
            if (activityInfo.getStatus() == ApiConstant.ACTIVITY_STATUS_NOT_START){
                resultInfo.put("type",1);
                resultInfo.put("data",activityInfo);
                return resultInfo;
            }
            if (activityInfo.getStatus() == ApiConstant.ACTIVITY_STATUS_STARTING){
                //活动正在进行中，查询用户是否有领取过奖品，如果有返回优惠券信息，如果优惠券用户已经使用提示用户明天继续参加抽奖
                // 没有优惠券让用户参与抢优惠券
                Map<String,Object> condition = new HashMap<>();
                condition.put("oppenId",oppenId);
                condition.put("receiveDate", DateUtil.getDateYyyyMMdd());
                TbUserCoupons tbUserCoupons = apiCouponsService.findCouponsByCondition(condition);
                if (tbUserCoupons == null){
                    resultInfo.put("type",2);
                    resultInfo.put("data",activityInfo);
                    return resultInfo;
                }
                if (tbUserCoupons.getStatus() == ApiConstant.USER_COUPONS_STATUS_NO_USE){
                    resultInfo.put("type",3);
                    resultInfo.put("data",tbUserCoupons);
                    return resultInfo;
                }else {
                    resultInfo.put("type",4);
                }
            }
            if (activityInfo.getStatus() == ApiConstant.ACTIVITY_STATUS_END){
                //活动已经结束，如果有优惠券返回优惠券 没有提示用户明天继续参与抽奖
                TbUserCoupons tbUserCoupons = apiCouponsService.findOneCouponsByOppenId(oppenId);
                if (tbUserCoupons == null){
                    resultInfo.put("type",4);
                    resultInfo.put("data",tbUserCoupons);
                }
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

}
