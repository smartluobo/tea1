package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
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

    @Override
    public List<TbItem> getGoodsListByCategoryId(long categoryId) {
        return tbItemMapper.getGoodsListByCategoryId(categoryId);
    }
}
