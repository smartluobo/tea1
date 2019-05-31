package com.ibay.tea.api.service.order.impl;

import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.dao.TbCartMapper;
import com.ibay.tea.entity.TbApiUserAddress;
import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbOrder;
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

    @Resource
    private ApiAddressService apiAddressService;

    @Override
    public void createOrderByCart(String oppenId, String cartItemIds, Long couponsId, int addressId, int selfGet) {

        //创建订单成功自动调起支付接口支付
        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (userAddress == null){
            return ;
        }

        if (cartItemIds != null && cartItemIds.trim().length() > 0){
            //订单总商品数
            int totalGoodsCount = 0;
            //订单总价格
            double orderTotalPrice = 0.0;

            String[] cartItemIdArr = cartItemIds.split(",");
            List<TbCart> cartItemByIds = tbCartMapper.findCartItemByIds(Arrays.asList(cartItemIdArr));
            List<TbItem> goodsList = new ArrayList<>();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem);
                orderTotalPrice += tbItem.getCartPrice();
                goodsList.add(tbItem);
            }
            //商品信息组装完后创建订单，创建订单明细，生成支付记录

            TbOrder tbOrder = new TbOrder();

        }


    }

    @Override
    public void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds, Long couponsId, int addressId, int selfGet) {

    }

    @Override
    public boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, Long couponsId, int addressId, int selfGet) {
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && addressId == 0){
            return false;
        }
        if (goodsId == 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCartOrderParameter(String oppenId, String cartItemIds, Long couponsId, int addressId, int selfGet) {
        if (cartItemIds == null || cartItemIds.trim().length() == 0){
            return false;
        }
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && addressId == 0){
            return false;
        }
        return true;
    }
}
