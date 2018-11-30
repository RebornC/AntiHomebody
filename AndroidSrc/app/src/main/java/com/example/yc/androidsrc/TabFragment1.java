package com.example.yc.androidsrc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yc on 2018/11/28.
 */

public class TabFragment1 extends Fragment {

    private Integer gifViewId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, container, false);

        final GifView gifV = (GifView) view.findViewById(R.id.gifV);
        gifV.setGifResource(R.drawable.level_1);
        final TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("1");

        Button btn_1 = (Button) view.findViewById(R.id.btn_1);
        btn_1.setTag('1');
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals('1')) {
                    v.setTag('0');
                    gifV.pause();
                } else if (v.getTag().equals('0')) {
                    v.setTag('1');
                    gifV.play();
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
                    gifV.setBackgroundColor(getResources().getColor(R.color.colorAccent));
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

        return view;
    }
}
