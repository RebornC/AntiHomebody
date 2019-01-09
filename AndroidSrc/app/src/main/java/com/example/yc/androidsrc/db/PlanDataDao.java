package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.PlanEntity;
import com.example.yc.androidsrc.model.StepEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装：访问数据库中的计划清单数据并进行相关操作
 *
 * @author RebornC
 * Created on 2019/1/2.
 */

public class PlanDataDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_2 = "PlanInfo"; // 计划清单数据表

    public PlanDataDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 在计划清单数据表中添加一条新记录
     *
     * @param planEntity
     */
    public void addNewPlanData(PlanEntity planEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", planEntity.getUserId());
        values.put("curDate", planEntity.getCurDate());
        values.put("plan", planEntity.getPlan());
        values.put("status", planEntity.getStatus());
        db.insert(TABLE_NAME_2, null, values);
        db.close();
    }


    /**
     * 通过用户Id、指定日期和指定内容找到对应的数据
     * 可更新其计划内容或完成状态（"0"与"1"的切换）
     *
     * @param oldPlanEntity
     * @param newPlanEntity
     */
    public void updatePlanData(PlanEntity oldPlanEntity, PlanEntity newPlanEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", newPlanEntity.getUserId());
        values.put("curDate", newPlanEntity.getCurDate());
        values.put("plan", newPlanEntity.getPlan());
        values.put("status", newPlanEntity.getStatus());
        db.update(TABLE_NAME_2, values, "userId=? AND curDate=? AND plan=?",
                new String[]{oldPlanEntity.getUserId(), oldPlanEntity.getCurDate(), oldPlanEntity.getPlan()});
        db.close();
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天的所有计划清单
     *
     * @param userId
     * @return curDate
     */
    public List<PlanEntity> getPlanDataByIdAndDate(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        List<PlanEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_2, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            if (userId.equals(id) && curDate.equals(date)) {
                String plan = cursor.getString(cursor.getColumnIndexOrThrow("plan"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
                PlanEntity planEntity = new PlanEntity(id, curDate, plan, status);
                dataList.add(planEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天的已完成计划清单
     *
     * @param userId
     * @return curDate
     */
    public List<PlanEntity> getCompletedPlan(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        List<PlanEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_2, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            if (userId.equals(id) && curDate.equals(date) && status.equals("1")) {
                String plan = cursor.getString(cursor.getColumnIndexOrThrow("plan"));
                PlanEntity planEntity = new PlanEntity(id, curDate, plan, status);
                dataList.add(planEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 根据用户ID、指定日期查询该用户在这天的未完成计划清单
     *
     * @param userId
     * @return curDate
     */
    public List<PlanEntity> getTodoPlan(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        List<PlanEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_2, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            if (userId.equals(id) && curDate.equals(date) && status.equals("0")) {
                String plan = cursor.getString(cursor.getColumnIndexOrThrow("plan"));
                PlanEntity planEntity = new PlanEntity(id, curDate, plan, status);
                dataList.add(planEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 查询所有的计划清单记录（不限用户）
     *
     * @return
     */
     public List<PlanEntity> getAllPlanDatas() {
        List<PlanEntity> dataList = new ArrayList<>();
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_2, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String curDate = cursor.getString(cursor.getColumnIndex("curDate"));
            String plan = cursor.getString(cursor.getColumnIndex("plan"));
            String status = cursor.getString(cursor.getColumnIndex("status"));
            PlanEntity entity = new PlanEntity(userId, curDate, plan, status);
            dataList.add(entity);
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 删除某条计划
     *
     * @param planEntity
     */
    public void deletePlanData(PlanEntity planEntity) {
        db = dbOpenHelper.getReadableDatabase();
        if (db.isOpen())
            db.delete(TABLE_NAME_2, "userId=? AND curDate=? AND plan=? AND status=?",
                    new String[]{planEntity.getUserId(), planEntity.getCurDate(), planEntity.getPlan(), planEntity.getStatus()});
        db.close();
    }


    /**
     * 根据用户ID、指定日期进行查询
     * 返回该用户在这天的已完成计划数量
     * 若无，则返回0
     *
     * @param userId
     * @return curDate
     */
    public int getCompletedPlanSum(String userId, String curDate) {
        db = dbOpenHelper.getReadableDatabase();
        int sum = 0;
        Cursor cursor = db.query(TABLE_NAME_2, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            if (userId.equals(id) && curDate.equals(date) && status.equals("1")) {
                sum++;
            }
        }
        db.close();
        cursor.close();
        return sum;
    }
}
