package com.ibay.tea.api.controller.order;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.order.ApiOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/order")
public class ApiOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderController.class);

    @Resource
    private ApiOrderService apiOrderService;

    @RequestMapping("createOrderByCart/{oppenId}/{cartItemIds}/{userCouponsId}/{addressId}/{selfGet}")
    public ResultInfo createOrderByCart(@PathVariable("oppenId") String oppenId,
                                        @PathVariable("cartItemIds") String cartItemIds,
                                        @PathVariable("userCouponsId") int userCouponsId,
                                        @PathVariable("addressId") int addressId,
                                        @PathVariable("selfGet") int selfGet){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag = apiOrderService.checkCartOrderParameter(oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            apiOrderService.createOrderByCart(oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("createOrderByCart happen exception oppenId : {}, cartItemIds : {}, userCouponsId: {} ,addressId :{} selfGet : {}",
                    oppenId,cartItemIds,userCouponsId,addressId,selfGet,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("createOrderByGoodsId/{oppenId}/{goodsId}/{skuDetailIds}/{couponsId}/{addressId}/{selfGet}")
    public ResultInfo createOrderByGoodsId(@PathVariable("oppenId") String oppenId,
                                           @PathVariable("goodsId") long goodsId,
                                           @PathVariable("skuDetailIds") String skuDetailIds,
                                           @PathVariable("userCouponsId") int userCouponsId,
                                           @PathVariable("addressId") int addressId,
                                           @PathVariable("selfGet") int selfGet,
                                           @PathVariable("goodsCount") int goodsCount){
        try {
            boolean flag = apiOrderService.checkGoodsOrderParameter(oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

            apiOrderService.createOrderByGoodsId(oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
