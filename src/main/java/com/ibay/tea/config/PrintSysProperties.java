package com.ibay.tea.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "print.sys")
@Component
public class PrintSysProperties {

    private String printUserName;
    private String printUKey;
    private String printUrl;
    public String getPrintUserName() {
        return printUserName;
    }

    public void setPrintUserName(String printUserName) {
        this.printUserName = printUserName;
    }

    public String getPrintUKey() {
        return printUKey;
    }

    public void setPrintUKey(String printUKey) {
        this.printUKey = printUKey;
    }

    public String getPrintUrl() {
        return printUrl;
    }

    public void setPrintUrl(String printUrl) {
        this.printUrl = printUrl;
    }
}
