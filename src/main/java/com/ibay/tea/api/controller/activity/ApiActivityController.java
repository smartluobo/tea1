package com.ibay.tea.api.controller.activity;

import com.ibay.tea.api.service.activity.ApiActivityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/activity")
public class ApiActivityController {

    @Resource
    private ApiActivityService apiActivityService;

    //查询活动，如果当前时间没有活动在进行中查询最近的活动开始时间，
    // 如果在开奖时间内且还有奖品提示用户参与抽奖，若奖品已经发放完毕且用户没有获奖提示用户明天继续参与
    // 若用户已经在当天参与抽奖并获得优惠券提示用户立即使用

    @GetMapping("getActivityInfo")
    public Object getActivityInfo(@PathVariable("oppenId") String oppenId){
        try {
            apiActivityService.getActivityInfo(oppenId);
            return null;
        }catch (Exception e){
            return null;
        }
    }

}
