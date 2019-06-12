package com.ibay.tea.api.controller.address;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.entity.TbApiUserAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class ApiAddressController {

    @Resource
    private ApiAddressService apiAddressService;

    @RequestMapping("/findListByOppenId")
    public ResultInfo findUserAddressByOppenId(@RequestBody Map<String,String> params){

        //判断oppenId是否有效
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        if (StringUtils.isEmpty(oppenId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbApiUserAddress> userAddressByOppenId = apiAddressService.findUserAddressByOppenId(oppenId);
            resultInfo.setData(userAddressByOppenId);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findById")
    public ResultInfo findUserAddressById(@RequestBody Map<String ,Integer> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        Integer id = params.get("id");
        if (id == null || id == 0){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbApiUserAddress userAddressById = apiAddressService.findUserAddressById(id);
            resultInfo.setData(userAddressById);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/addUserAddress")
   public ResultInfo insertApiUserAddress(@RequestBody TbApiUserAddress tbApiUserAddress){

       try {
           if (tbApiUserAddress == null){
               return ResultInfo.newEmptyParamsResultInfo();
           }
           apiAddressService.insertApiUserAddress(tbApiUserAddress);
           return ResultInfo.newSuccessResultInfo();
       }catch (Exception e){
           return ResultInfo.newExceptionResultInfo();
       }
   }


}
