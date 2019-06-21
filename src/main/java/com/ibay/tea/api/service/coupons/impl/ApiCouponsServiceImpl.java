package com.ibay.tea.api.service.coupons.impl;

import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.dao.TbUserCouponsMapper;
import com.ibay.tea.dao.UserCouponsMapper;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbUserCoupons;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ApiCouponsServiceImpl implements ApiCouponsService {

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Override
    public TbUserCoupons findCouponsByCondition(Map<String, Object> condition) {
        return tbUserCouponsMapper.findCouponsByCondition(condition);
    }

    @Override
    public TbUserCoupons findOneCouponsByOppenId(String oppenId) {
        return tbUserCouponsMapper.findOneCouponsByOppenId(oppenId);
    }

    @Override
    public List<TbUserCoupons> findUserValidCoupons(String oppenId) {
        return tbUserCouponsMapper.findUserValidCoupons(oppenId);
    }
}
