package com.ibay.tea.api.controller.category;

import com.ibay.tea.cache.CategoryCache;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/category")
public class ApiCategoryController {

    @Resource
    private CategoryCache categoryCache;

    @RequestMapping("list")
    public List<TbItemCat> list(){
        try {
            return categoryCache.findAll();
        }catch (Exception e){
            return null;
        }
    }
}
