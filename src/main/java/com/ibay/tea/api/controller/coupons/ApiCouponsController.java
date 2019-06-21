package com.ibay.tea.api.controller.coupons;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.coupons.ApiCouponsService;
import com.ibay.tea.entity.TbUserCoupons;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/coupons")
public class ApiCouponsController {

    @Resource
    private ApiCouponsService apiCouponsService;

    @RequestMapping("/findUserValidCoupons")
    public ResultInfo getUserValidCoupons(@RequestBody Map<String,String> params){

        if (CollectionUtils.isEmpty(params)){
        	return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbUserCoupons> userCouponsList = apiCouponsService.findUserValidCoupons(oppenId);
            resultInfo.setData(userCouponsList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

}
