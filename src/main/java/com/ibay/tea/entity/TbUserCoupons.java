package com.ibay.tea.entity;

import com.ibay.tea.common.utils.DateUtil;

import java.util.Date;

public class TbUserCoupons {

    private Integer id;

    private String oppenId;

    private Integer couponsId;

    private String couponsName;

    private Integer receiveDate;

    private Date createTime;

    private Integer status;

    private String couponsPoster;

    private Date expireDate;

    private String expireDateStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId;
    }

    public Integer getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(Integer couponsId) {
        this.couponsId = couponsId;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName == null ? null : couponsName.trim();
    }

    public Integer getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Integer receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCouponsPoster() {
        return couponsPoster;
    }

    public void setCouponsPoster(String couponsPoster) {
        this.couponsPoster = couponsPoster;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        if (expireDate != null){
            this.expireDateStr = DateUtil.formatExpireDate(expireDate);
        }
        this.expireDate = expireDate;
    }

    public String getExpireDateStr() {
        return expireDateStr;
    }

    public void setExpireDateStr(String expireDateStr) {
        if (this.expireDateStr == null){
            this.expireDateStr = expireDateStr;
        }
    }
}