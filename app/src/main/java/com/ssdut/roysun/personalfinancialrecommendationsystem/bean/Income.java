package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

/**
 * Created by roysun on 16/3/12.
 * 收入
 */
public class Income {

    public static final String CATEGORY = "CATEGORY";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String WEEK = "WEEK";
    public static final String DAY = "DAY";
    public static final String TIME = "TIME";
    public static final String AMOUNT = "AMOUNT";
    public static final String REMARK = "REMARK";

    // 收入条目id
    private int id;
    // 在那方面收入
    private String category = "";
    //收入年份
    private int year = TimeUtils.getYear();
    //收入月份
    private int month = TimeUtils.getMonth();
    //收入周(月中的某周)
    private int week = TimeUtils.getWeekOfYear();
    //收入日
    private int day = TimeUtils.getDay();
    //收入时间
    private String time = TimeUtils.getHour() + ":" + TimeUtils.getMinute();
    //收入总数
    private double amount = 0;
    //收入备注
    private String remark = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
