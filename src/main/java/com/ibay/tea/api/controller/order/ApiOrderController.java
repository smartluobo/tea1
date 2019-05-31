package com.ibay.tea.api.controller.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.order.ApiOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/order")
public class ApiOrderController {

    @Resource
    private ApiOrderService apiOrderService;

    @RequestMapping("createOrderByCart/{oppenId}/{cartItemIds}/{couponsId}/{addressId}/{selfGet}")
    public ResultInfo createOrderByCart(@PathVariable("oppenId") String oppenId,
                                        @PathVariable("cartItemIds") String cartItemIds,
                                        @PathVariable("couponsId") Long couponsId,
                                        @PathVariable("addressId") int addressId,
                                        @PathVariable("selfGet") int selfGet){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag = apiOrderService.checkCartOrderParameter(oppenId,cartItemIds,couponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            apiOrderService.createOrderByCart(oppenId,cartItemIds,couponsId,addressId,selfGet);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("createOrderByGoodsId/{oppenId}/{goodsId}/{skuDetailIds}/{couponsId}/{addressId}/{selfGet}")
    public ResultInfo createOrderByGoodsId(@PathVariable("oppenId") String oppenId,
                                           @PathVariable("goodsId") long goodsId,
                                           @PathVariable("skuDetailIds") String skuDetailIds,
                                           @PathVariable("couponsId") Long couponsId,
                                           @PathVariable("addressId") int addressId,
                                           @PathVariable("selfGet") int selfGet){
        try {
            boolean flag = apiOrderService.checkGoodsOrderParameter(oppenId,goodsId,skuDetailIds,couponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

            apiOrderService.createOrderByGoodsId(oppenId,goodsId,skuDetailIds,couponsId,addressId,selfGet);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
