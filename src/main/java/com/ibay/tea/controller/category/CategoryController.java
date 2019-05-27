package com.ibay.tea.controller.category;

import com.ibay.tea.cache.CategoryCache;
import com.ibay.tea.dao.CategoryMapper;
import com.ibay.tea.entity.Category;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryCache sysCache;

    @RequestMapping("/list")
    public List<Category> list(){
        return categoryMapper.findAll();
    }

    @RequestMapping("/findById/{id}")
    public Category findById(@PathVariable("id") String id){
        return sysCache.getCategoryFromSysCache(id);
    }

}
