package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/goods")
public class ApiGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;
}
