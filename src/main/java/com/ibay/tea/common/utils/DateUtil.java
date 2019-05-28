package com.ibay.tea.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyyMMdd");

    public static String getDateYyyyMMdd(){
        return yyyyMMddSdf.format(new Date());
    }

    public static String getDateYyyyMMdd(Date date){
        return yyyyMMddSdf.format(date);
    }

    public static int getHour(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getHour(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

}
