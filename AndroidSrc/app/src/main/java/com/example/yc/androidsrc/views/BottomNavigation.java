package com.example.yc.androidsrc.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.example.yc.androidsrc.R;

/**
 * 底部圆弧导航
 *
 * @author RebornC
 * Created on 2018/12/8.
 */

public class BottomNavigation extends View {

    private int mWidth;
    private int mHeight;
    // 圆弧的宽度
    private float borderWidth = dipToPx(2);

    public BottomNavigation(Context context) {
        super(context);
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 1 绘制整体的灰色圆弧
        drawArc(canvas);
    }

    /**
     * 1 绘制整体的圆弧
     * @param canvas
     */
    public void drawArc(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        Path path = new Path();
        // 起点
        path.moveTo(0, mHeight);
        // quadTo(float x1, float y1, float x2, float y2):
        // 该曲线又称为"贝塞尔曲线"(Bezier curve)，其中，x1，y1为控制点的坐标值，x2，y2为终点的坐标值；
        path.quadTo(mWidth / 2, 0, mWidth, mHeight);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        if (widthMode == MeasureSpec.EXACTLY) {
//            mWidth = widthSize;
//        }
//        if (heightMode == MeasureSpec.EXACTLY) {
//            mHeight = heightSize;
//        }
        mWidth = widthSize;
        mHeight = heightSize;
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * dip 转换成 px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
