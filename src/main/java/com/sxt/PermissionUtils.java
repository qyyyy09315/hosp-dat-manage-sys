package com.sxt;

import javax.swing.*;
import java.util.Set;

/**
 * 权限校验工具类
 */
public class PermissionUtils {

    /**
     * 判断当前用户是否有指定权限
     * @param permCode 权限编码
     * @return true-有权限 false-无权限
     */
    public static boolean hasPermission(String permCode) {
        if (!SessionHolder.isLogin()) {
            return false;
        }
        Set<String> permissions = SessionHolder.getCurrentPermissions();
        return permissions != null && permissions.contains(permCode);
    }

    /**
     * 校验权限，无权限则弹出提示
     * @param permCode 权限编码
     * @param operateName 操作名称，用于提示
     * @return true-有权限 false-无权限
     */
    public static boolean checkPermission(String permCode, String operateName) {
        if (hasPermission(permCode)) {
            return true;
        }
        JOptionPane.showMessageDialog(null, "您没有权限执行【" + operateName + "】操作，请联系管理员！", "权限不足", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    /**
     * 判断是否是超级管理员
     * @return true-是 false-否
     */
    public static boolean isAdmin() {
        return "超级管理员".equals(SessionHolder.getCurrentRole());
    }
}
