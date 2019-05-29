package com.ibay.tea.common.utils;

import java.math.BigDecimal;

public class PriceCalculateUtil {

    public static BigDecimal activiryPriceCalculate(BigDecimal price, BigDecimal ratio) {
        BigDecimal activityPrice = price.multiply(ratio);
        return activityPrice ;
    }
}
