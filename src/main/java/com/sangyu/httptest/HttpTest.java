package com.sangyu.httptest;

import com.sangyu.config.DBConfig;
import com.sangyu.model.DBDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * User: pengyapan
 * Date: 2020/5/14
 * Time: 下午11:06
 */
public class HttpTest {
    Logger logger = LogManager.getLogger(HttpTest.class);
    private static final String LOG_PRE = "[HttpTest类]";
    String sql;

    @Parameters({"sql"})
    @BeforeClass()
    public void beforeClass(String sql) {
        logger.info("{} 开始执行用例测试...", LOG_PRE);
        this.sql = sql;
        logger.info("{} 要执行的用例查询范围是 {}", LOG_PRE, sql);
    }

    @DataProvider(name = "testData")
    public Iterator<Object[]> getData() throws SQLException, ClassNotFoundException {
        DBDataProvider dateProvider = new DBDataProvider(sql);
        dateProvider.buildList();
        return dateProvider.getIter();
    }

    @Test(dataProvider = "testData")
    public void test(Map<String, String> data) throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        logger.info("{} 要执行的用例信息是 {}", LOG_PRE, data);
        new CaseResult().runCaseWithCheck(data);
        logger.info("{} 用例执行结束", LOG_PRE);

    }
}
