package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/3/12.
 * 天气数据
 */
public class Weather {

    private String location;  //位置
    private String day;  //日期
    private String week;  //周
    private String Temp;  //温度
    private String img1;  //图片1
    private String img2;  //图片2
    private String condition;  //天气
    private String windSpeed;  //风力
    private String indexClothes;  //穿衣
    private String indexTranning;  //晨练

    public String getLocation() {
        return location;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getIndexClothes() {
        return indexClothes;
    }

    public void setIndexClothes(String indexClothes) {
        this.indexClothes = indexClothes;
    }

    public String getIndexTranning() {
        return indexTranning;
    }

    public void setIndexTranning(String indexTranning) {
        this.indexTranning = indexTranning;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
