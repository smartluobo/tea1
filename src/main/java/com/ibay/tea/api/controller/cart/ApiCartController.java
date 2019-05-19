package com.ibay.tea.api.controller.cart;

import com.ibay.tea.api.service.cart.ApiCartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/cart")
public class ApiCartController {

    @Resource
    private ApiCartService apiCartService;
}
