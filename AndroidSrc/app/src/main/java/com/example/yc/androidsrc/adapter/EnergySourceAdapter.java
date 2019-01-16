package com.example.yc.androidsrc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.model.EnergySourceEntity;
import com.example.yc.androidsrc.model.SelfTalkingEntity;

import java.util.List;

/**
 * @author RebornC
 * Created on 2019/1/15.
 */

public class EnergySourceAdapter extends ArrayAdapter<EnergySourceEntity> {

    private int resourceId;

    public EnergySourceAdapter(Context context, int textViewResourceId, List<EnergySourceEntity> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EnergySourceEntity energySourceEntity = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.curDay = (TextView) view.findViewById (R.id.day);
            viewHolder.curTime = (TextView) view.findViewById (R.id.time);
            viewHolder.text = (TextView) view.findViewById(R.id.text);
            viewHolder.timelineIcon = (ImageView) view.findViewById(R.id.timeline_icon);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.curDay.setText(energySourceEntity.getCurDay());
        viewHolder.curTime.setText(energySourceEntity.getCurTime());
        viewHolder.text.setText(getText(view, energySourceEntity.getSource(), energySourceEntity.getEnergy()));
        viewHolder.timelineIcon.setImageResource(getImageId(view, energySourceEntity.getSource()));
        return view;
    }

    class ViewHolder {
        TextView curDay; // example: 2018-12-28
        TextView curTime; // example: 12:00
        TextView text;
        ImageView timelineIcon;
    }

    /**
     * 通过判断能量来源决定返回对应的表述
     * @param view
     * @param source
     * @param energy
     * @return
     */
    private String getText(View view, String source, Integer energy) {
        String[] list = view.getResources().getStringArray(R.array.source_of_energy);
        if (source.equals(list[0])) {
            // 计步
            return "通过计步获得了"+String.valueOf(energy)+"能量值";
        } else if (source.equals(list[1])) {
            // 打卡
            return "通过完成计划并打卡获得了"+String.valueOf(energy)+"能量值";
        } else if (source.equals(list[2])) {
            // 系统奖励
            return "由于表现积极，系统奖励了你50能量值";
        } else {
            // 系统扣罚
            return "由于活跃度过低，系统扣罚了你50能量值";
        }
    }

    /**
     * 通过判断能量来源决定返回对应的图标
     * @param view
     * @param source
     * @return
     */
    private int getImageId(View view, String source) {
        String[] list = view.getResources().getStringArray(R.array.source_of_energy);
        if (source.equals(list[3])) {
            // 系统扣罚
            return R.mipmap.circle_gray_icon;
        } else {
            return R.mipmap.circle_blue_icon;
        }
    }

}
