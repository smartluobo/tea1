package com.ibay.tea.api.service.activity.impl;

import com.ibay.tea.api.service.activity.ApiActivityService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.dao.TbActivityMapper;
import com.ibay.tea.entity.TbActivity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiActivityServiceImpl implements ApiActivityService {

    @Resource
    private TbActivityMapper tbActivityMapper;

    @Resource
    private ActivityCache activityCache;

    @Override
    public TbActivity getActivityInfo() {
        TbActivity activity = activityCache.getActivityInfo();
        return activity;
    }
}
