package com.ibay.tea.api.service.order.impl;

import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ApiOrderServiceImpl implements ApiOrderService {

    @Resource
    private TbCartMapper tbCartMapper;

    @Resource
    private TbOrderMapper tbOrderMapper;

    @Resource
    private TbOrderItemMapper tbOrderItemMapper;

    @Resource
    private TbUserCouponsMapper tbUserCouponsMapper;

    @Resource
    private TbUserPayRecordMapper tbUserPayRecordMapper;

    @Resource
    private ApiCartService apiCartService;

    @Resource
    private ApiAddressService apiAddressService;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private ApiPayService apiPayService;

    @Override
    public void createOrderByCart(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet,TbStore tbStore) throws Exception{
        TbUserCoupons tbUserCoupons = null;
        int sendPrice = 0;
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }
        //创建订单成功自动调起支付接口支付
        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return;
        }
        if (userCouponsId != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(oppenId,userCouponsId);
        }

        if (cartItemIds != null && cartItemIds.trim().length() > 0){
            //订单总商品数
            int totalGoodsCount = 0;
            //订单总价格
            double orderTotalPrice = 0.0;

            double maxPriceValue = 0.0;

            double minPriceValue = 100.0;

            double couponsReduceAmount = 0.0;

            String[] cartItemIdArr = cartItemIds.split(",");
            List<TbCart> cartItemByIds = tbCartMapper.findCartItemByIds(Arrays.asList(cartItemIdArr));
            List<TbItem> goodsList = new ArrayList<>();
            List<TbOrderItem> tbOrderItems = new ArrayList<>();
            String orderId = SerialGenerator.getOrderSerial();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem);
                tbItem = tbItem.copy();
                if (maxPriceValue < tbItem.getCartPrice()){
                    maxPriceValue = tbItem.getCartPrice();
                }
                if (minPriceValue > tbItem.getCartPrice()){
                    minPriceValue = tbItem.getCartPrice();
                }
                orderTotalPrice += tbItem.getCartTotalPrice();
                goodsList.add(tbItem);
                //创建订单item
                TbOrderItem tbOrderItem = buildOrderItem(tbItem,orderId);
                tbOrderItem.setPrice(tbItem.getCartPrice());
                tbOrderItem.setTotalFee(tbItem.getCartTotalPrice());
                tbOrderItem.setNum(cartItem.getItemCount());
                tbOrderItem.setSkuDetailIds(cartItem.getSkuDetailIds());
                tbOrderItems.add(tbOrderItem);
            }
            //商品信息组装完后创建订单，创建订单明细，生成支付记录

            TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
            tbOrder.setPosterUrl(goodsList.get(0).getImage());
            tbOrder.setStoreId(tbStore.getId());
            tbOrder.setStoreName(tbStore.getStoreName());
            tbOrder.setGoodsName(goodsList.get(0).getTitle());
            tbOrder.setGoodsTotalCount(totalGoodsCount);

            if (tbUserCoupons != null){
                //如果有优惠券计算优惠券减少金额
                Long couponsId = Long.valueOf(tbUserCoupons.getCouponsId());
                tbOrder.setUserCouponsId(couponsId);
                TbCoupons tbCouponsById = activityCache.getTbCouponsById(couponsId);
                if (tbCouponsById != null){
                    couponsReduceAmount = getCouponsReduceAmount(totalGoodsCount, orderTotalPrice, maxPriceValue, minPriceValue,tbCouponsById);
                }
            }
            //订单实际金额
            orderTotalPrice += sendPrice;
            tbOrder.setOrderPayment(orderTotalPrice);
            //用户支付金额
            tbOrder.setPayment(new BigDecimal(orderTotalPrice));
            tbOrder.setOrderId(orderId);
            if (couponsReduceAmount > 0){
                double payment = orderTotalPrice - couponsReduceAmount;
                tbOrder.setPayment(new BigDecimal(payment));
                tbOrder.setCouponsReduceAmount(couponsReduceAmount);
            }
            //保存订单商品item
            if (tbOrderItems.size() > 1){
                tbOrderItemMapper.insertBatch(tbOrderItems);
            }else {
                tbOrderItemMapper.insert(tbOrderItems.get(0));
            }
            //如果减少金额等于订单金额 更新优惠券为已经使用 订单状态为已支付
            insertPayRecordAndUpdateCoupons(oppenId, userCouponsId, orderId, tbOrder);

            //保存订单
            tbOrderMapper.insert(tbOrder);

            //调用支付接口
            //TODO
            //apiPayService.createPayOrderToWechat(tbOrder);

        }
    }

    private TbOrderItem buildOrderItem(TbItem tbItem,String orderId) {
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setOrderId(orderId);
        tbOrderItem.setTitle(String.valueOf(tbItem.getTitle()));
        tbOrderItem.setPicPath(tbItem.getImage());
        return tbOrderItem;
    }

    private double getCouponsReduceAmount(int totalGoodsCount, double orderTotalPrice, double maxPriceValue, double minPriceValue, TbCoupons tbCoupons) {
        double couponsReduceAmount = 0.0;
        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_FULL_REDUCE){
            //满减优惠券,消费金额满多少送多少
            if (orderTotalPrice > tbCoupons.getConsumeAmount()){
                couponsReduceAmount = tbCoupons.getReduceAmount();
            }
        }
        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_RATIO){
            //打折优惠券,选择商品中单价最大的进行打折
            couponsReduceAmount = PriceCalculateUtil.ratioCouponsPriceCalculate(tbCoupons, maxPriceValue);
        }
        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_FREE){
            //免费券 免费商品中单价最高的商品
            couponsReduceAmount = maxPriceValue;
        }
        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_GROUP){
            //满送优惠券，订单商品数量满多少进行赠送
            if (totalGoodsCount > tbCoupons.getConsumeCount()){
                couponsReduceAmount = minPriceValue;
            }
        }
        return couponsReduceAmount;
    }

    @Override
    public void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds,
                                     int userCouponsId, int addressId, int selfGet,int goodsCount,TbStore tbStore) {
        int sendPrice = 0;
        int skuPrice = 0;

        double couponsReduceAmount = 0.0;

        TbUserCoupons tbUserCoupons = null;

        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return;
        }

        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }

        if (userCouponsId != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(oppenId,userCouponsId);
        }

        TbItem goods = goodsCache.findGoodsById(goodsId);
        goods = goods.copy();
        double goodsPrice = goods.getPrice().doubleValue();
        if (goods.getShowActivityPrice() == 1){
            goodsPrice = goods.getActivityPrice().doubleValue();
        }

        if (skuDetailIds != null && skuDetailIds.trim().length() > 0){
            skuPrice = goodsCache.calculateSkuPrice(skuDetailIds);
        }

        goodsPrice = goodsPrice + skuPrice;
        double orderPayment = new BigDecimal(goodsPrice).multiply(new BigDecimal(goodsCount)).doubleValue();

        String orderId = SerialGenerator.getOrderSerial();

        //构建订单明细
        TbOrderItem tbOrderItem = buildOrderItem(goods,orderId);
        tbOrderItem.setPrice(goodsPrice);
        tbOrderItem.setTotalFee(orderPayment);
        tbOrderItem.setNum(goodsCount);
        tbOrderItem.setSkuDetailIds(skuDetailIds);
        TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
        tbOrder.setPosterUrl(goods.getImage());
        tbOrder.setGoodsName(goods.getTitle());
        tbOrder.setGoodsTotalCount(goodsCount);

        if (tbUserCoupons != null){
            //如果有优惠券计算优惠券减少金额
            Long couponsId = Long.valueOf(tbUserCoupons.getCouponsId());
            tbOrder.setUserCouponsId(couponsId);
            TbCoupons tbCouponsById = activityCache.getTbCouponsById(couponsId);
            if (tbCouponsById != null){
                couponsReduceAmount = getCouponsReduceAmount(1, goodsPrice, goodsPrice, goodsPrice,tbCouponsById);
            }
        }

        //订单实际金额
        orderPayment += sendPrice;
        tbOrder.setOrderPayment(orderPayment);
        //用户支付金额
        tbOrder.setPayment(new BigDecimal(orderPayment));
        tbOrder.setOrderId(orderId);
        if (couponsReduceAmount > 0){
            double payment = orderPayment - couponsReduceAmount;
            tbOrder.setPayment(new BigDecimal(payment));
            tbOrder.setCouponsReduceAmount(couponsReduceAmount);
        }
        //保存订单商品item
        tbOrderItemMapper.insert(tbOrderItem);

        //如果减少金额等于订单金额 更新优惠券为已经使用 订单状态为已支付
        insertPayRecordAndUpdateCoupons(oppenId, userCouponsId, orderId, tbOrder);

        //保存订单
        tbOrder.setStoreId(tbStore.getId());
        tbOrder.setStoreName(tbStore.getStoreName());
        tbOrderMapper.insert(tbOrder);

        //调用支付接口
        //TODO
    }

    private void insertPayRecordAndUpdateCoupons(String oppenId, int userCouponsId, String orderId, TbOrder tbOrder) {
        if (tbOrder.getPayment().doubleValue() == 0){
            tbUserCouponsMapper.updateStatusById(userCouponsId, ApiConstant.USER_COUPONS_STATUS_USED);
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
        }else {
            tbUserCouponsMapper.updateStatusById(userCouponsId,ApiConstant.USER_COUPONS_STATUS_LOCK);
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_NO_PAY);
            //生成付款记录
            TbUserPayRecord tbUserPayRecord = new TbUserPayRecord();
            String payId = SerialGenerator.getOrderSerial();
            tbUserPayRecord.setId(payId);
            tbUserPayRecord.setCreateTime(new Date());
            tbUserPayRecord.setOppenId(oppenId);
            tbUserPayRecord.setOrderId(orderId);
            tbUserPayRecord.setOrderPayment(tbOrder.getOrderPayment());
            tbUserPayRecord.setPayment(tbOrder.getPayment().doubleValue());
            tbUserPayRecordMapper.insert(tbUserPayRecord);
        }
    }

    private TbOrder buildTbOrder(String oppenId, int selfGet, TbApiUserAddress userAddress) {
        TbOrder tbOrder = new TbOrder();
        tbOrder.setPaymentType(1);
        tbOrder.setStatus(0);
        tbOrder.setCreateTime(new Date());
        tbOrder.setOppenId(oppenId);
        tbOrder.setSelfGet(selfGet);
        tbOrder.setAddress(userAddress == null ? "自提":userAddress.getAddressName());
        tbOrder.setPhoneNum(userAddress == null ? "" : userAddress.getBindNum());
        return tbOrder;
    }

    @Override
    public boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, int userCouponsId, int addressId, int selfGet) {
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && addressId == 0){
            return false;
        }
        if (goodsId == 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet) {
        if (cartItemIds == null || cartItemIds.trim().length() == 0){
            return false;
        }
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && addressId == 0){
            return false;
        }
        return true;
    }
}
