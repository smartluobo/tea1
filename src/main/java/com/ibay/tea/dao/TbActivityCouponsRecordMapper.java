package com.ibay.tea.dao;

import com.ibay.tea.entity.TbActivityCouponsRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TbActivityCouponsRecordMapper {
    int deleteByPrimaryKey(Integer id);

    TbActivityCouponsRecord selectByPrimaryKey(Integer id);

    List<TbActivityCouponsRecord> findAll();

    List<TbActivityCouponsRecord> getJackpotInfo(int activityId);
}