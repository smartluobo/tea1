package com.ibay.tea.api.responseVo;

public class CalculateReturnVo {
    private double orderTotalAmount;
    private double orderPayAmount;
    private double orderReduceAmount;
    private String couponsName;

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
}
