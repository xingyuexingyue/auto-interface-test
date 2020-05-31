package com.sangyu.dto;

import java.util.Map;

/**
 * db_manager 表对应 DTO 维护前后置动作
 * User: pengyapan
 * Date: 2020/5/14
 * Time: 下午11:16
 */
public class DBManagerDTO {

    private static final String DB_IP = "db_ip";
    private static final String DB_PORT = "db_port";
    private static final String BASE_NAME = "base_name";
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String DB_NAME = "db_name";

    private String db_ip;
    private String db_port;
    private String base_name;
    private String user_name;
    private String password;
    private String db_name;

    public DBManagerDTO(Map<String, String> map) {
        this.db_ip = map.get(DB_IP);
        this.db_port = map.get(DB_PORT);
        this.base_name = map.get(BASE_NAME);
        this.user_name = map.get(USER_NAME);
        this.password = map.get(PASSWORD);
        this.db_name = map.get(DB_NAME);
    }

    public String getDb_ip() {
        return db_ip;
    }

    public void setDb_ip(String db_ip) {
        this.db_ip = db_ip;
    }

    public String getDb_port() {
        return db_port;
    }

    public void setDb_port(String db_port) {
        this.db_port = db_port;
    }

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }
}
