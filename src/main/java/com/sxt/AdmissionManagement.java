package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class AdmissionManagement extends JFrame {

    private JTextField txtPatientId;
    private JButton btnUpdateStatus;
    private JTextField txtAdmissionStatus;
    private JTextArea txtResults;

    public AdmissionManagement() {
        setTitle("修改入院和出院信息");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 创建病人ID输入框
        txtPatientId = new JTextField(10);
        add(new JLabel("病人ID:"));
        add(txtPatientId);

        // 创建住院状态输入框
        txtAdmissionStatus = new JTextField(10);
        add(new JLabel("住院状态（入院/出院）:"));
        add(txtAdmissionStatus);

        // 创建更新按钮
        btnUpdateStatus = new JButton("更新状态");
        btnUpdateStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAdmissionStatus();
            }
        });
        add(btnUpdateStatus);

        // 创建结果显示区域
        txtResults = new JTextArea(5, 20);
        txtResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResults);
        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateAdmissionStatus() {
        String patientId = txtPatientId.getText().trim();
        String admissionStatus = txtAdmissionStatus.getText().trim();

        if (patientId.isEmpty() || admissionStatus.isEmpty()) {
            UIUtils.showWarning(this, "病人ID和住院状态不能为空！");
            return;
        }

        if (!admissionStatus.equals("入院") && !admissionStatus.equals("出院")) {
            UIUtils.showWarning(this, "住院状态只能输入\"入院\"或\"出院\"！");
            return;
        }

        if (!UIUtils.confirm(this, "确认将病人(ID:" + patientId + ")状态更新为 " + admissionStatus + " 吗？")) {
            return;
        }

        String sql = "UPDATE patients SET admission_status = ? WHERE patient_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, admissionStatus);
            pstmt.setInt(2, Integer.parseInt(patientId));
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                txtResults.setText("状态更新成功！");
                UIUtils.showSuccess(this, "病人(ID:" + patientId + ")状态已更新为 " + admissionStatus);
            } else {
                txtResults.setText("未找到该病人，请确认病人ID是否正确！");
                UIUtils.showError(this, "未找到该病人");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            UIUtils.showError(this, "数据库错误：" + ex.getMessage());
        } catch (NumberFormatException ex) {
            UIUtils.showWarning(this, "请输入有效的病人ID！");
        } finally {
            DBUtils.close(conn, pstmt);
        }
    }

    public static void main(String[] args) {
        new AdmissionManagement();
    }
}