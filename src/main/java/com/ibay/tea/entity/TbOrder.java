package com.ibay.tea.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TbOrder {
    private String orderId;

    private BigDecimal payment;

    private Integer paymentType;

    private BigDecimal postFee;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Date paymentTime;

    private Date consignTime;

    private Date endTime;

    private Date closeTime;

    private String shippingName;

    private String shippingCode;

    private Long userId;

    private String buyerMessage;

    private String buyerNick;

    private Boolean buyerComment;

    private String oppenId;

    private Integer selfGet;

    private String address;

    private String phoneNum;

    private String posterUrl;

    //订单商品数量
    private int goodsTotalCount = 1;

    //订单支付金额
    private double orderPayment;

    //使用的优惠券id
    private long userCouponsId;

    //优惠券减少金额
    private double couponsReduceAmount;

    //制作完成消息发送状态 0 未发送 1发送成功 2 发送失败
    private int makeCompleteSendStatus;

    //订单关闭消息发送状态 0未发送 1发送成功 2发送失败
    private int closeSendStatus;

    //订单店铺id
    private int storeId;

    //订单商铺名称
    private String storeName;

    private String goodsName;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName == null ? null : shippingName.trim();
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode == null ? null : shippingCode.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage == null ? null : buyerMessage.trim();
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick == null ? null : buyerNick.trim();
    }

    public Boolean getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(Boolean buyerComment) {
        this.buyerComment = buyerComment;
    }

    public String getOppenId() {
        return oppenId;
    }

    public void setOppenId(String oppenId) {
        this.oppenId = oppenId == null ? null : oppenId.trim();
    }

    public Integer getSelfGet() {
        return selfGet;
    }

    public void setSelfGet(Integer selfGet) {
        this.selfGet = selfGet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl == null ? null : posterUrl.trim();
    }

    public int getGoodsTotalCount() {
        return goodsTotalCount;
    }

    public void setGoodsTotalCount(int goodsTotalCount) {
        this.goodsTotalCount = goodsTotalCount;
    }

    public double getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(double orderPayment) {
        this.orderPayment = orderPayment;
    }

    public long getUserCouponsId() {
        return userCouponsId;
    }

    public void setUserCouponsId(long userCouponsId) {
        this.userCouponsId = userCouponsId;
    }

    public double getCouponsReduceAmount() {
        return couponsReduceAmount;
    }

    public void setCouponsReduceAmount(double couponsReduceAmount) {
        this.couponsReduceAmount = couponsReduceAmount;
    }

    public int getMakeCompleteSendStatus() {
        return makeCompleteSendStatus;
    }

    public void setMakeCompleteSendStatus(int makeCompleteSendStatus) {
        this.makeCompleteSendStatus = makeCompleteSendStatus;
    }

    public int getCloseSendStatus() {
        return closeSendStatus;
    }

    public void setCloseSendStatus(int closeSendStatus) {
        this.closeSendStatus = closeSendStatus;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}