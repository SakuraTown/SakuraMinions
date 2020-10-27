package com.sakuratown.sakuraminions;

import java.util.HashMap;
import java.util.Map;

public class RandomItem {

    public static String getRandomString(Map<String, Double> itemList) {

        double total = 0.0, set = 0.0; //total 加权值之合
        for (Map.Entry<String, Double> item : itemList.entrySet()) {
            total += item.getValue();
        }

        double randomNum = Math.random() * total;
        for (Map.Entry<String, Double> item : itemList.entrySet()) {
            set += item.getValue();   //分阶段判断随机区间是否匹配
            if (randomNum <= set) {
                return item.getKey();
            }
        }
        return null;
    }
}


