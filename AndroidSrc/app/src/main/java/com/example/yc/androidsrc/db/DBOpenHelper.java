package com.example.yc.androidsrc.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite数据库
 *
 * @author RebornC
 * Created on 2018/12/11.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DB_NAME = "MyDB.db";

    // 数据库版本
    private static final int DB_VERSION = 1;

    // 记录在该台手机登录过的用户的能量与等级情况
    private static final String TABLE_NAME_0 = "userLevelInfo";

    // 计步数据表
    private static final String TABLE_NAME_1 = "StepInfo";

    // 计划清单数据表，由于SQLite没有数组类型，所以每条计划清单都单独存储
    private static final String TABLE_NAME_2 = "PlanInfo";

    // 通过完成计划领取能量的情况
    // 每个日期最多只能领取一次能量奖励
    private static final String TABLE_NAME_3 = "energyOfPlan";

    // 每次领取/扣除能量后都要在这张数据表进行记录，注明能量来源途径
    private static final String TABLE_NAME_4 = "energyOfStep";

    // 用户每天获得的总能量，用于检测用户的能量增减情况
    private static final String TABLE_NAME_5 = "dailyEnergyInfo";


    /**
     * 执行SQL语句创建数据表
     */

    private static final String CREATE_TABLE_0 = "Create Table "
            + TABLE_NAME_0
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, " // 用户ID
            + "curLevel integer, " // 当前等级
            + "numerator integer, " // 当前进度条上的分子
            + "denominator integer, " // 当前进度条的分母，即从当前等级升到下一级所需要的总能量
            + "curEnergy integer, " // 当前用户所积攒的、还未浇灌的能量
            + "totalEnergy integer);"; // 当前用户所获得的总能量（从创建账号到现在）

    private static final String CREATE_TABLE_1 = "Create Table "
            + TABLE_NAME_1
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, "
            + "curDate text, "
            + "totalSteps text);";

    // 由于SQLite没有单独的boolean类，所以用text类代替
    // status "0"：未完成；status "1"：已完成
    private static final String CREATE_TABLE_2 = "Create Table "
            + TABLE_NAME_2
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, "
            + "curDate text, "
            + "plan text, "
            + "status text);";

    // status "0"：该用户未领取当天的能量；status "1"：该用户已领取当天的能量
    private static final String CREATE_TABLE_3 = "Create Table "
            + TABLE_NAME_3
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, "
            + "curDate text, "
            + "energy integer, "
            + "status text);";

    private static final String CREATE_TABLE_4 = "Create Table "
            + TABLE_NAME_4
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, "
            + "curDay text, " // example: 2018-12-28
            + "curTime text, " // example: 12:00
            + "energy integer, "
            + "source text);"; // 能量来源途径，例如计步、计划打卡

    private static final String CREATE_TABLE_5 = "Create Table "
            + TABLE_NAME_5
            + "(_id integer primary key AUTOINCREMENT, "
            + "userId text, "
            + "curDate text, "
            + "energy integer);";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_0);
        db.execSQL(CREATE_TABLE_1);
        db.execSQL(CREATE_TABLE_2);
        db.execSQL(CREATE_TABLE_3);
        db.execSQL(CREATE_TABLE_4);
        db.execSQL(CREATE_TABLE_5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
