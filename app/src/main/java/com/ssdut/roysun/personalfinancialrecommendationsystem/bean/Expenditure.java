package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

import com.ssdut.roysun.personalfinancialrecommendationsystem.utils.TimeUtils;

/**
 * Created by roysun on 16/3/12.
 * 开销
 */
public class Expenditure {

    public static final String CATEGORY = "CATEGORY";
    public static final String SUB_CATEGORY = "SUB_CATEGORY";
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String WEEK = "WEEK";
    public static final String DAY = "DAY";
    public static final String TIME = "TIME";
    public static final String AMOUNT = "AMOUNT";
    public static final String REMARK = "REMARK";
    public static final String PIC = "PIC";

    // 支出条目ID
    private int id;
    // 在那方面支出
    private String category = "";
    //项目的子项目
    private String subCategory = "";
    // 支出年份
    private int year = TimeUtils.getYear();
    // 支出月份
    private int month = TimeUtils.getMonth();
    // 支出星期（一月中的某一周）
    private int week = TimeUtils.getWeekOfYear();
    // 支出天
    private int day = TimeUtils.getDay();
    // 支出时间
    private String time = TimeUtils.getHour() + ":" + TimeUtils.getMinute();
    // 支出数量
    private double amount = 0;
    // 支出备注
    private String remark = "";
    //照片位置
    private String pic = "";

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

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
