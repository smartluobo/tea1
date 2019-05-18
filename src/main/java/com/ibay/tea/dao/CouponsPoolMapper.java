package com.ibay.tea.dao;

import com.ibay.tea.entity.CouponsPool;

public interface CouponsPoolMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponsPool record);

    CouponsPool selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(CouponsPool record);
}