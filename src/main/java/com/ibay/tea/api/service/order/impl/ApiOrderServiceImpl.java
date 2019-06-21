package com.ibay.tea.api.service.order.impl;

import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.DateUtil;
import com.ibay.tea.common.utils.Md5Util;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.common.utils.SerialGenerator;
import com.ibay.tea.dao.*;
import com.ibay.tea.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ApiOrderServiceImpl implements ApiOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderServiceImpl.class);

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

    @Resource
    private StoreCache storeCache;

    @Resource
    private TbCouponsMapper tbCouponsMapper;

    @Resource
    private ApiGoodsService apiGoodsService;

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Override
    public Map<String, Object> createOrderByCart(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet,TbStore tbStore) throws Exception{
        TbUserCoupons tbUserCoupons = null;
        int sendPrice = 0;
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }
        //创建订单成功自动调起支付接口支付
        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
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
            if (CollectionUtils.isEmpty(cartItemByIds)){
                return null;
            }
            List<TbItem> goodsList = new ArrayList<>();
            List<TbOrderItem> tbOrderItems = new ArrayList<>();
            String orderId = SerialGenerator.getOrderSerial();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem,tbStore);
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
                tbOrderItem.setSkuDetailDesc(cartItem.getSkuDetailDesc());
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
                    couponsReduceAmount = getCouponsReduceAmount(maxPriceValue,tbCouponsById);
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
            TbUserPayRecord tbUserPayRecord = buildPayRecordAndUpdateCoupons(oppenId, userCouponsId, orderId, tbOrder);

            //保存订单
            tbOrderMapper.insert(tbOrder);
            Map<String, Object> payMap = apiPayService.createPayOrderToWechat(tbOrder);
            tbUserPayRecord.setNonceStr(String.valueOf(payMap.get("nonce_str")));
            tbUserPayRecord.setPrepayId(String.valueOf(payMap.get("prepay_id")));
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            payMap.put("timeStamp",timeStamp);
            payMap.put("signType",wechatInfoProperties.getSignType());
            String paySign = apiPayService.secondEncrypt(tbUserPayRecord,timeStamp);
            payMap.put("paySign",paySign);
            tbUserPayRecordMapper.insert(tbUserPayRecord);
            tbCartMapper.deleteCartItemByIds(Arrays.asList(cartItemIdArr));
            payMap.remove("mch_id");
            return payMap;
        }
        return null;
    }

    private TbOrderItem buildOrderItem(TbItem tbItem,String orderId) {
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setOrderId(orderId);
        tbOrderItem.setTitle(String.valueOf(tbItem.getTitle()));
        tbOrderItem.setPicPath(tbItem.getImage());
        return tbOrderItem;
    }

    private double getCouponsReduceAmount( double maxPriceValue, TbCoupons tbCoupons) {
        double couponsReduceAmount = 0.0;

        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_RATIO){
            //打折优惠券,选择商品中单价最大的进行打折
            couponsReduceAmount = PriceCalculateUtil.ratioCouponsPriceCalculate(tbCoupons, maxPriceValue);
        }
        if (tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_FREE){
            //免费券 免费商品中单价最高的商品
            couponsReduceAmount = maxPriceValue;
        }
        return couponsReduceAmount;
    }

    @Override
    public Map<String, Object> createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds,
                                     int userCouponsId, int addressId, int selfGet, int goodsCount,
                                                    TbStore tbStore, GoodsOrderParamVo goodsOrderParamVo) throws Exception{
        int sendPrice = 0;
        int skuPrice = 0;

        double couponsReduceAmount = 0.0;

        TbUserCoupons tbUserCoupons = null;

        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
        }

        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }

        if (userCouponsId != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(oppenId,userCouponsId);
        }

        TbItem goods = goodsCache.findGoodsById(goodsId);
        goods = goods.copy();
        double goodsPrice = goods.getPrice();
        if (goods.getShowActivityPrice() == 1){
            goodsPrice = goods.getActivityPrice();
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
        tbOrderItem.setSkuDetailDesc(goodsOrderParamVo.getSkuDetailDesc());
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
                couponsReduceAmount = getCouponsReduceAmount(goodsPrice,tbCouponsById);
                LOGGER.error("11111");

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
        TbUserPayRecord tbUserPayRecord = buildPayRecordAndUpdateCoupons(oppenId, userCouponsId, orderId, tbOrder);


        //保存订单
        tbOrder.setStoreId(tbStore.getId());
        tbOrder.setStoreName(tbStore.getStoreName());
        tbOrderMapper.insert(tbOrder);

        //调用支付接口
        //TODO
        Map<String, Object> payMap = apiPayService.createPayOrderToWechat(tbOrder);
        tbUserPayRecord.setNonceStr(String.valueOf(payMap.get("nonce_str")));
        tbUserPayRecord.setPrepayId(String.valueOf(payMap.get("prepay_id")));
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        payMap.put("timeStamp",timeStamp);
        payMap.put("signType",wechatInfoProperties.getSignType());
        String paySign = apiPayService.secondEncrypt(tbUserPayRecord,timeStamp);
        payMap.put("paySign",paySign);
        tbUserPayRecordMapper.insert(tbUserPayRecord);
        payMap.remove("mch_id");
        return payMap;
    }



    private TbUserPayRecord buildPayRecordAndUpdateCoupons(String oppenId, int userCouponsId, String orderId, TbOrder tbOrder) {
        if (tbOrder.getPayment().doubleValue() == 0){
            tbUserCouponsMapper.updateStatusById(userCouponsId, ApiConstant.USER_COUPONS_STATUS_USED);
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
            return null;
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
            return tbUserPayRecord;
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
        return (selfGet != ApiConstant.ORDER_TAKE_WAY_SEND || addressId != 0) && goodsId != 0;
    }

    @Override
    public boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet) {
        if (cartItemIds == null || cartItemIds.trim().length() == 0){
            return false;
        }
        return selfGet != ApiConstant.ORDER_TAKE_WAY_SEND || addressId != 0;
    }

    @Override
    public CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo) {

        TbUserCoupons tbUserCoupons = null;
        int sendPrice = 0;
        if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        String oppenId = cartOrderParamVo.getOppenId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        if (userCouponsId != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(oppenId,userCouponsId);
        }

        if (cartItemIds != null && cartItemIds.trim().length() > 0) {
            //订单总商品数
            int totalGoodsCount = 0;
            //订单总价格
            double orderTotalPrice = 0.0;

            double maxPriceValue = 0.0;

            double minPriceValue = 100.0;

            double couponsReduceAmount = 0.0;
            double fullReduceAmount = 0.0 ;
            double groupGiveAmount = 0.0;

            String[] cartItemIdArr = cartItemIds.split(",");
            List<TbCart> cartItemByIds = tbCartMapper.findCartItemByIds(Arrays.asList(cartItemIdArr));
            TbStore store = storeCache.findStoreById(cartOrderParamVo.getStoreId());
            List<TbItem> goodsList = new ArrayList<>();
            for (TbCart cartItem : cartItemByIds) {
                totalGoodsCount += cartItem.getItemCount();
                TbItem tbItem = apiCartService.buildCartGoodsInfo(cartItem,store);
                tbItem = tbItem.copy();
                if (maxPriceValue < tbItem.getCartPrice()) {
                    maxPriceValue = tbItem.getCartPrice();
                }
                if (minPriceValue > tbItem.getCartPrice()) {
                    minPriceValue = tbItem.getCartPrice();
                }
                orderTotalPrice += tbItem.getCartTotalPrice();
                goodsList.add(tbItem);
            }

            apiGoodsService.calculateGoodsPrice(goodsList,store.getExtraPrice(),activityCache.getTodayActivityBean(store.getId()));
            String groupGiveName = null;
            String fullReduceName = null;
            String couponsName = null;
            if (totalGoodsCount >=6){
                //走满五赠一的流程，数量6 赠送一杯付款五杯价钱 数量12 赠送两杯 付款十杯价钱
                int giveCount = totalGoodsCount / 6;
                groupGiveName = "满"+(giveCount*5)+"杯送"+giveCount+"杯";
                Collections.sort(goodsList);
                for (int i = 0; i< goodsList.size() && giveCount > 0; i++){
                    TbItem tbItem = goodsList.get(i);
                    if (tbItem.getCartItemCount() >= giveCount){
                        groupGiveAmount += PriceCalculateUtil.multy(tbItem.getCartPrice(),String.valueOf(giveCount));
                        break;
                    }else if (giveCount > tbItem.getCartItemCount()){
                        groupGiveAmount  += PriceCalculateUtil.multy(tbItem.getCartPrice(), String.valueOf(tbItem.getCartItemCount()));
                        giveCount -= tbItem.getCartItemCount();
                    }else {
                        groupGiveAmount  += PriceCalculateUtil.multy(tbItem.getCartPrice(), String.valueOf(giveCount));
                        giveCount = 0;
                    }
                }
            }
            if (orderTotalPrice >= 100){
                List<TbCoupons> tbCouponsList = tbCouponsMapper.findFullReduceCoupons();
                for (TbCoupons tbCoupons : tbCouponsList) {
                    if (orderTotalPrice >= tbCoupons.getConsumeAmount()){
                        fullReduceAmount = tbCoupons.getReduceAmount();
                        fullReduceName = tbCoupons.getCouponsName();
                        break;
                    }
                }
            }

            if (tbUserCoupons != null) {
                //如果有优惠券计算优惠券减少金额
                Long couponsId = Long.valueOf(tbUserCoupons.getCouponsId());

                TbCoupons tbCouponsById = activityCache.getTbCouponsById(couponsId);
                if (tbCouponsById != null) {
                    couponsReduceAmount = getCouponsReduceAmount(maxPriceValue,tbCouponsById);
                    couponsName = tbCouponsById.getCouponsName();
                }
            }

            //判断哪种策略对消费者最优惠
            if (cartOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
                orderTotalPrice += sendPrice;
            }

            double maxReduceAmount = Math.max(groupGiveAmount, Math.max(fullReduceAmount, couponsReduceAmount));
            CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
            calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
            calculateReturnVo.setOrderPayAmount(orderTotalPrice);
            calculateReturnVo.setCouponsName("无优惠");
            if (maxReduceAmount == 0){
                return calculateReturnVo;
            }else if (maxReduceAmount == groupGiveAmount){
                calculateReturnVo.setOrderPayAmount(orderTotalPrice-groupGiveAmount);
                calculateReturnVo.setOrderReduceAmount(groupGiveAmount);
                calculateReturnVo.setCouponsName(groupGiveName);
            }else if (maxReduceAmount == fullReduceAmount){
                calculateReturnVo.setOrderPayAmount(orderTotalPrice-fullReduceAmount);
                calculateReturnVo.setOrderReduceAmount(fullReduceAmount);
                calculateReturnVo.setCouponsName(fullReduceName);
            }else if (maxReduceAmount == couponsReduceAmount){
                calculateReturnVo.setOrderPayAmount(orderTotalPrice-couponsReduceAmount);
                calculateReturnVo.setOrderReduceAmount(couponsReduceAmount);
                calculateReturnVo.setCouponsName(couponsName);
            }
            return calculateReturnVo;


        }

        return null;
    }

    @Override
    public CalculateReturnVo calculateGoodsOrderPrice(GoodsOrderParamVo goodsOrderParamVo){

        int sendPrice = 0;
        int skuPrice = 0;
        TbUserCoupons tbUserCoupons = null;
        if (goodsOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            sendPrice = ApiConstant.ORDER_SEND_PRICE;
        }

        if (goodsOrderParamVo.getUserCouponsId() != 0){
            tbUserCoupons = tbUserCouponsMapper.selectValidUserCoupons(goodsOrderParamVo.getOppenId(),goodsOrderParamVo.getUserCouponsId());
        }

        TbItem goods = goodsCache.findGoodsById(goodsOrderParamVo.getGoodsId());
        goods = goods.copy();
        List<TbItem> goodsList = new ArrayList<>();
        TbStore store = storeCache.findStoreById(goodsOrderParamVo.getStoreId());
        goodsList.add(goods);
        apiGoodsService.calculateGoodsPrice(goodsList,store.getExtraPrice(),activityCache.getTodayActivityBean(goodsOrderParamVo.getStoreId()));

        double goodsPrice = goods.getPrice();
        if (goods.getShowActivityPrice() == 1){
            goodsPrice = goods.getActivityPrice();
        }

        if (goodsOrderParamVo.getSkuDetailIds() != null ){
            skuPrice = goodsCache.calculateSkuPrice(goodsOrderParamVo.getSkuDetailIds());
        }

        goodsPrice = goodsPrice + skuPrice;
        double orderPayment = new BigDecimal(goodsPrice).multiply(new BigDecimal(goodsOrderParamVo.getGoodsCount())).doubleValue();

        double couponsReduceAmount = 0.0;
        String couponsName = null;
        double groupGiveAmount = 0.0;
        String groupGiveName = null;
        double fullReduceAmount = 0.0;
        String fullReduceName = null;
        int goodsCount = goodsOrderParamVo.getGoodsCount();
       double orderTotalPrice = PriceCalculateUtil.multy(goodsPrice,String.valueOf(goodsCount));
        if (goodsCount >=6){
            //走满五赠一的流程，数量6 赠送一杯付款五杯价钱 数量12 赠送两杯 付款十杯价钱
            int giveCount = goodsCount / 6;
            groupGiveName = "满"+(giveCount*5)+"杯送"+giveCount+"杯";
            groupGiveAmount += PriceCalculateUtil.multy(goodsPrice,String.valueOf(giveCount));
        }
        if (orderTotalPrice >= 100){
            List<TbCoupons> tbCouponsList = tbCouponsMapper.findFullReduceCoupons();
            for (TbCoupons tbCoupons : tbCouponsList) {
                if (orderTotalPrice >= tbCoupons.getConsumeAmount()){
                    fullReduceAmount = tbCoupons.getReduceAmount();
                    fullReduceName = tbCoupons.getCouponsName();
                    break;
                }
            }
        }

        if (tbUserCoupons != null){
            //如果有优惠券计算优惠券减少金额
            Long couponsId = Long.valueOf(tbUserCoupons.getCouponsId());
            TbCoupons tbCouponsById = activityCache.getTbCouponsById(couponsId);
            if (tbCouponsById != null){
                couponsReduceAmount = getCouponsReduceAmount(goodsPrice,tbCouponsById);
                couponsName = tbCouponsById.getCouponsName();
            }
        }
        //判断哪种策略对消费者最优惠
        if (goodsOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            orderTotalPrice += sendPrice;
        }

        double maxReduceAmount = Math.max(groupGiveAmount, Math.max(fullReduceAmount, couponsReduceAmount));
        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
        calculateReturnVo.setOrderPayAmount(orderTotalPrice);
        calculateReturnVo.setCouponsName("无优惠");
        if (maxReduceAmount == 0){
            return calculateReturnVo;
        }else if (maxReduceAmount == groupGiveAmount){
            calculateReturnVo.setOrderPayAmount(orderTotalPrice-groupGiveAmount);
            calculateReturnVo.setOrderReduceAmount(groupGiveAmount);
            calculateReturnVo.setCouponsName(groupGiveName);
        }else if (maxReduceAmount == fullReduceAmount){
            calculateReturnVo.setOrderPayAmount(orderTotalPrice-fullReduceAmount);
            calculateReturnVo.setOrderReduceAmount(fullReduceAmount);
            calculateReturnVo.setCouponsName(fullReduceName);
        }else if (maxReduceAmount == couponsReduceAmount){
            calculateReturnVo.setOrderPayAmount(orderTotalPrice-couponsReduceAmount);
            calculateReturnVo.setOrderReduceAmount(couponsReduceAmount);
            calculateReturnVo.setCouponsName(couponsName);
        }
        return calculateReturnVo;
    }

    @Override
    public TbOrder findOrderDetailById(String orderId) {
        return tbOrderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public List<TbOrderItem> findOrderItemByOrderId(String orderId) {
        return tbOrderItemMapper.findOrderItemByOrderId(orderId);
    }

    @Override
    public List<TbOrder> findOrderByOppenId(String oppenId) {
        List<TbOrder> orderByOppenId = tbOrderMapper.findOrderByOppenId(oppenId);
        if (!CollectionUtils.isEmpty(orderByOppenId)){
            for (TbOrder tbOrder : orderByOppenId) {
                tbOrder.setCreateDateStr(DateUtil.viewDateFormat(tbOrder.getCreateTime()));
            }
        }
        return orderByOppenId;
    }


}
