package com.sangyu.dto;

import java.util.Map;

/**
 * action (动作)表对应 DTO 维护前后置动作
 *
 * User: pengyapan
 * Date: 2020/4/21
 * Time: 下午2:42
 */
public class ActionDTO {

    private static final String ACTION_NAME = "action_name";
    private static final String TEAM_ID = "team_id";
    private static final String REMARK = "remark";
    private static final String ACTION_TYPE = "action_type";
    private static final String DATA_NAME = "data_name";
    private static final String S_SQL = "s_sql";
    private static final String URL = "url";
    private static final String GET_VALUE_TYPE = "get_value_type";
    private static final String PRE_KEY_NAME = "pre_key_name";
    private static final String JSON_PATH = "json_path";
    private static final String REGULAR = "regular";
    private static final String CASE_ID = "case_id";
    private static final String CASE_KEY_NAME = "case_key_name";
    private static final String CASE_JSON_PATH = "case_json_path";
    private static final String DEAL_WITH = "deal_with";

    private String actionName; // 动作名称
    private String teamId; // 团队id
    private String remark; // 备注
    private String actionType; // 动作类型 0 SQL类型 1 http请求 2 已有测试用例
    private String dataName; // 数据库名
    private String sSql; // sql 语句
    private String url; // url
    private String getValueType; // 取值方式 0 不需要返回值 1 全部内容 2 JsonPath方式取值 3 正则
    private String preKeyName; // 全部内容 ${pre.key}的key名
    private String jsonPath; // Json_Path方式取值
    private String regular; // 正则表达式
    private String caseId; // 用例 id 已有测试用例用
    private String caseKeyName; // 全部内容 ${case.key}的key名
    private String caseJsonPath; // Json_Path方式取值
    private String dealWith; // 结果处理 0结果去掉首尾[] 1数字数组转字符串数组

    public ActionDTO(Map<String, String> map) {
        this.actionName = map.get(ACTION_NAME);
        this.teamId = map.get(TEAM_ID);
        this.remark = map.get(REMARK);
        this.actionType = map.get(ACTION_TYPE);
        this.dataName = map.get(DATA_NAME);
        this.sSql = map.get(S_SQL);
        this.url = map.get(URL);
        this.getValueType = map.get(GET_VALUE_TYPE);
        this.preKeyName = map.get(PRE_KEY_NAME);
        this.jsonPath = map.get(JSON_PATH);
        this.regular = map.get(REGULAR);
        this.caseId = map.get(CASE_ID);
        this.caseKeyName = map.get(CASE_KEY_NAME);
        this.caseJsonPath = map.get(CASE_JSON_PATH);
        this.dealWith = map.get(DEAL_WITH);
    }

    public String getActionName() {
        return actionName;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getRemark() {
        return remark;
    }

    public String getActionType() {
        return actionType;
    }

    public String getDataName() {
        return dataName;
    }

    public String getsSql() {
        return sSql;
    }

    public String getUrl() {
        return url;
    }

    public String getGetValueType() {
        return getValueType;
    }

    public String getPreKeyName() {
        return preKeyName;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public String getRegular() {
        return regular;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getCaseKeyName() {
        return caseKeyName;
    }

    public String getCaseJsonPath() {
        return caseJsonPath;
    }

    public String getDealWith() {
        return dealWith;
    }
}
