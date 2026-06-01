package com.sxt;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * 输入校验工具类
 */
public class ValidatorUtils {
    // 常见正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");
    // 禁止的特殊字符，防止SQL注入和XSS
    private static final Pattern FORBIDDEN_CHAR_PATTERN = Pattern.compile("[<>\"'%;()&+]");

    /**
     * 校验非空
     * @param value 输入值
     * @param fieldName 字段名，用于提示
     * @return true-校验通过 false-校验失败
     */
    public static boolean require(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "【" + fieldName + "】不能为空！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 校验长度范围
     * @param value 输入值
     * @param min 最小长度
     * @param max 最大长度
     * @param fieldName 字段名
     * @return true-校验通过 false-校验失败
     */
    public static boolean length(String value, int min, int max, String fieldName) {
        if (value == null) return true;
        int len = value.trim().length();
        if (len < min || len > max) {
            JOptionPane.showMessageDialog(null, "【" + fieldName + "】长度必须在" + min + "-" + max + "之间！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 校验手机号
     * @param phone 手机号
     * @return true-校验通过 false-校验失败
     */
    public static boolean phone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return true;
        if (!PHONE_PATTERN.matcher(phone.trim()).matches()) {
            JOptionPane.showMessageDialog(null, "手机号格式不正确！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 校验身份证号
     * @param idCard 身份证号
     * @return true-校验通过 false-校验失败
     */
    public static boolean idCard(String idCard) {
        if (idCard == null || idCard.trim().isEmpty()) return true;
        if (!ID_CARD_PATTERN.matcher(idCard.trim()).matches()) {
            JOptionPane.showMessageDialog(null, "身份证号格式不正确！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 校验数字
     * @param number 数字字符串
     * @param fieldName 字段名
     * @return true-校验通过 false-校验失败
     */
    public static boolean number(String number, String fieldName) {
        if (number == null || number.trim().isEmpty()) return true;
        if (!NUMBER_PATTERN.matcher(number.trim()).matches()) {
            JOptionPane.showMessageDialog(null, "【" + fieldName + "】必须是数字！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * 校验是否包含特殊字符，防止SQL注入
     * @param value 输入值
     * @param fieldName 字段名
     * @return true-校验通过 false-校验失败
     */
    public static boolean noSpecialChar(String value, String fieldName) {
        if (value == null) return true;
        if (FORBIDDEN_CHAR_PATTERN.matcher(value).find()) {
            JOptionPane.showMessageDialog(null, "【" + fieldName + "】包含禁止的特殊字符！", "输入错误", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
