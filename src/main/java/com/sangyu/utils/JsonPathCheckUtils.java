package com.sangyu.utils;

import com.jayway.jsonpath.JsonPath;
import com.sangyu.vo.HttpResponse;

/**
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 下午12:00
 */
public class JsonPathCheckUtils {
    private String jsonPath;
    private HttpResponse httpResponse;

    public JsonPathCheckUtils(String jsonPath, HttpResponse httpResponse) {
        this.jsonPath = jsonPath;
        this.httpResponse = httpResponse;
    }

    /**
     * 字符串类型结果检验：
     * 等于 : ==
     * 不等于 :  !==
     * 包含 : =
     * 不包含 : !=
     * @return
     */
    public Boolean spilitJsonPath() {
        Boolean success = false;
        String leftJson = "";
        String rightJson = "";
        String[] items = jsonPath.split("&&");
        for (int i = 0; i < items.length; i++) {
            String resData = httpResponse.getData();
            if (resData.contains("==")) {
                String[] split = resData.split("==");
                leftJson = split[0].substring(2, split[0].length() - 2);
                rightJson = split[1];
                Object read = JsonPath.read(resData, leftJson);
                if (read instanceof String) {
                    success = read.equals(rightJson);
                }
            } else if (resData.contains("!==")) {
                String[] split = resData.split("!==");
                leftJson = split[0].substring(2, split[0].length() - 2);
                rightJson = split[1];
                Object read = JsonPath.read(resData, leftJson);
                if (read instanceof String) {
                    success = !(read.equals(rightJson));
                }
            } else if (resData.contains("=")) {
                String[] split = resData.split("=");
                leftJson = split[0].substring(2, split[0].length() - 2);
                rightJson = split[1];
                Object read = JsonPath.read(resData, leftJson);
                if (read instanceof String) {
                    success = ((String) read).contains(rightJson);
                } else if (read instanceof Integer) {
                    success = (read == rightJson);
                }
            } else {
                success = spilitJsonPathNum(resData);
            }
        }

        return success;
    }

    /**
     * 数值校验:
     * 等于 : =
     * 大于 : >
     * 大于等于 : >=
     * 小于 : <
     * 小于等于 : <=
     * @param resData
     * @return
     */
    public Boolean spilitJsonPathNum(String resData) {
        Boolean success = false;
        String leftJson = "";
        String rightJson = "";
        if (resData.contains(">")) {
            String[] split = resData.split(">");
            leftJson = split[0].substring(2, split[0].length() - 2);
            rightJson = split[1];
            Object read = JsonPath.read(resData, leftJson);
            if (read instanceof Integer) {
                success = ((Integer) read > Integer.getInteger(rightJson));
            }
        } else if (resData.contains(">=")) {
            String[] split = resData.split(">=");
            leftJson = split[0].substring(2, split[0].length() - 2);
            rightJson = split[1];
            Object read = JsonPath.read(resData, leftJson);
            if (read instanceof Integer) {
                success = ((Integer) read >= Integer.getInteger(rightJson));
            }
        } else if (resData.contains("<")) {
            String[] split = resData.split("<");
            leftJson = split[0].substring(2, split[0].length() - 2);
            rightJson = split[1];
            Object read = JsonPath.read(resData, leftJson);
            if (read instanceof Integer) {
                success = ((Integer) read < Integer.getInteger(rightJson));
            }
        } else if (resData.contains("<=")) {
            String[] split = resData.split("<=");
            leftJson = split[0].substring(2, split[0].length() - 2);
            rightJson = split[1];
            Object read = JsonPath.read(resData, leftJson);
            if (read instanceof Integer) {
                success = ((Integer) read <= Integer.getInteger(rightJson));
            }
        }
        return success;
    }
}
