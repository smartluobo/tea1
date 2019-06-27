package com.ibay.tea.api.service.order.impl;

import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.response.ResultInfo;
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

    @Resource
    private TbApiUserMapper tbApiUserMapper;

    @Override
    public Map<String, Object> createOrderByCart(CartOrderParamVo cartOrderParamVo) throws Exception{

        int selfGet = cartOrderParamVo.getSelfGet();
        int addressId = cartOrderParamVo.getAddressId();
        String oppenId = cartOrderParamVo.getOppenId();
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        if (StringUtils.isEmpty(cartItemIds)){
            return null;
        }
        String[] cartItemIdArr = cartItemIds.split(",");
        int storeId = cartOrderParamVo.getStoreId();
        TbStore store = storeCache.findStoreById(storeId);

        //创建订单成功自动调起支付接口支付
        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
        }
        CalculateReturnVo calculateReturnVo = calculateCartOrderPrice(cartOrderParamVo,true);
        if (calculateReturnVo == null){
            return null;
        }
        List<TbItem> goodsList = calculateReturnVo.getGoodsList();
        if (CollectionUtils.isEmpty(goodsList)){
            return null;
        }
        List<TbOrderItem> tbOrderItems = new ArrayList<>();
        String orderId = SerialGenerator.getOrderSerial();
        int totalGoodsCount = 0;
        for (TbItem tbItem : goodsList) {
            totalGoodsCount += tbItem.getCartItemCount();
            //创建订单item
            TbOrderItem tbOrderItem = buildOrderItem(tbItem,orderId);
            tbOrderItem.setPrice(tbItem.getCartPrice());
            tbOrderItem.setTotalFee(tbItem.getCartTotalPrice());
            tbOrderItem.setNum(tbItem.getCartItemCount());
            tbOrderItem.setSkuDetailIds(tbItem.getCartSkuDetailIds());
            tbOrderItem.setSkuDetailDesc(tbItem.getSkuDetailDesc());
            tbOrderItems.add(tbOrderItem);
        }
        TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
        tbOrder.setPosterUrl(goodsList.get(0).getImage());
        tbOrder.setStoreId(store.getId());
        tbOrder.setStoreName(store.getStoreName());
        tbOrder.setGoodsName(goodsList.get(0).getTitle());
        tbOrder.setGoodsTotalCount(totalGoodsCount);


        //订单实际金额
        tbOrder.setOrderPayment(calculateReturnVo.getOrderTotalAmount());
        //用户支付金额
        tbOrder.setPayment(calculateReturnVo.getOrderPayAmount());
        tbOrder.setCouponsReduceAmount(calculateReturnVo.getOrderReduceAmount());
        tbOrder.setOrderId(orderId);
        String takeCode = orderId.substring(orderId.length() - 4);
        tbOrder.setTakeCode(takeCode);
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
        //微信支付统一下单
        Map<String, Object> payMap = apiPayService.createPayOrderToWechat(tbOrder);
        LOGGER.info("wechat pay create order return result : {}",payMap);
        tbUserPayRecord.setNonceStr(String.valueOf(payMap.get("nonce_str")));
        tbUserPayRecord.setPrepayId(String.valueOf(payMap.get("prepay_id")));
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        payMap.put("timeStamp",timeStamp);
        payMap.put("signType",wechatInfoProperties.getSignType());
        String paySign = apiPayService.secondEncrypt(tbUserPayRecord,timeStamp);
        payMap.put("paySign",paySign);
        tbCartMapper.deleteCartItemByIds(Arrays.asList(cartItemIdArr));
        tbUserPayRecordMapper.insert(tbUserPayRecord);
        payMap.remove("mch_id");
        return payMap;
    }

    private TbOrderItem buildOrderItem(TbItem tbItem,String orderId) {
        TbOrderItem tbOrderItem = new TbOrderItem();
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setOrderId(orderId);
        tbOrderItem.setTitle(tbItem.getTitle());
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
    public Map<String, Object> createOrderByGoodsId(GoodsOrderParamVo goodsOrderParamVo) throws Exception{

        int selfGet = goodsOrderParamVo.getSelfGet();
        int addressId = goodsOrderParamVo.getAddressId();
        int userCouponsId = goodsOrderParamVo.getUserCouponsId();
        String oppenId = goodsOrderParamVo.getOppenId();
        String skuDetailIds = goodsOrderParamVo.getSkuDetailIds();
        int goodsCount = goodsOrderParamVo.getGoodsCount();
        int storeId = goodsOrderParamVo.getStoreId();
        TbStore store = storeCache.findStoreById(storeId);



        TbApiUserAddress userAddress = apiAddressService.findUserAddressById(addressId);
        if (selfGet == ApiConstant.ORDER_TAKE_WAY_SEND && userAddress == null){
            return null;
        }

        CalculateReturnVo calculateReturnVo = calculateGoodsOrderPrice(goodsOrderParamVo, true);
        if (calculateReturnVo == null){
            return null;
        }
        TbItem goodsInfo = calculateReturnVo.getGoodsInfo();


        String orderId = SerialGenerator.getOrderSerial();

        //构建订单明细
        TbOrderItem tbOrderItem = buildOrderItem(goodsInfo,orderId);
        tbOrderItem.setPrice(goodsInfo.getPrice());
        if (goodsInfo.getShowActivityPrice() == 1){
            tbOrderItem.setPrice(goodsInfo.getActivityPrice());
        }

        tbOrderItem.setTotalFee(calculateReturnVo.getOrderTotalAmount());
        tbOrderItem.setNum(goodsCount);
        tbOrderItem.setSkuDetailIds(skuDetailIds);
        tbOrderItem.setSkuDetailDesc(goodsOrderParamVo.getSkuDetailDesc());
        TbOrder tbOrder = buildTbOrder(oppenId, selfGet, userAddress);
        tbOrder.setPosterUrl(goodsInfo.getImage());
        tbOrder.setGoodsName(goodsInfo.getTitle());
        tbOrder.setGoodsTotalCount(goodsCount);



        //订单实际金额

        tbOrder.setOrderPayment(calculateReturnVo.getOrderTotalAmount());
        //用户支付金额
        tbOrder.setPayment(calculateReturnVo.getOrderPayAmount());
        tbOrder.setOrderId(orderId);

        //保存订单商品item
        tbOrderItemMapper.insert(tbOrderItem);

        //如果减少金额等于订单金额 更新优惠券为已经使用 订单状态为已支付
        TbUserPayRecord tbUserPayRecord = buildPayRecordAndUpdateCoupons(oppenId, userCouponsId, orderId, tbOrder);
        tbUserPayRecordMapper.insert(tbUserPayRecord);
        //保存订单
        tbOrder.setStoreId(store.getId());
        tbOrder.setStoreName(store.getStoreName());
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
        if (tbOrder.getPayment() == 0){
            tbUserCouponsMapper.updateStatusById(userCouponsId, ApiConstant.USER_COUPONS_STATUS_USED);
            tbOrder.setStatus(ApiConstant.ORDER_STATUS_PAYED);
            tbUserCouponsMapper.updateStatusById(userCouponsId,ApiConstant.USER_COUPONS_STATUS_USED);
            //生成付款记录
            TbUserPayRecord tbUserPayRecord = new TbUserPayRecord();
            String payId = SerialGenerator.getOrderSerial();
            tbUserPayRecord.setId(payId);
            tbUserPayRecord.setCreateTime(new Date());
            tbUserPayRecord.setOppenId(oppenId);
            tbUserPayRecord.setOrderId(orderId);
            tbUserPayRecord.setOrderPayment(tbOrder.getOrderPayment());
            tbUserPayRecord.setPayment(tbOrder.getPayment());
            return tbUserPayRecord;
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
            tbUserPayRecord.setPayment(tbOrder.getPayment());
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
        TbApiUser apiUser = tbApiUserMapper.findApiUserByOppenId(oppenId);
        if (apiUser == null){
            return false;
        }
        if (cartItemIds == null || cartItemIds.trim().length() == 0){
            return false;
        }
        return selfGet != ApiConstant.ORDER_TAKE_WAY_SEND || addressId != 0;
    }

    @Override
    public CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo,boolean isCreateOrder) {

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
                if (tbItem != null){
                    if (maxPriceValue < tbItem.getCartPrice()) {
                        maxPriceValue = tbItem.getCartPrice();
                    }
                    orderTotalPrice += tbItem.getCartTotalPrice();
                    goodsList.add(tbItem);
                }
            }
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
                LOGGER.info("goods full reduce calculate");
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
                LOGGER.info("cart order calculate coupons reduce amount");
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
            CalculateReturnVo calculateReturnVo = getCalculateReturnVo(orderTotalPrice, couponsReduceAmount, couponsName, groupGiveAmount, groupGiveName, fullReduceAmount, fullReduceName);
            if (isCreateOrder){
                calculateReturnVo.setGoodsList(goodsList);
            }
            return calculateReturnVo;
        }

        return null;
    }

    private void buildReturnVo(CalculateReturnVo calculateReturnVo, double payAmount, double reduceAmount, String couponsName) {
        calculateReturnVo.setOrderPayAmount(payAmount);
        calculateReturnVo.setOrderReduceAmount(reduceAmount);
        calculateReturnVo.setCouponsName(couponsName);
    }

    @Override
    public CalculateReturnVo calculateGoodsOrderPrice(GoodsOrderParamVo goodsOrderParamVo,boolean isCreateOrder){

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
        if (goods == null){
            return null;
        }
        goods = goods.copy();

        TbStore store = storeCache.findStoreById(goodsOrderParamVo.getStoreId());

        apiGoodsService.calculateGoodsPrice(goods,store.getExtraPrice(),activityCache.getTodayActivityBean(goodsOrderParamVo.getStoreId()));

        double goodsPrice = goods.getPrice();
        if (goods.getShowActivityPrice() == 1){
            goodsPrice = goods.getActivityPrice();
        }

        if (goodsOrderParamVo.getSkuDetailIds() != null ){
            skuPrice = goodsCache.calculateSkuPrice(goodsOrderParamVo.getSkuDetailIds());
        }

        goodsPrice = goodsPrice + skuPrice;
        goods.setPrice(goods.getPrice() + skuPrice);
        goods.setActivityPrice(goods.getActivityPrice() + skuPrice);
        goods.setCartItemCount(goodsOrderParamVo.getGoodsCount());
        goods.setCartSkuDetailIds(goodsOrderParamVo.getSkuDetailIds());
        goods.setSkuDetailDesc(goodsOrderParamVo.getSkuDetailDesc());
        double orderTotalPrice = PriceCalculateUtil.multy(goodsPrice,goodsOrderParamVo.getGoodsCount());
        double couponsReduceAmount = 0.0;
        String couponsName = null;
        double groupGiveAmount = 0.0;
        String groupGiveName = null;
        double fullReduceAmount = 0.0;
        String fullReduceName = null;
        int goodsCount = goodsOrderParamVo.getGoodsCount();

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
            if (tbCouponsById != null) {
                couponsReduceAmount = getCouponsReduceAmount(goodsPrice,tbCouponsById);
                couponsName = tbCouponsById.getCouponsName();
            }
        }
        //判断哪种策略对消费者最优惠
        if (goodsOrderParamVo.getSelfGet() == ApiConstant.ORDER_TAKE_WAY_SEND){
            orderTotalPrice += sendPrice;
        }

        CalculateReturnVo calculateReturnVo = getCalculateReturnVo(orderTotalPrice, couponsReduceAmount, couponsName, groupGiveAmount, groupGiveName, fullReduceAmount, fullReduceName);
        if (calculateReturnVo != null && isCreateOrder){
            calculateReturnVo.setGoodsInfo(goods);
        }
        return calculateReturnVo;
    }

    private CalculateReturnVo getCalculateReturnVo(double orderTotalPrice, double couponsReduceAmount, String couponsName, double groupGiveAmount, String groupGiveName, double fullReduceAmount, String fullReduceName) {
        double maxReduceAmount = Math.max(groupGiveAmount, Math.max(fullReduceAmount, couponsReduceAmount));
        CalculateReturnVo calculateReturnVo = new CalculateReturnVo();
        calculateReturnVo.setOrderTotalAmount(orderTotalPrice);
        calculateReturnVo.setOrderPayAmount(orderTotalPrice);
        calculateReturnVo.setCouponsName("无优惠");
        calculateReturnVo.setCouponsType(0);
        if (maxReduceAmount == 0){
            return calculateReturnVo;
        }else if (maxReduceAmount == groupGiveAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, groupGiveAmount);
            buildReturnVo(calculateReturnVo,payAmount,groupGiveAmount,groupGiveName);
            calculateReturnVo.setCouponsType(1);

        }else if (maxReduceAmount == fullReduceAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, fullReduceAmount);
            buildReturnVo(calculateReturnVo,payAmount,fullReduceAmount,fullReduceName);
            calculateReturnVo.setCouponsType(2);

        }else if (maxReduceAmount == couponsReduceAmount){
            double payAmount = PriceCalculateUtil.subtract(orderTotalPrice, couponsReduceAmount);
            buildReturnVo(calculateReturnVo,payAmount,couponsReduceAmount,couponsName);
            calculateReturnVo.setCouponsType(3);
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
