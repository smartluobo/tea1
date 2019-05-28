package com.ibay.tea.cms.service.goods.impl;

import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.entity.Goods;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CmsGoodsServiceImpl implements CmsGoodsService {

    private GoodsMapper goodsMapper;
    @Override
    public List<Goods> findGoodsListByPage(Integer pageNum, Integer pageSize) {
        return goodsMapper.findGoodsListByPage(pageNum,pageSize);
    }

    @Override
    public long countGoodsByCondition(Map<String,Object> condition){
        return goodsMapper.countGoodsByCondition(condition);
    }
}
