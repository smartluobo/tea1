package com.ibay.tea.cms.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.login.CmsLoginService;
import com.ibay.tea.entity.TbCmsUser;
import org.apache.ibatis.annotations.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin
@RequestMapping("cms/login")
public class CmsLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsLoginController.class);

    @Resource
    private CmsLoginService loginService;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResultInfo login(HttpServletRequest request,@RequestBody TbCmsUser tbCmsUser){

        if (tbCmsUser == null){
            LOGGER.error("login user info is null");
            return ResultInfo.newEmptyParamsResultInfo();
        }
        try {
            LOGGER.error("login user info :{}",tbCmsUser);
            TbCmsUser dbUser = loginService.login(tbCmsUser);
            if (dbUser == null){
                return ResultInfo.newEmptyParamsResultInfo();
            }
            //处理session
            request.getSession().setAttribute("user",dbUser);
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            dbUser.setPassword("********");
            resultInfo.setData(dbUser);
            LOGGER.info("login success ....");
            return resultInfo;
        }catch (Exception e){
            LOGGER.error("cms user login happen Exception",e);
            return ResultInfo.newExceptionResultInfo();
        }

    }

    @PostMapping(value = "/test")
    @ResponseBody
    public ResultInfo test(){
        try {
            ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
            resultInfo.setData("hello world");
            return resultInfo;
        }catch (Exception e){
            return ResultInfo.newExceptionResultInfo();
        }
    }

}
