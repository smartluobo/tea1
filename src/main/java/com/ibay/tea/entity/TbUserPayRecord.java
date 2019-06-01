package com.ibay.tea.entity;

import java.util.Date;

public class TbUserPayRecord {
    private Integer id;

    private String oppenId;

    private String orderId;

    private double orderPayment;

    private double payment;

    private Date createTime;

    private Date updateTime;

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
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public double getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(double orderPayment) {
        this.orderPayment = orderPayment;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}