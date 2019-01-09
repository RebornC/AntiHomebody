package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.DailyEnergyEntity;
import com.example.yc.androidsrc.model.EnergySourceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户每天获得的总能量，用于检测用户的能量增减情况
 *
 * @author RebornC
 * Created on 2019/1/4.
 */

public class DailyEnergyDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_5 = "DailyEnergyInfo";

    public DailyEnergyDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 在数据表中添加一条新记录
     *
     * @param dailyEnergyEntity
     */
    public void addNewData(DailyEnergyEntity dailyEnergyEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", dailyEnergyEntity.getUserId());
        values.put("curDate", dailyEnergyEntity.getCurDate());
        values.put("energy", dailyEnergyEntity.getEnergy());
        db.insert(TABLE_NAME_5, null, values);
        db.close();
    }


    /**
     * 更新指定用户和指定日期的总能量值数据表
     *
     * @param dailyEnergyEntity
     */
    public void updateData(DailyEnergyEntity dailyEnergyEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", dailyEnergyEntity.getUserId());
        values.put("curDate", dailyEnergyEntity.getCurDate());
        values.put("energy", dailyEnergyEntity.getEnergy());
        db.update(TABLE_NAME_5, values, "userId=? AND curDate=?", new String[]{dailyEnergyEntity.getUserId(), dailyEnergyEntity.getCurDate()});
        db.close();
    }


    /**
     * 在添加一条新数据时
     * 先判断指定用户和指定日期的当天总能量记录是否存在
     * 如果存在，则总能量值累加
     * 如果不存在，则新增该数据
     *
     * @param dailyEnergyEntity
     * @return
     */
    public void addDataByUserIdAndDate(DailyEnergyEntity dailyEnergyEntity) {
        Boolean isExist = false;
        db = dbOpenHelper.getReadableDatabase();
        String userId = dailyEnergyEntity.getUserId();
        String curDate = dailyEnergyEntity.getCurDate();
        Integer energy = dailyEnergyEntity.getEnergy();
        Cursor cursor = db.query(TABLE_NAME_5, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String mUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String mCurDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(mUserId) && curDate.equals(mCurDate)) {
                // 存在指定用户和指定日期的计步记录，则步数直接在原有记录上累加
                isExist = true;
                Integer mEnergy = cursor.getInt(cursor.getColumnIndexOrThrow("energy"));
                energy += mEnergy;
                dailyEnergyEntity.setEnergy(energy);
                updateData(dailyEnergyEntity);
                break;
            }
        }
        if (!isExist) {
            // 如果不存在指定用户和指定日期的计步记录，则添加新数据
            addNewData(dailyEnergyEntity);
        }
        db.close();
        cursor.close();
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天领取的总能量值
     *
     * @param userId
     * @return curDate
     */
    public int getEnergyByIdAndDate(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        int energy = 0;
        Cursor cursor = db.query(TABLE_NAME_5, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(id) && curDate.equals(date)) {
                energy = cursor.getInt(cursor.getColumnIndexOrThrow("energy"));
                break;
            }
        }
        db.close();
        cursor.close();
        return energy;
    }


    /**
     * 查询所有记录
     *
     * @return
     */
    public List<DailyEnergyEntity> getAllData() {
        db = dbOpenHelper.getReadableDatabase();
        List<DailyEnergyEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_5, null, null, null, null, null, null);
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
