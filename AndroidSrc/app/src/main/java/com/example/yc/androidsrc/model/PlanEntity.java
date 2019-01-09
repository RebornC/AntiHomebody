package com.example.yc.androidsrc.model;

/**
 * @author RebornC
 * Created on 2019/1/2.
 */

public class PlanEntity {

    private String userId;
    private String curDate;
    private String plan;
    private String status; // status "0"：未完成；status "1"：已完成

    public PlanEntity(String userId, String curDate, String plan, String status) {
        this.userId = userId;
        this.curDate = curDate;
        this.plan = plan;
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurDate() {
        return curDate;
    }

    public String getPlan() {
        return plan;
    }

    public String getStatus() {
        return status;
    }
}