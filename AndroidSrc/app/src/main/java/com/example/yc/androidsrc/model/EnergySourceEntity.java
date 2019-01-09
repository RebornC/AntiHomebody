package com.example.yc.androidsrc.model;

/**
 * @author RebornC
 * Created on 2019/1/4.
 */

public class EnergySourceEntity {

    private String userId;
    private String curDay; // example: 2018-12-28
    private String curTime; // example: 12:00
    private Integer energy;
    private String source; // 能量来源途径，例如计步、计划打卡

    public EnergySourceEntity(String userId, String curDay, String curTime, Integer energy, String source) {
        this.userId = userId;
        this.curDay = curDay;
        this.curTime = curTime;
        this.energy = energy;
        this.source = source;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCurDay(String curDay) {
        this.curDay = curDay;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurDay() {
        return curDay;
    }

    public String getCurTime() {
        return curTime;
    }

    public Integer getEnergy() {
        return energy;
    }

    public String getSource() {
        return source;
    }
}