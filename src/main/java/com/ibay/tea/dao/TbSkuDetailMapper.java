package com.ibay.tea.dao;

import com.ibay.tea.entity.TbSkuDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbSkuDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbSkuDetail record);

    TbSkuDetail selectByPrimaryKey(Integer id);

    List<TbSkuDetail> findAll();
}