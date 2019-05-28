package com.ibay.tea.cms.controller.goods;

import com.ibay.tea.cms.service.goods.CmsGoodsService;
import com.ibay.tea.common.cms.response.DefaultJsonResponse;
import com.ibay.tea.common.utils.DataTableUtil;
import com.ibay.tea.common.utils.PageUtil;
import com.ibay.tea.entity.Goods;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("cms/goods")
public class CmsGoodsController {

    @Resource
    private CmsGoodsService cmsGoodsService;

    @RequestMapping("list")
    private Object findGoodsListByPage(@PathVariable("pageUtil") PageUtil pageUtil){
        DefaultJsonResponse<DataTableUtil> response = new DefaultJsonResponse<>();
        Integer pageNum = pageUtil.getPage();
        Integer pageSize = pageUtil.getSize();
        long total = cmsGoodsService.countGoodsByCondition(new HashMap<>());
        List<Goods> goodsList = cmsGoodsService.findGoodsListByPage(pageNum,pageSize);
        DataTableUtil dataTableUtil = new DataTableUtil(total, total, goodsList, null);
        response.setResult(dataTableUtil);
        return response;
    }

}
