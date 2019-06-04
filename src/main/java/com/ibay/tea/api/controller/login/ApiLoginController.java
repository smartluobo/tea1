package com.ibay.tea.api.controller.login;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.login.ApiLoginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/login")
public class ApiLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoginController.class);

    @Resource
    private ApiLoginService apiLoginService;

    @RequestMapping("/login/{code}")
    public ResultInfo login(@PathVariable("code") String code){
        try {
            if (StringUtils.isEmpty(code)){
                return ResultInfo.newEmptyResultInfo();
            }
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            String oppenId = apiLoginService.login(code);
            resultInfo.setData(oppenId);
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("login happen exception code : {}",code,e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
