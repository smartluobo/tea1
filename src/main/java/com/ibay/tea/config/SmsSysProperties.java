package com.ibay.tea.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "eecup.sys")
@Component
public class SmsSysProperties {

}
