package com.ibay.tea.api.service.coupons.impl;

import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.dao.UserCouponsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiCouponsServiceImpl implements ApiCouponsService {

    @Resource
    private UserCouponsMapper userCouponsMapper;
}
