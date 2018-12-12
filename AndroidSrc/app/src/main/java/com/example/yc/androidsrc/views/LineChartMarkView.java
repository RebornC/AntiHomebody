package com.example.yc.androidsrc.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.utils.DateUtil;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;


/**
 * 折线图表的自定义MakerView
 *
 * @author RebornC
 * Created on 2018/12/11.
 */

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue;

    public LineChartMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvValue = (TextView) findViewById(R.id.tv_value);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String tradeDate = (int) e.getX() + "";
        tvDate.setText(DateUtil.formatDate3(tradeDate));
        tvValue.setText((int) e.getY() + "步");
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
