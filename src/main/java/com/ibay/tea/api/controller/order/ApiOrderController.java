package com.ibay.tea.api.controller.order;

import com.ibay.tea.api.paramVo.CartOrderParamVo;
import com.ibay.tea.api.paramVo.GoodsOrderParamVo;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.order.ApiOrderService;
import com.ibay.tea.dao.TbStoreMapper;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("api/order")
public class ApiOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderController.class);

    @Resource
    private ApiOrderService apiOrderService;

    @Resource
    private TbStoreMapper tbStoreMapper;

    @RequestMapping("createOrderByCart")
    public ResultInfo createOrderByCart(@RequestBody CartOrderParamVo cartOrderParamVo){
        if (cartOrderParamVo == null){
            LOGGER.error("createOrderByCart cartOrderParamVo is null");
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = cartOrderParamVo.getOppenId();
        String cartItemIds = cartOrderParamVo.getCartItemIds();
        int userCouponsId = cartOrderParamVo.getUserCouponsId();
        int addressId = cartOrderParamVo.getAddressId();
        int selfGet = cartOrderParamVo.getSelfGet();
        TbStore tbStore = tbStoreMapper.selectByPrimaryKey(cartOrderParamVo.getStoreId());
        if (tbStore == null){
            LOGGER.error("createOrderByCart storeId : {} not found store record ",cartOrderParamVo.getStoreId());
            return ResultInfo.newEmptyParamsResultInfo();

        }
        try {
            LOGGER.info("createOrderByCart params oppenId : {}, cartItemIds : {}, userCouponsId: {} ,addressId :{} selfGet : {}",
                    oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            boolean flag = apiOrderService.checkCartOrderParameter(oppenId,cartItemIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            apiOrderService.createOrderByCart(oppenId,cartItemIds,userCouponsId,addressId,selfGet,tbStore);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("createOrderByCart happen exception oppenId : {}, cartItemIds : {}, userCouponsId: {} ,addressId :{} selfGet : {}",
                    oppenId,cartItemIds,userCouponsId,addressId,selfGet,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/createOrderByGoodsId")
    public ResultInfo createOrderByGoodsId(@RequestBody GoodsOrderParamVo goodsOrderParamVo){
        if (goodsOrderParamVo == null){
            return ResultInfo.newEmptyParamsResultInfo();
        }

        String oppenId = goodsOrderParamVo.getOppenId();
        String skuDetailIds = goodsOrderParamVo.getSkuDetailIds();
        long goodsId = goodsOrderParamVo.getGoodsId();
        int userCouponsId = goodsOrderParamVo.getUserCouponsId();
        int addressId = goodsOrderParamVo.getAddressId();
        int selfGet = goodsOrderParamVo.getSelfGet();
        int goodsCount = goodsOrderParamVo.getGoodsCount();
        TbStore tbStore = tbStoreMapper.selectByPrimaryKey(goodsOrderParamVo.getStoreId());

        LOGGER.info("createOrderByGoodsId params oppenId : {}, goodsId : {},skuDetailIds : {} userCouponsId: {} ,addressId :{} selfGet : {} goodsCount : {}",
                oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount);
        if (tbStore == null){
            LOGGER.error("createOrderByGoodsId storeId : {} not found store record ",goodsOrderParamVo.getStoreId());
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            boolean flag = apiOrderService.checkGoodsOrderParameter(oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet);
            if (!flag){
                return ResultInfo.newParameterErrorResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

            apiOrderService.createOrderByGoodsId(oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount,tbStore,goodsOrderParamVo);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("createOrderByGoodsId happen exception oppenId : {}, goodsId : {},skuDetailIds : {} userCouponsId: {} ,addressId :{} selfGet : {} goodsCount : {}",
                    oppenId,goodsId,skuDetailIds,userCouponsId,addressId,selfGet,goodsCount,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findOrderItemByOrderId")
    public ResultInfo findOrderItemByOrderId(@RequestBody Map<String,String> params){
        return null;
    }
}
