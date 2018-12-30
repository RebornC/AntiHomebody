package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model.StepEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.utils.ToastUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 非正式页面，测试专用
 * Created by yc on 2018/12/18.
 */

public class SQLdataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqldata);

        String str = "";

        TextView text = (TextView) findViewById(R.id.text);

        StepDataDao stepDataDao = new StepDataDao(this);
        List<StepEntity> listData =  stepDataDao.getAllStepDatas();

        for (int i = 0; i < listData.size(); i++) {
            String a = listData.get(i).getCurDate();
            String b = listData.get(i).getUserId();
            String c = listData.get(i).getStepCount();
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c) + "\n";
        }

        UserDataDao userDataDao = new UserDataDao(this);
        List<_User> userList = userDataDao.getAllUserDatas();

        for (int i = 0; i < userList.size(); i++) {
            String a = userList.get(i).getObjectId();
            String b = String.valueOf(userList.get(i).getCurLevel());
            String c = String.valueOf(userList.get(i).getNumerator());
            String d = String.valueOf(userList.get(i).getDenominator());
            String e = String.valueOf(userList.get(i).getCurEnergy());
            String f = String.valueOf(userList.get(i).getTotalEnergy());
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c+"   "+d+"   "+e+"   "+f) + "\n";
        }

        text.setText(str);

    }
}
