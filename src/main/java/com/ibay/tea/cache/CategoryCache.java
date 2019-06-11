package com.ibay.tea.cache;

import com.ibay.tea.api.service.category.ApiCategoryService;
import com.ibay.tea.entity.TbItemCat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryCache implements InitializingBean {

    @Resource
    private ApiCategoryService apiCategoryService;


    private Map<String,TbItemCat> categoryCache = new HashMap<>();

    /**
     * 初始化分类缓存
     */
    private void initCategoryCache(){
        List<TbItemCat> categories = apiCategoryService.findAll();
        if (categories != null && categories.size() > 0){
            for (TbItemCat category : categories) {
                categoryCache.put(category.getId()+"",category);
            }
        }
    }

    /**
     * 刷新分类缓存
     */
    public void refreshCategoryCache() {
        categoryCache.clear();
        initCategoryCache();
    }


    public TbItemCat getCategoryFromSysCache(String id){
        return categoryCache.get(id);
    }

    public List<TbItemCat> findAll(){
        List<TbItemCat> categoryList = new ArrayList<>();
        for (Map.Entry<String,TbItemCat> entry : categoryCache.entrySet()){
            categoryList.add(entry.getValue());
        }
        return categoryList;
    }




    @Override
    public void afterPropertiesSet() throws Exception {
        initCategoryCache();
    }


}
