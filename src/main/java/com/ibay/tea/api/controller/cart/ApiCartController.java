package com.ibay.tea.api.controller.cart;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.cart.ApiCartService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/cart")
public class ApiCartController {

    @Resource
    private ApiCartService apiCartService;

    public ResultInfo getCartList(@PathVariable("oppenId") String oppenId){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();

            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
