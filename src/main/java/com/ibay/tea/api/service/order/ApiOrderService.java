package com.ibay.tea.api.service.order;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.responseVo.CalculateReturnVo;
import com.ibay.tea.entity.TbOrder;
import com.ibay.tea.entity.TbOrderItem;
import com.ibay.tea.entity.TbStore;

import java.util.List;
import java.util.Map;

public interface ApiOrderService {

    Map<String, Object> createOrderByCart(String oppenId, String cartItemIds, int UserCouponsId, int addressId, int selfGet, TbStore tbStore)throws Exception;

    Map<String, Object> createOrderByGoodsId(String oppenId, long goodsId, String skuDetailIds,
                              int UserCouponsId, int addressId, int selfGet, int goodsCount, TbStore tbStore,
                                             GoodsOrderParamVo goodsOrderParamVo)throws Exception;

    boolean checkGoodsOrderParameter(String oppenId, long goodsId, String skuDetailIds, int userCouponsId, int addressId, int selfGet);

    boolean checkCartOrderParameter(String oppenId, String cartItemIds, int userCouponsId, int addressId, int selfGet);

    CalculateReturnVo calculateCartOrderPrice(CartOrderParamVo cartOrderParamVo);

    CalculateReturnVo calculateGoodsOrderPrice(GoodsOrderParamVo goodsOrderParamVo);

    TbOrder findOrderDetailById(String orderId);

    List<TbOrderItem> findOrderItemByOrderId(String orderId);

    List<TbOrder> findOrderByOppenId(String oppenId);
}
