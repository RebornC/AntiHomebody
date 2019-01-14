package com.example.yc.androidsrc.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.utils.ToastUtil;

/**
 * 虫洞
 *
 * @author RebornC
 * Created on 2019/1/12.
 */

public class WormholeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wormhole, container, false);
        initView();
        return view;
    }


    public void initView() {
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                ToastUtil.showShort(getActivity(), "和我说下悄悄话吧");
                break;
        }
    }
}
