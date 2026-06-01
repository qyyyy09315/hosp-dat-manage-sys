package com.sxt;

import com.sxt.common.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * 审计日志工具类
 */
public class AuditLogUtils {

    // 操作类型常量
    public static final String TYPE_LOGIN = "LOGIN";
    public static final String TYPE_QUERY = "QUERY";
    public static final String TYPE_ADD = "ADD";
    public static final String TYPE_UPDATE = "UPDATE";
    public static final String TYPE_DELETE = "DELETE";
    public static final String TYPE_EXPORT = "EXPORT";

    /**
     * 记录操作日志
     */
    public static void log(String operateType, String operateContent, boolean success, String errorMsg) {
        if (!SessionHolder.isLogin()) {
            return;
        }
        String sql = "INSERT INTO sys_audit_log (username, operate_ip, operate_type, operate_content, operate_status, error_msg) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, SessionHolder.getCurrentUsername());
            pstmt.setString(2, SessionHolder.getLocalHostName());
            pstmt.setString(3, operateType);
            pstmt.setString(4, operateContent);
            pstmt.setInt(5, success ? 1 : 0);
            pstmt.setString(6, errorMsg);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt);
        }
    }

    /**
     * 记录成功操作日志
     * @param operateType 操作类型
     * @param operateContent 操作内容
     */
    public static void logSuccess(String operateType, String operateContent) {
        log(operateType, operateContent, true, null);
    }

    /**
     * 记录失败操作日志
     * @param operateType 操作类型
     * @param operateContent 操作内容
     * @param errorMsg 失败原因
     */
    public static void logFail(String operateType, String operateContent, String errorMsg) {
        log(operateType, operateContent, false, errorMsg);
    }
}
