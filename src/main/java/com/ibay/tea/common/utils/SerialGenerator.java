package com.ibay.tea.common.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SerialGenerator {

    private static AtomicInteger orderIndex = new AtomicInteger(50);

    public static String getOrderSerial(){
        int orderSerial = orderIndex.getAndIncrement();
        String dateYyyyMMddHHmmss = DateUtil.getDateYyyyMMddHHmmss();
        return dateYyyyMMddHHmmss + orderSerial;
    }
}
