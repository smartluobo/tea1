package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivityCouponsRecord;

public interface TbActivityCouponsRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbActivityCouponsRecord record);

    int insertSelective(TbActivityCouponsRecord record);

    TbActivityCouponsRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbActivityCouponsRecord record);

    int updateByPrimaryKey(TbActivityCouponsRecord record);
}