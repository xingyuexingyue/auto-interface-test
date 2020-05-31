package com.sangyu.utils;

/**
 * 封装 JsonPathUtils 类
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午10:20
 */
public class JsonPathUtils {
    public static String splitJsonPath(String JsonPathString){
        int len = JsonPathString.length();
        String sub = JsonPathString.substring(1, len - 1);
        return sub;
    }
}
