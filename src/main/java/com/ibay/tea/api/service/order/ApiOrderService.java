package com.ibay.tea.api.service.order;

public interface ApiOrderService {
    void createOrderByCart(String oppenId, String cartItemIds, Long couponsId, int addressId, int selfGet);

    void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds, Long couponsId, int addressId, int selfGet);

    boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, Long couponsId, int addressId, int selfGet);

    boolean checkCartOrderParameter(String oppenId, String cartItemIds, Long couponsId, int addressId, int selfGet);
}
