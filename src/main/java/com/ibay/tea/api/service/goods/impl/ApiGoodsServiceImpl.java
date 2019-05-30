package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.TbItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ApiGoodsServiceImpl implements ApiGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private GoodsCache goodsCache;

    @Override
    public List<TbItem> getGoodsListByCategoryId(long categoryId) {
        return goodsCache.getGoodsListByCategoryId(categoryId);
    }

    @Override
    public TbItem getGoodsDetailById(long goodsId) {
        TbItem goods = goodsCache.findGoodsById(goodsId);
        //组装sku相关信息
        return goods;
    }


}
