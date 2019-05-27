package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.entity.TbItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/goods")
public class ApiGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;

    @GetMapping("/listByCategoryId")
    public List<TbItem> getGoodsListByCategoryId(@PathVariable("categoryId") long categoryId){
        try {
            return apiGoodsService.getGoodsListByCategoryId(categoryId);
        }catch (Exception e){
            return null;
        }

    }
}
