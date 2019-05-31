package com.ibay.tea.api.controller.address;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.address.ApiAddressService;
import com.ibay.tea.entity.TbApiUserAddress;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class ApiAddressController {

    @Resource
    private ApiAddressService apiAddressService;

    @RequestMapping("/findListByOppenId/{oppenId}")
    public ResultInfo findUserAddressByOppenId(@PathVariable("oppenId") String oppenId){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbApiUserAddress> userAddressByOppenId = apiAddressService.findUserAddressByOppenId(oppenId);
            resultInfo.setData(userAddressByOppenId);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/findById/{id}")
    public ResultInfo findUserAddressById(@PathVariable("id") int id){
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
               return ResultInfo.newEmptyResultInfo();
           }
           apiAddressService.insertApiUserAddress(tbApiUserAddress);
           return ResultInfo.newSuccessResultInfo();
       }catch (Exception e){
           return ResultInfo.newExceptionResultInfo();
       }
   }


}
