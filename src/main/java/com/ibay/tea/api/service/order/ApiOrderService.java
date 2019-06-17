package com.ibay.tea.api.service.order;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.entity.TbStore;

public interface ApiOrderService {

    void createOrderByCart(String oppenId, String cartItemIds, int UserCouponsId, int addressId, int selfGet, TbStore tbStore)throws Exception;

    void createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds,
                              int UserCouponsId, int addressId, int selfGet, int goodsCount, TbStore tbStore, GoodsOrderParamVo goodsOrderParamVo)throws Exception;

    boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, int userCouponsId, int addressId, int selfGet);

    boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet);

    CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo);

    double calculateGoodsOrderPrice(GoodsOrderParamVo goodsOrderParamVo);
}
