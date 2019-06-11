package com.ibay.tea.api.service.user.impl;

import com.ibay.tea.api.service.user.ApiUserService;
import com.ibay.tea.dao.TbApiUserMapper;
import com.ibay.tea.dao.UserMapper;
import com.ibay.tea.entity.TbApiUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ApiUserServiceImpl implements ApiUserService{

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Override
    public TbApiUser findApiUserByOppenId(String oppenId) {
        return tbApiUserMapper.findApiUserByOppenId(oppenId);
    }

    @Override
    public void saveApiUser(String oppenId) {
        TbApiUser apiUserByOppenId = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUserByOppenId != null){
            return;
        }
        TbApiUser tbApiUser = new TbApiUser();
        tbApiUser.setOppenId(oppenId);
        tbApiUser.setCreateTime(new Date());
        tbApiUser.setUpdateTime(new Date());
        tbApiUserMapper.insert(tbApiUser);
    }
}
