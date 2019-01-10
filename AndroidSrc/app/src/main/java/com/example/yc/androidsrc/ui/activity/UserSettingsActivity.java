package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.PersonalMsgPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IPersonalMsgPresenter;
import com.example.yc.androidsrc.ui.impl.IPersonalMsgView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * 编辑资料界面
 *
 * @author RebornC
 * Created on 2019/1/10.
 */

public class UserSettingsActivity extends AppCompatActivity implements IPersonalMsgView, View.OnClickListener {

    private IPersonalMsgPresenter personalMsgPresenter;
    private _User curUser;
    private Toolbar toolbar;

    // 头像
    private ListView listView1;
    private List<Map<String,Object>> listData1 = new ArrayList<>();
    private SimpleAdapter simpleAdapter1;
    private String imageUrl;
    private static String LackOfHeadPortrait = "0";
    // 其余信息
    private ListView listView2;
    private List<Map<String,Object>> listData2 = new ArrayList<>();
    private SimpleAdapter simpleAdapter2;
    private String userName;
    private String userSignature;
    private String phoneNumber;
    private String defaultStr = "点击修改";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        initView();
    }

    public void initView() {
        personalMsgPresenter = new PersonalMsgPresenterCompl(this);
        curUser = BmobUser.getCurrentUser(_User.class);
        userName = curUser.getUsername();
        if (curUser.getHeadPortrait() == null) {
            imageUrl = LackOfHeadPortrait;
        } else {
            imageUrl = curUser.getHeadPortrait().getFileUrl();
        }
        if (curUser.getSignature() == null) {
            userSignature = getResources().getString(R.string.default_signature);
        } else {
            userSignature = curUser.getSignature();
        }
        phoneNumber = curUser.getMobilePhoneNumber();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("个人资料");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 用户头像实例化
        listView1 = (ListView) findViewById(R.id.list_view_1);
        Map<String,Object> temp_1 = new LinkedHashMap<>();
        temp_1.put("text", "头像");
        temp_1.put("image", imageUrl);
        listData1.add(temp_1);
        simpleAdapter1 = new SimpleAdapter(this, listData1, R.layout.list_view_item_2,
                new String[] {"text", "image"}, new int[] {R.id.text, R.id.image});
        simpleAdapter1.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // 判断是否为我们要处理的对象
                if(view instanceof ImageView && data instanceof String){
                    ImageView iv = (ImageView) view;
                    if (data.equals(LackOfHeadPortrait)) {
                        // 当用户未上传头像时，使用系统默认头像
                        Glide.with(UserSettingsActivity.this).load(R.mipmap.head).into(iv);
                    } else {
                        Glide.with(UserSettingsActivity.this).load(imageUrl).into(iv);
                    }
                    return true;
                }else
                    return false;
            }
        });
        listView1.setAdapter(simpleAdapter1);

        // 用户信息实例化
        listView2 = (ListView) findViewById(R.id.list_view_2);
        listView2.addHeaderView(new ViewStub(this)); // 顶部分割线
        listView2.addFooterView(new ViewStub(this)); // 底部分割线
        String[] attrList = new String[] {"昵称","签名","账号","密码"};
        String[] textList = new String[] {userName, defaultStr, phoneNumber, defaultStr};
        for (int i = 0; i < attrList.length; i++) {
            Map<String,Object> temp_2 = new LinkedHashMap<>();
            temp_2.put("attr", attrList[i]);
            temp_2.put("text", textList[i]);
            listData2.add(temp_2);
        }
        simpleAdapter2 = new SimpleAdapter(this, listData2, R.layout.list_view_item_3,
                new String[] {"attr","text"}, new int[] {R.id.attr, R.id.text});
        listView2.setAdapter(simpleAdapter2);
    }

    @Override
    public void onClick(View v) {

    }
}
