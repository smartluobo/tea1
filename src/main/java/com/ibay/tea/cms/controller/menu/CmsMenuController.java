package com.ibay.tea.cms.controller.menu;

import com.ibay.tea.api.response.ResultInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/cms/menu")
public class CmsMenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsMenuController.class);

    @RequestMapping("/findUserMenu/{menuIds}")
    public ResultInfo findUserMenu(@PathVariable("menuIds") String menuIds){
        if (StringUtils.isEmpty(menuIds)){
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms findUserMenu happen exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }
}
