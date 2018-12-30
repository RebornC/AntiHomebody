package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.StepEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装：访问数据库中的计步数据并进行相关操作
 *
 * @author RebornC
 * Created on 2018/12/11.
 */

public class StepDataDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_1 = "StepInfo"; // 计步数据表

    public StepDataDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 在计步数据表中添加一条新记录
     *
     * @param stepEntity
     */
    public void addNewStepData(StepEntity stepEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", stepEntity.getUserId());
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getStepCount());
        db.insert(TABLE_NAME_1, null, values);
        db.close();
    }


    /**
     * 更新指定用户和指定日期的计步数据
     *
     * @param stepEntity
     */
    public void updateStepData(StepEntity stepEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", stepEntity.getUserId());
        values.put("curDate", stepEntity.getCurDate());
        values.put("totalSteps", stepEntity.getStepCount());
        db.update(TABLE_NAME_1, values, "userId=? AND curDate=?", new String[]{stepEntity.getUserId(), stepEntity.getCurDate()});
        db.close();
    }


    /**
     * 在添加一条新数据时
     * 先判断指定用户和指定日期的计步记录是否存在
     * 如果存在，则步数累加
     * 如果不存在，则新增该数据
     *
     * @param stepEntity
     * @return
     */
    public void addStepDataByUserIdAndDate(StepEntity stepEntity) {
        Boolean isExist = false;
        db = dbOpenHelper.getReadableDatabase();
        String userId = stepEntity.getUserId();
        String curDate = stepEntity.getCurDate();
        Integer stepCount = Integer.parseInt(stepEntity.getStepCount());
        Cursor cursor = db.query(TABLE_NAME_1, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String mUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String mCurDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(mUserId) && curDate.equals(mCurDate)) {
                // 存在指定用户和指定日期的计步记录，则步数直接在原有记录上累加
                isExist = true;
                String mStepCount = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                stepCount += Integer.parseInt(mStepCount);
                stepEntity.setStepCount(Integer.toString(stepCount));
                updateStepData(stepEntity);
                break;
            }
        }
        if (!isExist) {
            // 如果不存在指定用户和指定日期的计步记录，则添加新数据
            addNewStepData(stepEntity);
        }
        db.close();
        cursor.close();
    }


    /**
     * 查询所有的计步记录（不限用户）
     *
     * @return
     */
    public List<StepEntity> getAllStepDatas() {
        List<StepEntity> dataList = new ArrayList<>();
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_1, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String curDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String totalSteps = cursor.getString(cursor.getColumnIndex("totalSteps"));
            StepEntity entity = new StepEntity(userId, curDate, totalSteps);
            dataList.add(entity);
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 根据用户ID查询其所有的计步记录
     *
     * @param userId
     * @return
     */
    public List<StepEntity> getStepDataByUserId(String userId) {
        db = dbOpenHelper.getReadableDatabase();
        List<StepEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_1, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (userId.equals(id)) {
                String curDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
                String stepCount = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                StepEntity stepEntity = new StepEntity(id, curDate, stepCount);
                dataList.add(stepEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }

    /**
     * 将计步数据展示在图表中
     * 先判断指定用户和指定日期的计步记录是否存在
     * 如果存在，则返回该步数
     * 如果不存在，则返回0
     *
     * @param userId
     * @param curDate
     * @return
     */
    public int getStepDataInChart(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_1, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String mUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String mCurDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(mUserId) && curDate.equals(mCurDate)) {
                // 存在指定用户和指定日期的计步记录，则返回步数
                String mStepCount = cursor.getString(cursor.getColumnIndexOrThrow("totalSteps"));
                db.close();
                cursor.close();
                return Integer.parseInt(mStepCount);
            }
        }
        // 如果不存在指定用户和指定日期的计步记录，则返回0
        db.close();
        cursor.close();
        return 0;
    }


//    /**
//     * 删除指定日期的计步记录
//     *
//     * @param curDate
//     */
//    public void deleteStepData(String curDate) {
//        db = dbOpenHelper.getReadableDatabase();
//        if (db.isOpen())
//            db.delete(TABLE_NAME_1, "curDate", new String[]{curDate});
//        db.close();
//    }

}
