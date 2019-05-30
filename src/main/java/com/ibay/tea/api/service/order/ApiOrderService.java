package com.ibay.tea.api.service.order;

public interface ApiOrderService {
    void createOrderByCart(String oppenId, String cartItemIds, Long couponsId, Long addressId, int selfGet);

    void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds, Long couponsId, Long addressId, int selfGet);
}
