package com.ibay.tea.api.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.goods.ApiGoodsService;
import com.ibay.tea.entity.TbItem;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/goods")
public class ApiGoodsController {

    @Resource
    private ApiGoodsService apiGoodsService;

    @GetMapping("/listByCategoryId")
    public ResultInfo getGoodsListByCategoryId(@RequestBody Map<String,Long> params){
        if (CollectionUtils.isEmpty(params)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        Long categoryId = params.get("categoryId");
        if (categoryId == null || categoryId == 0){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbItem> goodsListByCategoryId = apiGoodsService.getGoodsListByCategoryId(categoryId);
            resultInfo.setData(goodsListByCategoryId);
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

    @RequestMapping("/getGoodsDetailById")
    public ResultInfo getGoodsDetailById(@RequestBody Map<String,Long> params){
        if (CollectionUtils.isEmpty(params)){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
        Long goodsId = params.get("goodsId");
        if (goodsId == null || goodsId == 0){
            return  ResultInfo.newEmptyParamsResultInfo();
        }
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
