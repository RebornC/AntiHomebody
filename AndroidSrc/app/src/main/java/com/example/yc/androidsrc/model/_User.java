package com.example.yc.androidsrc.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * @author RebornC
 * Created on 2018/12/12.
 */

public class _User extends BmobUser {

    private BmobFile headPortrait;

    public void setheadPortrait(BmobFile headPortrait) {
        this.headPortrait = headPortrait;
    }

    public BmobFile getHeadPortrait() {
        return headPortrait;
    }
}
