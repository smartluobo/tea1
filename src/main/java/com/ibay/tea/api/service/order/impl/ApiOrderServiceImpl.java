package com.ibay.tea.api.service.order.impl;

import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.dao.TbCartMapper;
import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiOrderServiceImpl implements ApiOrderService {

    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private ApiCartService apiCartService;

    @Override
    public void createOrderByCart(String oppenId, String cartItemIds, Long couponsId, Long addressId, int selfGet) {
        //创建订单成功自动调起支付接口支付
        if (cartItemIds != null && cartItemIds.trim().length() > 0){
            String[] cartItemIdArr = cartItemIds.split(",");
            List<TbCart> cartItemByIds = tbCartMapper.findCartItemByIds(Arrays.asList(cartItemIdArr));
            List<TbItem> goodsList = new ArrayList<>();
            for (TbCart cartItem : cartItemByIds) {
                goodsList.add(apiCartService.buildCartGoodsInfo(cartItem));
            }
        }
    }

    @Override
    public void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds, Long couponsId, Long addressId, int selfGet) {

    }
}
