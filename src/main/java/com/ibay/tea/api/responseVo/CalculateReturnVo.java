package com.ibay.tea.api.responseVo;

import com.ibay.tea.entity.TbItem;

import java.util.List;

public class CalculateReturnVo {
    //订单总金额
    private double orderTotalAmount;
    //订单支付金额
    private double orderPayAmount;
    //订单优惠金额
    private double orderReduceAmount;
    //优惠策略名称
    private String couponsName;
    //0-无优惠 1 满五赠一系列 2 满减系列 3 优惠券
    private int couponsType;

    private List<TbItem> goodsList;

    private TbItem goodsInfo;

    private int userCouponsId;

    private String userCouponsName;

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public double getOrderPayAmount() {
        return orderPayAmount;
    }

    public void setOrderPayAmount(double orderPayAmount) {
        this.orderPayAmount = orderPayAmount;
    }

    public double getOrderReduceAmount() {
        return orderReduceAmount;
    }

    public void setOrderReduceAmount(double orderReduceAmount) {
        this.orderReduceAmount = orderReduceAmount;
    }

    public String getCouponsName() {
        return couponsName;
    }

    public void setCouponsName(String couponsName) {
        this.couponsName = couponsName;
    }

    public int getCouponsType() {
        return couponsType;
    }

    public void setCouponsType(int couponsType) {
        this.couponsType = couponsType;
    }

    public List<TbItem> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<TbItem> goodsList) {
        this.goodsList = goodsList;
    }

    public TbItem getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(TbItem goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public int getUserCouponsId() {
        return userCouponsId;
    }

    public void setUserCouponsId(int userCouponsId) {
        this.userCouponsId = userCouponsId;
    }

    public String getUserCouponsName() {
        return userCouponsName;
    }

    public void setUserCouponsName(String userCouponsName) {
        this.userCouponsName = userCouponsName;
    }

    @Override
    public String toString() {
        return "CalculateReturnVo{" +
                "orderTotalAmount=" + orderTotalAmount +
                ", orderPayAmount=" + orderPayAmount +
                ", orderReduceAmount=" + orderReduceAmount +
                ", couponsName='" + couponsName + '\'' +
                ", couponsType=" + couponsType +
                ", goodsList=" + goodsList +
                ", goodsInfo=" + goodsInfo +
                ", userCouponsId=" + userCouponsId +
                ", userCouponsName='" + userCouponsName + '\'' +
                '}';
    }
}
