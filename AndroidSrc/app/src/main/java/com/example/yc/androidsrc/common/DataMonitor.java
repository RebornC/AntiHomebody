package com.example.yc.androidsrc.common;


import android.content.Context;
import android.util.Log;

import com.bumptech.glide.load.engine.Resource;
import com.example.yc.androidsrc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 传入用户前n天的每日能量总值
 * 对该数组进行趋势与均值的评估（各占50%权重）
 *
 * 1. 趋势评估：每个节点，当前数值与上一个数值进行比较
 * 得出该节点的趋势（下降/平稳/上升），分别得到0/60/100的比值
 *
 * 2. 均值评估：求出n天的能量平均值，除以maxDailyEnergy得到比值
 * 超过100%则按100%计算
 *
 * 3. 通过以上计算得到活跃度评价：
 * （0 <= score < 30）消极，罚扣50能量值
 * （30 <= score <= 70）平稳，null
 * （70 < score <= 100）积极，奖励50能量值
 *
 * 4. 当所有数据都为0时，估算分值为30，刚好处于"平稳状态"
 * 因为考虑到这种情况可能是(1)刚注册或者注册后一直没使用，此时不宜对用户活跃度作评价
 * (2)曾经使用过，但最近超过n天未使用，在刚暂停使用那段时间已罚扣能量值，之后不必再罚扣
 * PS：这里的"使用"指"通过系统途径积攒能量值"
 *
 * @author RebornC
 * Created on 2019/1/8.
 */

public class DataMonitor {

    private static float maxDailyEnergy = 650;

    /**
     * @param dailyEnergy
     * @return {score, scoreOfTrend, scoreOfAver}
     */
    public static List<Integer> estimate(List<Integer> dailyEnergy) {

        float num = dailyEnergy.size();
        float score = 0;
        float totalEnergy = 0;
        float averageEnergy;
        float scoreOfTrend = 0;
        float scoreOfAver;

        // 趋势评估
        for (int i = 0; i < num; i++) {
            totalEnergy += dailyEnergy.get(i);
            if (i != 0) {
                if (dailyEnergy.get(i) > dailyEnergy.get(i-1)) {
                    scoreOfTrend += 100 * (1 / (num - 1));
                } else if (dailyEnergy.get(i).equals(dailyEnergy.get(i-1))) {
                    // 比较Integer该包装类型的值是否相等时
                    // 当整型字面量的值在-128到127之间，不会new新的Integer对象，直接引用常量池中的对象，可以使用==进行比较
                    // 当不在这个范围内时，应该用equals()
                    scoreOfTrend += 60 * (1 / (num - 1));
                } else {
                    scoreOfTrend += 0;
                }
            }
        }
        score += scoreOfTrend / 2;

        // 均值评估
        averageEnergy = totalEnergy / num;
        scoreOfAver = 100 * averageEnergy / maxDailyEnergy;
        if (scoreOfAver > 100) scoreOfAver = 100;
        score += scoreOfAver / 2;

        List<Integer> result = new ArrayList<>();
        result.add((int) score);
        result.add((int) scoreOfTrend);
        result.add((int) scoreOfAver);

        return result;
    }

    /**
     * 根据活跃度返回对应的评价
     *
     * @param context
     * @param score
     * @return
     */
    public static String getEvaluationByScore(Context context, int score) {
        if (score < 30)
            return context.getResources().getStringArray(R.array.evaluation)[0];
        else if (score > 70)
            return context.getResources().getStringArray(R.array.evaluation)[2];
        else
            return context.getResources().getStringArray(R.array.evaluation)[1];
    }

    /**
     * 根据活跃度返回相应的能量值奖罚
     *
     * @param score
     * @return
     */
    public static int getResultByScore(int score) {
        if (score < 30)
            return -50;
        else if (score > 70)
            return 50;
        else
            return 0;
    }

}
