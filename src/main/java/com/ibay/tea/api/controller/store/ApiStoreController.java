package com.ibay.tea.api.controller.store;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.api.service.store.ApiStoreService;
import com.ibay.tea.cache.StoreCache;
import com.ibay.tea.entity.TbStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/store")
public class ApiStoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiStoreController.class);

    @Resource
    private StoreCache storeCache;

    @RequestMapping("/list")
    public ResultInfo list(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            List<TbStore> all = storeCache.getStoreList();
            resultInfo.setData(all);
            return  resultInfo;
        }catch (Exception e){
            LOGGER.error("findAll store info happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }
    }
}
