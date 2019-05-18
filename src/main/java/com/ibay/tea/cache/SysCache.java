package com.ibay.tea.cache;

import com.ibay.tea.dao.CategoryMapper;
import com.ibay.tea.entity.Category;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysCache implements InitializingBean {

    @Resource
    private CategoryMapper categoryMapper;


    private Map<String,Category> categoryCache = new HashMap<>();

    public void initCategoryCache(){
        List<Category> categories = categoryMapper.findAll();
        if (categories != null && categories.size() > 0){
            for (Category category : categories) {
                categoryCache.put(category.getId()+"",category);
            }
        }
    }


    public Category getCategoryFromSysCache(String id){
        return categoryCache.get(id);
    }


    public void updateCatrgoryCache(Category category){
        Category oldCategory = categoryCache.get(category.getId().toString());
        if (oldCategory != null){
            categoryCache.remove(category.getId().toString());
            categoryCache.put(category.getId().toString(),category);
        }else {
            categoryCache.put(category.getId().toString(),category);
        }

    }



    @Override
    public void afterPropertiesSet() throws Exception {
        initCategoryCache();
    }
}
