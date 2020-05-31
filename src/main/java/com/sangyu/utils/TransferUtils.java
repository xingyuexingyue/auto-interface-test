package com.sangyu.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装 TransferUtils 类
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午10:51
 */
public class TransferUtils {

    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        String sbToStr = sb.toString();
        return sbToStr.substring(0, sbToStr.length() - 1);
    }

    public static Map<String, String> stringToMap(String str) {
        String[] strs = str.split("&");
        Map<String, String> m = new HashMap<String, String>();
        for (String s : strs) {
            String[] ms = s.split("=");
            m.put(ms[0], ms[1]);
        }
        return m;
    }
}
