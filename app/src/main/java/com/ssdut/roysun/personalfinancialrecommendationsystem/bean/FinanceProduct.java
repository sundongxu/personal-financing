package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/5/21.
 * 理财产品
 */
public class FinanceProduct {

    public static final String NAME = "NAME";  // 产品名
    public static final String TYPE = "TYPE";  // 类型，四种：债券型、信托型、挂钩型、QDII型
    public static final String RETURN_RATE = "RETURN_RATE";  // 回报率，百分比
    public static final String INVESTMENT_CYCLE = "INVESTMENT_CYCLE";  // 投资周期，以月为单位
    public static final String MIN_PURCHASE_AMOUNT = "MIN_PURCHASE_AMOUNT";  // 起购金额
    public static final String EXTRA_FEE_PERCENTAGE = "EXTRA_FEE_PERCENTAGE";  // 手续费率

    private int id;
    private String name;
    private String type;
    private double returnRate;
    private double investmentCircle;
    private int minPurchaseAmount;
    private double extraFeePercentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(double returnRate) {
        this.returnRate = returnRate;
    }

    public double getInvestmentCircle() {
        return investmentCircle;
    }

    public void setInvestmentCircle(double investmentCircle) {
        this.investmentCircle = investmentCircle;
    }

    public int getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public void setMinPurchaseAmount(int minPurchaseAmount) {
        this.minPurchaseAmount = minPurchaseAmount;
    }

    public double getExtraFeePercentage() {
        return extraFeePercentage;
    }

    public void setExtraFeePercentage(double extraFeePercentage) {
        this.extraFeePercentage = extraFeePercentage;
    }
}
