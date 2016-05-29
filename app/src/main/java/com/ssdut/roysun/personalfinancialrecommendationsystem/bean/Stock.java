package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/3/12.
 * 股票
 */
public class Stock {

    //为了支持购买模拟投资功能，还可以增加以下三个字段

    public static final String WATCHER_NAME = "WATCHER_NAME"; // 关注本股用户名
//    public static final String BUYER_NAME = "BUYER_NAME";  // 买家用户名
    public static final String BUY_NUMBER = "BUY_NUMBER";  // 买家买了多少股

    public static final String NAME = "NAME";  // 股票名称
    public static final String CODE = "CODE";  // 股票代码
    public static final String NOW_PRICE = "NOW_PRICE";  // 当前价格
    public static final String INCREASE_AMOUNT = "INCREASE_AMOUNT";  // 涨跌额
    public static final String INCREASE_PERSENTAGE = "INCREASE_PERSENTAGE";  // 涨跌百分比

    public static final String TODAY_START_PRICE = "TODAY_START_PRICE";  // 今日开盘价
    public static final String YESTERDAY_END_PRICE = "YESTERDAY_END_PRICE";  // 昨日收盘价
    public static final String TODAY_MAX_PRICE = "TODAY_MAX_PRICE";  // 今日最高价
    public static final String TODAY_MIN_PRICE = "TODAY_MIN_PRICE";  // 今日最低价
    public static final String COMPETITIVE_PRICE = "COMPETITIVE_PRICE";  // 竞买价
    public static final String RESERVE_PRICE = "RESERVE_PRICE"; // 竞卖价
    public static final String DEAL_NUMBER = "DEAL_NUMBER";  // 成交量
    public static final String DEAL_PRICE = "DEAL_PRICE";  // 成交价

    public static final String BUY_ONE = "BUY_ONE", BUY_ONE_PRICE = "BUY_ONE_PRICE";
    public static final String BUY_TWO = "BUY_TWO", BUY_TWO_PRICE = "BUY_TWO_PRICE";
    public static final String BUY_THREE = "BUY_THREE", BUY_THREE_PRICE = "BUY_THREE_PRICE";
    public static final String BUY_FOUR = "BUY_FOUR", BUY_FOUR_PRICE = "BUY_FOUR_PRICE";
    public static final String BUY_FIVE = "BUY_FIVE", BUY_FIVE_PRICE = "BUY_FIVE_PRICE";

    public static final String SELL_ONE = "SELL_ONE", SELL_ONE_PRICE = "SELL_ONE_PRICE";
    public static final String SELL_TWO = "SELL_TWO", SELL_TWO_PRICE = "SELL_TWO_PRICE";
    public static final String SELL_THREE = "SELL_THREE", SELL_THREE_PRICE = "SELL_THREE_PRICE";
    public static final String SELL_FOUR = "SELL_FOUR", SELL_FOUR_PRICE = "SELL_FOUR_PRICE";
    public static final String SELL_FIVE = "SELL_FIVE", SELL_FIVE_PRICE = "SELL_FIVE_PRICE";

    public static final String DATE = "DATE";
    public static final String TIME = "TIME";

    // 预留模拟数据，6-12-24-72天的历史价格
    private int id;
    private String watcherName;
//    private String buyerName;
    private int buyNum;
    private String code;
    private String name;
    private double nowPrice;
    private double increasePersentage;
    private double increaseAmount;

    private double todayStartPrice;
    private double yesterdayEndPrice;
    private double todayMaxPrice;
    private double todayMinPrice;
    private double competitivePrice;
    private double reservePrice;
    private int dealNumber;
    private double dealPrice;

    private int buyOne, buyTwo, buyThree, buyFour, buyFive;
    private double buyOnePrice, buyTwoPrice, buyThreePrice, buyFourPrice, buyFivePrice;
    private int sellOne, sellTwo, sellThree, sellFour, sellFive;
    private double sellOnePrice, sellTwoPrice, sellThreePrice, sellFourPrice, sellFivePrice;

    private String date;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWatcherName() {
        return watcherName;
    }

    public void setWatcherName(String watcherName) {
        this.watcherName = watcherName;
    }

//    public String getBuyerName() {
//        return buyerName;
//    }
//
//    public void setBuyerName(String buyerName) {
//        this.buyerName = buyerName;
//    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getIncreasePersentage() {
        return increasePersentage;
    }

    public void setIncreasePersentage(double increasePersentage) {
        this.increasePersentage = increasePersentage;
    }

    public double getIncreaseAmount() {
        return increaseAmount;
    }

    public void setIncreaseAmount(double increaseAmount) {
        this.increaseAmount = increaseAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTodayStartPrice() {
        return todayStartPrice;
    }

    public void setTodayStartPrice(double todayStartPrice) {
        this.todayStartPrice = todayStartPrice;
    }

    public double getYesterdayEndPrice() {
        return yesterdayEndPrice;
    }

    public void setYesterdayEndPrice(double yesterdayEndPrice) {
        this.yesterdayEndPrice = yesterdayEndPrice;
    }

    public double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public double getTodayMaxPrice() {
        return todayMaxPrice;
    }

    public void setTodayMaxPrice(double todayMaxPrice) {
        this.todayMaxPrice = todayMaxPrice;
    }

    public double getTodayMinPrice() {
        return todayMinPrice;
    }

    public void setTodayMinPrice(double todayMinPrice) {
        this.todayMinPrice = todayMinPrice;
    }

    public double getCompetitivePrice() {
        return competitivePrice;
    }

    public void setCompetitivePrice(double competitivePrice) {
        this.competitivePrice = competitivePrice;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public int getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(int dealNumber) {
        this.dealNumber = dealNumber;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public int getBuyOne() {
        return buyOne;
    }

    public void setBuyOne(int buyOne) {
        this.buyOne = buyOne;
    }

    public int getBuyTwo() {
        return buyTwo;
    }

    public void setBuyTwo(int buyTwo) {
        this.buyTwo = buyTwo;
    }

    public int getBuyThree() {
        return buyThree;
    }

    public void setBuyThree(int buyThree) {
        this.buyThree = buyThree;
    }

    public int getBuyFour() {
        return buyFour;
    }

    public void setBuyFour(int buyFour) {
        this.buyFour = buyFour;
    }

    public int getBuyFive() {
        return buyFive;
    }

    public void setBuyFive(int buyFive) {
        this.buyFive = buyFive;
    }

    public double getBuyOnePrice() {
        return buyOnePrice;
    }

    public void setBuyOnePrice(double buyOnePrice) {
        this.buyOnePrice = buyOnePrice;
    }

    public double getBuyTwoPrice() {
        return buyTwoPrice;
    }

    public void setBuyTwoPrice(double buyTwoPrice) {
        this.buyTwoPrice = buyTwoPrice;
    }

    public double getBuyThreePrice() {
        return buyThreePrice;
    }

    public void setBuyThreePrice(double buyThreePrice) {
        this.buyThreePrice = buyThreePrice;
    }

    public double getBuyFourPrice() {
        return buyFourPrice;
    }

    public void setBuyFourPrice(double buyFourPrice) {
        this.buyFourPrice = buyFourPrice;
    }

    public double getBuyFivePrice() {
        return buyFivePrice;
    }

    public void setBuyFivePrice(double buyFivePrice) {
        this.buyFivePrice = buyFivePrice;
    }

    public int getSellOne() {
        return sellOne;
    }

    public void setSellOne(int sellOne) {
        this.sellOne = sellOne;
    }

    public int getSellTwo() {
        return sellTwo;
    }

    public void setSellTwo(int sellTwo) {
        this.sellTwo = sellTwo;
    }

    public int getSellThree() {
        return sellThree;
    }

    public void setSellThree(int sellThree) {
        this.sellThree = sellThree;
    }

    public int getSellFour() {
        return sellFour;
    }

    public void setSellFour(int sellFour) {
        this.sellFour = sellFour;
    }

    public int getSellFive() {
        return sellFive;
    }

    public void setSellFive(int sellFive) {
        this.sellFive = sellFive;
    }

    public double getSellOnePrice() {
        return sellOnePrice;
    }

    public void setSellOnePrice(double sellOnePrice) {
        this.sellOnePrice = sellOnePrice;
    }

    public double getSellTwoPrice() {
        return sellTwoPrice;
    }

    public void setSellTwoPrice(double sellTwoPrice) {
        this.sellTwoPrice = sellTwoPrice;
    }

    public double getSellThreePrice() {
        return sellThreePrice;
    }

    public void setSellThreePrice(double sellThreePrice) {
        this.sellThreePrice = sellThreePrice;
    }

    public double getSellFourPrice() {
        return sellFourPrice;
    }

    public void setSellFourPrice(double sellFourPrice) {
        this.sellFourPrice = sellFourPrice;
    }

    public double getSellFivePrice() {
        return sellFivePrice;
    }

    public void setSellFivePrice(double sellFivePrice) {
        this.sellFivePrice = sellFivePrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
