package com.ibay.tea.dao;

import com.ibay.tea.entity.TbItemCat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbItemCatMapper {
    int deleteByPrimaryKey(Long id);

    TbItemCat selectByPrimaryKey(Long id);

    List<TbItemCat> findAll();
}