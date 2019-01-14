package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.EnergySourceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 当用户当天第一次登录后，便增添记录，以便系统进行“能量增减监测”
 * 同时用以用户在该天是否已经登录过（仅限于本地手机）
 *
 * @author RebornC
 * Created on 2010/1/14.
 */

public class LocalLoginDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_6 = "LocalLoginInfo";

    public LocalLoginDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 当天首次登录后，在数据表中添加一条新记录
     *
     * @param curDate
     * @param userId
     */
    public void addNewData(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("curDate", curDate);
        db.insert(TABLE_NAME_6, null, values);
        db.close();
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天是否已经登录过（本地）
     *
     * @param userId
     * @return curDate
     */
    public boolean queryData(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        boolean isExist = false;
        Cursor cursor = db.query(TABLE_NAME_6, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(id) && curDate.equals(date)) {
                isExist = true;
                break;
            }
        }
        db.close();
        cursor.close();
        return isExist;
    }

    /**
     * 根据用户ID查询其在本地登录过的最早日期
     *
     * @param userId
     */
    public String queryFirstLoginDate(String userId) {
        String date = "";
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_6, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (userId.equals(id)) {
                date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
                break;
            }
        }
        db.close();
        cursor.close();
        return date;
    }

}
