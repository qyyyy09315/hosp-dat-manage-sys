package com.sxt;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SuppressWarnings("serial")
class StudentInfoManagement extends JFrame {
	private Connection conn; // 数据库连接对象
	
    public StudentInfoManagement() {
        setTitle("学生信息管理系统");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
     // 加载 JDBC 驱动
        try {
            // 这里以 MySQL 为例，确保你的驱动名称和 URL 与实际使用的数据库一致
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oh", "root", "123456");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库连接失败！", "错误", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        // 添加顶部标题栏
        JLabel titleLabel = new JLabel("学生信息管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 添加菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0)); // 点击退出时关闭程序
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // 添加操作面板
        JPanel operationPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("添加学生");
        operationPanel.add(addButton);
        JButton editButton = new JButton("编辑学生");
        operationPanel.add(editButton);
        JButton deleteButton = new JButton("删除学生");
        operationPanel.add(deleteButton);
        add(operationPanel, BorderLayout.SOUTH);

        // 添加学生信息显示区域
        JPanel studentInfoPanel = new JPanel();
        studentInfoPanel.setLayout(new BoxLayout(studentInfoPanel, 
        										BoxLayout.Y_AXIS)); // 设置布局为垂直排列
        JLabel studentLabel = new JLabel("学生信息显示区域");
        studentLabel.setFont(new Font("宋体", Font.BOLD, 18)); // 可以设置标题字体
        studentInfoPanel.add(studentLabel);
        add(studentInfoPanel, BorderLayout.CENTER);
        // 从数据库获取学生信息并显示
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");
            while (rs.next()) {
                String name = rs.getString("name");
                String studentId = rs.getString("student_id");
                JLabel studentInfo = new JLabel(name + " (" + studentId + ")");
                studentInfoPanel.add(studentInfo);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "获取学生信息失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 设置窗口居中
        setLocationRelativeTo(null);
    }

    // 在关闭窗口时关闭数据库连接
    public void dispose() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dispose();
    }
}