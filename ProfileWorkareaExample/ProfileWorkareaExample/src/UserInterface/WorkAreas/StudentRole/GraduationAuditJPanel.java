/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.StudentRole;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Transcript;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author hug0_
 */
public class GraduationAuditJPanel extends javax.swing.JPanel {
    private StudentProfile studentProfile;
    private JPanel CardSequencePanel;
    
    private static final int TOTAL_CREDITS_REQUIRED = 32;
    private static final int CORE_CREDITS_REQUIRED = 4;
    private static final String CORE_COURSE_ID = "INFO 5100";

    /**
     * Creates new form GraduationAuditJPanel
     */
    public GraduationAuditJPanel(StudentProfile studentProfile,JPanel cardSequencePanel) {
        initComponents();
    this.studentProfile = studentProfile;
    this.CardSequencePanel = cardSequencePanel;
    
    loadGraduationStatus();  // 加载和计算毕业状态
    }
    
    // ==================== 加载毕业状态 ====================
    /**
     * 计算并显示毕业审核信息
     */
    private void loadGraduationStatus() {
        try {
            // 1. 计算学分
            int totalCredits = calculateTotalCredits();
            int coreCredits = calculateCoreCredits();
            int electiveCredits = totalCredits - coreCredits;
            
            // 2. 检查核心课程
            boolean hasCoreCourse = checkCoreCourse();
            
            // 3. 计算GPA
            double overallGPA = calculateOverallGPA();
            
            // 4. 判断是否Ready to Graduate
            boolean readyToGraduate = (totalCredits >= TOTAL_CREDITS_REQUIRED) && hasCoreCourse;
            
            // ========== 5. 更新UI ==========
            updateCoreStatus(hasCoreCourse);
            updateCreditsProgress(totalCredits, coreCredits, electiveCredits);
            updateGPA(overallGPA);
            updateGraduationStatus(readyToGraduate, totalCredits, hasCoreCourse);
            
            System.out.println("Graduation status loaded successfully");
            System.out.println("Total Credits: " + totalCredits + "/32");
            System.out.println("Core Course: " + (hasCoreCourse ? "Completed" : "Not Completed"));
            System.out.println("Ready to Graduate: " + readyToGraduate);
            
        } catch (Exception e) {
            System.err.println("Error loading graduation status: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
// ==================== 学分计算 ====================
    /**
     * 计算总学分
     */
    private int calculateTotalCredits() {
        int total = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> courses = transcript.getCourseList();
            
            for (SeatAssignment seat : courses) {
                int credits = seat.getCreditHours();
                total += credits;
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating total credits: " + e.getMessage());
        }
        
        return total;
    }
    
    
    
     
    /**
     * 计算核心课程学分
     */
    private int calculateCoreCredits() {
        int coreCredits = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> courses = transcript.getCourseList();
            
            for (SeatAssignment seat : courses) {
                Course course = seat.getAssociatedCourse();
                
                if (course.getCourseNumber().equals(CORE_COURSE_ID)) {
                    coreCredits = course.getCredits();
                    break;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating core credits: " + e.getMessage());
        }
        
        return coreCredits;
    }
    
    /**
     * 检查是否完成核心课程INFO 5100
     */
    private boolean checkCoreCourse() {
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> courses = transcript.getCourseList();
            
            for (SeatAssignment seat : courses) {
                Course course = seat.getAssociatedCourse();
                
                if (course.getCourseNumber().equals(CORE_COURSE_ID)) {
                    return true;  // 找到了INFO 5100
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error checking core course: " + e.getMessage());
        }
        
        return false;  // 没有找到
    }
    
    /**
     * 计算Overall GPA
     */
    private double calculateOverallGPA() {
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> courses = transcript.getCourseList();
            
            for (SeatAssignment seat : courses) {
                float gradeValue = seat.getGrade();
                
                if (gradeValue <= 0) {
                    continue;  // 跳过没有成绩的课程
                }
                
                int credits = seat.getCreditHours();
                
                totalQualityPoints += gradeValue * credits;
                totalCredits += credits;
            }
            
            if (totalCredits > 0) {
                return totalQualityPoints / totalCredits;
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating GPA: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // ==================== UI 更新方法 ====================
    /**
     * 更新核心课程状态
     */
    private void updateCoreStatus(boolean completed) {
        if (completed) {
            lblCoreStatus.setText("☑ INFO 5100 - Application Engineering (4 credits)");
            lblCoreResult.setText("Status: ✓ Completed");
            lblCoreResult.setForeground(new Color(0, 150, 0));  // 绿色
        } else {
            lblCoreStatus.setText("☐ INFO 5100 - Application Engineering (4 credits)");
            lblCoreResult.setText("Status: ✗ Not Completed");
            lblCoreResult.setForeground(new Color(200, 0, 0));  // 红色
        }
    }
    
    /**
     * 更新学分进度
     */
    private void updateCreditsProgress(int total, int core, int elective) {
        // 更新学分Label
        lblCoreCredits.setText(core + "/4");
        lblElectiveCredits.setText(elective + "/28");
        lblTotalCredits.setText(total + "/32");
        
        // 更新进度条
        progressBar.setValue(total);
        progressBar.setString(total + "/32 credits completed");
        
        // 更新进度文字
        lblProgressText.setText(total + "/32 credits completed");
        
        // 根据进度设置进度条颜色
        if (total >= 32) {
            progressBar.setForeground(new Color(0, 150, 0));  // 绿色（完成）
        } else if (total >= 16) {
            progressBar.setForeground(new Color(52, 152, 219));  // 蓝色（进行中）
        } else {
            progressBar.setForeground(new Color(231, 76, 60));  // 红色（刚开始）
        }
    }
    
    /**
     * 更新GPA显示
     */
    private void updateGPA(double gpa) {
        lblGPA.setText(String.format("%.2f", gpa));
        
        // 根据GPA设置颜色
        if (gpa >= 3.5) {
            lblGPA.setForeground(new Color(0, 150, 0));  // 绿色（优秀）
        } else if (gpa >= 3.0) {
            lblGPA.setForeground(new Color(52, 152, 219));  // 蓝色（良好）
        } else if (gpa >= 2.0) {
            lblGPA.setForeground(new Color(243, 156, 18));  // 橙色（及格）
        } else {
            lblGPA.setForeground(new Color(200, 0, 0));  // 红色（不及格）
        }
    }
    
    /**
     * 更新毕业状态
     */
    private void updateGraduationStatus(boolean ready, int totalCredits, boolean hasCore) {
        if (ready) {
            // ========== Ready to Graduate ==========
            lblStatus.setText("✓ READY TO GRADUATE!");
            lblStatus.setForeground(new Color(0, 150, 0));  // 绿色
            lblStatus.setFont(new Font("Arial", Font.BOLD, 22));
            
            lblStatusDetail.setText("Congratulations! You have met all requirements.");
            lblStatusDetail.setForeground(new Color(0, 100, 0));
            
        } else {
            // ========== NOT Ready ==========
            lblStatus.setText("✗ Not Ready to Graduate");
            lblStatus.setForeground(new Color(200, 0, 0));  // 红色
            lblStatus.setFont(new Font("Arial", Font.BOLD, 20));
            
            // 构建详细说明
            StringBuilder details = new StringBuilder("(");
            
            if (!hasCore) {
                details.append("Must complete INFO 5100. ");
            }
            
            int neededCredits = TOTAL_CREDITS_REQUIRED - totalCredits;
            if (neededCredits > 0) {
                details.append("Need ").append(neededCredits).append(" more credits.");
            }
            
            details.append(")");
            
            lblStatusDetail.setText(details.toString());
            lblStatusDetail.setForeground(new Color(150, 0, 0));
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
        lblTittle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblCoreTittle = new javax.swing.JLabel();
        lblCoreStatus = new javax.swing.JLabel();
        lblCoreResult = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblProgressTitle = new javax.swing.JLabel();
        lblProgressText = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        lblCoreCreditsLabel = new javax.swing.JLabel();
        lblCoreCredits = new javax.swing.JLabel();
        lblElectiveCreditsLabel = new javax.swing.JLabel();
        lblElectiveCredits = new javax.swing.JLabel();
        lblTotalCreditsLabel = new javax.swing.JLabel();
        lblTotalCredits = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblGPALabel = new javax.swing.JLabel();
        lblGPA = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblStatusTitle = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblStatusDetail = new javax.swing.JLabel();

        btnBack.setText("< < Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTittle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTittle.setText("MSIS Graduation Requirements");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCoreTittle.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCoreTittle.setText("Core Course(Required):");

        lblCoreStatus.setText("INFO 5100  - Application Engineering( 4 credits )  ");

        lblCoreResult.setText("Status : Not Completed");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblCoreTittle)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblCoreStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCoreResult)
                        .addGap(35, 35, 35))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblCoreTittle)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCoreStatus)
                    .addComponent(lblCoreResult))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblProgressTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblProgressTitle.setText("Credits Progress:");

        lblProgressText.setText("0/32 credits completed");

        progressBar.setMaximum(32);
        progressBar.setStringPainted(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblProgressTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(lblProgressText)
                .addGap(35, 35, 35))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblProgressTitle)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProgressText)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCoreCreditsLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblCoreCreditsLabel.setText("Core Credits:");

        lblCoreCredits.setText("0/4");

        lblElectiveCreditsLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblElectiveCreditsLabel.setText("Elective Credits:");

        lblElectiveCredits.setText("0/28");

        lblTotalCreditsLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblTotalCreditsLabel.setText("Total Credits:");

        lblTotalCredits.setText("0/32");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblCoreCreditsLabel)
                .addGap(18, 18, 18)
                .addComponent(lblCoreCredits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblElectiveCreditsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblElectiveCredits)
                .addGap(208, 208, 208)
                .addComponent(lblTotalCreditsLabel)
                .addGap(27, 27, 27)
                .addComponent(lblTotalCredits)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCoreCreditsLabel)
                    .addComponent(lblCoreCredits)
                    .addComponent(lblElectiveCreditsLabel)
                    .addComponent(lblElectiveCredits)
                    .addComponent(lblTotalCreditsLabel)
                    .addComponent(lblTotalCredits))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblGPALabel.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblGPALabel.setText("Overal GPA : ");

        lblGPA.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblGPALabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblGPA)
                .addGap(39, 39, 39))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGPALabel)
                    .addComponent(lblGPA))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblStatusTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblStatusTitle.setText("Graduation Status : ");

        lblStatus.setText("x Not Ready to Graduate");

        lblStatusDetail.setText("(Missing requirements)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblStatusTitle)
                .addGap(183, 183, 183)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblStatusDetail)
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatusTitle)
                    .addComponent(lblStatus)
                    .addComponent(lblStatusDetail))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(266, 266, 266)
                        .addComponent(lblTittle))
                    .addComponent(btnBack))
                .addContainerGap(88, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTittle)
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) CardSequencePanel.getLayout()).show(CardSequencePanel, "StudentMenu");
    
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblCoreCredits;
    private javax.swing.JLabel lblCoreCreditsLabel;
    private javax.swing.JLabel lblCoreResult;
    private javax.swing.JLabel lblCoreStatus;
    private javax.swing.JLabel lblCoreTittle;
    private javax.swing.JLabel lblElectiveCredits;
    private javax.swing.JLabel lblElectiveCreditsLabel;
    private javax.swing.JLabel lblGPA;
    private javax.swing.JLabel lblGPALabel;
    private javax.swing.JLabel lblProgressText;
    private javax.swing.JLabel lblProgressTitle;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusDetail;
    private javax.swing.JLabel lblStatusTitle;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JLabel lblTotalCredits;
    private javax.swing.JLabel lblTotalCreditsLabel;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
