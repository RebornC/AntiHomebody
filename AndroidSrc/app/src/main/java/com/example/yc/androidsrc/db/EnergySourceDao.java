package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.EnergySourceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 每次领取/扣除能量后都要在这张数据表进行记录，注明能量来源途径
 * 等同于用户的动态
 *
 * @author RebornC
 * Created on 2010/1/4.
 */

public class EnergySourceDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_4 = "EnergySource";

    public EnergySourceDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 在数据表中添加一条新记录
     *
     * @param energySourceEntity
     */
    public void addNewData(EnergySourceEntity energySourceEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", energySourceEntity.getUserId());
        values.put("curDay", energySourceEntity.getCurDay());
        values.put("curTime", energySourceEntity.getCurTime());
        values.put("energy", energySourceEntity.getEnergy());
        values.put("source", energySourceEntity.getSource());
        db.insert(TABLE_NAME_4, null, values);
        db.close();
    }


    /**
     * 根据用户ID查询该用户的所有动态
     *
     * @param userId
     * @return
     */
    public List<EnergySourceEntity> getEnergySourceById(String userId) {
        db = dbOpenHelper.getReadableDatabase();
        List<EnergySourceEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_4, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (userId.equals(id)) {
                String curDay = cursor.getString(cursor.getColumnIndexOrThrow("curDay"));
                String curTime = cursor.getString(cursor.getColumnIndexOrThrow("curTime"));
                Integer energy = cursor.getInt(cursor.getColumnIndexOrThrow("energy"));
                String source = cursor.getString(cursor.getColumnIndexOrThrow("source"));
                EnergySourceEntity energySourceEntity = new EnergySourceEntity(id, curDay, curTime, energy, source);
                dataList.add(energySourceEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 查询所有动态记录
     *
     * @return
     */
    public List<EnergySourceEntity> getAllEnergySource() {
        db = dbOpenHelper.getReadableDatabase();
        List<EnergySourceEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_4, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String curDay = cursor.getString(cursor.getColumnIndexOrThrow("curDay"));
            String curTime = cursor.getString(cursor.getColumnIndexOrThrow("curTime"));
            Integer energy = cursor.getInt(cursor.getColumnIndexOrThrow("energy"));
            String source = cursor.getString(cursor.getColumnIndexOrThrow("source"));
            EnergySourceEntity energySourceEntity = new EnergySourceEntity(userId, curDay, curTime, energy, source);
            dataList.add(energySourceEntity);
        }
        db.close();
        cursor.close();
        return dataList;
    }

}
