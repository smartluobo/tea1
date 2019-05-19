package com.ibay.tea.api.controller.order;

import com.ibay.tea.api.service.order.ApiOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/order")
public class ApiOrderController {

    @Resource
    private ApiOrderService apiOrderService;
}
