package com.ssdut.roysun.personalfinancialrecommendationsystem.utils;

import java.util.Calendar;

/**
 * Created by roysun on 16/3/12.
 * 时间工具类
 */
public class TimeUtils {
    static final Calendar c = Calendar.getInstance();

    public static int getYear() {
        return c.get(Calendar.YEAR);
    }

    public static int getMonth() {
        //获取的月份和中国相差一个月
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getWeek() {
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return c.get(Calendar.MINUTE);
    }

    public static String getTime() {
        String hour = getHour() + "";
        String minute = getMinute() + "";
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return hour + ":" + minute;
    }

    //由于直接从系统得到的月份和现实月份少一，星期是多一，（欧洲和中国不同的）所以用如下方法得到正确星期
    public static int getWeekOfYear() {
        return getTheWeekOfYear(getYear(), getMonth(), getDay());
    }

    //获取指定日期的星期
    public static int getTheWeekOfDay(int year, int month, int day) {
        Calendar cd = Calendar.getInstance();
        cd.set(year, month - 1, day - 1);
        return cd.get(Calendar.DAY_OF_WEEK);
    }

    //获取指定日期在当月中的第几个星期
    public static int getTheWeekOfYear(int year, int month, int day) {
        Calendar cd = Calendar.getInstance();
        cd.set(year, month - 1, day - 1);
        return cd.get(Calendar.WEEK_OF_YEAR);
    }
}
