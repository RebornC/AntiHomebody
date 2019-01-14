package com.example.yc.androidsrc.model;

/**
 * Created by yc on 2019/1/14.
 */

public class SelfTalkingEntity {

    private String userId;
    private String curDate;
    private String selfTalking;

    public SelfTalkingEntity(String userId, String curDate, String selfTalking) {
        this.userId = userId;
        this.curDate = curDate;
        this.selfTalking = selfTalking;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public void setSelfTalking(String selfTalking) {
        this.selfTalking = selfTalking;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurDate() {
        return curDate;
    }

    public String getSelfTalking() {
        return selfTalking;
    }
}
