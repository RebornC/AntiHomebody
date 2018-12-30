package com.example.yc.androidsrc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yc.androidsrc.R;

/**
 * 自定义按钮组件
 *
 * @author RebornC
 * Created on 2018/12/27.
 */

public class TextButton extends RelativeLayout {

    private TextView energy;
    private TextView textView;

    public TextButton(Context context) {
        super(context,null);
    }
    public TextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.text_btn, this,true);
        this.energy = (TextView) findViewById(R.id.energy);
        this.textView = (TextView) findViewById(R.id.text_view);
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setEnergyValue(int energyValue) {
        this.energy.setText(String.valueOf(energyValue));
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }
}
