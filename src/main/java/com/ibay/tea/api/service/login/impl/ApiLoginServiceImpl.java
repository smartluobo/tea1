package com.ibay.tea.api.service.login.impl;

import com.ibay.tea.api.service.login.ApiLoginService;
import com.ibay.tea.dao.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiLoginServiceImpl implements ApiLoginService {

    @Resource
    private UserMapper userMapper;
}
