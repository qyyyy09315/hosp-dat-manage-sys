package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class PatientInfoGUI extends JFrame {

    private JTextField txtPatientId;
    private JTextField txtName;
    private JRadioButton radioMale;
    private JRadioButton radioFemale;
    private JTextField txtBirthDate;
    private JTextField txtContactInfo;
    private JComboBox<String> comboAdmissionStatus;
    private JButton btnSubmit;

    public PatientInfoGUI() {
        // 权限校验：没有病人信息录入权限直接关闭
        if (!PermissionUtils.checkPermission("patient:add", "病人信息录入")) {
            dispose();
            return;
        }

        setTitle("录入病人信息");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭窗口不退出程序
        setLayout(new FlowLayout());

        // 创建表单字段
        txtPatientId = new JTextField(10);
        txtName = new JTextField(10);
        txtBirthDate = new JTextField(10);
        txtContactInfo = new JTextField(10);

        ButtonGroup genderGroup = new ButtonGroup();
        radioMale = new JRadioButton("男", true);
        radioFemale = new JRadioButton("女");
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);

        comboAdmissionStatus = new JComboBox<>(new String[]{"入院", "出院", "未入院"});
        comboAdmissionStatus.setSelectedIndex(2); // 默认选择“未入院”

        btnSubmit = new JButton("提交");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitPatientInfo();
            }
        });

        // 添加组件到窗口
        add(new JLabel("病人ID:"));
        add(txtPatientId);
        add(new JLabel("姓名:"));
        add(txtName);
        add(new JLabel("性别:"));
        add(radioMale);
        add(radioFemale);
        add(new JLabel("出生日期:"));
        add(txtBirthDate);
        add(new JLabel("联系方式:"));
        add(txtContactInfo);
        add(new JLabel("住院状态:"));
        add(comboAdmissionStatus);
        add(btnSubmit);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitPatientInfo() {
        // 从表单字段获取数据
        String patientId = txtPatientId.getText().trim();
        String name = txtName.getText().trim();
        String gender = radioMale.isSelected() ? "男" : "女";
        String birthDate = txtBirthDate.getText().trim();
        String contactInfo = txtContactInfo.getText().trim();
        String admissionStatus = (String) comboAdmissionStatus.getSelectedItem();

        // 输入校验
        if (!ValidatorUtils.require(patientId, "病人ID")
                || !ValidatorUtils.number(patientId, "病人ID")
                || !ValidatorUtils.require(name, "病人姓名")
                || !ValidatorUtils.length(name, 2, 20, "病人姓名")
                || !ValidatorUtils.require(birthDate, "出生日期")
                || !ValidatorUtils.phone(contactInfo)
                || !ValidatorUtils.noSpecialChar(name, "病人姓名")
                || !ValidatorUtils.noSpecialChar(contactInfo, "联系方式")) {
            return;
        }
        
        // 操作二次确认
        if (!UIUtils.confirm(this, "确认要录入该病人信息吗？")) {
            return;
        }

        boolean success = false;
        String errorMsg = null;
        String sql = "INSERT INTO patients (patient_id, name, gender, birth_date, contact_info, admission_status, created_at) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        JDialog loading = UIUtils.showLoading(this, "正在保存数据，请稍候...");

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, name);
            pstmt.setString(3, gender);
            pstmt.setDate(4, Date.valueOf(birthDate));
            pstmt.setString(5, contactInfo);
            pstmt.setString(6, admissionStatus);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
                UIUtils.showSuccess(this, "病人信息录入成功！");
                dispose(); // 关闭录入窗口
            } else {
                errorMsg = "数据库返回0行受影响";
                UIUtils.showError(this, "病人信息录入失败！");
            }
        } catch (Exception ex) {
            errorMsg = ex.getMessage();
            ex.printStackTrace();
            UIUtils.showError(this, "操作失败：" + ex.getMessage());
        } finally {
            DBUtils.close(conn, pstmt);
            loading.dispose();
            // 记录审计日志
            if (success) {
                AuditLogUtils.logSuccess(AuditLogUtils.TYPE_ADD, "新增病人信息：" + name + "(ID:" + patientId + ")");
            } else {
                AuditLogUtils.logFail(AuditLogUtils.TYPE_ADD, "新增病人信息失败：" + name, errorMsg);
            }
        }
    }

    public static void main(String[] args) {
        new PatientInfoGUI();
    }
}
