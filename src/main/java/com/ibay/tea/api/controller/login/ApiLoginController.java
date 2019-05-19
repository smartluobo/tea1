package com.ibay.tea.api.controller.login;

import com.ibay.tea.api.service.login.ApiLoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/login")
public class ApiLoginController {

    @Resource
    private ApiLoginService apiLoginService;
}
