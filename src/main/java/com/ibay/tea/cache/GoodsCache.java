package com.ibay.tea.cache;

import com.ibay.tea.common.constant.ApiConstant;
import com.ibay.tea.common.utils.PriceCalculateUtil;
import com.ibay.tea.dao.TbItemMapper;
import com.ibay.tea.entity.TbActivity;
import com.ibay.tea.entity.TbActivityCouponsRecord;
import com.ibay.tea.entity.TbCoupons;
import com.ibay.tea.entity.TbItem;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsCache implements InitializingBean {

    private Map<Long,List<TbItem>> goodsCacheMap;

    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private ActivityCache activityCache;

    private void initGoodsCacheMap(){
        List<TbItem> goodsList = tbItemMapper.findAll();
        TbActivity todayFullActivity = activityCache.getTodayFullActivity();
        if (todayFullActivity != null){
            List<TbActivityCouponsRecord> records = activityCache.getActivityCouponsRecordsByActivityId(todayFullActivity.getId());
            if (records != null && records.size() > 0){
                for (TbActivityCouponsRecord record : records) {
                    TbCoupons tbCoupons = activityCache.getTbCouponsById(record.getCouponsId());
                    if (tbCoupons != null && tbCoupons.getCouponsType() == ApiConstant.USER_COUPONS_TYPE_RATIO){
                        String couponsRatio = tbCoupons.getCouponsRatio();
                        for (TbItem tbItem : goodsList) {
                            BigDecimal activityPrice = PriceCalculateUtil.activiryPriceCalculate(tbItem.getPrice(),new BigDecimal(couponsRatio));
                            tbItem.setActivityPrice(activityPrice);
                            tbItem.setShowActivityPrice(1);
                        }
                        break;
                    }
                }
            }
        }
        goodsCacheMap = new HashMap<>();
        for (TbItem tbItem : goodsList) {
            List<TbItem> categoryTbItems = goodsCacheMap.get(tbItem.getCid());
            if (categoryTbItems == null){
                List<TbItem> categoryGoodsList = new ArrayList<>();
                categoryGoodsList.add(tbItem);
                goodsCacheMap.put(tbItem.getCid(),categoryGoodsList);
            }else {
                categoryTbItems.add(tbItem);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initGoodsCacheMap();
    }
}
