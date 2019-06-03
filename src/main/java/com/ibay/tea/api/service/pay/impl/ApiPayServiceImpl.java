package com.ibay.tea.api.service.pay.impl;

import com.ibay.tea.api.factory.XStreamFactory;
import com.ibay.tea.api.request.WechatCreateOrderRequest;
import com.ibay.tea.api.service.pay.ApiPayService;
import com.ibay.tea.api.service.wechat.WechatSendService;
import com.ibay.tea.common.utils.WechatSignUtil;
import com.ibay.tea.common.utils.WechatXmlParser;
import com.ibay.tea.entity.TbOrder;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ApiPayServiceImpl implements ApiPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayServiceImpl.class);

    String apiKey = "1234564565";

    @Resource
    private WechatSendService wechatSendService;

    @Override
    public void createPayOrderToWechat(TbOrder tbOrder) throws Exception{

        WechatCreateOrderRequest request = buildWechatCreateOrderRequest(tbOrder);
        String sendXml = getSendXml(request);
        String wechatServerReturnResult = wechatSendService.sendDataToWechatServer(sendXml);
        if (StringUtils.isNotEmpty(wechatServerReturnResult)){
            Map<String, Object> resultMap = WechatXmlParser.getMapFromXML(wechatServerReturnResult);
            //对结果做处理
        }


    }

    private WechatCreateOrderRequest buildWechatCreateOrderRequest(TbOrder tbOrder) {
        WechatCreateOrderRequest wechatCreateOrderRequest = new WechatCreateOrderRequest();
        return wechatCreateOrderRequest;
    }

    private String getSendXml(WechatCreateOrderRequest request) {
        String sign = WechatSignUtil.getSignForObject(request, apiKey);
        request.setSign(sign);
        XStream st = XStreamFactory.getRequestXstream();
        String resultXml = st.toXML(request);
        return resultXml;
    }
}
