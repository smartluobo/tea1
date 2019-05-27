package com.ibay.tea.api.service.activity.impl;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.dao.TbActivityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiActivityServiceImpl implements ApiActivityService {

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Override
    public Object getActivityInfo(String oppenId) {

        return null;
    }
}
