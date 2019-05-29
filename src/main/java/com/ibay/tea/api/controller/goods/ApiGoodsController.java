package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
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

    @GetMapping("/listByCategoryId/{categoryId}")
    public ResultInfo getGoodsListByCategoryId(@PathVariable("categoryId") long categoryId){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> goodsListByCategoryId = apiGoodsService.getGoodsListByCategoryId(categoryId);
            resultInfo.setData(goodsListByCategoryId);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getGoodsDetailById/{goodsId}")
    public ResultInfo getGoodsDetailById(@PathVariable("goodsId") long goodsId){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            TbItem goods = apiGoodsService.getGoodsDetailById(goodsId);
            resultInfo.setData(goods);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
