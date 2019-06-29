package com.ibay.tea.common.utils;

import java.math.BigDecimal;

public class EecupMapCalculateUtil {

    private static final double EARTH_RADIUS = 6378.137;//地球半径,单位千米

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public static int calculateDistance(String startLongitude ,String startLatitude,String endLongitude ,String endLatitude){
        double startLng = new BigDecimal(startLongitude).doubleValue();
        double endLng = new BigDecimal(endLongitude).doubleValue();

        double startLat = new BigDecimal(startLatitude).doubleValue();
        double endLat = new BigDecimal(endLatitude).doubleValue();


        double radLat1 = rad(startLat);
        double radLat2 = rad(endLat);
        double a = radLat1 - radLat2;
        double b = rad(startLng) - rad(endLng);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000);

        return (int)s;
    }

    public static void main(String[] args) {
        int distance = calculateDistance("114.037", "22.616", "114.0477", "22.6231");
        System.out.println(distance);
    }

}
