package com.example.yc.androidsrc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model.MenuItem;
import com.example.yc.androidsrc.model.SelfTalkingEntity;

import java.util.List;

/**
 * @author RebornC
 * Created on 2019/1/15.
 */

public class TimeLineAdapter extends ArrayAdapter<SelfTalkingEntity> {

    private int resourceId;

    public TimeLineAdapter(Context context, int textViewResourceId, List<SelfTalkingEntity> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelfTalkingEntity selfTalkingEntity = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.day = (TextView) view.findViewById (R.id.day);
            viewHolder.time = (TextView) view.findViewById (R.id.time);
            viewHolder.selftalking = (TextView) view.findViewById(R.id.self_talking);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.day.setText(selfTalkingEntity.getCurDate().substring(0, 10));
        viewHolder.time.setText(selfTalkingEntity.getCurDate().substring(11, 16));
        viewHolder.selftalking.setText(selfTalkingEntity.getSelfTalking());
        return view;
    }

    class ViewHolder {
        TextView day;
        TextView time;
        TextView selftalking;
    }

}
