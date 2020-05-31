package com.sangyu.httptest;

import com.jayway.jsonpath.JsonPath;
import com.sangyu.config.DBConfig;
import com.sangyu.dto.ParamerDTO;
import com.sangyu.model.DBDataProvider;
import com.sangyu.utils.JsonPathUtils;
import com.sangyu.vo.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数替换
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午12:50
 */
public class CaseParameters {

    Logger logger = LogManager.getLogger(CaseParameters.class);
    private static final String LOG_PRE = "[请求参数替换类]";

    public Map<String, String> splitParameters(String path) throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
        Map<String, String> map = new HashMap<>();
        String[] listFirst = path.split("&");
        for (int i = 0; i < listFirst.length; i++) {
            String[] listSecond = listFirst[i].split("=");
            String value = listSecond[1];
            if (value.startsWith("${pre.") && value.endsWith("}")) {
                map.put(listSecond[0], value);
                continue;
            } else if (value.startsWith("${case.") && value.endsWith("}")) {
                map.put(listSecond[0], value);
                continue;
            } else if (value.startsWith("${") && value.endsWith("}")) {
                ParamerDTO paramerDTO = getParamerDTO(value);
                value = replaceParameter(paramerDTO, value);
            }
            map.put(listSecond[0], value);
        }
        return map;
    }


    private String replaceParameter(ParamerDTO paramerDTO, String value) throws ClassNotFoundException, SQLException, URISyntaxException, IOException {
        if (paramerDTO.getParamerType().equals("0")) {
            logger.info("{} 使用 k-v 方式替换", LOG_PRE);
            logger.info("{} 替换前 value = {}", LOG_PRE, value);
            // 状态 0 k-v 方式，直接获取 value 的值并赋值给 value
            value = paramerDTO.getValue();
            logger.info("{} 替换后 value = {}", LOG_PRE, value);
        } else if (paramerDTO.getParamerType().equals("1")) {
            logger.info("{} 使用 sql 方式替换", LOG_PRE);
            // 状态 1 sql 方式，通过执行 sql 在结果中取出 field 对应的value
            DBDataProvider data = new DBDataProvider(DBConfig.DB_IP, DBConfig.DB_PORT, dataName("我的服务"),
                    DBConfig.DB_USERNAME, DBConfig.DB_PASSWORD, paramerDTO.getsSql());
            Map<String, String> mapList = data.getMapList();
            logger.info("{} 替换前 value = {}", LOG_PRE, value);
            value = mapList.get(paramerDTO.getField());
            logger.info("{} 替换后 value = {}", LOG_PRE, value);
        } else if (paramerDTO.getParamerType().equals("2")) {
            logger.info("{} 使用用例方式替换", LOG_PRE);
            // 状态 2 用例方式，从其他用例执行获得结果，有四种方式
            // 先去数据库查到对应的用例
            String caseSql = "select * from http_cases where id = " + '"' + paramerDTO.getCaseId() + '"';
            DBDataProvider data = new DBDataProvider(caseSql);
            CaseResult caseResult = new CaseResult();
            HttpResponse httpResponse = caseResult.runCaseNoCheck(data.getMapList());
            // 获得用例响应结果
            String stringResData = httpResponse.getData();
            logger.info("{} 替换前 value = {}", LOG_PRE, value);
            if (paramerDTO.getCaseGetType().equals("1")) {
                logger.info("{} 使用 JsonPath 方式替换", LOG_PRE);
                // 1 jsonpath命中内容
                value = JsonPath.read(stringResData, JsonPathUtils.splitJsonPath(paramerDTO.getJsonPath()));
                logger.info("{} 替换后 value = {}", LOG_PRE, value);
            } else if (paramerDTO.getCaseGetType().equals("0")) {
                logger.info("{} 使用 获得全部响应内容 方式替换", LOG_PRE);
                // 0 全部内容
                value = stringResData;
                logger.info("{} 替换后 value = {}", LOG_PRE, value);

            } else if (paramerDTO.getCaseGetType().equals("2")) {
                logger.info("{} 使用 JsonPath 方式替换命中数组只取第一条", LOG_PRE);
                // 2 jsonpath命中内容如果是数组只取第一条
                value = JsonPath.read(stringResData, JsonPathUtils.splitJsonPath(paramerDTO.getJsonPath()) + "[0]");
                logger.info("{} 替换后 value = {}", LOG_PRE, value);
            } else if (paramerDTO.getCaseGetType().equals("3")) {
                // TODO 正则表达式 待增
            }
        }
        return value;
    }


    /**
     * 获得参数化维护表的数据映射到 ParamerDTO
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private  ParamerDTO getParamerDTO(String value) throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM parameterize where paramer_name = " + '"' + value.substring(2, value.length() - 1) + '"';
        // 第 1 步. 从 parameterize 表中获得参数化的那条记录
        DBDataProvider data = new DBDataProvider(sql);
        data.buildList();
        List<Object[]> pmList = data.getList();
        logger.info("{} 查询参数化维护表获得数据", LOG_PRE);
        return new ParamerDTO((Map<String, String>) pmList.get(0)[0]);
    }

    /**
     * 获得数据库维护表中的数据库名称
     * @param name
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public  String dataName(String name) throws SQLException, ClassNotFoundException {
        String sql = "select * from db_manager where db_name = " + "\"" + name + "\"" + ";";
        DBDataProvider data = new DBDataProvider(sql);
        Map<String, String> map = (Map<String, String>) data.getAfterList().get(0)[0];
        return map.get("base_name");
    }

}
