package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/4/10.
 */
public class TopTabInfo {
    private String name;  //tab的title
    private int Type;  //类型：（1）看盘（2）投资（3）推荐

    public TopTabInfo(String name, int type) {
        this.name = name;
        this.Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}