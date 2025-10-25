/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.StudentRole;

import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Person;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author hug0_
 */
public class ProfileJPanel extends javax.swing.JPanel {
    private StudentProfile studentProfile;
    private String originalName;
    private String originalEmail;
    private String originalPhone;
    private JPanel CardSequencePanel;

    /**
     * Creates new form ProfileJPanel
     */
    public ProfileJPanel( StudentProfile studentProfile, JPanel cardSequencePanel) {
        this.studentProfile = studentProfile;
       this.CardSequencePanel = cardSequencePanel; 
        
        initComponents();//NetBeans 生成ui初始
        customInit();
        loadData();
        setViewMode();
        
    }
    
    private void customInit() {
        // 1. Student ID不可编辑
        fieldStudentID.setEditable(false);
        fieldStudentID.setBackground(new Color(240, 240, 240));  // 灰色背景表示不可编辑
        
        // 2. Department字段也设为只读
        fieldDepartment.setEditable(false);
        fieldDepartment.setBackground(new Color(240, 240, 240));
       
        // 3. 初始状态：Save和Cancel按钮禁用
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
     //* 从StudentProfile加载数据到UI
   
    private void loadData() {
        Person person = studentProfile.getPerson();
        
        if (person == null) {
        System.err.println("Person is null!");
        return;
    }
        
        // 加载基本信息
        String name = person.getName();
        String id = person.getPersonId();
        String email = person.getEmail();
        String phone = person.getPhoneNumber();
        
        // 设置到UI组件
        fieldName.setText(name != null ? name : "");
        fieldStudentID.setText(id != null ? id : "");
        fieldEmail.setText(email != null ? email : "Not Set");
        fieldPhone.setText(phone != null ? phone : "Not Set");
       
        fieldDepartment.setText("Information Systems");  // 默认值
     
        
        // 保存原始数据（用于Cancel）
        saveOriginalData();
    }
    
    /**
     * 保存原始数据，用于Cancel时恢复
     */
    private void saveOriginalData() {
        originalName = fieldName.getText();
        originalEmail = fieldEmail.getText();
        originalPhone = fieldPhone.getText();
    }
    
    /**
     * 设置为查看模式（只读）
     */
    private void setViewMode() {
        // 可编辑字段设为不可编辑
        fieldName.setEditable(false);
        fieldEmail.setEditable(false);
        fieldPhone.setEditable(false);
        
        // 设置背景色（可选，表示不可编辑）
        Color viewBg = new Color(250, 250, 250);
        fieldName.setBackground(viewBg);
        fieldEmail.setBackground(viewBg);
        fieldPhone.setBackground(viewBg);
        
        // 按钮状态
        btnEdit.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }
    
    /**
     * 设置为编辑模式
     */
    private void setEditMode() {
        // 可编辑字段启用编辑
        fieldName.setEditable(true);
        fieldEmail.setEditable(true);
        fieldPhone.setEditable(true);
        
        // 恢复白色背景
        Color editBg = Color.WHITE;
        fieldName.setBackground(editBg);
        fieldEmail.setBackground(editBg);
        fieldPhone.setBackground(editBg);
        
        // 按钮状态
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        
        // 聚焦到第一个可编辑字段
        fieldName.requestFocus();
    }
    private boolean validateInput() {
    String name = fieldName.getText().trim();
    String email = fieldEmail.getText().trim();
    String phone = fieldPhone.getText().trim();
    
    if (name.isEmpty() || name.length() < 2) {
        showError("Name must be at least 2 characters!");
        fieldName.requestFocus();
        return false;
    }
    
    if (!name.matches("^[a-zA-Z\\s\\-]+$")) {
        showError("Name can only contain letters, spaces, and hyphens!");
        fieldName.requestFocus();
        return false;
    }
    
    if (!email.equals("Not Set") && 
        !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
        showError("Invalid email format! Example: student@university.edu");
        fieldEmail.requestFocus();
        return false;
    }
    
    if (!phone.equals("Not Set")) {
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        if (digitsOnly.length() != 10) {
            showError("Phone must be 10 digits! Example: 617-555-0001");
            fieldPhone.requestFocus();
            return false;
        }
    }
    
    return true;
}

private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, 
        "Validation Error", JOptionPane.ERROR_MESSAGE);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        lblTittle = new javax.swing.JLabel();
        btnEdit = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        fieldName = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        lblStudentID = new javax.swing.JLabel();
        fieldStudentID = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        fieldEmail = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        fieldPhone = new javax.swing.JTextField();
        lblDepartment = new javax.swing.JLabel();
        fieldDepartment = new javax.swing.JTextField();

        btnBack.setText("< < Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTittle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTittle.setText("My Profile");

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.MatteBorder(null));

        fieldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNameActionPerformed(evt);
            }
        });

        lblName.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblName.setText("Name");

        lblStudentID.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblStudentID.setText("Student ID");

        lblEmail.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblEmail.setText("Email");

        lblPhone.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblPhone.setText("Phone");

        lblDepartment.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblDepartment.setText("Department");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDepartment, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPhone, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblStudentID, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(74, 74, 74)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(fieldName)
                    .addComponent(fieldStudentID)
                    .addComponent(fieldEmail)
                    .addComponent(fieldPhone)
                    .addComponent(fieldDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStudentID)
                    .addComponent(fieldStudentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhone)
                    .addComponent(fieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment)
                    .addComponent(fieldDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addComponent(btnBack)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(110, 110, 110)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(lblTittle)))
                .addContainerGap(231, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(lblTittle))
                .addGap(38, 38, 38)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnSave)
                    .addComponent(btnCancel))
                .addContainerGap(81, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        saveOriginalData();  // 保存当前数据
        setEditMode();       // 切换到编辑模式
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        // 1. 验证输入
        if (!validateInput()) {
            return;  // 验证失败，不保存
        }
        
        // 2. 获取输入的值
        String newName = fieldName.getText().trim();
        String newEmail = fieldEmail.getText().trim();
        String newPhone = fieldPhone.getText().trim();
        
        // 3. 保存到数据模型
        try {
            Person person = studentProfile.getPerson();
            person.setName(newName);
            person.setEmail(newEmail);
            person.setPhoneNumber(newPhone);
            
            // 4. 切换回查看模式
            setViewMode();
            
            // 5. 显示成功消息
            JOptionPane.showMessageDialog(
                this,
                "Profile updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            System.out.println("Profile updated: " + newName + ", " + newEmail + ", " + newPhone);
            
        } catch (Exception e) {
            // 保存失败
            JOptionPane.showMessageDialog(
                this,
                "Failed to save profile: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
         fieldName.setText(originalName);
        fieldEmail.setText(originalEmail);
        fieldPhone.setText(originalPhone);
        
        // 切换回查看模式
        setViewMode();
        
        // 可选：显示提示
        System.out.println("Edit cancelled, data restored.");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).show(CardSequencePanel, "StudentMenu");

    }//GEN-LAST:event_btnBackActionPerformed

    private void fieldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JTextField fieldDepartment;
    private javax.swing.JTextField fieldEmail;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldPhone;
    private javax.swing.JTextField fieldStudentID;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblStudentID;
    private javax.swing.JLabel lblTittle;
    // End of variables declaration//GEN-END:variables
}
