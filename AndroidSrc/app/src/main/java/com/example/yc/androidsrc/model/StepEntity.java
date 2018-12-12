package com.example.yc.androidsrc.model;

/**
 * Created by yc on 2018/12/11.
 */

public class StepEntity {

    private String curDate;
    private String stepCount;

    public StepEntity(String curDate, String stepCount) {
        this.curDate = curDate;
        this.stepCount = stepCount;
    }

    public String getCurDate() {
        return curDate;
    }

    public String getStepCount() {
        return stepCount;
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
                "curDate='" + curDate + '\'' +
                ", stepCount=" + stepCount +
                '}';
    }
}
