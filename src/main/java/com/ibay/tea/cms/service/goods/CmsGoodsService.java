package com.ibay.tea.cms.service.goods;

import com.ibay.tea.entity.Goods;

import java.util.List;
import java.util.Map;

public interface CmsGoodsService {
    List<Goods> findGoodsListByPage(Integer pageNum, Integer pageSize);

    long countGoodsByCondition(Map<String,Object> condition);
}
