package com.sangyu.model;

import com.sangyu.config.DBConfig;

import java.sql.*;
import java.util.*;

/**
 * 查询用例数据
 * User: pengyapan
 * Date: 2020/5/14
 * Time: 下午11:26
 */
public class DBDataProvider {

    ResultSet rs;
    ResultSetMetaData rd;

    private List<Object[]> list = new ArrayList<Object[]>();
    private Map<String, String> mapList;
    private int index = 0;

    public DBDataProvider(String ip, String port, String baseName, String userName, String password, String sql) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%s/%s", ip, port, baseName);
        Connection conn = DriverManager.getConnection(url, userName, password);
        Statement createStatement = conn.createStatement();

        rs = createStatement.executeQuery(sql);
        rd = rs.getMetaData();
    }

    public DBDataProvider(String sql) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = String.format("jdbc:mysql://%s:%s/%s", DBConfig.DB_IP, DBConfig.DB_PORT, DBConfig.DB_BASE_NAME);
        Connection conn = DriverManager.getConnection(url, DBConfig.DB_USERNAME, DBConfig.DB_PASSWORD);
        Statement createStatement = conn.createStatement();

        rs = createStatement.executeQuery(sql);
        rd = rs.getMetaData();
    }

    public void buildList() throws SQLException {

        while (rs.next()) {
            Map<String, String> data = new HashMap<>();
            for (int i = 1; i <= rd.getColumnCount(); i++) {
                data.put(rd.getColumnName(i), rs.getString(i));
            }

            Object[] r = new Object[1];
            r[0] = data;
            list.add(r);
        }
    }

    public List<Object[]> getList() {
        return list;
    }

    public List<Object[]> getAfterList() throws SQLException {
        buildList();
        return list;
    }

    public Map<String, String> getMapList() throws SQLException {
        buildList();
        List<Object[]> dataList = getList();
        // [[{}],[{}],[{}],...]
        this.mapList = (Map<String, String>) dataList.get(0)[0];
        return this.mapList;
    }

    public Map<String, String> getMapList(int i) throws SQLException {
        buildList();
        List<Object[]> dataList = getList();
        // [[{}],[{}],[{}],...]
        this.mapList = (Map<String, String>) dataList.get(0)[i];
        return this.mapList;
    }

    public DataProviderIterator getIter() {
        return new DataProviderIterator();
    }

    public class DataProviderIterator implements Iterator<Object[]> {

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public Object[] next() {
            Object[] objects = list.get(index++);
            return objects;
        }

        @Override
        public void remove() {
            list.remove(--index);
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBDataProvider dbDataProvider = new DBDataProvider(DBConfig.DB_IP,DBConfig.DB_PORT,DBConfig.DB_BASE_NAME,DBConfig.DB_USERNAME,DBConfig.DB_PASSWORD,"select * from db_manager");
        dbDataProvider.buildList();
    }

}
