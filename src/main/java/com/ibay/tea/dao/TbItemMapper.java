package com.ibay.tea.dao;

import com.ibay.tea.entity.TbItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbItemMapper {
    int deleteByPrimaryKey(Long id);

    TbItem selectByPrimaryKey(Long id);

    List<TbItem> getGoodsListByCategoryId(long categoryId);

    List<TbItem> findAll();

}