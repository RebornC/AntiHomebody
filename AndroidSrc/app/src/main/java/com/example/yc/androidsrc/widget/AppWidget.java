package com.example.yc.androidsrc.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.yc.androidsrc.R;
import com.example.yc.androidsrc.ui.activity.WelcomeActivity;

/**
 * 桌面小窗口部件
 * 可作为程序快速入口
 *
 * @author RebornC
 * Created on 2018/02/19.
 */

public class AppWidget extends AppWidgetProvider {

    public static final String CLICK_TO_STEP = "click.step";
    public static final String CLICK_TO_PLAN = "click.plan";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        Intent intentToStep = new Intent(context, WelcomeActivity.class);
        intentToStep.setAction(CLICK_TO_STEP);
        intentToStep.putExtra("key", "switchStepCounterFragment"); // 点击widget跳转到计步器fragment界面
        // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); // 这样会导致intent携带的数据无法更新
        PendingIntent pendingIntentToStep = PendingIntent.getActivity(context, 1, intentToStep, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.step, pendingIntentToStep);

        Intent intentToPlan = new Intent(context, WelcomeActivity.class);
        intentToPlan.setAction(CLICK_TO_PLAN);
        intentToPlan.putExtra("key", "switchDailyPlanFragment"); // 点击widget跳转到每日计划fragment界面
        PendingIntent pendingIntentToPlan = PendingIntent.getActivity(context, 1, intentToPlan, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.plan, pendingIntentToPlan);

        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}