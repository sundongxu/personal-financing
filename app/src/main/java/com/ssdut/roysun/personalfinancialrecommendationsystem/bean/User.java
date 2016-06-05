package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/4/29.
 * 用户类，属性共计9个包含：id，用户名，密码，用户头像，注册时间，修改时间，密保问题，答案，管理员标志位
 */
public class User {

    public static final String NAME = "NAME";  // 用户名
    public static final String PASSWORD = "PASSWORD";  // 密码
    public static final String PIC = "PIC";  // 用户头像
    public static final String CREATE_TIME = "CREATE_TIME";  // 注册时间
    public static final String UPDATE_TIME = "UPDATE_TIME";  // 更新时间
    public static final String QUESTION = "QUESTION";  // 密保问题
    public static final String ANSWER = "ANSWER";  // 密保答案
    public static final String BALANCE = "BALANCE";  // 账户余额
    public static final String BUDGET = "BUDGET";  // 本月预算
    public static final String IS_SPECIAL = "IS_SPECIAL";  // 管理员标志位

    private int id;
    private String name;
    private String password;
    private String pic;
    private String createTime;
    private String updateTime;
    private String question;
    private String answer;
    private double balance;
    private int budget;
    private int isSpecial;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setSpecial(int special) {
        isSpecial = special;
    }

    public int isSpecial() {
        return isSpecial;
    }
}
