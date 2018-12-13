package com.example.yc.androidsrc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.utils.DateUtil;
import com.example.yc.androidsrc.views.LineChartMarkView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 用户步数历史记录统计
 *
 * @author RebornC
 * Created on 2018/12/10.
 */

public class StepChartActivity extends AppCompatActivity {

    private LineChart lineChart;
    private BarChart barChart;

    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_chart);

        lineChart = (LineChart) findViewById(R.id.linechart);

        // 初始化一条折线的数据源 一个数据点对应一个Entry对象 Entry对象包含x值和y值
        final List<Entry> entries = new ArrayList<>();
        Random random = new Random();

        entries.add(new Entry(1201, 7800));
        entries.add(new Entry(1202, 0));
        entries.add(new Entry(1203, 2030));
        entries.add(new Entry(1204, 0));
        entries.add(new Entry(1205, 0));
        entries.add(new Entry(1206, 700));
        entries.add(new Entry(1207, 5000));
        entries.add(new Entry(1208, 1800));
        entries.add(new Entry(1209, 0));
        entries.add(new Entry(1210, 0));
        entries.add(new Entry(1211, 0));
        entries.add(new Entry(1212, 0));
        entries.add(new Entry(1213, 7700));
        entries.add(new Entry(1214, 3400));
        entries.add(new Entry(1215, 100));
        entries.add(new Entry(1216, 0));
        entries.add(new Entry(1217, 2730));
        entries.add(new Entry(1218, 0));
        entries.add(new Entry(1219, 0));
        entries.add(new Entry(1220, 9700));
        entries.add(new Entry(1221, 8000));
        entries.add(new Entry(1223, 5800));
        entries.add(new Entry(1224, 0));
        entries.add(new Entry(1225, 1030));
        entries.add(new Entry(1226, 0));
        entries.add(new Entry(1227, 600));
        entries.add(new Entry(1228, 700));
        entries.add(new Entry(1229, 3000));
        // 将数据源装进折线对象LineDataSet 并设置折线对象的相关属性
        LineDataSet set = new LineDataSet(entries, "BarDataSet");
        set.setColor(getResources().getColor(R.color.colorAccent));         //设置线条颜色
        set.setDrawValues(false);                                      //设置显示数据点值
        set.setValueTextColor(getResources().getColor(R.color.colorAccent));//设置显示值的字体颜色
        set.setValueTextSize(12); //设置显示值的字体大小
        set.setCircleColorHole(getResources().getColor(R.color.colorAccent));

        set.setHighLightColor(getResources().getColor(R.color.gray));//设置点击交点后显示交高亮线的颜色
        set.setHighlightEnabled(true);//是否使用点击高亮线

        //设置曲线展示为圆滑曲线（如果不设置则默认折线）
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        // 设置折线图数据并绘制
        LineData lineData = new LineData(set);
        lineChart.setData(lineData);
        lineChart.setDrawGridBackground(false);
        lineChart.setNoDataTextDescription("无数据");  //设置无数据时显示的描述信息
        lineChart.setDrawGridBackground(false);        //设置是否绘制网格背景
        lineChart.setScaleEnabled(false);              //设置图表是否缩放
        lineChart.setDescription("");            //设置图表的描述信息
        lineChart.setDescriptionColor(getResources().getColor(R.color.white));    //设置描述信息的字体颜色
        //lineChart.setDescriptionPosition(550,330);     //设置描述信息的显示位置
        lineChart.setDescriptionTextSize(12);          //设置描述信息的字体大小

        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(false);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        leftYAxis.setTextColor(getResources().getColor(R.color.white));
        //xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinValue(0f);
        rightYaxis.setAxisMinValue(0f);

        // xAxis.setLabelCount(6, true); // 分割数量

        // 横坐标值转换
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String tradeDate = (int) value + "";
                return DateUtil.formatDate2(tradeDate);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }

        });

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        //是否显示边界
        lineChart.setDrawBorders(false);
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        rightYaxis.setEnabled(false);

        // 设置MarkerView
        LineChartMarkView mv = new LineChartMarkView(this, R.layout.chart_mark_view);
        lineChart.setMarkerView(mv);

        lineChart.invalidate();                                                //刷新显示绘图
        // lineChart.setBackgroundColor(getResources().getColor(R.color.black));  //设置LineChart的背景颜色
        // 设置折线图中每个数据点的选中监听
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
            }
        });

    }
}
