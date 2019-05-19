package com.ibay.tea.dao;

import com.ibay.tea.entity.Goods;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    Goods selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Goods record);
}