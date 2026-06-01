package com.sxt;

import com.jtattoo.plaf.fast.FastLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SystemMain extends JFrame {
    public SystemMain() {
        // 设置自定义外观
        try {
            // 设置JTattoo主题
            UIManager.setLookAndFeel(new FastLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("管理系统主界面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 添加菜单栏
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // 设置背景和欢迎面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getResource("/bg1.png")));
        backgroundLabel.setLayout(new BorderLayout());

        JPanel welcomePanel = createWelcomePanel();
        backgroundLabel.add(welcomePanel, BorderLayout.CENTER);

        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        // 添加底部的消息标签
        JLabel footerLabel = new JLabel("大数据23   小小系统", JLabel.CENTER);
        footerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        add(mainPanel);

        // 设置窗口居中并显示
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 用户管理菜单：只有拥有用户管理权限的才能看到
        if (PermissionUtils.hasPermission("user:manage")) {
            JMenu menuUserManagement = new JMenu("用户管理");
            menuUserManagement.add(createMenuItem("注册", e -> new Register()));
            menuUserManagement.add(createMenuItem("切换账户", e -> {
                SessionHolder.logout();
                new Sur();
                dispose();
            }));
            menuBar.add(menuUserManagement);
        }

        // 病人管理菜单
        JMenu menuPatientManagement = new JMenu("病人管理");
        if (PermissionUtils.hasPermission("patient:add")) {
            menuPatientManagement.add(createMenuItem("信息录入", e -> new PatientInfoGUI().setVisible(true)));
        }
        if (PermissionUtils.hasPermission("patient:query")) {
            menuPatientManagement.add(createMenuItem("查询", e -> new PatientInfoSearch().setVisible(true)));
        }
        if (PermissionUtils.hasPermission("patient:admission")) {
            menuPatientManagement.add(createMenuItem("入院和出院", e -> new AdmissionManagement().setVisible(true)));
        }
        // 只有当菜单有子项时才添加到菜单栏
        if (menuPatientManagement.getItemCount() > 0) {
            menuBar.add(menuPatientManagement);
        }

        // 医生管理菜单
        JMenu menuDoctorManagement = new JMenu("医生管理");
        if (PermissionUtils.hasPermission("doctor:schedule")) {
            menuDoctorManagement.add(createMenuItem("医生排班管理", e -> new DoctorScheduleManagement().setVisible(true)));
        }
        if (PermissionUtils.hasPermission("doctor:info")) {
            menuDoctorManagement.add(createMenuItem("医生信息管理", e -> new DoctorInfoManagement().setVisible(true)));
        }
        if (PermissionUtils.hasPermission("doctor:performance")) {
            menuDoctorManagement.add(createMenuItem("医生绩效统计", e -> new DoctorPerformanceStatistics().setVisible(true)));
        }
        if (menuDoctorManagement.getItemCount() > 0) {
            menuBar.add(menuDoctorManagement);
        }

        // 药品库存管理菜单
        if (PermissionUtils.hasPermission("medicine:manage")) {
            JMenu menuMedicineStockManagement = new JMenu("药品库存管理");
            menuMedicineStockManagement.add(createMenuItem("药品信息录入和维护", e -> showFeatureUnavailable()));
            menuMedicineStockManagement.add(createMenuItem("药品采购和入库管理", e -> showFeatureUnavailable()));
            menuMedicineStockManagement.add(createMenuItem("药品出库和使用记录", e -> showFeatureUnavailable()));
            menuBar.add(menuMedicineStockManagement);
        }

        // 审计日志菜单：只有超级管理员能看到
        if (PermissionUtils.hasPermission("audit:view")) {
            JMenu menuAudit = new JMenu("审计管理");
            menuAudit.add(createMenuItem("操作日志查询", e -> JOptionPane.showMessageDialog(this, "审计日志查询功能开发中", "提示", JOptionPane.INFORMATION_MESSAGE)));
            menuBar.add(menuAudit);
        }

        // 数据库概览菜单：只有有权限的用户能看到
        if (PermissionUtils.hasPermission("database:overview")) {
            JMenu menuDatabase = new JMenu("数据管理");
            menuDatabase.add(createMenuItem("数据库概览", e -> new DatabaseOverview()));
            menuBar.add(menuDatabase);
        }

        return menuBar;
    }

    private JMenuItem createMenuItem(String text, ActionListener action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblWelcome = new JLabel("欢迎使用管理系统！");
        lblWelcome.setFont(new Font("微软雅黑", Font.BOLD, 36));
        lblWelcome.setForeground(new Color(0, 0, 0));
        panel.add(lblWelcome, gbc);

        return panel;
    }

    private void showFeatureUnavailable() {
        JOptionPane.showMessageDialog(this, "该功能暂未实现", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 设置JTattoo主题
                UIManager.setLookAndFeel(new FastLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SystemMain();
        });
    }
}