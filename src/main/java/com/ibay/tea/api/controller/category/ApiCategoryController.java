package com.ibay.tea.api.controller.category;

import com.ibay.tea.api.service.category.ApiCategoryService;
import com.ibay.tea.entity.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/category")
public class ApiCategoryController {

    private ApiCategoryService apiCategoryService;

    @RequestMapping("list")
    public List<Category> list(){
        return null;
    }
}
