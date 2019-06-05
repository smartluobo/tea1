package com.ibay.tea.api.service.cart;

import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;

import java.util.List;

public interface ApiCartService {

    List<TbItem> findCartGoodsListByOppenId(String oppenId);

    TbItem findCartGoodsById(int id);

    void addCartItem(TbCart tbCart);

    TbItem buildCartGoodsInfo(TbCart tbCart);

    void cartGoodsDelete(String oppenId, int cartItemId);
}
