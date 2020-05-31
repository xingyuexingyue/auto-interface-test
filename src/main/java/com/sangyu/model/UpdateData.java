package com.sangyu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  insert 和 delete 语句
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午11:03
 */
public class UpdateData {
    // 获取数据库连接
    public Connection myConnection() throws Exception{
        String driverClass = "com.mysql.jdbc.Driver";
        String jdbcUrl = "jdbc:mysql://localhost:3306/mydb";
        String user = "user";
        String password = "password";

        Class.forName(driverClass);
        Connection connection= DriverManager.getConnection(jdbcUrl,user,password);
        return connection;
    }
    public void myStatement(String sql) throws SQLException {
        Connection conn = null;
        Statement statement = null;
        try{
            conn = myConnection();
            statement = conn.createStatement();
            statement.executeUpdate(sql);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(statement != null){
                try{
                    statement.close();
                }catch(Exception e2){
                    e2.printStackTrace();
                }
            }
            if(conn != null){
                try{
                    conn.close();
                }catch(Exception e2){
                    e2.printStackTrace();
                }
            }
        }
    }
}
