package com.example.yc.androidsrc.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.GifView;

import cn.bmob.v3.BmobUser;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class TabFragment1 extends Fragment {

    private _User curUser = BmobUser.getCurrentUser(_User.class);
    private Integer gifViewId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        TextView title = (TextView) getActivity().findViewById(R.id.title);
        title.setText("");

        final GifView gifV = (GifView) view.findViewById(R.id.gifview);
        gifV.setGifResource(R.drawable.level_1);
        final TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("1");

        final ImageView changeStateBtn = (ImageView) view.findViewById(R.id.change_state_btn);
        changeStateBtn.setTag('1');
        changeStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals('1')) {
                    v.setTag('0');
                    gifV.pause();
                    changeStateBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else if (v.getTag().equals('0')) {
                    v.setTag('1');
                    gifV.play();
                    changeStateBtn.setImageResource(R.drawable.ic_stop_black_24dp);
                }

            }
        });

        Button btn_2 = (Button) view.findViewById(R.id.btn_2);
        btn_2.setTag('1');
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals('1')) {
                    v.setTag('0');
                    gifV.setBackgroundColor(getResources().getColor(R.color.white));
                } else if (v.getTag().equals('0')) {
                    v.setTag('1');
                    gifV.setBackgroundColor(getResources().getColor(R.color.offWhite));
                }

            }
        });

        Button btn_3 = (Button) view.findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gifViewId < 8) gifViewId++;
                else gifViewId = 1;
                switch (gifViewId) {
                    case 1:
                        text.setText("1");
                        gifV.setGifResource(R.drawable.level_1);
                        break;
                    case 2:
                        text.setText("2");
                        gifV.setGifResource(R.drawable.level_2);
                        break;
                    case 3:
                        text.setText("3");
                        gifV.setGifResource(R.drawable.level_3);
                        break;
                    case 4:
                        text.setText("4");
                        gifV.setGifResource(R.drawable.level_4);
                        break;
                    case 5:
                        text.setText("5");
                        gifV.setGifResource(R.drawable.level_5);
                        break;
                    case 6:
                        text.setText("6");
                        gifV.setGifResource(R.drawable.level_6);
                        break;
                    case 7:
                        text.setText("7");
                        gifV.setGifResource(R.drawable.level_7);
                        break;
                    case 8:
                        text.setText("8");
                        gifV.setGifResource(R.drawable.level_8);
                        break;
                    default:
                        gifV.setGifResource(R.drawable.level_1);
                        break;
                }
            }
        });

        TextView degree = (TextView) view.findViewById(R.id.degree);
        degree.setText("Lv." + String.valueOf(curUser.getCurLevel()));
        TextView exp = (TextView) view.findViewById(R.id.exp);
        exp.setText(String.valueOf(curUser.getNumerator()) + "/" + String.valueOf(curUser.getDenominator()));

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            TextView title = (TextView) getActivity().findViewById(R.id.title);
            title.setText("");
        }
    }

}
