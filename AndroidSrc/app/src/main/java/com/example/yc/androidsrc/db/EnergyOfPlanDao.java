package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.DailyEnergyEntity;
import com.example.yc.androidsrc.model.PlanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 当用户在某天通过计划打卡领取能量后，则在这张数据表进行记录
 *
 * @author RebornC
 * Created on 2019/1/4.
 */

public class EnergyOfPlanDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_3 = "EnergyOfPlan";

    public EnergyOfPlanDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 在数据表中添加一条新记录
     *
     * @param userId
     * @param curDate
     * @param energy
     */
    public void addNewData(String userId, String curDate, Integer energy) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("curDate", curDate);
        values.put("energy", energy);
        db.insert(TABLE_NAME_3, null, values);
        db.close();
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天是否已经领取奖励
     *
     * @param userId
     * @return curDate
     */
    public boolean queryData(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        boolean isExist = false;
        Cursor cursor = db.query(TABLE_NAME_3, null, null, null, null, null, null);
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
     * 查询所有记录
     *
     * @return
     */
    public List<DailyEnergyEntity> getAllData() {
        db = dbOpenHelper.getReadableDatabase();
        List<DailyEnergyEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_3, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String curDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            Integer energy = cursor.getInt(cursor.getColumnIndexOrThrow("energy"));
            DailyEnergyEntity entity = new DailyEnergyEntity(userId, curDate, energy);
            dataList.add(entity);
        }
        db.close();
        cursor.close();
        return dataList;
    }

}
