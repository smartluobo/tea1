package com.ibay.tea.api.service.user.impl;

import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.dao.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    @Resource
    private UserMapper userMapper;
}
