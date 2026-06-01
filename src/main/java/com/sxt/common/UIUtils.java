package com.sxt.common;

import javax.swing.*;
import java.awt.*;

/**
 * 统一UI交互工具类
 */
public class UIUtils {

    /**
     * 成功提示
     * @param parent 父窗口
     * @param message 提示内容
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "操作成功", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 错误提示
     * @param parent 父窗口
     * @param message 提示内容
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "操作失败", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 警告提示
     * @param parent 父窗口
     * @param message 提示内容
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "提示", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 操作确认弹窗
     * @param parent 父窗口
     * @param message 确认内容
     * @return true-确认，false-取消
     */
    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "确认操作", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * 窗口居中显示
     * @param window 窗口
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * 设置窗口为标准大小并居中
     * @param window 窗口
     * @param width 宽度
     * @param height 高度
     */
    public static void setWindowStandard(Window window, int width, int height) {
        window.setSize(width, height);
        window.setMinimumSize(new Dimension(width, height));
        centerWindow(window);
    }

    /**
     * 创建加载提示框
     * @param parent 父窗口
     * @param message 提示内容
     * @return 提示框对象，操作完成后调用dispose()关闭
     */
    public static JDialog showLoading(Component parent, String message) {
        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "处理中", true);
        loadingDialog.setSize(300, 100);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        centerWindow(loadingDialog);
        loadingDialog.setAlwaysOnTop(true);
        
        // 异步显示，不阻塞主线程
        SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
        return loadingDialog;
    }
}
