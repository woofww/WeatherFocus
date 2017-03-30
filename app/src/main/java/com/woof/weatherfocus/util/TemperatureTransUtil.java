package com.woof.weatherfocus.util;

/**
 * Created by Woof on 3/13/2017.
 */

public class TemperatureTransUtil {

    public static String CTOF(String Celsius) {
        int C = Integer.parseInt(Celsius);
        double T = C * 1.8 + 32;
        return String.valueOf(T);
    }

    public static float CTOF(float Celsius) {
        return (float) (Celsius * 1.8 + 32);
    }
}
