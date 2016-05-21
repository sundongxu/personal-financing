package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/3/12.
 * 备忘条目
 */
public class MemoContent {
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String WEEK = "WEEK";
    public static final String DAY = "DAY";
    public static final String TIME = "TIME";
    public static final String CONTENT = "CONTENT";
    public static final String PIC = "PIC";
    public static final String BG_COLOR = "BG_COLOR";
    public static final String TEXT_SIZE = "TEXT_SIZE";

    private int id;  //条目id
    private int year;  //记事年
    private int month;  //记事月
    private int week;  //记事周（星期几）
    private int day;  //记事日
    private String time;  //记事时间
    private String content;  //记事内容
    private String pic;  //记事图片路径
    private int color;  //记事背景色
    private float size;  //记事字体大小

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
