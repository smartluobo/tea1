package com.ibay.tea.api.service.map.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ibay.tea.api.responseVo.ApiAddressVo;
import com.ibay.tea.api.service.map.ApiMapService;
import com.ibay.tea.common.utils.EecupMapCalculateUtil;
import com.ibay.tea.common.utils.HttpUtil;
import com.ibay.tea.config.MapSysProperties;
import com.ibay.tea.entity.TbStore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ApiMapServiceImpl implements ApiMapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiMapServiceImpl.class);

    @Resource
    private MapSysProperties mapSysProperties;



    @Override
    public TbStore selectStore(List<TbStore> storeList, Map<String, String> params) {
        if (CollectionUtils.isEmpty(storeList)){
            return null;
        }
        int minDistance = 10000000;
        TbStore targetStore = null;

        String longitude = params.get("longitude");
        String latitude = params.get("latitude");
        //本地计算距离 调用接口选择最近的店铺进行路劲规划
        for (TbStore store : storeList) {
            int storeDistance = EecupMapCalculateUtil.calculateDistance(store.getLongitude(), store.getLatitude(), longitude, latitude);
            if (storeDistance < minDistance){
                minDistance = storeDistance;
                targetStore = store;
            }
        }
        //获取到目标店铺后计算骑行距离
        if (targetStore != null){

        }




        return null;
    }

    @Override
    public List<Object> getAddressList(Map<String, String> params) {
        String cityId = params.get("cityId");
        String longitude = params.get("longitude");
        String latitude = params.get("latitude");
        String location = longitude+","+latitude;
        String keywords = params.get("keywords");
        String url = null;
        if (StringUtils.isEmpty(keywords)){
            url = mapSysProperties.getMapAroundUrl()+mapSysProperties.getKey()+"&location="+location+"&keywords=&types=&offset=20&page=1&extensions=all";
        }else {
            url = mapSysProperties.getMapAddressListUrl()+mapSysProperties.getKey()+"&keywords="+keywords+"&location="+location+"&city="+cityId+"&datatype=all";
        }

        String result = HttpUtil.get(url);
        Map map = JSON.parseObject(result, Map.class);
        if ("OK".equals(map.get("info"))){
            List<Map<String ,Object>> poisList = (List<Map<String, Object>>) map.get("pois");
            if (CollectionUtils.isEmpty(poisList)){
                return null;
            }
            List<ApiAddressVo> apiAddressVoList = new ArrayList<>();
            for (Map<String, Object> itemMap : poisList) {
                ApiAddressVo apiAddressVo = new ApiAddressVo();
                apiAddressVo.setAddress(String.valueOf(itemMap.get("address")));
                apiAddressVo.setAdname(String.valueOf(itemMap.get("adname")));
                apiAddressVo.setName(String.valueOf(itemMap.get("name")));
                apiAddressVo.setLocation(String.valueOf(itemMap.get("location")));
                apiAddressVoList.add(apiAddressVo);
            }

        }
        return null;
    }


}
