package com.ibay.tea.cms.service.user.impl;

import com.ibay.tea.cms.service.user.CmsUserService;
import com.ibay.tea.dao.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CmsUserServiceImpl implements CmsUserService{

    @Resource
    private UserMapper userMapper;
}
