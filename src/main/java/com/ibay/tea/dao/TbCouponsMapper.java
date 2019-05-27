package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCoupons;

public interface TbCouponsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbCoupons record);

    int insertSelective(TbCoupons record);

    TbCoupons selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbCoupons record);

    int updateByPrimaryKey(TbCoupons record);
}