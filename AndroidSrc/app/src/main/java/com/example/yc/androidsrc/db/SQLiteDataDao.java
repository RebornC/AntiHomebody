package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.StepEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装：访问数据库中的数据并进行相关操作
 * Created by yc on 2018/12/11.
 */

public class SQLiteDataDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_1 = "StepInfo"; // 计步数据表
    private static final String TABLE_NAME_2 = "Info"; // 计划打卡数据表

    /**
     * 在计步数据表中添加一条新记录
     * @param stepEntity
     */
    public void addNewStepData(StepEntity stepEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getStepCount());
        db.insert(TABLE_NAME_1, null, values);
        db.close();
    }

    /**
     * 根据日期查询计步记录
     * @param curDate
     * @return
     */
    public StepEntity getStepDataByDate(String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        StepEntity stepEntity = null;
        Cursor cursor = db.query(TABLE_NAME_1, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (curDate.equals(date)) {
                String stepCount = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                stepEntity = new StepEntity(date, stepCount);
                break;
            }
        }
        db.close();
        cursor.close();
        return stepEntity;
    }

    /**
     * 查询所有的计步记录
     * @return
     */
    public List<StepEntity> getAllStepDatas() {
        List<StepEntity> dataList = new ArrayList<>();
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_1, null);
        while (cursor.moveToNext()) {
            String curDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String totalSteps = cursor.getString(cursor.getColumnIndex("totalSteps"));
            StepEntity entity = new StepEntity(curDate, totalSteps);
            dataList.add(entity);
        }
        db.close();
        cursor.close();
        return dataList;
    }

    /**
     * 更新指定日期的计步数据
     * @param stepEntity
     */
    public void updateStepData(StepEntity stepEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("curDate",stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getStepCount());
        db.update(TABLE_NAME_1, values, "curDate=?", new String[]{stepEntity.getCurDate()});
        db.close();
    }

    /**
     * 删除指定日期的计步记录
     * @param curDate
     */
    public void deleteStepData(String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        if (db.isOpen())
            db.delete(TABLE_NAME_1, "curDate", new String[]{curDate});
        db.close();
    }

}
