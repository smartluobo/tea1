package com.ibay.tea.api.service.goods;

import com.ibay.tea.entity.TbItem;

import java.util.List;

public interface ApiGoodsService {

    List<TbItem> getGoodsListByCategoryId(long categoryId);

    TbItem getGoodsDetailById(long goodsId);
}
