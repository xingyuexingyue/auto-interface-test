package com.sangyu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装 RegularUtils 类
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午11:05
 */
public class RegularUtils {
    public static List<String> getSubUtil(String soap) {
        String rgex = "\\$\\{pre.(.*?)\\[";
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
            i++;
        }
        return list;
    }

    public static String getNumUtil(String value) {
        String strIndex = "";
        String regex = "\\d*";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(value);
        while (match.find()) {
            if (!"".equals(match.group()))
                strIndex = match.group();
        }
        return strIndex;
    }
}

