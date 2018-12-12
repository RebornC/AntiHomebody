package com.example.yc.androidsrc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 2018/12/11.
 */

public class DateUtil {

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

    public static String formatDate3(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("MMdd");
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

}