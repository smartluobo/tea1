package com.ibay.tea.api.controller.cart;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.cart.ApiCartService;
import com.ibay.tea.entity.TbCart;
import com.ibay.tea.entity.TbItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/cart")
public class ApiCartController {

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
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("cartGoodsDetail/{id}")
    public ResultInfo getCartList(@PathVariable("id") int id){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbItem cartGoodsInfo = apiCartService.findCartGoodsById(id);
            resultInfo.setData(cartGoodsInfo);
            return resultInfo;
        }catch (Exception e){
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
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
