package com.sxt;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

/**
 * 会话持有类：存储当前登录用户信息，模拟桌面端会话
 */
public class SessionHolder {
    private static String currentUsername; // 当前登录用户名
    private static Set<String> currentPermissions; // 当前用户权限集合
    private static String currentRole; // 当前用户角色
    private static String localHostName; // 本地机器名，用于审计日志

    static {
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            localHostName = "未知主机";
        }
    }

    public static void setLoginUser(String username, Set<String> permissions, String role) {
        currentUsername = username;
        currentPermissions = permissions;
        currentRole = role;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static Set<String> getCurrentPermissions() {
        return currentPermissions;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

    public static String getLocalHostName() {
        return localHostName;
    }

    public static boolean isLogin() {
        return currentUsername != null;
    }

    public static void logout() {
        currentUsername = null;
        currentPermissions = null;
        currentRole = null;
    }
}
