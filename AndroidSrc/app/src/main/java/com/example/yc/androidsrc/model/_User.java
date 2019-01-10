package com.example.yc.androidsrc.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author RebornC
 * Created on 2018/12/12.
 */

public class _User extends BmobUser {

    private BmobFile headPortrait; // 用户头像
    private String signature; // 签名
    private Integer curLevel; // 当前等级
    private Integer numerator; // 当前进度条上的分子
    private Integer denominator; // 当前进度条的分母，即从当前等级升到下一级所需要的总能量
    private Integer curEnergy; // 当前用户所积攒的、还未浇灌的能量
    private Integer totalEnergy; // 当前用户所获得的总能量（从创建账号到现在）

    public _User() {
    }

    public _User(Integer curLevel, Integer numerator, Integer denominator, Integer curEnergy, Integer totalEnergy) {
        this.curLevel = curLevel;
        this.numerator = numerator;
        this.denominator = denominator;
        this.curEnergy = curEnergy;
        this.totalEnergy = totalEnergy;
    }

    public void setheadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setCurLevel(Integer curLevel) {
        this.curLevel = curLevel;
    }

    public void setNumerator(Integer numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(Integer denominator) {
        this.denominator = denominator;
    }

    public void setCurEnergy(Integer curEnergy) {
        this.curEnergy = curEnergy;
    }

    public void setTotalEnergy(Integer totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public BmobFile getHeadPortrait() {
        return headPortrait;
    }

    public String getSignature() {
        return signature;
    }

    public Integer getCurLevel() {
        return curLevel;
    }

    public Integer getNumerator() {
        return numerator;
    }

    public Integer getDenominator() {
        return denominator;
    }

    public Integer getCurEnergy() {
        return curEnergy;
    }

    public Integer getTotalEnergy() {
        return totalEnergy;
    }
}
