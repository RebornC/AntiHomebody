package com.example.yc.androidsrc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
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

}