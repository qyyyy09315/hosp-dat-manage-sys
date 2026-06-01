package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class DoctorInfoManagement extends JFrame {

    private JTextField txtDoctorId;
    private JTextField txtName;
    private JTextField txtSpecialization;
    private JTextField txtPerformanceScore;
    private JButton btnUpdateInfo;
    private JTextArea txtResults;

    public DoctorInfoManagement() {
        setTitle("医生信息管理");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 创建医生ID输入框
        txtDoctorId = new JTextField(10);
        add(new JLabel("医生ID:"));
        add(txtDoctorId);

        // 创建姓名输入框
        txtName = new JTextField(20);
        add(new JLabel("姓名:"));
        add(txtName);

        // 创建专业输入框
        txtSpecialization = new JTextField(20);
        add(new JLabel("专业:"));
        add(txtSpecialization);

        // 创建绩效得分输入框
        txtPerformanceScore = new JTextField(10);
        add(new JLabel("绩效得分:"));
        add(txtPerformanceScore);

        // 创建更新按钮
        btnUpdateInfo = new JButton("更新信息");
        btnUpdateInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDoctorInfo();
            }
        });
        add(btnUpdateInfo);

        // 创建结果显示区域
        txtResults = new JTextArea(5, 20);
        txtResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResults);
        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateDoctorInfo() {
        String doctorId = txtDoctorId.getText().trim();
        String name = txtName.getText().trim();
        String specialization = txtSpecialization.getText().trim();
        String performanceScoreStr = txtPerformanceScore.getText().trim();

        if (doctorId.isEmpty() || name.isEmpty() || specialization.isEmpty() || performanceScoreStr.isEmpty()) {
            UIUtils.showWarning(this, "所有字段都是必填项！");
            return;
        }

        if (!ValidatorUtils.number(doctorId, "医生ID") || !ValidatorUtils.noSpecialChar(name, "医生姓名")) {
            return;
        }

        if (!UIUtils.confirm(this, "确认更新医生(ID:" + doctorId + ")的信息吗？")) {
            return;
        }

        double performanceScore;
        try {
            performanceScore = Double.parseDouble(performanceScoreStr);
        } catch (NumberFormatException ex) {
            UIUtils.showWarning(this, "绩效得分必须是数字！");
            return;
        }

        String sql = "UPDATE doctors SET name = ?, specialization = ?, performance_score = ? WHERE doctor_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, specialization);
            pstmt.setDouble(3, performanceScore);
            pstmt.setInt(4, Integer.parseInt(doctorId));
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                txtResults.setText("医生信息更新成功！");
                UIUtils.showSuccess(this, "医生(ID:" + doctorId + ")信息已更新");
            } else {
                txtResults.setText("未找到该医生或信息未改变！");
                UIUtils.showError(this, "未找到该医生");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            UIUtils.showError(this, "数据库错误：" + ex.getMessage());
        } catch (NumberFormatException ex) {
            UIUtils.showWarning(this, "请输入有效的医生ID！");
        } finally {
            DBUtils.close(conn, pstmt);
        }
    }

    public static void main(String[] args) {
        new DoctorInfoManagement();
    }
}
