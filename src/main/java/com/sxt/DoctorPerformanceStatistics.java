package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("serial")
public class DoctorPerformanceStatistics extends JFrame {

    private JTextArea txtPerformanceData;

    public DoctorPerformanceStatistics() {
        setTitle("医生绩效统计");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        txtPerformanceData = new JTextArea(15, 50);
        txtPerformanceData.setEditable(false);
        txtPerformanceData.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        add(new JScrollPane(txtPerformanceData), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnRefresh = new JButton("刷新统计");
        btnRefresh.addActionListener(e -> refreshStatistics());
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        // 打开窗口自动加载数据
        refreshStatistics();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshStatistics() {
        String sql = "SELECT name, specialization, schedule, performance_score FROM doctors ORDER BY performance_score DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            StringBuilder stats = new StringBuilder();
            stats.append("排名\t姓名\t\t科室\t\t排班时间\t\t绩效得分\n");
            stats.append("--------------------------------------------------------------------------------\n");
            int rank = 1;
            while (rs.next()) {
                stats.append(rank++).append("\t")
                     .append(rs.getString("name")).append("\t\t")
                     .append(rs.getString("specialization")).append("\t\t")
                     .append(rs.getString("schedule")).append("\t\t")
                     .append(rs.getDouble("performance_score")).append("\n");
            }
            txtPerformanceData.setText(stats.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            UIUtils.showError(this, "数据库错误：" + ex.getMessage());
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
    }

    public static void main(String[] args) {
        new DoctorPerformanceStatistics();
    }
}
