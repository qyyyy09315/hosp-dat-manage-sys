package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings("serial")
public class Register extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;

    public Register() {
        setTitle("用户注册");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 使用GridBagLayout布局管理器
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 添加标题
        JLabel lblTitle = new JLabel("用户注册");
        lblTitle.setFont(new Font("宋体", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        // 添加用户名标签和文本框
        JLabel lblUsername = new JLabel("用户名:");
        lblUsername.setFont(new Font("宋体", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblUsername, gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtUsername, gbc);

        // 添加密码标签和密码框
        JLabel lblPassword = new JLabel("密码:");
        lblPassword.setFont(new Font("宋体", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblPassword, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtPassword, gbc);

        // 添加确认密码标签和密码框
        JLabel lblConfirmPassword = new JLabel("确认密码:");
        lblConfirmPassword.setFont(new Font("宋体", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblConfirmPassword, gbc);

        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtConfirmPassword, gbc);

        // 添加注册按钮
        btnRegister = new JButton("注册");
        btnRegister.setFont(new Font("宋体", Font.PLAIN, 14));
        btnRegister.addActionListener(e -> register());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnRegister, gbc);

        // 添加取消按钮
        btnCancel = new JButton("取消");
        btnCancel.setFont(new Font("宋体", Font.PLAIN, 14));
        btnCancel.addActionListener(e -> cancel());
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(btnCancel, gbc);

        // 设置窗口可见
        setVisible(true);
    }

    // 注册方法
    private void register() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        // 校验输入
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            UIUtils.showWarning(this, "用户名或密码不能为空！");
            return;
        }

        if (!password.equals(confirmPassword)) {
            UIUtils.showWarning(this, "两次输入的密码不一致！");
            return;
        }

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();

            // 检查用户名是否已存在
            String checkSql = "SELECT * FROM users WHERE username = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                UIUtils.showWarning(this, "用户名已存在，请选择其他用户名！");
                return;
            }

            // 插入新用户
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, '用户')";
            insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);

            int rows = insertStmt.executeUpdate();
            if (rows > 0) {
                UIUtils.showSuccess(this, "注册成功！");
                dispose();
                new Sur();
            }
        } catch (Exception ex) {
            UIUtils.showError(this, "数据库连接失败！");
            ex.printStackTrace();
        } finally {
            DBUtils.close(conn, checkStmt, rs);
            DBUtils.close(null, insertStmt, null);
        }
    }

    // 清空输入框
    private void cancel() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtUsername.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Register::new);
    }
}