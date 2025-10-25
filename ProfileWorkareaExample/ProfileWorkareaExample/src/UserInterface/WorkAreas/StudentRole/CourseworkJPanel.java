/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.StudentRole;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Transcript;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hug0_
 */
public class CourseworkJPanel extends javax.swing.JPanel {
     private StudentProfile studentProfile;
     private JPanel CardSequencePanel;
     
    private static final int TUITION_PER_CREDIT = 1500;
    private static final String CURRENT_SEMESTER = "Fall 2025";
    
    private ArrayList<String[]> paymentHistory;  // 支付历史
    private double currentBalance = 0.0;         // 当前余额
    

    /**
     * Creates new form CourseworkPanel
     */
    public CourseworkJPanel(StudentProfile studentProfile,JPanel cardSequencePanel) {
        this.studentProfile = studentProfile;
        this.CardSequencePanel = cardSequencePanel;
        this.paymentHistory = new ArrayList<>();
        initComponents();
        
        
        calculateTuitionBalance();
        loadCourseFees();
        loadPaymentHistory();
        updateBalanceDisplay();
        
    }
    // ==================== 计算学费余额 ====================
    /**
     * 计算当前学期的学费
     */
    
    private void calculateTuitionBalance() {
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) {
                currentBalance = 0.0;
                return;
            }
            
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            int totalCredits = 0;
            
            for (SeatAssignment seat : seats) {
                totalCredits += seat.getCreditHours();
            }
            
            // 计算学费 = 总学分 × 每学分费用
            currentBalance = totalCredits * TUITION_PER_CREDIT;
            
            System.out.println("Calculated tuition: $" + currentBalance + 
                " (Credits: " + totalCredits + " × $" + TUITION_PER_CREDIT + ")");
            
        } catch (Exception e) {
            System.err.println("Error calculating tuition: " + e.getMessage());
            currentBalance = 0.0;
        }
    }
    
    
     // ==================== 加载课程费用明细 ====================
    /**
     * 显示当前学期每门课程的费用
     */
    private void loadCourseFees() {
        DefaultTableModel model = (DefaultTableModel) tblCourseFees.getModel();
        model.setRowCount(0);
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) {
                return;
            }
            
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            int totalCredits = 0;
            double totalTuition = 0.0;
            
            // 添加每门课程
            for (SeatAssignment seat : seats) {
                Course course = seat.getAssociatedCourse();
                
                String courseId = course.getCourseNumber();
                String courseName = course.getName();
                int credits = course.getCredits();
                double tuition = credits * TUITION_PER_CREDIT;
                
                Object[] row = {
                    courseId,
                    courseName,
                    credits,
                    "$" + String.format("%,d", (int)tuition)
                };
                
                model.addRow(row);
                
                totalCredits += credits;
                totalTuition += tuition;
            }
            
            // 添加总计行
            if (!seats.isEmpty()) {
                Object[] totalRow = {
                    "",
                    "TOTAL",
                    totalCredits,
                    "$" + String.format("%,d", (int)totalTuition)
                };
                model.addRow(totalRow);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading course fees: " + e.getMessage());
        }
    }
    
     // ==================== 加载支付历史 ====================
    /**
     * 显示支付历史记录
     */
    private void loadPaymentHistory() {
        DefaultTableModel model = (DefaultTableModel) tblPaymentHistory.getModel();
        model.setRowCount(0);
        
        // 显示所有支付记录
        for (String[] record : paymentHistory) {
            model.addRow(record);
        }
        
        if (paymentHistory.isEmpty()) {
            // 如果没有支付记录，显示提示
            Object[] emptyRow = {"No payments yet", "", "", ""};
            model.addRow(emptyRow);
        }
    }
    
    
    // ==================== 更新余额显示 ====================
    /**
     * 更新余额显示（带颜色）
     */
    private void updateBalanceDisplay() {
        lblBalance.setText("$" + String.format("%,.2f", currentBalance));
        
        if (currentBalance > 0) {
            // 有欠费 - 红色
            lblBalance.setForeground(new Color(200, 0, 0));
            btnPayTuition.setEnabled(true);
        } else {
            // 已付清 - 绿色
            lblBalance.setForeground(new Color(0, 150, 0));
            lblBalance.setText("$0.00 (Paid)");
            btnPayTuition.setEnabled(false);
        }
    }
    
    
     // ==================== 支付学费 ====================
    /**
     * 支付学费功能
     */
    private void payTuition() {
        // 1. 检查余额
        if (currentBalance <= 0) {
            JOptionPane.showMessageDialog(this,
                "No balance to pay. Your account is current!",
                "No Balance",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 2. 显示确认对话框
        double amountToPay = currentBalance;
        
        String message = String.format(
            "Confirm Payment\n\n" +
            "Amount Due: $%,.2f\n\n" +
            "Payment Method: Credit Card\n\n" +
            "Do you want to proceed?",
            amountToPay
        );
        
        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;  // 用户取消
        }
        
        // 3. 选择支付方式
        String[] paymentMethods = {"Credit Card", "Debit Card", "Check", "Bank Transfer"};
        String selectedMethod = (String) JOptionPane.showInputDialog(this,
            "Select Payment Method:",
            "Payment Method",
            JOptionPane.QUESTION_MESSAGE,
            null,
            paymentMethods,
            paymentMethods[0]);
        
        if (selectedMethod == null) {
            return;  // 用户取消
        }
        
        // 4. 执行支付
        try {
            // 记录支付历史
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentDate = dateFormat.format(new Date());
            
            String[] paymentRecord = {
                currentDate,
                "$" + String.format("%,.2f", amountToPay),
                selectedMethod,
                "Paid"
            };
            
            paymentHistory.add(paymentRecord);
            
            // 清零余额
            currentBalance = 0.0;
            
            // 5. 更新显示
            updateBalanceDisplay();
            loadPaymentHistory();
            
            // 6. 显示成功消息
            JOptionPane.showMessageDialog(this,
                String.format("Payment successful!\n\nAmount Paid: $%,.2f\nMethod: %s\n\nYour account is now current.",
                    amountToPay, selectedMethod),
                "Payment Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            System.out.println("Payment processed: $" + amountToPay + " via " + selectedMethod);
            
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this,
                "Error processing payment: " + e.getMessage(),
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
        }
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
        lblTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblAccountTitle = new javax.swing.JLabel();
        lblBalanceLabel = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        lblTuitionRateLabel = new javax.swing.JLabel();
        lblTuitionRate = new javax.swing.JLabel();
        btnPayTuition = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblCourseFees = new javax.swing.JLabel();
        scrollPaneFees = new javax.swing.JScrollPane();
        tblCourseFees = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblPaymentHistory = new javax.swing.JLabel();
        scrollPaneHistory = new javax.swing.JScrollPane();
        tblPaymentHistory = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        lblCoursework = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCourseworkInfo = new javax.swing.JTextArea();

        btnBack.setText("< < Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Coursework & Financial Management");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblAccountTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblAccountTitle.setText("Tuition Account");

        lblBalanceLabel.setText("Current Balance :");

        lblBalance.setText("$0.00");

        lblTuitionRateLabel.setText(" Tuition Rate :");

        lblTuitionRate.setText("$1,500 per credit");

        btnPayTuition.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btnPayTuition.setText("Pay Tuition");
        btnPayTuition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayTuitionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblAccountTitle)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBalanceLabel)
                            .addComponent(lblTuitionRateLabel))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBalance)
                            .addComponent(lblTuitionRate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPayTuition)
                        .addGap(19, 19, 19))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(lblAccountTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBalance)
                            .addComponent(lblBalanceLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTuitionRate)
                            .addComponent(lblTuitionRateLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnPayTuition)
                        .addGap(15, 15, 15))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCourseFees.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCourseFees.setText("Current Semester Tuition Breakdown");

        tblCourseFees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course ID", "Name", "Credits", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPaneFees.setViewportView(tblCourseFees);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(lblCourseFees)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneFees, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCourseFees)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneFees, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblPaymentHistory.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblPaymentHistory.setText("Payment History");

        tblPaymentHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Date", "Amount", "Payment Method", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPaneHistory.setViewportView(tblPaymentHistory);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPaymentHistory)
                    .addComponent(scrollPaneHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPaymentHistory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCoursework.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCoursework.setText("Coursework ( Coming Soon )");

        txtCourseworkInfo.setColumns(20);
        txtCourseworkInfo.setRows(5);
        txtCourseworkInfo.setText("Assignment submission feature...");
        jScrollPane3.setViewportView(txtCourseworkInfo);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCoursework))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCoursework)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(195, 195, 195)
                        .addComponent(lblTitle))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(lblTitle))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayTuitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayTuitionActionPerformed
        // TODO add your handling code here:
        payTuition();
    }//GEN-LAST:event_btnPayTuitionActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        ((java.awt.CardLayout) CardSequencePanel.getLayout()).show(CardSequencePanel, "StudentMenu");
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnPayTuition;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAccountTitle;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblBalanceLabel;
    private javax.swing.JLabel lblCourseFees;
    private javax.swing.JLabel lblCoursework;
    private javax.swing.JLabel lblPaymentHistory;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTuitionRate;
    private javax.swing.JLabel lblTuitionRateLabel;
    private javax.swing.JScrollPane scrollPaneFees;
    private javax.swing.JScrollPane scrollPaneHistory;
    private javax.swing.JTable tblCourseFees;
    private javax.swing.JTable tblPaymentHistory;
    private javax.swing.JTextArea txtCourseworkInfo;
    // End of variables declaration//GEN-END:variables
}
