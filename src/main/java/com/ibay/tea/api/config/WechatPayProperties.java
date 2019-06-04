package com.ibay.tea.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "wechat.pay")
@Component
public class WechatPayProperties {

    //创建预支付订单地址
    private String createOrderUrl;
    //小程序id
    private String appId;
    //商户id
    private String mchId;
    //签名类型
    private String signType;
    //登陆地址
    private String loginUrl;
    //小程序后台配置的随机字符串
    private String secret;

    public String getCreateOrderUrl() {
        return createOrderUrl;
    }

    public void setCreateOrderUrl(String createOrderUrl) {
        this.createOrderUrl = createOrderUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
