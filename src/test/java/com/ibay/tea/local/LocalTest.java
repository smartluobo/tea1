package com.ibay.tea.local;

import java.util.Random;

public class LocalTest {

    public static void main(String[] args) {
        Random random = new Random();

        int firstCount = 0;
        int secondCount = 0;
        int threeCount = 0;
        int fourCount = 0;
        for (int i = 0; i < 100; i++) {
            int r = random.nextInt(100);
            if (r <= 10){
                firstCount++;
            }else if (secondCount <= 30){
                secondCount++;
            }else if (r <= 60){
                threeCount++;
            }else {
                fourCount++;
            }
        }
        System.out.println("firstCount : "+firstCount+" secondCount : "+ secondCount+" threeCount : "+ threeCount+" fourCount : "+fourCount);
    }
}
