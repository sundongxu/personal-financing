package com.ssdut.roysun.personalfinancialrecommendationsystem.bean;

/**
 * Created by roysun on 16/4/29.
 * 用户类，属性共计9个包含：id，用户名，密码，用户头像，注册时间，修改时间，密保问题，答案，管理员标志位
 */
public class User {

    public static final String NAME = "NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String PIC = "PIC";
    public static final String CREATE_TIME = "CREATE_TIME";
    public static final String UPDATE_TIME = "UPDATE_TIME";
    public static final String QUESTION = "QUESTION";
    public static final String ANSWER = "ANSWER";
    public static final String IS_SPECIAL = "IS_SPECIAL";

    private int id;
    private String name;
    private String password;
    private String pic;
    private String createTime;
    private String updateTime;
    private String question;
    private String answer;
    private int isSpecial;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setSpecial(int special) {
        isSpecial = special;
    }

    public int isSpecial() {
        return isSpecial;
    }
}
