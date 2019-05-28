package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCoupons;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public interface TbCouponsMapper {

    int deleteByPrimaryKey(Integer id);

    TbCoupons selectByPrimaryKey(Integer id);

    List<TbCoupons> findAll();
}