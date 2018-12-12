package com.example.yc.androidsrc.views;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.yc.androidsrc.R;

import static com.example.yc.androidsrc.R.attr.borderWidth;
import static com.example.yc.androidsrc.R.attr.thickness;

/**
 * 一个显示步数与进度的圆弧
 *
 * @author RebornC
 * Created on 2018/12/8.
 */

public class StepProgressView extends View {

    // 圆弧的宽度
    private float borderWidth = dipToPx(10);
    // 开始绘制圆弧的角度
    private float startAngle = 125;
    // 终点对应的角度和起始点对应的角度的夹角
    private float angleLength = 290;
    // 所要绘制的当前步数的蓝色圆弧终点到起点的夹角
    private float currentAngleLength = 0;
    // 当前目标步数
    private int targetStepNumber = 8000;
    // 所走步数
    private String stepCounts = "0";
    // 所走步数（限制不能超过目标步数，以免圆弧画过界）
    private String limitedStepCounts = "0";
    // 步数的数值字体大小
    private float numberTextSize = 0;
    // 步数上方文字
    private String current = "当前步数";
    // 动画时长
    private int animationLength = 1000;

    public StepProgressView(Context context) {
        super(context);
    }

    public StepProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 中心点的x坐标
        float centerX = getWidth() / 2;
        // 指定圆弧的外轮廓矩形区域
        RectF rectF = new RectF(borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);
        // 1 绘制整体的灰色圆弧
        drawArcGray(canvas, rectF);
        // 2 绘制当前进度的蓝色圆弧
        drawArcBlue(canvas, rectF);
        // 3 绘制当前进度的白色数字
        drawTextNumber(canvas, centerX);
        // 4 绘制"当前步数"的灰色文字
        drawTextStepString(canvas, centerX);
        // 5 绘制当前目标
        drawTextTarget(canvas, centerX);
    }

    /**
     * 1 绘制整体的灰色圆弧
     * @param canvas
     * @param rectF
     */
    public void drawArcGray(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        // 灰色画笔
        paint.setColor(getResources().getColor(R.color.gray));
        // 结合处为圆弧
        paint.setStrokeJoin(Paint.Join.MITER);
        // 设置画笔的样式
        paint.setStrokeCap(Paint.Cap.BUTT);
        // 设置画笔的填充样式
        paint.setStyle(Paint.Style.STROKE);
        // 设置路径效果
        float[] floats = {4, 16, 4, 16};
        paint.setPathEffect(new DashPathEffect(floats, 0));
        // 抗锯齿功能
        paint.setAntiAlias(true);
        // 设置画笔宽度
        paint.setStrokeWidth(borderWidth);
        /**
         * 开始绘制圆弧
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
         * 参数1是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧
         * 参数2是起始角(右中间开始为零度)在电弧的开始，圆弧起始角度，单位为度
         * 参数3圆弧扫过的角度，顺时针方向
         * 参数4是如果这是true的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false这将是一个弧线,
         * 参数5为Paint对象
         */
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);
    }

    /**
     * 2 绘制当前进度的蓝色圆弧
     * @param canvas
     * @param rectF
     */
    public void drawArcBlue(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        // 蓝色画笔
        paint.setColor(getResources().getColor(R.color.colorAccent));
        // 结合处为圆弧
        paint.setStrokeJoin(Paint.Join.MITER);
        // 设置画笔的样式
        paint.setStrokeCap(Paint.Cap.BUTT);
        // 设置画笔的填充样式
        paint.setStyle(Paint.Style.STROKE);
        // 设置路径效果
        // float[] floats = {4, 16, 4, 16};
        // paint.setPathEffect(new DashPathEffect(floats, 0));
        // 抗锯齿功能
        paint.setAntiAlias(true);
        // 设置画笔宽度
        paint.setStrokeWidth(borderWidth);
        // 开始绘制圆弧
        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paint);
    }

    /**
     * 3 绘制当前进度的白色数字
     * @param canvas
     * @param centerX
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true); // 抗锯齿功能
        vTextPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);
        vTextPaint.setColor(getResources().getColor(R.color.white));
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(stepCounts, 0, stepCounts.length(), bounds_Number);
        canvas.drawText(stepCounts, centerX, getHeight() / 2 + bounds_Number.height() / 2, vTextPaint);
    }

    /**
     * 4 绘制"当前步数"的灰色文字
     * @param canvas
     * @param centerX
     */
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(16));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.gray));
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(current, 0, current.length(), bounds);
        canvas.drawText(current, centerX, getHeight() / 2 + bounds.height() - 2 * getFontHeight(numberTextSize), vTextPaint);
    }

    /**
     * 5 绘制当前目标
     * @param canvas
     * @param centerX
     */
    private void drawTextTarget(Canvas canvas, float centerX) {
        Paint mLevelPaint = new Paint();
        mLevelPaint.setTextSize(dipToPx(12));
        mLevelPaint.setTextAlign(Paint.Align.CENTER);
        mLevelPaint.setAntiAlias(true);
        mLevelPaint.setColor(getResources().getColor(R.color.gray));
        Rect bounds = new Rect();
        String target = "目标：" + String.valueOf(targetStepNumber) +"步";
        mLevelPaint.getTextBounds(target, 0, target.length(), bounds);
        canvas.drawText(target, centerX, getHeight() / 2 + 3.5f * bounds.height() + 2 * getFontHeight(numberTextSize), mLevelPaint);
    }

    /**
     * 设置当前目标步数
     * @param targetStepNumber
     */
    public void setTargetStepNumber(int targetStepNumber) {
        this.targetStepNumber = targetStepNumber;
    }

    /**
     * 设置所走的步数进度
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int totalStepNum, int currentCounts) {
        stepCounts = String.valueOf(currentCounts);
        setTextSize(currentCounts);
        // 如果当前走的步数超过目标总步数，圆弧已满270度无法再变化
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }
        // 上次所走步数占用总共步数的百分比
        float scalePrevious = (float) Integer.valueOf(limitedStepCounts) / totalStepNum;
        // 换算成弧度最后要到达的角度数
        float previousAngleLength = scalePrevious * angleLength;
        // 当前所走步数占用总共步数的百分比
        float scale = (float) currentCounts / totalStepNum;
        // 换算成弧度最后要到达的角度数
        float currentAngleLength = scale * angleLength;
        // 开始执行动画
        setAnimation(previousAngleLength, currentAngleLength, animationLength);
        // 动态规划
        limitedStepCounts = String.valueOf(currentCounts);
    }

    /**
     * 为步数的进度变化设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果
     * @param start   初始值
     * @param current 结束值
     * @param length  动画时长
     */
    private void setAnimation(float start, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(start, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 每次在初始值和结束值之间产生的一个平滑过渡的值，逐步去更新进度
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
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

    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 5) {
            numberTextSize = dipToPx(50);
        } else if (length > 5 && length <= 7) {
            numberTextSize = dipToPx(40);
        } else if (length > 7 && length <= 9) {
            numberTextSize = dipToPx(30);
        } else if (length > 10) {
            numberTextSize = dipToPx(25);
        }
    }

    /**
     * 获取当前步数的数字的高度
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepCounts, 0, stepCounts.length(), bounds_Number);
        return bounds_Number.height();
    }

}
