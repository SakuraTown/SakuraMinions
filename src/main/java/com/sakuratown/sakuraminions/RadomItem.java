package com.sakuratown.sakuraminions;
import java.util.Map;

public class RadomItem {
    public String getRandomString(Map<String, Double> itemList) {
        double total = 0.0,set = 0.0;
        for (Map.Entry<String, Double> item : itemList.entrySet()) {
            total += item.getValue();
        }
        double randomNum = Math.random() * total;
        for (Map.Entry<String, Double> item : itemList.entrySet()) {
            set += item.getValue();
            if(randomNum <= set){
                return item.getKey();
            }
        }
        return null;
    }
}


