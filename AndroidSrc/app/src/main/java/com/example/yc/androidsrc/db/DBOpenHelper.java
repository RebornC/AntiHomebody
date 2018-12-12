package com.example.yc.androidsrc.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yc on 2018/12/11.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MyDB.db"; // 数据库名称
    private static final int DB_VERSION = 1; // 数据库版本
    private static final String TABLE_NAME_1 = "StepInfo"; // 计步数据表
    private static final String TABLE_NAME_2 = "Info"; // 计划打卡数据表

    // 执行SQL语句创建数据表
    private static final String CREATE_TABLE_1 = "Create Table "
            + TABLE_NAME_1
            + "(_id integer primary key AUTOINCREMENT, "
            + "curDate text, "
            + "totalSteps text);";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
