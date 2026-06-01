package com.sxt;

import com.sxt.common.DBUtils;
import com.sxt.common.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class MedicineInfoManagement extends JFrame {

    private JTextField txtMedId;
    private JTextField txtName;
    private JTextArea txtDescription;
    private JTextField txtStock;
    private JButton btnUpdate;
    private JTextArea txtResults;

    public MedicineInfoManagement() {
        setTitle("药品信息录入和维护");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // 创建药品ID输入框
        txtMedId = new JTextField(10);
        add(new JLabel("药品ID:"));
        add(txtMedId);

        // 创建药品名称输入框
        txtName = new JTextField(20);
        add(new JLabel("药品名称:"));
        add(txtName);

        // 创建药品描述输入框
        txtDescription = new JTextArea(5, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(txtDescription);
        add(new JLabel("药品描述:"));
        add(descriptionScrollPane);

        // 创建库存数量输入框
        txtStock = new JTextField(10);
        add(new JLabel("库存数量:"));
        add(txtStock);

        // 创建更新按钮
        btnUpdate = new JButton("更新药品信息");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedicineInfo();
            }
        });
        add(btnUpdate);

        // 创建结果显示区域
        txtResults = new JTextArea(5, 20);
        txtResults.setEditable(false);
        JScrollPane resultsScrollPane = new JScrollPane(txtResults);
        add(resultsScrollPane);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateMedicineInfo() {
        String medId = txtMedId.getText().trim();
        String name = txtName.getText().trim();
        String description = txtDescription.getText().trim();
        int stock;
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException ex) {
            UIUtils.showWarning(this, "库存数量必须为整数！");
            return;
        }

        if (medId.isEmpty() || name.isEmpty()) {
            UIUtils.showWarning(this, "药品ID和名称不能为空！");
            return;
        }

        if (!UIUtils.confirm(this, "确认更新药品(ID:" + medId + "：" + name + ")的信息吗？")) {
            return;
        }

        String sql = "REPLACE INTO medicines (medicine_id, name, description, stock) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtils.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(medId));
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setInt(4, stock);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                txtResults.setText("药品信息更新成功！");
                UIUtils.showSuccess(this, "药品(ID:" + medId + ")信息已更新");
            } else {
                txtResults.setText("药品信息更新失败！");
                UIUtils.showError(this, "更新失败");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            UIUtils.showError(this, "数据库错误：" + ex.getMessage());
        } finally {
            DBUtils.close(conn, pstmt);
        }
    }

    public static void main(String[] args) {
        new MedicineInfoManagement();
    }
}
