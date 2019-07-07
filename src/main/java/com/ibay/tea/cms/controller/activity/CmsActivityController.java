package com.ibay.tea.cms.controller.activity;

import com.ibay.tea.api.response.ResultInfo;
import com.ibay.tea.cms.service.activity.CmsActivityService;
import com.ibay.tea.entity.TbActivity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cms/activity")
public class CmsActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsActivityController.class);

    @Resource
    private CmsActivityService cmsActivityService;

    @RequestMapping("/list")
    public ResultInfo list(){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	List<TbActivity> activityList =cmsActivityService.findAll();
            resultInfo.setData(activityList);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/add")
    public ResultInfo addActivity(@RequestBody TbActivity tbActivity){

        if (tbActivity == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.addActivity(tbActivity);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/delete/{id}")
    public ResultInfo deleteActivity(@PathVariable("id") int id){

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.deleteActivity(id);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }

    @RequestMapping("/update")
    public ResultInfo updateActivity(@RequestBody TbActivity tbActivity){

        if (tbActivity == null){
        	return ResultInfo.newEmptyParamsResultInfo();
        }

        try {
        	ResultInfo resultInfo = ResultInfo.newSuccessResultInfo();
        	cmsActivityService.updateActivity(tbActivity);
        	return resultInfo;
        }catch (Exception e){
        	return ResultInfo.newExceptionResultInfo();
        }

    }



}
