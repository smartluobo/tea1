package com.ibay.tea.api.service.goods.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.GoodsMapper;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.dao.TbStoreGoodsMapper;
import com.ibay.tea.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiGoodsServiceImpl implements ApiGoodsService {

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private TbStoreGoodsMapper tbStoreGoodsMapper;

    @Override
    public List<TbItem> getGoodsListByCategoryId(long categoryId) {

        List<TbItem> goodsListByCategoryId = goodsCache.getGoodsListByCategoryId(categoryId);

        if (CollectionUtils.isEmpty(goodsListByCategoryId)){
            List<TbItem> goodsList = new ArrayList<>(goodsListByCategoryId.size());
            for (TbItem tbItem : goodsListByCategoryId) {
                goodsList.add(tbItem.copy());
            }
            return goodsList;
        }

        return null;
    }

    @Override
    public TbItem getGoodsDetailById(long goodsId) {
        TbItem goods = goodsCache.findGoodsById(goodsId);
        //组装sku相关信息
        return goods;
    }

    @Override
    public void calculateGoodsPrice(List<TbItem> goodsListByCategoryId, int extraPrice, TodayActivityBean todayActivityBean) {
        if (extraPrice == 0 && todayActivityBean == null){
            return;
        }
        if (todayActivityBean != null){
            //活动类型为全场折扣
            if (todayActivityBean.getTbActivity().getActivityType() == ApiConstant.ACTIVITY_TYPE_FULL){
                for (TbItem tbItem : goodsListByCategoryId) {
                    tbItem.setShowActivityPrice(1);
                    tbItem.setPrice(tbItem.getPrice() + extraPrice);
                    TbActivityCouponsRecord tbActivityCouponsRecord = todayActivityBean.getTbActivityCouponsRecordList().get(0);
                    TbCoupons tbCoupons = activityCache.getTbCouponsById(tbActivityCouponsRecord.getCouponsId());

                    double activityPrice = PriceCalculateUtil.multy(tbItem.getPrice(),tbCoupons.getCouponsRatio());
                    tbItem.setActivityPrice(activityPrice);
                }
            }else {
                if (extraPrice != 0){
                    for (TbItem tbItem : goodsListByCategoryId) {
                        tbItem.setPrice(tbItem.getPrice() + extraPrice);
                    }
                }
            }
        }
    }

    @Override
    public void checkGoodsInventory(List<TbItem> goodsList, int storeId) {
        if (CollectionUtils.isEmpty(goodsList)){
            return;
        }
        List<Long> goodsIds = new ArrayList<>(goodsList.size());
        for (TbItem tbItem : goodsList) {
            goodsIds.add(tbItem.getId());
        }
        Map<String ,Object> condition = new HashMap<>();
        condition.put("goodsIds",goodsIds);
        condition.put("storeId",storeId);
        List<TbStoreGoods> storeGoodsList = tbStoreGoodsMapper.findRecordByCondition(condition);
        for (TbItem tbItem : goodsList) {
            for (TbStoreGoods tbStoreGoods : storeGoodsList) {
                if (tbItem.getId().intValue() == tbStoreGoods.getGoodsId()){
                    tbItem.setNum(tbStoreGoods.getGoodsInventory());
                }
            }
        }
    }


}
