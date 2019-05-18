package com.ibay.tea.dao;

import com.ibay.tea.entity.GoodsSku;

public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSku record);

    GoodsSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(GoodsSku record);
}