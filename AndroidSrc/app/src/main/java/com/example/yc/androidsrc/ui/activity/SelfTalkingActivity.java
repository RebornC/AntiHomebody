package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.TimeLineAdapter;
import com.example.yc.androidsrc.model.SelfTalkingEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.SelfTalkingPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.ISelfTalkingPresenter;
import com.example.yc.androidsrc.utils.ToastUtil;


import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 碎碎念界面
 *
 * @author RebornC
 * Created on 2019/1/15.
 */

public class SelfTalkingActivity extends AppCompatActivity implements View.OnClickListener {

    private ISelfTalkingPresenter selfTalkingPresenter;
    private String userId;
    private ImageView backIcon;
    private ListView mListView;
    private TimeLineAdapter adapter;

    private AlertDialog displayDialog;
    private AlertDialog modifyDialog;
    private String content;
    private EditText input;
    private SelfTalkingEntity curSelfTalkingEntity;
    private SelfTalkingEntity newSelfTalkingEntity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_talking);
        initView();
        initList();
    }

    public void initView() {
        userId = BmobUser.getCurrentUser(_User.class).getObjectId();
        selfTalkingPresenter = new SelfTalkingPresenterCompl();
        backIcon = (ImageView) findViewById(R.id.back_icon);
        backIcon.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.list_view);
    }

    public void initList() {
        final List<SelfTalkingEntity> listData = selfTalkingPresenter.getSqlData(SelfTalkingActivity.this, userId);
        adapter = new TimeLineAdapter(SelfTalkingActivity.this, R.layout.selftalking_timeline_list_item, listData);
        mListView.setAdapter(adapter);
        setListViewHeightBasedOnChildren();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                content = listData.get(position).getSelfTalking();
                curSelfTalkingEntity = listData.get(position);
                popupDisplayDialog(content);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify:
                displayDialog.dismiss();
                popupModifyDialog(content);
                break;
            case R.id.btn_delete:
                selfTalkingPresenter.deleteData(SelfTalkingActivity.this, curSelfTalkingEntity);
                displayDialog.dismiss();
                ToastUtil.showShort(SelfTalkingActivity.this, "已删除");
                initList();
                break;
            case R.id.btn_confirm:
                String newContent = input.getText().toString();
                if (newContent.equals("")) {
                    ToastUtil.showShort(SelfTalkingActivity.this, "内容不能为空");
                } else {
                    newSelfTalkingEntity = new SelfTalkingEntity(userId, curSelfTalkingEntity.getCurDate(), newContent);
                    selfTalkingPresenter.updateData(SelfTalkingActivity.this, curSelfTalkingEntity, newSelfTalkingEntity);
                    modifyDialog.dismiss();
                    ToastUtil.showShort(SelfTalkingActivity.this, "已修改");
                    initList();
                }
                break;
            case R.id.back_icon:
                finish();
                break;
        }
    }

    /**
     * 动态修改listview高度，使得listview能完全展开
     */
    private void setListViewHeightBasedOnChildren() {
        if (mListView == null) {
            return;
        }
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, mListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight;
        mListView.setLayoutParams(params);
    }

    /**
     * 弹出展示内容的对话框
     *
     * @param content
     */
    public void popupDisplayDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfTalkingActivity.this);
        View view = View.inflate(SelfTalkingActivity.this, R.layout.display_selftalking_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tv = (TextView) view.findViewById(R.id.dialog_tv);
        tv.setText(String.valueOf(content));
        Button modifyBtn = (Button) view.findViewById(R.id.btn_modify);
        modifyBtn.setOnClickListener(this);
        Button deleteBtn = (Button) view.findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(this);
        displayDialog = builder.create();
        displayDialog.show();
        displayDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    /**
     * 弹出修改内容的对话框
     *
     * @param content
     */
    public void popupModifyDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelfTalkingActivity.this);
        View view = View.inflate(SelfTalkingActivity.this, R.layout.modify_selftalking_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        input = (EditText) view.findViewById(R.id.dialog_edit);
        input.setText(String.valueOf(content));
        Button confirmBtn = (Button) view.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
        modifyDialog = builder.create();
        modifyDialog.show();
        modifyDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }
}
