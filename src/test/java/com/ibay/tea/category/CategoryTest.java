package com.ibay.tea.category;

import com.ibay.tea.BaseTest;
import com.ibay.tea.dao.CategoryMapper;
import com.ibay.tea.entity.Category;
import org.junit.Test;

import javax.annotation.Resource;

public class CategoryTest extends BaseTest {


    @Resource
    private CategoryMapper categoryMapper;

    @Test
    public void testInsert(){
        Category category = new Category();
        category.setCategoryName("单元测试2");
        category.setStatus(1);
        category.setWeight(6);
        category.setCreateTime("2018-05-17 18:22:55");
        categoryMapper.insert(category);
    }
}
