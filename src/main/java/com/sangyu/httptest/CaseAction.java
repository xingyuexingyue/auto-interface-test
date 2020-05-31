package com.sangyu.httptest;

import com.jayway.jsonpath.JsonPath;
import com.sangyu.dto.ActionDTO;
import com.sangyu.model.DBDataProvider;
import com.sangyu.model.UpdateData;
import com.sangyu.utils.RegularUtils;
import com.sangyu.utils.TransferUtils;
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
 * 动作替换
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午10:53
 */
public class CaseAction {

    Logger logger = LogManager.getLogger(CaseAction.class);
    private static final String LOG_PRE = "[请求前置动作类]";

    /**
     * 替换参数
     *
     * @param actionName
     * @param parametersPath
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public Map<String, String> replaceParameters(String actionName, String parametersPath) throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
        Map<String, String> dealWithAction = new HashMap<String, String>();
        ActionDTO actionDTO = actionData(actionName);
        if (actionDTO.getActionType().equals("0")) { // 使用 SQL 类型替换
            logger.info("{} 前置动作，使用 SQL 类型替换", LOG_PRE);
            dealWithAction = replaceBySql(parametersPath, actionDTO.getsSql());
        } else if (actionDTO.getActionType().equals("1")) { // 使用 http 请求类型替换
            // TODO http请求方式
        } else if (actionDTO.getActionType().equals("2")) { // 使用已有测试用例类型替换
            logger.info("{} 前置动作，使用已有测试用例类型替换 ", LOG_PRE);
            dealWithAction = replaceByCase(actionDTO.getCaseId(), actionDTO.getGetValueType(), parametersPath, actionDTO.getCaseKeyName(), actionDTO.getCaseJsonPath());
        }
        return dealWithAction;
    }

    /**
     * 使用 SQL 类型替换
     *
     * @param parametersPath
     * @param sql
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<String, String> replaceBySql(String parametersPath, String sql) throws SQLException, ClassNotFoundException {
        Map<String, String> map = new HashMap<String, String>();
        // 判断SQL的类型，如果不是select开头，则只执行不替换数据
        if (sql.toLowerCase().startsWith("select")) {
            logger.info("{} 前置动作 sql 类型，sql 语句是 {}", LOG_PRE, sql);
            map = sqlSelect(parametersPath, sql);

        } else if (sql.toLowerCase().startsWith("delete") | sql.toLowerCase().startsWith("insert")) {
            logger.info("{} 前置动作 sql 类型，sql 语句 {}", LOG_PRE, sql);
            logger.info("{} 前置动作 sql 类型，没有执行替换操作，由于是 insert 和 delete 语句", LOG_PRE);
            UpdateData updateData = new UpdateData();
            updateData.myStatement(sql);
            map = TransferUtils.stringToMap(parametersPath);
            logger.info("{} 前置动作 sql 类型，替换后的结果是 {}", LOG_PRE, map);
        }
        return map;
    }

    /**
     * 使用 SQL 类型替换
     * 使用 SQL 的 SELECT
     *
     * @param parametersPath
     * @param sql
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Map<String, String> sqlSelect(String parametersPath, String sql) throws SQLException, ClassNotFoundException {
        Map<String, String> map = new HashMap<String, String>();
        logger.info("{} 前置动作 sql 类型，开始执行 sql", LOG_PRE);
        DBDataProvider data = new DBDataProvider(sql);
        List<Object[]> sqlResultMap = data.getAfterList();
        Map<String, String> mapList = null;
        String mapKey = "";
        String[] listFirst = parametersPath.split("&");
        for (int i = 0; i < listFirst.length; i++) {
            String[] listSecond = listFirst[i].split("=");
            String value = listSecond[1];
            logger.info("{} 前置动作 sql 类型，参数替换前 {}", LOG_PRE, value);
            if (value.startsWith("${pre") && value.endsWith("]}")) {
                int index = Integer.parseInt(RegularUtils.getNumUtil(value));
                mapList = (Map<String, String>) sqlResultMap.get(0)[index];
                mapKey = RegularUtils.getSubUtil(value).get(0);
                value = mapList.get(mapKey);
                logger.info("{} 前置动作 sql 类型，参数替换后 {}", LOG_PRE, value);
            } else if (value.startsWith("${pre") && value.endsWith("}")) {
                mapList = (Map<String, String>) sqlResultMap.get(0)[0];
                mapKey = value.substring(6, value.length() - 1);
                value = mapList.get(mapKey);
                logger.info("{} 前置动作 sql 类型，参数替换后 {}", LOG_PRE, value);
            }
            map.put(listSecond[0], value);
        }
        return map;
    }

    /**
     * 前置动作只依赖 sql 的执行或请求的执行
     *
     * @param actionName
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public void replaceNoRes(String actionName) throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
        // 动作类型 0 SQL类型 只能是 insert 或 delete 或 update
        if (actionData(actionName).getActionType().equals("0")) {
            UpdateData updateDB = new UpdateData();
            updateDB.myStatement(actionData(actionName).getsSql());
            // 动作类型 1 http请求
        } else if (actionData(actionName).getActionType().equals("1")) {
            // TODO
            // 动作类型 2 已有测试用例 执行用例但不获得结果
        } else if (actionData(actionName).getActionType().equals("2")) {
            String caseSql = "select * from http_cases where id = " + '"' + actionData(actionName).getCaseId() + '"';
            DBDataProvider data = new DBDataProvider(caseSql);
            new CaseResult().runCaseNoCheck(data.getMapList());
        }
    }

    /**
     * 使用已有测试用例类型
     *
     * @param caseId
     * @param valueType
     * @param parametersPath
     * @param caseKeyName
     * @param caseJsonPath
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public Map<String, String> replaceByCase(String caseId, String valueType, String parametersPath, String caseKeyName, String caseJsonPath) throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
        Map<String, String> map = new HashMap<String, String>();
        CaseResult caseResult = new CaseResult();
        String caseSql = "select * from http_cases where id = " + '"' + caseId + '"';
        DBDataProvider data = new DBDataProvider(caseSql);
        Map<String, String> mapList = data.getMapList();
        if (valueType.equals("0")) { // 已有测试用例，不需要返回值
            logger.info("{} 已有测试用例，不需要返回值", LOG_PRE);
            caseResult.runCaseNoCheck(mapList);
            map = TransferUtils.stringToMap(parametersPath);
        } else if (valueType.equals("1")) { // 已有测试用例，全部内容
            logger.info("{} 已有测试用例，获得返回值的全部内容", LOG_PRE);
            map = caseReplaceAll(mapList, parametersPath, caseKeyName);
            logger.info("{} 替换后的全部参数是 {}", LOG_PRE, map);
        } else if (valueType.equals("2")) { // 已有测试用例，JsonPath方式取值
            logger.info("{} 已有测试用例，JsonPath方式取值", LOG_PRE);
            map = caseReplaceSec(mapList, parametersPath, caseJsonPath, caseKeyName);
            logger.info("{} 替换后的全部参数是 {}", LOG_PRE, map);
        } else if ((valueType.equals("3"))) { // 已有测试用例，正则
            // TODO 正则
        }
        return map;
    }

    /**
     * 已有测试用例，全部内容
     *
     * @param map
     * @param parametersPath
     * @param caseKeyName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws URISyntaxException
     */
    public Map<String, String> caseReplaceAll(Map<String, String> map, String parametersPath, String caseKeyName) throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        CaseResult caseResult = new CaseResult();
        HttpResponse httpResponse = caseResult.runCaseNoCheck(map);
        Map<String, String> parametermMap = new HashMap<String, String>();
        String stringResData = httpResponse.getData();
        String[] listFirst = parametersPath.split("&");
        for (int i = 0; i < listFirst.length; i++) {
            String[] listSecond = listFirst[i].split("=");
            String value = listSecond[1];
            logger.info("{} 替换前的参数是 {}", LOG_PRE, value);
            if (value.startsWith("${case") && value.endsWith("}")) {
                String sub = value.substring(7, value.length() - 1);
                if (sub.equals(caseKeyName)) {
                    value = stringResData;
                    logger.info("{} 替换后的参数是 {}", LOG_PRE, value);

                }
            }
            parametermMap.put(listSecond[0], value);
        }
        return parametermMap;
    }

    /**
     * 已有测试用例，JsonPath方式取值
     *
     * @param map
     * @param parametersPath
     * @param caseJsonPath
     * @param caseKeyName
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws URISyntaxException
     */
    public Map<String, String> caseReplaceSec(Map<String, String> map, String parametersPath, String caseJsonPath, String caseKeyName) throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        CaseResult caseResult = new CaseResult();
        HttpResponse httpResponse = caseResult.runCaseNoCheck(map);
        String stringResData = httpResponse.getData();

        // keyMap 键为key名，值为响应数据根据JsonPath匹配的结果
        String[] stringCaseKey = caseJsonPath.split("&&");
        Map<String, String> resMap = new HashMap<String, String>();
        for (int i = 0; i < stringCaseKey.length; i++) {
            String str = stringCaseKey[i].substring(1, stringCaseKey[i].length() - 1);
            String valueMap = JsonPath.read(stringResData, str);
            resMap.put(caseKeyName, valueMap);
        }
        Map<String, String> parametersMap = new HashMap<String, String>();


        // 替换参数值，如果key名存在参数中，则替换
        String[] listFirst = parametersPath.split("&");
        for (int i = 0; i < listFirst.length; i++) {
            String[] listSecond = listFirst[i].split("=");
            String value = listSecond[1];
            logger.info("{} 替换前的参数是 {}", LOG_PRE, value);

            if (value.startsWith("${case") && value.endsWith("}")) {
                int len = value.length();
                String subKey = value.substring(7, len - 1);
                if (resMap.containsKey(subKey)) {
                    value = resMap.get(subKey);
                    logger.info("{} 替换后的参数是 {}", LOG_PRE, value);
                }
            }
            parametersMap.put(listSecond[0], value);
        }
        return parametersMap;
    }

    /**
     * 获得动作维护表的数据映射到 ActionDTO
     *
     * @param actionName
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ActionDTO actionData(String actionName) throws SQLException, ClassNotFoundException {
        /** 根据action_name从action表查出对应记录 */
        String actionSql = "select * from action where action_name = " + '"' + actionName + '"';
        DBDataProvider data = new DBDataProvider(actionSql);
        ActionDTO actionDTO = new ActionDTO(data.getMapList());
        return actionDTO;
    }

}
