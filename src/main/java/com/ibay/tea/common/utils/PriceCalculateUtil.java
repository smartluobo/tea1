package com.ibay.tea.common.utils;

import com.ibay.tea.entity.TbCoupons;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    public static BigDecimal activityPriceCalculate(BigDecimal price, BigDecimal ratio) {
        BigDecimal activityPrice = price.multiply(ratio);
        return activityPrice ;
    }

    public static double ratioCouponsPriceCalculate(TbCoupons tbCoupons, double orderTotalPrice) {

        return 0.0;
    }
}
