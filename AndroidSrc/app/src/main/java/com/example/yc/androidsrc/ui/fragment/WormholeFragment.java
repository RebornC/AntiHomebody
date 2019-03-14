package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model.SelfTalkingEntity;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.GrowUpPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.activity.SelfTalkingActivity;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.utils.ToastUtil;

import cn.bmob.v3.BmobUser;

/**
 * 虫洞
 *
 * @author RebornC
 * Created on 2019/1/12.
 */

public class WormholeFragment extends Fragment implements IGrowUpView, View.OnClickListener {

    private String objectId;
    private _User curUser;
    private IGrowUpPresenter growUpPresenter;
    private TextView title;
    private View view;
    private Button btn;
    private AlertDialog dialog;
    private EditText input_edt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wormhole, container, false);
        initView();
        return view;
    }


    public void initView() {
        growUpPresenter = new GrowUpPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        curUser = growUpPresenter.getUserData(getActivity(), objectId);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                popupAddSelfTalkingDialog(getActivity());
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm:
                String text = input_edt.getText().toString();
                if (text.equals("")) {
                    ToastUtil.showShort(getActivity(), "内容不能为空");
                } else {
                    growUpPresenter.addNewSelfTalking(getActivity(), curUser, text);
                    dialog.dismiss();
                }
                break;
            case R.id.title:
                Intent intent = new Intent(getActivity(), SelfTalkingActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 弹出带有输入框的自定义对话框，用于增加碎碎念
     *
     * @param context
     */
    public void popupAddSelfTalkingDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.add_selftalking_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        input_edt = (EditText) view.findViewById(R.id.dialog_edit);
        Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
        Button btn_comfirm = (Button)view.findViewById(R.id.btn_comfirm);
        // 取消/确定按钮监听事件处理
        btn_cancel.setOnClickListener(this);
        btn_comfirm.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        ToastUtil.showShort(getActivity(), message);
    }

    /**
     * 当ViewPager中的fragment切换为可见时
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (view != null) {
                title = (TextView) getActivity().findViewById(R.id.title);
                title.setText("查看更多");
                title.setOnClickListener(this);
            }
        }
    }
}
