package com.ibay.tea.cms.controller.goods;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.entity.TbItem;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("cms/goods")
public class CmsGoodsController {

    @Resource
    private CmsGoodsService cmsGoodsService;

    @PostMapping("/list")
    @ResponseBody
    public ResultInfo listByPage(@RequestBody Map<String,Integer> params){
       try {
           if (CollectionUtils.isEmpty(params)){
               return ResultInfo.newEmptyParamsResultInfo();
           }
       	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
           long total = cmsGoodsService.countGoodsByCondition(new HashMap<>());
           int pageNum = params.get("pageNum");
           int pageSize = params.get("pageSize");
           List<TbItem> goodsList = cmsGoodsService.findGoodsListByPage(pageNum,pageSize);
           resultInfo.setTotal(total);
           resultInfo.setData(goodsList);
       	return resultInfo;
       }catch (Exception e){
       	return ResultInfo.newExceptionResultInfo();
       }

    }

    @RequestMapping("/add")
    public ResultInfo addGoods(@RequestBody TbItem tbItem){

        if (tbItem == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsGoodsService.addGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteGoods(@PathVariable("id") long id){
        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsGoodsService.deleteGoods(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateGoods(@RequestBody TbItem tbItem){

        if (tbItem == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsGoodsService.updateGoods(tbItem);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }



}
