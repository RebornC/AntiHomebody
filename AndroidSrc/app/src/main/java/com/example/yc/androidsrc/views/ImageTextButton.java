package com.example.yc.androidsrc.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yc.androidsrc.R;

/**
 * 自定义按钮组件
 *
 * @author RebornC
 * Created on 2018/12/10.
 */

public class ImageTextButton extends RelativeLayout {

    private ImageView imageView;
    private TextView textView;

    public ImageTextButton(Context context) {
        super(context,null);
    }
    public ImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.image_text_btn, this,true);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        this.textView = (TextView) findViewById(R.id.text_view);
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void setImageResource(int resourceID) {
        this.imageView.setImageResource(resourceID);
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
}
