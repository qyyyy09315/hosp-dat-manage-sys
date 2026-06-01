package com.sxt;

import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.sxt.common.DBUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Sur extends JFrame {
    private static JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    public Sur() {
        // 设置自定义外观
        try {
            // 设置JTattoo主题
            UIManager.setLookAndFeel(new FastLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("医院管理系统登录界面");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 使用GridBagLayout布局管理器
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 设置边距

        // 创建带有背景的面板
        JPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        // 添加标题
        JLabel lblTitle = new JLabel("欢迎登录医院管理系统");
        lblTitle.setFont(new Font("微软雅黑", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        backgroundPanel.add(lblTitle, gbc);

        // 添加用户名标签和文本框
        JLabel lblUsername = new JLabel("用户名:");
        lblUsername.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblUsername.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        txtUsername.setBackground(new Color(255, 255, 255, 200)); // 半透明背景
        txtUsername.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(txtUsername, gbc);

        // 添加密码标签和密码框
        JLabel lblPassword = new JLabel("密码:");
        lblPassword.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        lblPassword.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        txtPassword.setBackground(new Color(255, 255, 255, 200)); // 半透明背景
        txtPassword.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(txtPassword, gbc);

        // 添加登录按钮
        btnLogin = createStyledButton("登录");
        btnLogin.addActionListener(e -> login());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(btnLogin, gbc);

        // 支持密码框中按回车登录
        txtPassword.addActionListener(e -> login());

        // 添加取消按钮
        btnCancel = createStyledButton("取消");
        btnCancel.addActionListener(e -> cancel());
        gbc.gridx = 1;
        gbc.gridy = 3;
        backgroundPanel.add(btnCancel, gbc);

        // 设置窗口可见
        setVisible(true);
    }

    // 登录方法
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名或密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        boolean loginSuccess = false;
        String errorMsg = null;
        PreparedStatement stmt = null;
        PreparedStatement roleStmt = null;
        PreparedStatement permStmt = null;
        ResultSet rs = null;
        ResultSet roleRs = null;
        ResultSet permRs = null;
        try {
            conn = DBUtils.getConnection();

            // 查询用户账号
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                loginSuccess = true;
                // 查询用户角色
                String roleSql = "SELECT r.role_name FROM sys_user_role ur LEFT JOIN sys_role r ON ur.role_id = r.role_id WHERE ur.username = ?";
                roleStmt = conn.prepareStatement(roleSql);
                roleStmt.setString(1, username);
                roleRs = roleStmt.executeQuery();
                String roleName = "普通用户";
                if (roleRs.next()) {
                    roleName = roleRs.getString("role_name");
                }

                // 查询用户权限列表
                Set<String> permissions = new HashSet<>();
                String permSql = "SELECT p.perm_code FROM sys_user_role ur " +
                        "LEFT JOIN sys_role_permission rp ON ur.role_id = rp.role_id " +
                        "LEFT JOIN sys_permission p ON rp.perm_id = p.perm_id " +
                        "WHERE ur.username = ? AND p.status = 1";
                permStmt = conn.prepareStatement(permSql);
                permStmt.setString(1, username);
                permRs = permStmt.executeQuery();
                while (permRs.next()) {
                    permissions.add(permRs.getString("perm_code"));
                }

                // 存入会话
                SessionHolder.setLoginUser(username, permissions, roleName);

                JOptionPane.showMessageDialog(this, "登录成功！欢迎您，" + roleName + "：" + username, "成功", JOptionPane.INFORMATION_MESSAGE);
                // 跳转到系统主界面
                SystemMain main = new SystemMain();
                main.setVisible(true);
                dispose(); // 关闭登录界面
            } else {
                errorMsg = "用户名或密码错误";
                JOptionPane.showMessageDialog(this, errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            errorMsg = "数据库连接失败：" + ex.getMessage();
            JOptionPane.showMessageDialog(this, "数据库连接失败！", "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            DBUtils.close(conn, permStmt, permRs);
            DBUtils.close(null, roleStmt, roleRs);
            DBUtils.close(null, stmt, rs);
            // 记录登录审计日志
            if (loginSuccess) {
                AuditLogUtils.logSuccess(AuditLogUtils.TYPE_LOGIN, "用户登录系统");
            } else {
                AuditLogUtils.logFail(AuditLogUtils.TYPE_LOGIN, "用户尝试登录失败", errorMsg);
            }
        }
    }

    // 清空输入框
    private void cancel() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.requestFocus();
    }

    /**
     * 获取用户名输入框（为外部调用保留）
     */
    public static JTextField getTxtUsername() {
        return txtUsername;
    }

    private static class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("/bg2.png"));
                if (backgroundImage == null) {
                    System.err.println("Failed to load background image");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(76, 175, 80)); // 绿色背景
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 圆角效果
        button.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isArmed()) {
                button.setBackground(new Color(67, 160, 71)); // 按下时变暗
            } else {
                button.setBackground(new Color(76, 175, 80));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置JTattoo主题
                UIManager.setLookAndFeel(new FastLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Sur();
        });
    }
}