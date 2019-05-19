package com.ibay.tea.api.service.category.impl;

import com.ibay.tea.api.service.category.ApiCategoryService;
import com.ibay.tea.dao.CategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ApiCategoryServiceImpl implements ApiCategoryService {

    @Resource
    private CategoryMapper categoryMapper;
}
