package com.sangyu.httptest;

import com.sangyu.config.DBConfig;
import com.sangyu.dto.HttpTestDTO;
import com.sangyu.model.DBDataProvider;
import com.sangyu.utils.HttpRequestsUtils;
import com.sangyu.utils.TransferUtils;
import com.sangyu.vo.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行用例
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午12:14
 */
public class RunCase {

    private HttpTestDTO httpTestDTO;
    private HttpRequestsUtils httpRequestsUtils;
    private HttpResponse httpResponse;
    private CaseParameters caseParameters = new CaseParameters();
    private CaseAction caseAction = new CaseAction();

    Logger logger = LogManager.getLogger(HttpTest.class);
    private static final String LOG_PRE = "[RunCase类]";

    public RunCase(HttpTestDTO httpTestDTO) {
        this.httpTestDTO = httpTestDTO;
    }
    public RunCase() {
    }

    /**
     * 请求无参数
     *
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public HttpResponse runCaseNoParameter() throws IOException, SQLException, ClassNotFoundException {
        String url = requestUrl();
        logger.info("{} 开始发起请求...", LOG_PRE);
        logger.info("{} 请求没有携带参数", LOG_PRE);
        logger.info("{} 请求 url {} ", LOG_PRE, url);
        httpRequestsUtils = new HttpRequestsUtils(url);
        if (httpTestDTO.getRequestType().equals("1")) {
            httpResponse = httpRequestsUtils.doGet();
            logger.info("{} 发起 Get 请求, 请求结果是{}", LOG_PRE, httpResponse);
        } else if (httpTestDTO.getRequestType().equals("2")) {
            httpResponse = httpRequestsUtils.doPost();
            logger.info("{} 发起 Post 请求, 请求结果 {}", LOG_PRE, httpResponse);

        }
        return httpResponse;
    }


    /**
     * 请求有参数
     *
     * @param parameters
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public HttpResponse runCaseWithParameter(Map<String, String> parameters) throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        String url = requestUrl();
        logger.info("{} 开始发起请求", LOG_PRE);
        logger.info("{} 请求 url {} ", LOG_PRE, url);
        httpRequestsUtils = new HttpRequestsUtils(url);
        List<NameValuePair> params = urlParams(parameters);
        logger.info("{} 请求携带参数 {} ", LOG_PRE, params);
        if (httpTestDTO.getRequestType().equals("1")) {
            httpResponse = httpRequestsUtils.doGet(params);
            logger.info("{} 发起 Get 请求, 请求结果 {}", LOG_PRE, httpResponse);
        } else if (httpTestDTO.getRequestType().equals("2")) {
            httpResponse = httpRequestsUtils.doPost(params);
            logger.info("{} 发起 Post 请求, 请求结果 {}", LOG_PRE, httpResponse);
        }
        return httpResponse;
    }

    /**
     * 替换参数使用参数化或前置动作
     *
     * @param parameters
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws URISyntaxException
     * @throws IOException
     */
    public Map<String, String> getRequestParameters(String parameters) throws ClassNotFoundException, SQLException, URISyntaxException, IOException {
        logger.info("{} 请求前先执行参数替换", LOG_PRE);
        Map<String, String> map = new HashMap<String, String>();
        // 前置动作为空 只用参数化来替换
        if (httpTestDTO.getBeforeAction() == null) {
            logger.info("{} 使用参数化替换", LOG_PRE);
            map = caseParameters.splitParameters(parameters);
            // 前置动作不为空，先参数化替换再前置动作替换
        } else if (httpTestDTO.getBeforeAction() != null) {
            logger.info("{} 请求前使用参数化替换", LOG_PRE);
            String parameterize = TransferUtils.mapToString(caseParameters.splitParameters(parameters));
            logger.info("{} 请求前使用前置动作替换", LOG_PRE);
            map = caseAction.replaceParameters(httpTestDTO.getBeforeAction(), parameterize);
        }
        logger.info("{} 参数化替换完成后的请求参数{}", LOG_PRE, map);
        return map;
    }


    /**
     * 请求 url
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String requestUrl() throws SQLException, ClassNotFoundException {
        DBDataProvider data = new DBDataProvider("select * from service_manager where service_name = \"myService\";");
        List<Object[]> list = data.getAfterList();
        Map<String, String> map = (Map<String, String>) list.get(0)[0];
        String url = map.get("service_url");
        return url + httpTestDTO.getPath();
    }

    /**
     * 请求参数转换格式
     *
     * @param map
     * @return
     */
    public List<NameValuePair> urlParams(Map<String, String> map) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RunCase runCase = new RunCase();
        System.out.println(runCase.requestUrl());

    }

}
