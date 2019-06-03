package com.ibay.tea.api.service.order;

public interface ApiOrderService {
    void createOrderByCart(String oppenId, String cartItemIds, int UserCouponsId, int addressId, int selfGet);

    void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds,
                              int UserCouponsId, int addressId, int selfGet,int goodsCount);

    boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, int userCouponsId, int addressId, int selfGet);

    boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet);
}
