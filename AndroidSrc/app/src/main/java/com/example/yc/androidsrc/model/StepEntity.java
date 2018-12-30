package com.example.yc.androidsrc.model;

/**
 * @author RebornC
 * Created on 2018/12/11.
 */

public class StepEntity {

    private String userId;
    private String curDate;
    private String stepCount;

    public StepEntity(String userId, String curDate, String stepCount) {
        this.userId = userId;
        this.curDate = curDate;
        this.stepCount = stepCount;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurDate() {
        return curDate;
    }

    public String getStepCount() {
        return stepCount;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public void setStepCount(String stepCount) {
        this.stepCount = stepCount;
    }

    @Override
    public String toString() {
        return "StepEntity{" +
                "userId='" + userId + '\'' +
                "curDate='" + curDate + '\'' +
                ", stepCount=" + stepCount +
                '}';
    }
}
