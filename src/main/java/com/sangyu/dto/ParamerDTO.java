package com.sangyu.dto;

import java.util.Map;

/**
 * parameterize (参数)表对应ATO 维护参数化字段
 *
 * User: pengyapan
 * Date: 2020/4/20
 * Time: 上午10:16
 */
public class ParamerDTO {

    private static final String JSON_PATH = "json_path";
    private static final String REMARK = "remark";
    private static final String TEAM_ID = "team_id";
    private static final String PARAMER_TYPE = "paramer_type";
    private static final String S_SQL = "s_sql";
    private static final String GET_TYPE = "get_type";
    private static final String DATA_NAME = "data_name";
    private static final String FIELD = "field";
    private static final String CASE_ID = "case_id";
    private static final String CASE_GET_TYPE = "case_get_type";
    private static final String ID = "id";
    private static final String IS_CHECK = "is_check";
    private static final String VALUE = "value";
    private static final String PARAMER_NAME = "paramer_name";
    private static final String REGULAR = "regular";

    private String jsonPath; // JsonPath
    private String remark; // 备注
    private String teamId; // 团队id
    private String paramerType; // 参数类型 0 k-v 类型 1 sql 类型 2 测试用例类型
    private String sSql; // sql
    private String getType; // 结果获取值方式 sql 类型用 0 单个值
    private String dataName; // 数据库名
    private String field; // 从 sql 结果中取用的字段
    private String caseId; // 用例 id
    private String caseGetType; // 用例取值方式 0全部内容 1jsonpath部分内容 2jsonpath部分内容如果是数组只取第一条 3正则表达式
    private String id;
    private String isCheck; // 是否开启检查点 0开启 1不开启且报错忽略
    private String value; // k-v 类型用
    private String paramerName; // 参数名称
    private String regular; // 正则表达式

    public ParamerDTO(Map<String, String> data){
        this.jsonPath = data.get(JSON_PATH);
        this.remark = data.get(REMARK);
        this.teamId = data.get(TEAM_ID);
        this.paramerType = data.get(PARAMER_TYPE);
        this.sSql = data.get(S_SQL);
        this.getType = data.get(GET_TYPE);
        this.dataName = data.get(DATA_NAME);
        this.field = data.get(FIELD);
        this.caseId = data.get(CASE_ID);
        this.caseGetType = data.get(CASE_GET_TYPE);
        this.id = data.get(ID);
        this.isCheck = data.get(IS_CHECK);
        this.value = data.get(VALUE);
        this.paramerName = data.get(PARAMER_NAME);
        this.regular = data.get(REGULAR);
    }

    public String getRemark() {
        return remark;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getParamerType() {
        return paramerType;
    }

    public String getsSql() {
        return sSql;
    }

    public String getGetType() {
        return getType;
    }

    public String getDataName() {
        return dataName;
    }

    public String getField() {
        return field;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getCaseGetType() {
        return caseGetType;
    }

    public String getId() {
        return id;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public String getValue() {
        return value;
    }

    public String getParamerName() {
        return paramerName;
    }

    public String getRegular() {
        return regular;
    }

    public String getJsonPath() {
        return jsonPath;
    }
}
