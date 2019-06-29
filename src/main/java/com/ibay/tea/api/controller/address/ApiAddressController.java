package com.ibay.tea.api.controller.address;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.responseVo.ApiAddressVo;
import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.api.service.map.ApiMapService;
import com.ibay.tea.common.service.SendSmsService;
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

    @Resource
    private SendSmsService sendSmsService;

    @Resource
    private ApiMapService apiMapService;

    @RequestMapping("/findListByOppenId")
    public ResultInfo findUserAddressByOppenId(@RequestBody Map<String,String> params){

        //判断oppenId是否有效
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        String oppenId = params.get("oppenId");
        String storeId = params.get("storeId");
        if (StringUtils.isEmpty(oppenId) || StringUtils.isEmpty(storeId)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbApiUserAddress> userAddressByOppenId = apiAddressService.findUserAddressByOppenId(oppenId,storeId);
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
           if (tbApiUserAddress.getVerificationCode() == null){
               return ResultInfo.newEmptyParamsResultInfo();
           }
           boolean flag = sendSmsService.checkVerificationCode(tbApiUserAddress.getPhoneNum(), tbApiUserAddress.getVerificationCode());
           if (!flag){
               return ResultInfo.newFailResultInfo("验证码不正确");
           }
           apiAddressService.insertApiUserAddress(tbApiUserAddress);
           return ResultInfo.newSuccessResultInfo();
       }catch (Exception e){
           return ResultInfo.newExceptionResultInfo();
       }
   }


    @RequestMapping("/updateUserAddress")
    public ResultInfo updateUserAddress(@RequestBody TbApiUserAddress tbApiUserAddress){

        try {
            if (tbApiUserAddress == null){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            apiAddressService.updateUserAddress(tbApiUserAddress);
            return ResultInfo.newSuccessResultInfo();
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getAddressList")
    public ResultInfo getAddressList(@RequestBody Map<String,String> params){

        if (params == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<ApiAddressVo> address = apiMapService.getAddressList(params);
            resultInfo.setData(address);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }


    }


    @RequestMapping("/deleteUserAddress")
    public ResultInfo deleteUserAddress(@RequestBody Map<String,String> params){

        try {
            if (params == null){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            apiAddressService.deleteApiUserAddress(params);
            return ResultInfo.newSuccessResultInfo();
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }


}
