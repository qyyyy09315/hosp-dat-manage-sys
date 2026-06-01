package com.sxt.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库工具类，统一管理数据库连接
 */
public class DBUtils {
    // 数据库配置统一管理，后续可以改成读取配置文件
    private static final String URL = "jdbc:mysql://localhost:3306/oh?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    // 加载驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("数据库驱动加载失败", e);
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     */
    public static void close(Connection conn) {
        close(conn, null, null);
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param stmt 执行对象
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param stmt 执行对象
     * @param rs 结果集对象
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
