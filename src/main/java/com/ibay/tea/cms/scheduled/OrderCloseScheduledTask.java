package com.ibay.tea.cms.scheduled;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class OrderCloseScheduledTask {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void orderCloseTaskMethod(){
        //每三十分钟执行一次定是任务扫描订单表 并关闭超过半小时未支付的订单
        System.out.println("我是定是任务");
    }
}
