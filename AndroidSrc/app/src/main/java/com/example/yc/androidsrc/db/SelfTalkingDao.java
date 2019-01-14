package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.PlanEntity;
import com.example.yc.androidsrc.model.SelfTalkingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * “碎碎念”模块的文本数据访问与操作
 *
 * @author RebornC
 * Created on 2019/1/14.
 */

public class SelfTalkingDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_7 = "SelfTalkingInfo";

    public SelfTalkingDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 在“碎碎念”数据表中添加一条新记录
     *
     * @param selfTalkingEntity
     */
    public void addNewData(SelfTalkingEntity selfTalkingEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", selfTalkingEntity.getUserId());
        values.put("curDate", selfTalkingEntity.getCurDate());
        values.put("selfTalking", selfTalkingEntity.getSelfTalking());
        db.insert(TABLE_NAME_7, null, values);
        db.close();
    }


    /**
     * 修改文本内容
     *
     * @param oldEntity
     * @param newEntity
     */
    public void updateData(SelfTalkingEntity oldEntity, SelfTalkingEntity newEntity) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", newEntity.getUserId());
        values.put("curDate", newEntity.getCurDate());
        values.put("selfTalking", newEntity.getSelfTalking());
        db.update(TABLE_NAME_7, values, "userId=? AND curDate=? AND selfTalking=?",
                new String[]{oldEntity.getUserId(), oldEntity.getCurDate(), oldEntity.getSelfTalking()});
        db.close();
    }


    /**
     * 根据用户ID查询该用户的所有“碎碎念”
     *
     * @param userId
     */
    public List<SelfTalkingEntity> getDataById(String userId) {
        db = dbOpenHelper.getReadableDatabase();
        List<SelfTalkingEntity> dataList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME_7, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (userId.equals(id)) {
                String curDate = cursor.getString(cursor.getColumnIndexOrThrow("curDate"));
                String selfTalking = cursor.getString(cursor.getColumnIndexOrThrow("selfTalking"));
                SelfTalkingEntity selfTalkingEntity = new SelfTalkingEntity(id, curDate, selfTalking);
                dataList.add(selfTalkingEntity);
            }
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 删除某条“碎碎念”
     *
     * @param selfTalkingEntity
     */
    public void deleteData(SelfTalkingEntity selfTalkingEntity) {
        db = dbOpenHelper.getReadableDatabase();
        if (db.isOpen())
            db.delete(TABLE_NAME_7, "userId=? AND curDate=? AND selfTalking=?",
                    new String[]{selfTalkingEntity.getUserId(), selfTalkingEntity.getCurDate(), selfTalkingEntity.getSelfTalking()});
        db.close();
    }

}
