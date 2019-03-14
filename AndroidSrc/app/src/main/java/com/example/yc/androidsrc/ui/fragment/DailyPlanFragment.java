package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.TodoListAdapter;
import com.example.yc.androidsrc.common.AppConfig;
import com.example.yc.androidsrc.model.PlanEntity;
import com.example.yc.androidsrc.model.TodoListItem;
import com.example.yc.androidsrc.model._User;
import com.example.yc.androidsrc.presenter.DailyPlanPresenterCompl;
import com.example.yc.androidsrc.presenter.impl.IDailyPlanPresenter;
import com.example.yc.androidsrc.ui.impl.IDailyPlanView;
import com.example.yc.androidsrc.utils.DateUtil;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class DailyPlanFragment extends Fragment implements IDailyPlanView, View.OnClickListener {

    private View view;
    private IDailyPlanPresenter dailyPlanPresenter;

    private String objectId;
    private _User curUser;

    private SimpleDateFormat df;
    private String curDate;
    private String selectedDate;

    private Toolbar toolbar;
    private TextView title;
    private MaterialCalendarView calendarView;
    private ListView todoListView;
    private ListView completedListView;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private TextView levelOfComplete;
    private ImageView receiveEnergyIcon;
    private TextView energyValueTV;

    private List<TodoListItem> todoList = new ArrayList<>();
    private TodoListAdapter todoListAdapter;
    private List<PlanEntity> todoListData;
    private int todoLen = 0;
    private List<TodoListItem> completedList = new ArrayList<>();
    private TodoListAdapter completedListAdapter;
    private List<PlanEntity> completedListData;
    private int completedLen = 0;
    private int maxPlanEnergy = AppConfig.getMaxPlanEnergy();
    private float statusOfComplete = 0; // 完成度
    private int energyValue = 0; // 该天所积攒的能量值
    private boolean hasReceived = true; // 该天的能量值是否已被领取

    private AlertDialog dialog;
    private EditText input_edt;
    private int pos; // 点击进行修改的todoListView下标
    private static final String STATUS_TODO = "0";
    private static final String STATUS_COMPLETED = "1";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily_plan, container, false);

        initView();
        initDailyPlan();

        return view;
    }


    public void initView() {

        // 先通过Bmob本地缓存获取用户Id，在通过Id获取数据库中的本地记录
        dailyPlanPresenter = new DailyPlanPresenterCompl(this);
        objectId = BmobUser.getCurrentUser(_User.class).getObjectId();
        curUser = dailyPlanPresenter.getUserData(getActivity(), objectId);

        df = new SimpleDateFormat("yyyy-MM-dd");
        curDate = df.format(new Date());
        selectedDate = curDate;

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText(curDate);
        title.setTextColor(getResources().getColor(R.color.offWhite));
        title.setOnClickListener(this);

        todoListView = (ListView) view.findViewById(R.id.todo_list);
        completedListView = (ListView) view.findViewById(R.id.completed_list);
        todoListView.setDivider(null);
        completedListView.setDivider(null);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        levelOfComplete = (TextView) view.findViewById(R.id.level_of_complete);
        receiveEnergyIcon = (ImageView) view.findViewById(R.id.receive_energy_icon);
        receiveEnergyIcon.setOnClickListener(this);
        energyValueTV = (TextView) view.findViewById(R.id.energy_value);

        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendar_view);
        calendarView.state().edit()
                .commit();

        calendarView.setTopbarVisible(false); // 隐藏标题栏

        // 选中当日并设置颜色
        Calendar calendar = Calendar.getInstance();
        calendarView.setSelectedDate(calendar.getTime());
        calendarView.setSelectionColor(getResources().getColor(R.color.selectedDateBg));

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = df.format(date.getDate());
                title.setText(selectedDate);
                initDailyPlan();
            }
        });

    }


    public void initDailyPlan() {
        todoList.clear();
        todoListData = dailyPlanPresenter.getTodoPlan(getActivity(), objectId, selectedDate);
        todoLen = todoListData.size();
        for (int i = 0; i < todoListData.size(); i++) {
            TodoListItem todoListItem = new TodoListItem(todoListData.get(i).getPlan());
            todoList.add(todoListItem);
        }
        todoListAdapter = new TodoListAdapter(getActivity(), R.layout.todo_list_item, todoList);
        todoListView.setAdapter(todoListAdapter);
        setListViewHeightBasedOnChildren(todoListView);

        // TodoList点击item，弹出dialog，可修改或删除
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                popupModifyPlanDialog(getActivity());
            }
        });

        // TodoList点击icon，表示该事件已完成，同时将完成的该事件移入CompletedList首位
        todoListAdapter.setOnIconClickListener(new TodoListAdapter.onIconListener() {
            @Override
            public void onIconClick(int position) {
                PlanEntity oldPlanEntity = new PlanEntity(objectId, selectedDate, todoList.get(position).getText(), STATUS_TODO);
                PlanEntity newPlanEntity = new PlanEntity(objectId, selectedDate, todoList.get(position).getText(), STATUS_COMPLETED);
                dailyPlanPresenter.modifyPlan(getActivity(), oldPlanEntity, newPlanEntity);
            }
        });

        completedList.clear();
        completedListData = dailyPlanPresenter.getCompletedPlan(getActivity(), objectId, selectedDate);
        completedLen = completedListData.size();
        for (int i = 0; i < completedListData.size(); i++) {
            TodoListItem todoListItem = new TodoListItem(completedListData.get(i).getPlan());
            completedList.add(todoListItem);
        }
        completedListAdapter = new TodoListAdapter(getActivity(), R.layout.completed_list_item, completedList);
        completedListView.setAdapter(completedListAdapter);
        setListViewHeightBasedOnChildren(completedListView);

        // CompletedList点击icon，表示该事件其实未完成，将该事件移入TodoList末位
        completedListAdapter.setOnIconClickListener(new TodoListAdapter.onIconListener() {
            @Override
            public void onIconClick(int position) {
                PlanEntity oldPlanEntity = new PlanEntity(objectId, selectedDate, completedList.get(position).getText(), STATUS_COMPLETED);
                PlanEntity newPlanEntity = new PlanEntity(objectId, selectedDate, completedList.get(position).getText(), STATUS_TODO);
                dailyPlanPresenter.modifyPlan(getActivity(), oldPlanEntity, newPlanEntity);
            }
        });

        // 数据状态的变化
        levelOfComplete.setText(String.valueOf(completedLen) + "/" + String.valueOf(todoLen+completedLen));
        progressBar.setMax(todoLen+completedLen);
        progressBar.setProgress(completedLen);
        statusOfComplete = (float) completedLen / (float) (todoLen + completedLen);
        energyValue = (int) ((float) maxPlanEnergy * statusOfComplete);
        energyValueTV.setText(String.valueOf(energyValue));
        hasReceived = dailyPlanPresenter.hasReceivedEnergy(getActivity(), objectId, selectedDate);
        if (hasReceived) {
            receiveEnergyIcon.setImageResource(R.mipmap.get_energy_gray_icon);
        } else {
            receiveEnergyIcon.setImageResource(R.mipmap.get_energy_blue_icon);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                break;
            case R.id.fab:
                if (!curDate.equals(selectedDate) && DateUtil.isBeforeCurDate(curDate, selectedDate)) {
                    // 该日期在当天之前
                    ToastUtil.showShort(getActivity(), "不能为过去的日子新增计划");
                } else {
                    popupAddPlanDialog(getActivity());
                }
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm:
                addNewPlan();
                dialog.dismiss();
                break;
            case R.id.btn_modify:
                modifyPlan();
                dialog.dismiss();
                break;
            case R.id.btn_delete:
                deletePlan();
                dialog.dismiss();
                break;
            case R.id.receive_energy_icon:
                if (hasReceived) {
                    ToastUtil.showShort(getActivity(), "你已经领取过该能量值了");
                } else if (!curDate.equals(selectedDate) && !DateUtil.isBeforeCurDate(curDate, selectedDate)) {
                    // 该日期在当天之后，不能提前领取
                    ToastUtil.showShort(getActivity(), "诶？未来的能量值还是等到那天再领取吧");
                } else {
                    popupReceiveEnergyDialog(getActivity());
                }
                break;
            case R.id.btn_cancel_receive:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm_receive:
                if (energyValue == 0) {
                    ToastUtil.showShort(getActivity(), "抱歉，暂无可领取的能量");
                } else {
                    receiveEnergy();
                    dialog.dismiss();
                }
                break;
            default:
                break;
        }
    }


    public void addNewPlan() {
        String planText = input_edt.getText().toString();
        if (planText.equals("")) {
            ToastUtil.showShort(getActivity(), "请输入你的计划");
        } else {
            PlanEntity newPlan = new PlanEntity(objectId, selectedDate, planText, STATUS_TODO);
            dailyPlanPresenter.addNewPlan(getActivity(), newPlan);
        }
    }


    public void modifyPlan() {
        String planText = input_edt.getText().toString();
        if (planText.equals("")) {
            ToastUtil.showShort(getActivity(), "计划不能为空");
        } else {
            PlanEntity oldPlanEntity = new PlanEntity(objectId, selectedDate, todoList.get(pos).getText(), STATUS_TODO);
            PlanEntity newPlanEntity = new PlanEntity(objectId, selectedDate, planText, STATUS_TODO);
            dailyPlanPresenter.modifyPlan(getActivity(), oldPlanEntity, newPlanEntity);
        }
    }


    public void deletePlan() {
        PlanEntity planEntity = new PlanEntity(objectId, selectedDate, todoList.get(pos).getText(), STATUS_TODO);
        dailyPlanPresenter.deletePlan(getActivity(), planEntity);
    }


    public void receiveEnergy() {
        curUser = dailyPlanPresenter.getUserData(getActivity(), objectId);
        dailyPlanPresenter.receiveEnergy(getActivity(), curUser, selectedDate, energyValue);
        dailyPlanPresenter.updateBackendData(energyValue);
    }

    /**
     * 使得嵌套在ScrollView里的listView高度自适应
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }


    /**
     * 弹出带有输入框的自定义对话框，用于增加计划清单Item
     *
     * @param context
     */
    public void popupAddPlanDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.add_plan_custom_dialog, null);
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
        dialog.getWindow().setBackgroundDrawableResource(R.color.offWhite);
    }


    /**
     * 弹出带有输入框的自定义对话框，用于修改（包括删除）该计划
     *
     * @param context
     */
    public void popupModifyPlanDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.modify_plan_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        input_edt = (EditText) view.findViewById(R.id.dialog_edit);
        input_edt.setText(todoList.get(pos).getText());
        Button btn_modify = (Button)view.findViewById(R.id.btn_modify);
        Button btn_delete = (Button)view.findViewById(R.id.btn_delete);
        // 修改/删除按钮监听事件处理
        btn_modify.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.offWhite);
    }


    /**
     * 弹出领取能量值的对话框
     *
     * @param context
     */
    public void popupReceiveEnergyDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(getActivity(), R.layout.receive_energy_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView energy_value = (TextView) view.findViewById(R.id.energy_value);
        energy_value.setText(String.valueOf(energyValue));
        Button btn_cancel_receive = (Button) view.findViewById(R.id.btn_cancel_receive);
        Button btn_comfirm_receive = (Button) view.findViewById(R.id.btn_comfirm_receive);
        btn_cancel_receive.setOnClickListener(this);
        btn_comfirm_receive.setOnClickListener(this);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(800, 800);
    }


    @Override
    public void onUpdateData(boolean result, String message) {
        initDailyPlan();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            // selectedDate = df.format(calendarView.getSelectedDate().getDate());
            title.setText(selectedDate);
            title.setOnClickListener(this);
            initDailyPlan();
        }
    }
}
