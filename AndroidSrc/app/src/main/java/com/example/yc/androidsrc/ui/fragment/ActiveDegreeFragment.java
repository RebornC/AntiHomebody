package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.common.DataMonitor;
import com.example.yc.androidsrc.db.DailyEnergyDao;
import com.example.yc.androidsrc.db.StepDataDao;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.GrowUpPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IGrowUpPresenter;
import com.example.yc.androidsrc.ui.impl.IGrowUpView;
import com.example.yc.androidsrc.utils.DateUtil;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.example.yc.androidsrc.views.EnergyMarkView;
import com.example.yc.androidsrc.views.GifView;
import com.example.yc.androidsrc.views.LineChartMarkView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 用户近七天活跃度界面
 *
 * @author RebornC
 * Created on 2019/1/8.
 */

public class ActiveDegreeFragment extends Fragment implements IGrowUpView, View.OnClickListener {

    private String objectId;
    private _User curUser;
    private View view;
    private IGrowUpPresenter growUpPresenter;
    private Button scoreBtn;
    private TextView scoreOfTrendTv;
    private TextView totalEnergyTv;
    private TextView totalStepTv;
    private TextView totalPlanTv;
    private List<Integer> dailyEnergy;
    private LineChart lineChart;
    private static int len = 7;
    private XAxis xAxis; // X轴
    private YAxis leftYAxis; // 左侧Y轴
    private YAxis rightYaxis; // 右侧Y轴
    private Legend legend; // 图例
    private List<String> dateList; // 最近七天（不包含当天）
    private List<String> weekIndexList;
    private List<Entry> entries; // 数据源，一个数据点对应一个Entry对象，Entry对象包含x值和y值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_active_degree, container, false);

        initView();
        // 获取最近七天对应日期
        getDate();
        // 获取指定用户的数据库每日总能量值信息
        getUserEnergyData();
        // 将数据源装进折线对象LineDataSet，并设置折线对象的相关属性
        drawLineChart();

        return view;
    }

    public void initView() {
        growUpPresenter = new GrowUpPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        curUser = growUpPresenter.getUserDate(getActivity(), objectId);
        lineChart = (LineChart) view.findViewById(R.id.linechart);
        scoreBtn = (Button) view.findViewById(R.id.score);
        scoreOfTrendTv = (TextView) view.findViewById(R.id.score_of_trend);
        totalEnergyTv = (TextView) view.findViewById(R.id.total_energy);
        totalStepTv = (TextView) view.findViewById(R.id.total_step);
        totalPlanTv = (TextView) view.findViewById(R.id.total_plan);
        dailyEnergy = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {

    }

    public void getDate() {
        dateList = new ArrayList<>();
        weekIndexList = new ArrayList<>();
        // Calendar 的 getInstance 方法返回一个 Calendar 对象，其日历字段已由当前日期和时间初始化
        Calendar cal = Calendar.getInstance();
        for (int i = 0 ; i < len + 1; i++) {
            // 获取当前日历的日期的星期数（比如1:星期天）
            int week_index = cal.get(Calendar.DAY_OF_WEEK);
            Date date = cal.getTime();
            // 日期格式化 yyyy-MM-dd
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            if (i != 0) {
                dateList.add(dateString);
                weekIndexList.add(DateUtil.getWeekIndex(week_index));
            }
            // 将日历日期往前推1天
            cal.add(cal.DATE, -1);
        }
        // 翻转数组
        Collections.reverse(dateList);
        Collections.reverse(weekIndexList);
    }

    public void getUserEnergyData() {
        entries = new ArrayList<>();
        int totalEnergy = 0;
        for (int i = 0; i < dateList.size(); i++) {
            int energyValue = growUpPresenter.getUserDailyEnergyData(getActivity(), curUser.getObjectId(), dateList.get(i));
            entries.add(new Entry(i, energyValue));
            dailyEnergy.add(energyValue);
            totalEnergy += energyValue;
        }
        // 进行数据评估，返回相关结果
        List<Integer> result = DataMonitor.estimate(dailyEnergy);
        int totalStep = growUpPresenter.getTotalStepByDateList(getActivity(), curUser.getObjectId(), dateList);
        int totalPlan = growUpPresenter.getTotalPlanByDateList(getActivity(), curUser.getObjectId(), dateList);
        scoreBtn.setText("# 活跃度" + String.valueOf(result.get(0)) + "分");
        scoreOfTrendTv.setText(String.valueOf(result.get(1)));
        totalEnergyTv.setText(String.valueOf(totalEnergy));
        totalStepTv.setText(String.valueOf(totalStep));
        totalPlanTv.setText(String.valueOf(totalPlan));
    }

    public void drawLineChart() {
        LineDataSet set = new LineDataSet(entries, "BarDataSet");
        set.setColor(getResources().getColor(R.color.colorAccent)); // 设置线条颜色
        set.setDrawValues(false); // 设置显示数据点值
        set.setValueTextColor(getResources().getColor(R.color.colorAccent)); // 设置显示值的字体颜色
        set.setValueTextSize(12); // 设置显示值的字体大小
        // set.setCircleColorHole(getResources().getColor(R.color.colorAccent));
        set.setCircleColor(getResources().getColor(R.color.colorAccent));
        set.setHighLightColor(getResources().getColor(R.color.transparent)); // 设置点击交点后显示交高亮线的颜色
        set.setHighlightEnabled(true); // 是否使用点击高亮线
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置曲线展示为圆滑曲线（如果不设置则默认折线）
        // 设置折线图数据并绘制
        LineData lineData = new LineData(set);
        lineChart.setData(lineData);
        lineChart.setDrawGridBackground(false);
        lineChart.setNoDataTextDescription("无数据"); // 设置无数据时显示的描述信息
        lineChart.setDrawGridBackground(false); // 设置是否绘制网格背景
        lineChart.setScaleEnabled(false); // 设置图表是否缩放
        lineChart.setDescription(""); // 设置图表的描述信息
        lineChart.setDescriptionColor(getResources().getColor(R.color.colorPrimary)); // 设置描述信息的字体颜色
        //lineChart.setDescriptionPosition(550,330); // 设置描述信息的显示位置
        lineChart.setDescriptionTextSize(12); // 设置描述信息的字体大小
        lineChart.setDrawGridBackground(false); // 是否展示网格线
        lineChart.setDrawBorders(false); // 是否显示边界
        lineChart.setDragEnabled(false); // 是否可以拖动
        lineChart.setTouchEnabled(true); // 是否有触摸事件
        // 设置图表距离上下左右的距离: setExtraOffsets(left, top, right, bottom)
        lineChart.setExtraOffsets(30, 40, 30, 0);
        // 设置XY轴动画效果和属性
        lineChart.animateY(2500);
        lineChart.animateX(1500);
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        // X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        leftYAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        // xAxis.setAxisMinValue(0f);
        xAxis.setGranularity(1f);
        // 保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinValue(0f);
        rightYaxis.setAxisMinValue(0f);
        // xAxis.setLabelCount(6, true); // 分割数量
        // 是否显示边界
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        rightYaxis.setEnabled(false);
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setEnabled(false);
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);

        // X轴横坐标值转换，不显示
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }

        });

        // 折线图例的标签设置
        legend = lineChart.getLegend();
        // 设置显示类型
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        // 设置显示位置为左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        // 是否绘制在图表里面
        legend.setDrawInside(false);
        // 采用自定义标签MarkerView
        EnergyMarkView mv = new EnergyMarkView(getActivity(), R.layout.chart_mark_view_2, len);
        lineChart.setMarkerView(mv);

        // 设置图表为自定义渐变色
        Drawable drawable = getResources().getDrawable(R.drawable.chart_draw_fade);
        setChartFillDrawable(drawable);

        lineChart.invalidate(); // 刷新显示绘图
        // lineChart.setBackgroundColor(getResources().getColor(R.color.black)); // 设置LineChart的背景颜色
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

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    @Override
    public void onUpdateData(boolean result, int resultCode, String message) {
        //
    }
}
