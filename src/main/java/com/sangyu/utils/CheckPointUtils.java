package com.sangyu.utils;

import com.sangyu.config.DBConfig;
import com.sangyu.dto.ParamerDTO;
import com.sangyu.httptest.CaseParameters;
import com.sangyu.model.DBDataProvider;
import com.sangyu.vo.HttpResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午11:59
 */
public class CheckPointUtils {
    /**
     * 异常检查点 当响应的状态码异常时，则会报错
     *
     * @param httpResponse
     * @return
     */
    public boolean checkError(HttpResponse httpResponse) {
        if (httpResponse.getStatusCode() != 200) {
            return false;
        }
        return true;
    }

    /**
     * 为空检查点 当响应 data 出现""、"[]"、"{}"、null 这样的的结果，正常
     *
     * @param httpResponse:
     * @return
     */
    public boolean checkNull(HttpResponse httpResponse) {

        if (checkError(httpResponse)) {
            return false;
        }
        if (httpResponse.getData() == "" || httpResponse.getData() == "[]" || httpResponse.getData() == "{}" || httpResponse.getData() == "null") {
            return false;
        }
        return true;
    }

    /**
     * 不为空检查点 当响应 data 出现""、"[]"、"{}"、null 这样的的结果，都会报错
     *
     * @param httpResponse
     * @return
     */
    public boolean checkNoNull(HttpResponse httpResponse) {
        return !(checkNull(httpResponse));
    }

    /**
     * 包含检查点 “包含”和“不包含”检查点是将接口的返回结果作为一个String类型来看，检查所有返回内容中是否“包含”或“不包含”指定的内容
     *
     * @param httpResponse
     * @param keyWord
     * @return
     */
    public boolean checkContains(HttpResponse httpResponse, String keyWord) {
        if (checkError(httpResponse)) {
            return false;
        }
        if (checkNull(httpResponse)) {
            return false;
        }
        String data = httpResponse.getData();
        if (data.contains(keyWord)) {
            return true;
        }
        return false;
    }

    /**
     * 不包含检查点 与 checkContains() 相反
     *
     * @param httpResponse
     * @param keyWord
     * @return
     */
    public boolean checkNoContains(HttpResponse httpResponse, String keyWord) {
        return !checkContains(httpResponse, keyWord);
    }

    /**
     * 数据库参数检查点
     * <p>
     * 包括几种情况：
     * ${example}=="abc" 检查参数化名example的结果是否等于abc。
     * ${example}='a' : 检查参数化名example的结果是否包含a。
     * ${example}!='false' : 检查参数化名example的结果是否不包含false。
     * ${example1}=='aa' && ${example2}=='bb' : 多个检查点使用&&符号连接。
     *
     * @param dbCheck
     * @return
     */
    public boolean checkDBFileds(String dbCheck) throws SQLException, ClassNotFoundException {
        Boolean success = false;
        String[] items = dbCheck.split("&&");
        for (int i = 0; i < items.length; i++) {
            Fileds fileds = spiltFileds(items[i]);
            String value = dbValue(fileds.leftValue);
            String option = fileds.option;
            if ("==".equals(option)) {
                success = (value.equals(fileds.rightValue));
            } else if (option.equals("=")) {
                success = (value.contains(fileds.rightValue));
            } else if (option.equals("!=")) {
                success = !(value.contains(fileds.rightValue));
            }
        }
        return success;
    }


    /**
     * 从数据库获得字段的值，会拿这个值会预期的值进行比较
     *
     * @param sub
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String dbValue(String sub) throws SQLException, ClassNotFoundException {
        CaseParameters caseParameters = new CaseParameters();
        String sql = "SELECT * FROM parameterize where paramer_name = " + '"' + sub + '"';
        String value = "";
        DBDataProvider dataProvider_forDB = new DBDataProvider(DBConfig.DB_IP, DBConfig.DB_PORT,
                DBConfig.DB_BASE_NAME, DBConfig.DB_USERNAME, DBConfig.DB_PASSWORD, sql);
        dataProvider_forDB.buildList();
        List<Object[]> pmList = dataProvider_forDB.getList();
        ParamerDTO paramerDTO = new ParamerDTO((Map<String, String>) pmList.get(0)[0]);
        if (paramerDTO.getParamerType().equals("0")) {
            value = paramerDTO.getValue();
        } else if (paramerDTO.getParamerType().equals("1")) { //
            DBDataProvider data = new DBDataProvider(
                    DBConfig.DB_IP, DBConfig.DB_PORT, caseParameters.dataName(paramerDTO.getDataName()),
                    DBConfig.DB_USERNAME, DBConfig.DB_PASSWORD, paramerDTO.getsSql());
            Map<String, String> mapList = data.getMapList();
            value = mapList.get(paramerDTO.getField());
        }
        return value;

    }

    /**
     * 定义内部类 将 DB 检查点拆分为三部分，如 ${example}=="abc" ，三部分分别为 example、==、"abc"，example 是存储在参数化维护表里面的的name
     */
    class Fileds {
        String leftValue;
        String rightValue;
        String option;

        Fileds(String leftValue, String rightValue, String option) {
            this.leftValue = leftValue;
            this.rightValue = rightValue;
            this.option = option;
        }

        public String getLeftValue() {
            return leftValue;
        }

        public void setLeftValue(String leftValue) {
            this.leftValue = leftValue;
        }

        public String getRightValue() {
            return rightValue;
        }

        public void setRightValue(String rightValue) {
            this.rightValue = rightValue;
        }

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }
    }

    /**
     * DB 检查点拆分为三部分
     *
     * @param fileds
     * @return
     */
    public Fileds spiltFileds(String fileds) {
        String leftValue = "";
        String rightValue = "";
        String option = "";
        if (fileds.contains("==")) {
            String[] items = fileds.split("==");
            leftValue = items[0].substring(2, items[0].length() - 2);
            rightValue = items[1];
            option = "==";
        } else if (fileds.contains("=")) {
            String[] items = fileds.split("=");
            leftValue = items[0].substring(2, items[0].length() - 2);
            rightValue = items[1];
            option = "=";
        } else if (fileds.contains("!=")) {
            String[] items = fileds.split("!=");
            leftValue = items[0].substring(2, items[0].length() - 2);
            rightValue = items[1];
            option = "!=";
        }

        return new Fileds(leftValue, rightValue, option);

    }
}
