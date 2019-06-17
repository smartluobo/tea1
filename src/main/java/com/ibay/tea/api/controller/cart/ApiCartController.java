package com.ibay.tea.api.controller.cart;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/cart")
public class ApiCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCartController.class);

    @Resource
    private ApiCartService apiCartService;

    @Resource
    private ApiGoodsService apiGoodsService;

    @RequestMapping("cartGoodsList")
    public ResultInfo getCartList(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String storeId = params.get("storeId");
        if (StringUtils.isEmpty(oppenId)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> cartGoodsList = apiCartService.findCartGoodsListByOppenId(oppenId,Integer.valueOf(storeId));
            apiGoodsService.checkGoodsInventory(cartGoodsList,Integer.valueOf(storeId));
            resultInfo.setData(cartGoodsList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getCartList happen exception oppenId : {}",oppenId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("cartGoodsDetail")
    public ResultInfo getCartGoodsDetailById(@RequestBody Map<String,Integer> params){
        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        Integer id = params.get("id");
        Integer storeId = params.get("storeId");
        if (id == null || id == 0 ||storeId == null || storeId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbItem cartGoodsInfo = apiCartService.findCartGoodsById(id,storeId);
            resultInfo.setData(cartGoodsInfo);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getCartGoodsDetailById happen exception goodsId : {}",id,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/addCartItem")
    public ResultInfo addCartItem(@RequestBody TbCart tbCart){
        try {
            if (tbCart == null){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiCartService.addCartItem(tbCart);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("addCartItem happen exception tbCart : {}",tbCart,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping(value = "cartGoodsDelete")
    public ResultInfo cartGoodsDelete(@RequestBody Map<String,String> params){
        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        Integer cartItemId = Integer.valueOf(params.get("cartItemId"));
        if (StringUtils.isEmpty(oppenId) || cartItemId == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiCartService.cartGoodsDelete(oppenId,cartItemId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cartGoodsDelete happen exception oppenId : {}, cartItemId : {}",oppenId,cartItemId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
