package com.ibay.tea.dao;

import com.ibay.tea.entity.TbSkuType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbSkuTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbSkuType record);

    TbSkuType selectByPrimaryKey(Integer id);

    List<TbSkuType> findAll();
}