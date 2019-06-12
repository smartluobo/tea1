package com.ibay.tea.api.service.token.impl;

import com.alibaba.fastjson.JSON;
import com.ibay.tea.api.config.WechatInfoProperties;
import com.ibay.tea.api.service.token.WechatTokenService;
import com.ibay.tea.common.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class WechatTokenServiceImpl implements WechatTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatTokenServiceImpl.class);

    @Resource
    private WechatInfoProperties wechatInfoProperties;

    @Override
    public String getToken() {
        Map<String,Object> param = new HashMap<>();
        param.put("grant_type","client_credential");
        param.put("appid",wechatInfoProperties.getAppId());
        param.put("secret",wechatInfoProperties.getSecret());
        String result = HttpUtil.get(wechatInfoProperties.getTokenUrl(), param);
        LOGGER.info("from wechat server remote get Token return result : {}",result);
        Map resultMap = JSON.parseObject(result, Map.class);
        LOGGER.info("from wechat server remote get Token return result swap map :{}",resultMap);
        Object token = resultMap.get("access_token");
        LOGGER.info("from wechat server remote get Token,token value : {} ",token);
        if (null != token){
            return token.toString();
        }
        return null;
    }
}
