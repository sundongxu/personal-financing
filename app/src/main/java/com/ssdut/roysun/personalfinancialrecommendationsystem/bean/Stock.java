package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/3/12.
 */
public class Stock {
    private String code;  // 股票代码
    private String name;  // 股票名称
    private String time;  // 价格时间
    private String price; // 最新价格
    private String YDP;   // 收盘价格
    private String TDP;   // 开盘价格
    private String ZDP;   // 涨跌额：
    private String LP;    // 最低价格
    private String HP;    // 最高价格
    private String ZDF;   // 涨跌幅
    private String CJL;   // 成交数量
    private String JE;    // 成交额
    private String BP;    // 竞买价
    private String SP;    // 竞卖价
    private String WB;    // 委比
    private String buy1;  // 买一
    private String buy2;  // 买二
    private String buy3;  // 买三
    private String buy4;  // 买四
    private String buy5;  // 买五
    private String sell1; // 卖一
    private String sell2; // 卖二
    private String sell3; // 卖三
    private String sell4; // 卖四
    private String sell5; // 卖五
    private Stock stock;

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stockBean) {
        this.stock = stockBean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYDP() {
        return YDP;
    }

    public void setYDP(String YDP) {
        this.YDP = YDP;
    }

    public String getTDP() {
        return TDP;
    }

    public void setTDP(String TDP) {
        this.TDP = TDP;
    }

    public String getZDP() {
        return ZDP;
    }

    public void setZDP(String ZDP) {
        this.ZDP = ZDP;
    }

    public String getLP() {
        return LP;
    }

    public void setLP(String LP) {
        this.LP = LP;
    }

    public String getHP() {
        return HP;
    }

    public void setHP(String HP) {
        this.HP = HP;
    }

    public String getZDF() {
        return ZDF;
    }

    public void setZDF(String ZDF) {
        this.ZDF = ZDF;
    }

    public String getCJL() {
        return CJL;
    }

    public void setCJL(String CJL) {
        this.CJL = CJL;
    }

    public String getJE() {
        return JE;
    }

    public void setJE(String JE) {
        this.JE = JE;
    }

    public String getBP() {
        return BP;
    }

    public void setBP(String BP) {
        this.BP = BP;
    }

    public String getSP() {
        return SP;
    }

    public void setSP(String SP) {
        this.SP = SP;
    }

    public String getWB() {
        return WB;
    }

    public void setWB(String WB) {
        this.WB = WB;
    }

    public String getBuy1() {
        return buy1;
    }

    public void setBuy1(String buy1) {
        this.buy1 = buy1;
    }

    public String getBuy2() {
        return buy2;
    }

    public void setBuy2(String buy2) {
        this.buy2 = buy2;
    }

    public String getBuy3() {
        return buy3;
    }

    public void setBuy3(String buy3) {
        this.buy3 = buy3;
    }

    public String getBuy4() {
        return buy4;
    }

    public void setBuy4(String buy4) {
        this.buy4 = buy4;
    }

    public String getBuy5() {
        return buy5;
    }

    public void setBuy5(String buy5) {
        this.buy5 = buy5;
    }

    public String getSell1() {
        return sell1;
    }

    public void setSell1(String sell1) {
        this.sell1 = sell1;
    }

    public String getSell2() {
        return sell2;
    }

    public void setSell2(String sell2) {
        this.sell2 = sell2;
    }

    public String getSell3() {
        return sell3;
    }

    public void setSell3(String sell3) {
        this.sell3 = sell3;
    }

    public String getSell4() {
        return sell4;
    }

    public void setSell4(String sell4) {
        this.sell4 = sell4;
    }

    public String getSell5() {
        return sell5;
    }

    public void setSell5(String sell5) {
        this.sell5 = sell5;
    }

}
