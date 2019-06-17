package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbCoupons;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    public static BigDecimal activityPriceCalculate(BigDecimal price, BigDecimal ratio) {
        BigDecimal activityPrice = price.multiply(ratio);
        return activityPrice ;
    }

    public static double ratioCouponsPriceCalculate(TbCoupons tbCoupons, double orderTotalPrice) {

        BigDecimal realAmount = new BigDecimal(tbCoupons.getCouponsRatio()).multiply(new BigDecimal(orderTotalPrice));
        BigDecimal reduceAmount = new BigDecimal(orderTotalPrice).subtract(realAmount);
        return reduceAmount.doubleValue();
    }

    public static double multy(double price, String couponsRatio) {
        BigDecimal multiply = new BigDecimal(price).multiply(new BigDecimal(couponsRatio));
        return multiply.doubleValue();
    }
}
