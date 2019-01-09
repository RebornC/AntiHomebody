package com.example.yc.androidsrc.views;

import android.content.Context;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.utils.DateUtil;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 用户活跃度的折线图表的自定义MakerView
 * 根据传进来的dayLength，获取X轴每个index对应的日期并展示（不包括今天）
 *
 * @author RebornC
 * Created on 2019/1/8.
 */

public class EnergyMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;
    private int dayLength;
    private List<String> dateList = new ArrayList<>();

    public EnergyMarkView(Context context, int layoutResource, int dayLength) {
        super(context, layoutResource);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvValue = (TextView) findViewById(R.id.tv_value);
        this.dayLength = dayLength;

        // 根据dayLength获取近7天or30天的日期
        Calendar cal = Calendar.getInstance();
        for(int i = 0 ; i < dayLength + 1; i++){
            Date date = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
            String dateString = formatter.format(date);
            if (i != 0) {
                dateList.add(dateString);
            }
            // 将日历日期往前推1天
            cal.add(cal.DATE, -1);
        }

        // 翻转数组
        Collections.reverse(dateList);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String tradeDate = dateList.get((int) e.getX());
        tvDate.setText(tradeDate);
        tvValue.setText((int) e.getY() + "能量值");
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

}
