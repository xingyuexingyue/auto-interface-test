package com.sangyu.httptest;

import com.sangyu.dto.HttpTestDTO;
import com.sangyu.utils.CheckPointUtils;
import com.sangyu.utils.JsonPathCheckUtils;
import com.sangyu.vo.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

/**
 * 用例执行结果
 * User: pengyapan
 * Date: 2020/5/14
 * Time: 下午11:58
 */
public class CaseResult {

    Logger logger = LogManager.getLogger(CaseResult.class);
    private static final String LOG_PRE = "[用例结果类]";
    private static final String UN_CONFIG = "未配置";

    String checkError;
    String checkNull;
    String checkContain;
    String checkUnContain;
    String checkJsonpath;

    /**
     * 请求结果无检查
     *
     * @param data
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResponse runCaseNoCheck(Map<String, String> data) throws SQLException, ClassNotFoundException, URISyntaxException, IOException {
        return new RunCaseFac(new HttpTestDTO(data)).execCase();
    }

    /**
     * 请求结果有检查
     *
     * @param data
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws IOException
     */
    public Boolean runCaseWithCheck(Map<String, String> data) throws SQLException, ClassNotFoundException, URISyntaxException, IOException {
        return execCheckPoint(new HttpTestDTO(data), runCaseNoCheck(data));
    }

    /**
     * 校验检查点
     *
     * @param httpResponse
     * @return
     */
    public Boolean execCheckPoint(HttpTestDTO httpTestDTO, HttpResponse httpResponse) {
        Boolean success = false;
        CheckPointUtils responseCheck = new CheckPointUtils();
        logger.info("{} 开始校验检查点...", LOG_PRE);
        // 异常检查点 0-不开启 1-不开启
        if ("0".equals(httpTestDTO.getCheckError())) {
            checkError = UN_CONFIG;
        } else if ("1".equals(httpTestDTO.getCheckError())) {
            success = responseCheck.checkError(httpResponse);
            checkError = success ? "成功" : "失败";
        } else {
            checkError = UN_CONFIG;
        }
        logger.info("{} 异常检查点结果 {}", LOG_PRE, checkError);

        // 判空检查点 0-为空检查 1-不为空检查 2-不开启
        if ("0".equals(httpTestDTO.getCheckNull())) {
            success = responseCheck.checkNoNull(httpResponse);
            checkNull = success ? "成功" : "失败";
        } else if ("1".equals(httpTestDTO.getCheckError())) {
            success = responseCheck.checkNull(httpResponse);
            checkNull = success ? "成功" : "失败";
        } else if ("2".equals(httpTestDTO.getCheckError())) {
            checkNull = UN_CONFIG;
        } else {
            checkNull = UN_CONFIG;
        }
        logger.info("{} 判空检查点结果 {}", LOG_PRE, checkNull);
        // 包含检查点
        if (httpTestDTO.getCheckContain() == null) {
            checkContain = UN_CONFIG;
        } else {
            success = responseCheck.checkContains(httpResponse, httpTestDTO.getCheckContain());
            checkContain = success ? "成功" : "失败";
        }
        logger.info("{} 包含检查结果 {}", LOG_PRE, checkContain);
        // 不包含检查点
        if (httpTestDTO.getCheckUncontain() == null) {
            checkUnContain = UN_CONFIG;
        } else {
            success = responseCheck.checkNoContains(httpResponse, httpTestDTO.getCheckUncontain());
            checkUnContain = success ? "成功" : "失败";
        }
        logger.info("{} 不包含检查结果 {}", LOG_PRE, checkUnContain);
        // JsonPath 检查点
        if (httpTestDTO.getCheckJsonpath() == null) {
            checkJsonpath = UN_CONFIG;
        } else {
            JsonPathCheckUtils responseJsonPathCheck = new JsonPathCheckUtils(httpTestDTO.getCheckJsonpath(), httpResponse);
            success = responseJsonPathCheck.spilitJsonPath();
            checkJsonpath = success ? "成功" : "失败";
        }
        logger.info("{} JsonPath 检查结果 {}", LOG_PRE, checkJsonpath);
        logger.info("{} 最终执行结果 {}", LOG_PRE, success);
        return success;
    }
}
