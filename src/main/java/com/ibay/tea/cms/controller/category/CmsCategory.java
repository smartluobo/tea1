package com.ibay.tea.cms.controller.category;

import com.ibay.tea.entity.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cms/category")
public class CmsCategory {

    @RequestMapping("list")
    public List<Category> list(){
        return null;
    }
}
