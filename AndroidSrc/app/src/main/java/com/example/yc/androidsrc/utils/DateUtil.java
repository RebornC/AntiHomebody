package com.example.yc.androidsrc.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 注意，在liySimpleDateFormat转换日期格式时
 * HH表示24小时制，如18 （下午18时）
 * hh表示12小时制，如06 （下午18时）
 * 没有特殊情况时选择用HH
 *
 * @author RebornC
 * Created on 2018/12/11.
 */

public class DateUtil {

    /**
     * "20181230"转为"12-30"
     */
    public static String formatDate1(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    /**
     * "1230"转为"12-30"
     */
    public static String formatDate2(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("MMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    /**
     * "12-30"转为"12月30日"
     */
    public static String formatDate3(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("MM-dd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sf3 = new SimpleDateFormat("dd");
        int monthStr;
        int dayStr;
        String formatStr = "";
        try {
            monthStr = Integer.parseInt(sf2.format(sf1.parse(str)));
            dayStr = Integer.parseInt(sf3.format(sf1.parse(str)));
            formatStr = String.valueOf(monthStr) + "月" + String.valueOf(dayStr) + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    /**
     * "2018-12-30"转为"12-30"
     */
    public static String formatDate4(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    /**
     * 日期转为对应的周几
     */
    public static String DateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        String[] WEEK = {"日", "一", "二", "三", "四", "五", "六"};
        return WEEK[dayIndex - 1];
    }

    /**
     * "1、2、3..."转为"日、一、二..."
     */
    public static String getWeekIndex(int week_index) {
        switch (week_index) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
        }
        return "";
    }

    /**
     * 给出两个日期，例如2019-01-04和2019-01-03
     * 判断后者是否在前者日期之前
     *
     * @param selectedDate
     * @param curDate
     * @return
     */
    public static boolean isBeforeCurDate(String curDate, String selectedDate) {
        int curDate_Y = Integer.valueOf(curDate.substring(0, 4));
        int selectedDate_Y = Integer.valueOf(selectedDate.substring(0, 4));
        int curDate_M = Integer.valueOf(curDate.substring(5, 7));
        int selectedDate_M = Integer.valueOf(selectedDate.substring(5, 7));
        int curDate_D = Integer.valueOf(curDate.substring(8, 10));
        int selectedDate_D = Integer.valueOf(selectedDate.substring(8, 10));
        if (curDate_Y > selectedDate_Y) return true;
        else if (curDate_Y < selectedDate_Y) return false;
        else if (curDate_M > selectedDate_M) return true;
        else if (curDate_M < selectedDate_M) return false;
        else if (curDate_D > selectedDate_D) return true;
        else if (curDate_D < selectedDate_D) return false;
        else return false; // 此时日期相等
    }

}