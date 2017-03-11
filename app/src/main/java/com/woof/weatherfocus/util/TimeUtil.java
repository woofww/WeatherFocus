package com.woof.weatherfocus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.HOUR_OF_DAY;

/**
 * Created by Woof on 2/25/2017.
 */

public class TimeUtil {


    Calendar mCalendar = Calendar.getInstance();

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 需要判断的时间
     * @return dayForWeek 判断结果
     */
    public static String dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        String week = "";
        dayForWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayForWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    public static int getSystemCurrentHour(int currentHour) {

        if (currentHour > 6 && currentHour < 16) {
            return 1;
        } else if (currentHour >= 16 && currentHour <= 19) {
            return 2;
        } else if ((currentHour > 19 && currentHour < 24) || (currentHour >=0 && currentHour <= 6)){
            return 3;
        }
        return 0;
    }

}
