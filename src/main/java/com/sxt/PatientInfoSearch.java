package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class PatientInfoSearch extends JFrame {

    private JTextField txtSearch;
    private JButton btnSearch;
    private JTextArea txtResults;
    private JComboBox<String> comboSearchType;

    public PatientInfoSearch() {
        setTitle("查询病人信息");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 创建搜索类型下拉框
        comboSearchType = new JComboBox<>(new String[]{"病人ID", "姓名"});
        add(new JLabel("搜索类型:"));
        add(comboSearchType);

        // 创建搜索框
        txtSearch = new JTextField(20);
        add(new JLabel("搜索内容:"));
        add(txtSearch);

        // 创建查询按钮
        btnSearch = new JButton("查询");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPatientInfo();
            }
        });
        add(btnSearch);

        // 创建结果显示区域
        txtResults = new JTextArea(10, 30);
        txtResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResults);
        add(scrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void searchPatientInfo() {
        String searchType = (String) comboSearchType.getSelectedItem();
        String searchContent = txtSearch.getText();
        
        // 中文搜索类型映射到数据库字段名，白名单校验
        String columnName;
        if ("病人ID".equals(searchType)) {
            columnName = "patient_id";
        } else if ("姓名".equals(searchType)) {
            columnName = "name";
        } else {
            JOptionPane.showMessageDialog(this, "非法的搜索类型");
            return;
        }
        
        String sql = "SELECT * FROM patients WHERE " + columnName + " = ?";

        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, searchContent);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder results = new StringBuilder();
            while (rs.next()) {
                results.append("病人ID: ").append(rs.getInt("patient_id"))
                        .append(", 姓名: ").append(rs.getString("name"))
                        .append(", 性别: ").append(rs.getString("gender"))
                        .append(", 出生日期: ").append(rs.getDate("birth_date"))
                        .append(", 联系方式: ").append(rs.getString("contact_info"))
                        .append(", 住院状态: ").append(rs.getString("admission_status"))
                        .append("\n");
            }
            
            if (results.length() == 0) {
                results.append("未查询到匹配的病人信息，请确认搜索内容是否正确");
            }
            
            txtResults.setText(results.toString());
            DBUtils.close(conn, pstmt, rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
            UIUtils.showError(this, "数据库错误：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new PatientInfoSearch();
    }
}
