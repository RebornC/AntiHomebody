package com.example.yc.androidsrc.model;

/**
 * @author RebornC
 * Created on 2019/1/4.
 */

public class DailyEnergyEntity {

    private String userId;
    private String curDate;
    private Integer energy;

    public DailyEnergyEntity(String userId, String curDate, Integer energy) {
        this.userId = userId;
        this.curDate = curDate;
        this.energy = energy;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurDate(String curDate) {
        this.curDate = curDate;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurDate() {
        return curDate;
    }

    public Integer getEnergy() {
        return energy;
    }
}