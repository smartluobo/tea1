package com.ibay.tea.api.service.user.impl;

import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.UserMapper;
import com.ibay.tea.entity.TbApiUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Override
    public TbApiUser findApiUserByOppenId(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }
}
