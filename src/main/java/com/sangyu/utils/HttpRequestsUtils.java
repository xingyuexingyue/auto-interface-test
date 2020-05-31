package com.sangyu.utils;

import com.sangyu.vo.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 封装请求类
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午12:04
 */
public class HttpRequestsUtils {
    private String url;
    private CloseableHttpClient httpClient;
    private CloseableHttpResponse response;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private String header;
    private HttpResponse httpResponse;


    public HttpRequestsUtils(String url) {
        this.url = url;
        httpClient = HttpClients.createDefault();
    }


    public HttpRequestsUtils(String url, String header) {
        this.url = url;
        this.header = header;
        httpClient = HttpClients.createDefault();
    }

    public HttpResponse doGet() throws IOException {
        httpGet = new HttpGet(url);
        response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpResponse = new HttpResponse(statusCode, data);
        closeResources(response, httpClient);
        return httpResponse;

    }


    public HttpResponse doGet(List<NameValuePair> params) throws URISyntaxException, IOException {
        URI uri = new URIBuilder(url).setParameters(params).build();
        httpGet = new HttpGet(uri);
        httpGet.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));
        response = httpClient.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpResponse = new HttpResponse(statusCode, data);
        closeResources(response, httpClient);
        return httpResponse;
    }

    public HttpResponse doPost() throws IOException {
        httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", header);
        response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpResponse = new HttpResponse(statusCode, data);
        closeResources(response, httpClient);
        return httpResponse;
    }


    public HttpResponse doPost(List<NameValuePair> params) throws IOException {
        httpPost = new HttpPost(url);
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params); // 构造一个form表单式的实体
        httpPost.setEntity(formEntity); // 将请求实体设置到httpPost对象中
        httpPost.setHeader("User-Agent", header);  // 伪装浏览器
        response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        httpResponse = new HttpResponse(statusCode, data);
        closeResources(response, httpClient);
        return httpResponse;
    }


    public void closeResources(CloseableHttpResponse response, CloseableHttpClient httpClient) throws IOException {
        if (response != null)
            response.close();
        httpClient.close();
    }
}
