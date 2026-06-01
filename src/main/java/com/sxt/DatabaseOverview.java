package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

@SuppressWarnings("serial")
public class DatabaseOverview extends JFrame {

    public DatabaseOverview() {
        // 权限校验
        if (!PermissionUtils.checkPermission("database:overview", "数据库概览")) {
            dispose();
            return;
        }

        setTitle("数据库概览");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部统计面板
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        statsPanel.setBorder(BorderFactory.createTitledBorder("数据统计"));
        statsPanel.setBackground(Color.WHITE);

        // 查询统计数据
        int patientCount = getCount("SELECT COUNT(*) FROM patients");
        int doctorCount = getCount("SELECT COUNT(*) FROM doctors");
        int medicineCount = getCount("SELECT COUNT(*) FROM medicines");
        int inHospitalCount = getCount("SELECT COUNT(*) FROM patients WHERE admission_status = '入院'");

        // 添加统计卡片
        statsPanel.add(createStatCard("总病人数", String.valueOf(patientCount), new Color(66, 133, 244)));
        statsPanel.add(createStatCard("入院中病人数", String.valueOf(inHospitalCount), new Color(219, 68, 55)));
        statsPanel.add(createStatCard("医生总数", String.valueOf(doctorCount), new Color(244, 180, 0)));
        statsPanel.add(createStatCard("药品总数", String.valueOf(medicineCount), new Color(15, 157, 88)));

        add(statsPanel, BorderLayout.NORTH);

        // 选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();

        // 病人列表
        tabbedPane.addTab("病人信息", createDataTable("patients"));
        // 医生列表
        tabbedPane.addTab("医生信息", createDataTable("doctors"));
        // 药品列表
        tabbedPane.addTab("药品信息", createDataTable("medicines"));
        // 账单列表
        tabbedPane.addTab("账单信息", createDataTable("bills"));
        // 操作日志
        tabbedPane.addTab("操作审计日志", createDataTable("sys_audit_log"));

        add(tabbedPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        // 记录操作日志
        AuditLogUtils.logSuccess("database:view", "查看数据库概览");
    }

    // 创建统计卡片
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 32));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return card;
    }

    // 查询数量
    private int getCount(String sql) {
        int count = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
        return count;
    }

    // 创建数据表格
    private JScrollPane createDataTable(String tableName) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " LIMIT 100");
            rs = pstmt.executeQuery();

            // 获取列名
            int columnCount = rs.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rs.getMetaData().getColumnLabel(i));
            }
            model.setColumnIdentifiers(columnNames);

            // 加载数据
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showError(this, "加载" + tableName + "数据失败：" + e.getMessage());
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }

        return new JScrollPane(table);
    }
}
