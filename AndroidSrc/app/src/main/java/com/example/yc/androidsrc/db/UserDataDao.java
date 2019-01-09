package com.example.yc.androidsrc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yc.androidsrc.model.StepEntity;
import com.example.yc.androidsrc.model._User;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装：访问数据库中所记录的用户信息（包括能量与等级情况）
 *
 * @author RebornC
 * Created on 2018/12/29.
 */

public class UserDataDao {

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    private static final String TABLE_NAME_0 = "UserLevelInfo";

    public UserDataDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }


    /**
     * 对于第一次在该台手机登录的用户
     * 在数据表中为其添加一条新记录
     *
     * @param user
     */
    public void addNewUserData(_User user) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", user.getObjectId());
        values.put("curLevel", user.getCurLevel());
        values.put("numerator", user.getNumerator());
        values.put("denominator", user.getDenominator());
        values.put("curEnergy", user.getCurEnergy());
        values.put("totalEnergy", user.getTotalEnergy());
        db.insert(TABLE_NAME_0, null, values);
        db.close();
    }


    /**
     * 更新指定用户的相关数据
     *
     * @param user
     */
    public void updateUserData(_User user) {
        db = dbOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", user.getObjectId());
        values.put("curLevel", user.getCurLevel());
        values.put("numerator", user.getNumerator());
        values.put("denominator", user.getDenominator());
        values.put("curEnergy", user.getCurEnergy());
        values.put("totalEnergy", user.getTotalEnergy());
        db.update(TABLE_NAME_0, values, "userId=?", new String[]{user.getObjectId()});
        db.close();
    }


    /**
     * 对于每个在该台手机登录的用户，查询数据库中是否存在记录
     * 若没有，即是第一次登录，则在数据表中为其添加一条新记录
     * 若存在，则进行数据更新
     *
     * @param user
     */
    public void updateUserDataAfterLogin(_User user) {
        Boolean isExist = false;
        db = dbOpenHelper.getReadableDatabase();
        String userId = user.getObjectId();
        Cursor cursor = db.query(TABLE_NAME_0, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String mUserId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (userId.equals(mUserId)) {
                // 存在该用户的数据记录，直接进行更新
                isExist = true;
                updateUserData(user);
                break;
            }
        }
        if (!isExist) {
            // 如果不存在该用户的数据记录，则添加新数据
            addNewUserData(user);
        }
        db.close();
        cursor.close();
    }


    /**
     * 查询所有的用户记录
     *
     * @return
     */
    public List<_User> getAllUserDatas() {
        List<_User> dataList = new ArrayList<>();
        db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME_0, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            Integer curLevel = cursor.getInt(cursor.getColumnIndex("curLevel"));
            Integer numerator = cursor.getInt(cursor.getColumnIndex("numerator"));
            Integer denominator = cursor.getInt(cursor.getColumnIndex("denominator"));
            Integer curEnergy = cursor.getInt(cursor.getColumnIndex("curEnergy"));
            Integer totalEnergy = cursor.getInt(cursor.getColumnIndex("totalEnergy"));
            _User user = new _User(curLevel, numerator, denominator, curEnergy, totalEnergy);
            user.setObjectId(userId);
            dataList.add(user);
        }
        db.close();
        cursor.close();
        return dataList;
    }


    /**
     * 根据用户ID查询其记录
     *
     * @param objectId
     * @return
     */
    public _User getUserDataById(String objectId) {
        db = dbOpenHelper.getReadableDatabase();
        _User user = new _User();
        Cursor cursor = db.query(TABLE_NAME_0, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            if (objectId.equals(id)) {
                Integer curLevel = cursor.getInt(cursor.getColumnIndex("curLevel"));
                Integer numerator = cursor.getInt(cursor.getColumnIndex("numerator"));
                Integer denominator = cursor.getInt(cursor.getColumnIndex("denominator"));
                Integer curEnergy = cursor.getInt(cursor.getColumnIndex("curEnergy"));
                Integer totalEnergy = cursor.getInt(cursor.getColumnIndex("totalEnergy"));
                user = new _User(curLevel, numerator, denominator, curEnergy, totalEnergy);
                user.setObjectId(objectId);
                break;
            }
        }
        db.close();
        cursor.close();
        return user;
    }

}
