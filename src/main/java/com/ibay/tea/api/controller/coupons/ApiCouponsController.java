package com.ibay.tea.api.controller.coupons;

import com.ibay.tea.api.service.coupons.ApiCouponsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/coupons")
public class ApiCouponsController {

    @Resource
    private ApiCouponsService apiCouponsService;


}
