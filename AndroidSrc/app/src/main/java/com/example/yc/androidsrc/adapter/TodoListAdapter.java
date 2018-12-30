package com.example.yc.androidsrc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model.MenuItem;
import com.example.yc.androidsrc.model.TodoListItem;
import com.example.yc.androidsrc.utils.ToastUtil;

import java.util.List;

/**
 * @author RebornC
 * Created on 2018/12/27.
 */

public class TodoListAdapter extends ArrayAdapter<TodoListItem> {

    private int resourceId;
    private Context context;

    public TodoListAdapter(Context context, int textViewResourceId, List<TodoListItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TodoListItem todoListItem = getItem(position);
        View view;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.text = (TextView) view.findViewById (R.id.text);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.text.setText(todoListItem.getText());
        // 为icon设置点击事件
        viewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastUtil.showShort(context, String.valueOf(position));
                // 先让图标变换样式然后再移动listItem，从而达到视觉上的移动效果
                if (resourceId == R.layout.todo_list_item) {
                    viewHolder.icon.setImageResource(R.mipmap.completed_list_icon);
                } else {
                    viewHolder.icon.setImageResource(R.mipmap.todo_list_icon);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnIconListener.onIconClick(position);
                        if (resourceId == R.layout.todo_list_item) {
                            viewHolder.icon.setImageResource(R.mipmap.todo_list_icon);
                        } else {
                            viewHolder.icon.setImageResource(R.mipmap.completed_list_icon);
                        }
                    }
                }, 200); // 0.2秒后执行
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView icon;
        TextView text;
    }

    /**
     * icon点击事件的监听接口
     */
    public interface onIconListener {
        void onIconClick(int position);
    }

    private onIconListener mOnIconListener;

    public void setOnIconClickListener(onIconListener mOnIconListener) {
        this.mOnIconListener = mOnIconListener;
    }

}
