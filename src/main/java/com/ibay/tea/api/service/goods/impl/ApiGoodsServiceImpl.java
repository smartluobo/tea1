package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.dao.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiGoodsServiceImpl implements ApiGoodsService {

    @Resource
    private GoodsMapper goodsMapper;
}
