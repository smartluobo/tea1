package com.ibay.tea.dao;

import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbUserCoupons;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface TbUserCouponsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TbUserCoupons record);

    TbUserCoupons selectByPrimaryKey(Integer id);

    TbUserCoupons findCouponsByCondition(Map<String, Object> condition);

    TbUserCoupons findOneCouponsByOppenId(String oppenId);
}