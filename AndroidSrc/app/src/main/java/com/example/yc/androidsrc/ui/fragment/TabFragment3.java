package com.example.yc.androidsrc.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.adapter.MenuItemAdapter;
import com.example.yc.androidsrc.adapter.TodoListAdapter;
import com.example.yc.androidsrc.model.MenuItem;
import com.example.yc.androidsrc.model.TodoListItem;
import com.example.yc.androidsrc.ui.activity.StepChartActivity;
import com.example.yc.androidsrc.utils.ToastUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import b.Android;

/**
 * @author RebornC
 * Created on 2018/11/28.
 */

public class TabFragment3 extends Fragment implements View.OnClickListener {

    private SimpleDateFormat df;

    private View view;

    private MaterialCalendarView calendarView;
    private TextView title;
    private ListView todoListView;
    private ListView completedListView;
    private FloatingActionButton fab;

    private List<TodoListItem> todoList = new ArrayList<>();
    private TodoListAdapter todoListAdapter;
    private List<TodoListItem> completedList = new ArrayList<>();
    private TodoListAdapter completedListAdapter;

    private AlertDialog dialog;
    private EditText input_edt;
    private int pos; // 点击进行修改的todoListView下标

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        initView();

        df = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = df.format(new Date());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        title = (TextView) getActivity().findViewById(R.id.title);
        title.setText(curDate);
        title.setTextColor(getResources().getColor(R.color.offWhite));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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
                title.setText(df.format(date.getDate()));
            }
        });

        return view;
    }

    public void initView() {
        // find view

        todoListView = (ListView) view.findViewById(R.id.todo_list);
        completedListView = (ListView) view.findViewById(R.id.completed_list);
        todoListView.setDivider(null);
        completedListView.setDivider(null);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        // init ListView

        final String[] todoListData = {"床前明月光", "我在等风也在等你", "吃饭", "要记得早点睡觉", "买铅笔"};
        for (int i = 0; i < todoListData.length; i++) {
            TodoListItem todoListItem = new TodoListItem(todoListData[i]);
            todoList.add(todoListItem);
        }
        todoListAdapter = new TodoListAdapter(getActivity(), R.layout.todo_list_item, todoList);
        todoListView.setAdapter(todoListAdapter);
        setListViewHeightBasedOnChildren(todoListView);
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
                // completedList.add(0, todoList.get(position));
                completedList.add(todoList.get(position));
                completedListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(completedListView);
                todoList.remove(position);
                todoListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(todoListView);
            }
        });

        String[] completedListData = {"床前明月光疑是地上霜举头望明月低头思故乡", "学习", "逛超市去咯~", "买薯片蛋糕辣条"};
        for (int i = 0; i < completedListData.length; i++) {
            TodoListItem todoListItem = new TodoListItem(completedListData[i]);
            completedList.add(todoListItem);
        }
        completedListAdapter = new TodoListAdapter(getActivity(), R.layout.completed_list_item, completedList);
        completedListView.setAdapter(completedListAdapter);
        setListViewHeightBasedOnChildren(completedListView);

        // CompletedList点击icon，表示该事件其实未完成，将该事件移入TodoList末位
        completedListAdapter.setOnIconClickListener(new TodoListAdapter.onIconListener() {
            @Override
            public void onIconClick(int position) {
                todoList.add(completedList.get(position));
                todoListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(todoListView);
                completedList.remove(position);
                completedListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(completedListView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                popupAddPlanDialog(getActivity());
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_comfirm:
                String planText = input_edt.getText().toString();
                if (planText.equals("")) {
                    ToastUtil.showShort(getActivity(), "请输入你的计划");
                } else {
                    dialog.dismiss();
                    todoList.add(new TodoListItem(planText));
                    todoListAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(todoListView);
                }
                break;
            case R.id.btn_modify:
                String newPlanText = input_edt.getText().toString();
                if (newPlanText.equals("")) {
                    ToastUtil.showShort(getActivity(), "计划不能为空");
                } else {
                    dialog.dismiss();
                    todoList.get(pos).setText(newPlanText);
                    todoListAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(todoListView);
                }
                break;
            case R.id.btn_delete:
                dialog.dismiss();
                todoList.remove(pos);
                todoListAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(todoListView);
                ToastUtil.showShort(getActivity(), "删除成功");
                break;
            default:
                break;
        }
    }

    // 使得嵌套在ScrollView里的listView高度自适应
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

    // 弹出带有输入框的自定义对话框，用于增加计划清单Item
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

    // 弹出带有输入框的自定义对话框，用于修改（包括删除）该计划
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // onPause()
        } else {
            // onResume()
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            TextView title = (TextView) getActivity().findViewById(R.id.title);
            String selectedDate = df.format(calendarView.getSelectedDate().getDate());
            title.setText(selectedDate);
            title.setTextColor(getResources().getColor(R.color.offWhite));
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
