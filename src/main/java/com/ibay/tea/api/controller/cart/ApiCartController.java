package com.ibay.tea.api.controller.cart;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class ApiCartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCartController.class);

    @Resource
    private ApiCartService apiCartService;

    @RequestMapping("cartGoodsList/{oppenId}")
    public ResultInfo getCartList(@PathVariable("oppenId") String oppenId){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> cartGoodsList = apiCartService.findCartGoodsListByOppenId(oppenId);
            resultInfo.setData(cartGoodsList);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("getCartList happen exception oppenId : {}",oppenId,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("cartGoodsDetail/{id}")
    public ResultInfo getCartGoodsDetailById(@PathVariable("id") int id){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbItem cartGoodsInfo = apiCartService.findCartGoodsById(id);
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
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            apiCartService.addCartItem(tbCart);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("addCartItem happen exception tbCart : {}",tbCart,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("cartGoodsDelete/{oppenId}/{cartItemId}")
    public ResultInfo cartGoodsDelete(@PathVariable("oppenId") String oppenId ,@PathVariable("cartItemId") int cartItemId){
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
