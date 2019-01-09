package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.db.DailyEnergyDao;
import com.example.yc.androidsrc.db.EnergyOfPlanDao;
import com.example.yc.androidsrc.db.EnergySourceDao;
import com.example.yc.androidsrc.db.PlanDataDao;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.db.UserDataDao;
import com.example.yc.androidsrc.model.DailyEnergyEntity;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.model.PlanEntity;
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

        UserDataDao userDataDao = new UserDataDao(this);
        List<_User> userList = userDataDao.getAllUserDatas();

        str = str + "用户表" + "\n";
        for (int i = 0; i < userList.size(); i++) {
            String a = userList.get(i).getObjectId();
            String b = String.valueOf(userList.get(i).getCurLevel());
            String c = String.valueOf(userList.get(i).getNumerator());
            String d = String.valueOf(userList.get(i).getDenominator());
            String e = String.valueOf(userList.get(i).getCurEnergy());
            String f = String.valueOf(userList.get(i).getTotalEnergy());
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c+"   "+d+"   "+e+"   "+f) + "\n";
        }

        StepDataDao stepDataDao = new StepDataDao(this);
        List<StepEntity> listData =  stepDataDao.getAllStepDatas();

        str = str + "计步表" + "\n";
        for (int i = 0; i < listData.size(); i++) {
            String a = listData.get(i).getCurDate();
            String b = listData.get(i).getUserId();
            String c = listData.get(i).getStepCount();
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c) + "\n";
        }


        PlanDataDao planDataDao = new PlanDataDao(this);
        List<PlanEntity> planList = planDataDao.getAllPlanDatas();

        str = str + "计划打卡表" + "\n";
        for (int i = 0; i < planList.size(); i++) {
            String a = planList.get(i).getUserId();
            String b = planList.get(i).getCurDate();
            String c = planList.get(i).getPlan();
            String d = planList.get(i).getStatus();
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c+"   "+d) + "\n";
        }

        EnergyOfPlanDao energyOfPlanDao = new EnergyOfPlanDao(this);
        List<DailyEnergyEntity> energyOfPlanList = energyOfPlanDao.getAllData();

        str = str + "计划打卡表是否已领取" + "\n";
        for (int i = 0; i < energyOfPlanList.size(); i++) {
            String a = energyOfPlanList.get(i).getUserId();
            String b = energyOfPlanList.get(i).getCurDate();
            String c = String.valueOf(energyOfPlanList.get(i).getEnergy());
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c) + "\n";
        }

        EnergySourceDao energySourceDao = new EnergySourceDao(this);
        List<EnergySourceEntity> energySourceList = energySourceDao.getAllEnergySource();

        str = str + "每日动态" + "\n";
        for (int i = 0; i < energySourceList.size(); i++) {
            String a = energySourceList.get(i).getUserId();
            String b = energySourceList.get(i).getCurDay();
            String c = energySourceList.get(i).getCurTime();
            String d = String.valueOf(energySourceList.get(i).getEnergy());
            String f = energySourceList.get(i).getSource();
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c+"   "+d+"   "+f) + "\n";
        }

        DailyEnergyDao dailyEnergyDao = new DailyEnergyDao(this);
        List<DailyEnergyEntity> dailyList = dailyEnergyDao.getAllData();

        str = str + "每日总能量" + "\n";
        for (int i = 0; i < dailyList.size(); i++) {
            String a = dailyList.get(i).getUserId();
            String b = dailyList.get(i).getCurDate();
            String c = String.valueOf(dailyList.get(i).getEnergy());
            str = str +  Integer.toString(i) + ("   "+a+"   "+b+"   "+c) + "\n";
        }

        text.setText(str);

    }
}
