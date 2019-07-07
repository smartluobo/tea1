package com.ibay.tea.api.service.recommend.impl;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.api.service.recommend.ApiRecommendService;
import com.ibay.tea.cache.ActivityCache;
import com.ibay.tea.cache.GoodsCache;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.dao.TbRecommendMapper;
import com.ibay.tea.entity.TbItem;
import com.ibay.tea.entity.TbStore;
import com.ibay.tea.entity.TodayActivityBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiRecommendServiceImpl implements ApiRecommendService {

    @Resource
    private GoodsCache goodsCache;

    @Resource
    private TbRecommendMapper tbRecommendMapper;

    @Resource
    private ActivityCache activityCache;

    @Resource
    private StoreCache storeCache;

    @Resource
    private ApiGoodsService apiGoodsService;

    @Override
    public List<TbItem> findRecommendList(String oppenId, String storeId) {

        List<TbItem> goodsList = new ArrayList<>();
        List<Long> goodsIds = tbRecommendMapper.findStoreRecommend(storeId);
        if (CollectionUtils.isEmpty(goodsIds)){
            return null;
        }
        for (Long goodsId : goodsIds) {
            TbItem goodsById = goodsCache.findGoodsById(goodsId);
            if (goodsById != null){
                goodsList.add(goodsById.copy());
            }
        }
        Integer storeIdInt = Integer.valueOf(storeId);
        TodayActivityBean todayActivityBean = activityCache.getTodayActivityBean(storeIdInt);
        TbStore store = storeCache.findStoreById(storeIdInt);
        apiGoodsService.calculateGoodsPrice(goodsList,store.getExtraPrice(),todayActivityBean);
        apiGoodsService.checkGoodsInventory(goodsList,storeIdInt);
        return goodsList;
    }
}
