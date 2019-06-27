package com.ibay.tea.common.service.impl;

import com.ibay.tea.common.service.SendSmsService;
import com.ibay.tea.config.SmsSysProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SendSmsServiceImpl implements SendSmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendSmsServiceImpl.class);
    @Resource
    private SmsSysProperties smsSysProperties;


}
