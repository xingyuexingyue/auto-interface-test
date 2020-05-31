package com.sangyu.vo;

/**
 * 返回数据类
 * User: pengyapan
 * Date: 2020/4/18
 * Time: 下午7:15
 */
public class HttpResponse {
    private int statusCode;
    private String data;

    /**
     *
     * @param statusCode 响应状态码
     * @param data 响应数据
     */
    public HttpResponse(int statusCode, String data){
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return
                "statusCode=" + statusCode +
                ", data='" + data + '\'';
    }
}
