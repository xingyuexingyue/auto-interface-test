package com.sangyu.dto;

import java.util.Map;

/**
 * http_cases (用例)表对应DTO 维护所有用例
 *
 * User: pengyapan
 * Date: 2020/4/19
 * Time: 下午8:42
 */
public class HttpTestDTO {
    private static final String HEADERS = "headers";
    private static final String AFTER_ACTION = "after_action";
    private static final String REQUEST_TYPE = "request_type";
    private static final String CHECK_CONTAIN = "check_contain";
    private static final String SERVICE_NAME = "service_name";
    private static final String CHECK_ERROR = "check_error";
    private static final String CHECK_NULL = "check_null";
    private static final String TEAM_ID = "team_id";
    private static final String CHECK_UNCONTAIN = "check_uncontain";
    private static final String ENV = "env";
    private static final String AFTER_TEST_ACTION = "after_test_action";
    private static final String CHECK_JSONPATH = "check_jsonpath";
    private static final String PATH = "path";
    private static final String BEFORE_ACTION = "before_action";
    private static final String REPONSE_TYPE = "reponse_type";
    private static final String PARAMETERS = "parameters";
    private static final String IS_RUN = "is_run";
    private static final String REMARK = "remark";
    private static final String IS_DELETE = "is_delete";

    private String headers; // 请求头
    private String afterAction; // 后置操作
    private String requestType; // 请求类型 1 get 2 post
    private String checkContain; // 包含检查点
    private String serviceName; // 服务名称
    private String checkError; // 异常检查点
    private String checkNull; // 不为空检查点
    private String teamId; // 团队id
    private String checkUncontain; // 不包含检查点
    private String env; // 环境
    private String afterTestAction; // 测试后操作
    private String checkJsonpath; // JsonPath检查点
    private String path; // 路径
    private String beforeAction; // 前置动作
    private String reponseType; // 结果类型
    private String parameters; // 参数
    private String isRun; // 是否开启每日CI 0开始 1不开启
    private String remark; // 备注
    private String isDelete; // 状态删除 0删除 1未删除

    public HttpTestDTO(Map<String, String> data) {
        this.headers = data.get(HEADERS);
        this.afterAction = data.get(AFTER_ACTION);
        this.requestType = data.get(REQUEST_TYPE);
        this.checkContain = data.get(CHECK_CONTAIN);
        this.serviceName = data.get(SERVICE_NAME);
        this.checkError = data.get(CHECK_ERROR);
        this.checkNull = data.get(CHECK_NULL);
        this.teamId = data.get(TEAM_ID);
        this.checkUncontain = data.get(CHECK_UNCONTAIN);
        this.env = data.get(ENV);
        this.afterTestAction = data.get(AFTER_TEST_ACTION);
        this.checkJsonpath = data.get(CHECK_JSONPATH);
        this.path = data.get(PATH);
        this.beforeAction = data.get(BEFORE_ACTION);
        this.reponseType = data.get(REPONSE_TYPE);
        this.parameters = data.get(PARAMETERS);
        this.isRun = data.get(IS_RUN);
        this.remark = data.get(REMARK);
        this.isDelete = data.get(IS_DELETE);
    }

    public String getHeaders() {
        return headers;
    }

    public String getAfterAction() {
        return afterAction;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getCheckContain() {
        return checkContain;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCheckError() {
        return checkError;
    }

    public String getCheckNull() {
        return checkNull;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getCheckUncontain() {
        return checkUncontain;
    }

    public String getEnv() {
        return env;
    }

    public String getAfterTestAction() {
        return afterTestAction;
    }

    public String getCheckJsonpath() {
        return checkJsonpath;
    }

    public String getPath() {
        return path;
    }

    public String getBeforeAction() {
        return beforeAction;
    }

    public String getReponseType() {
        return reponseType;
    }

    public String getParameters() {
        return parameters;
    }

    public String getIsRun() {
        return isRun;
    }

    public String getRemark() {
        return remark;
    }

    public String getIsDelete() {
        return isDelete;
    }
}
