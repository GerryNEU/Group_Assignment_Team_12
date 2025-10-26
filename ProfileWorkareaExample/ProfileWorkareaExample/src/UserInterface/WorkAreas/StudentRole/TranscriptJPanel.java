/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.StudentRole;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Transcript;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hug0_
 */
public class TranscriptJPanel extends javax.swing.JPanel {
    private StudentProfile studentProfile;
    private JPanel CardSequencePanel;
    private HashMap<String, Double> gradeMap;

    /**
     * Creates new form TranscriptJPanel
     */
    public TranscriptJPanel(StudentProfile studentProfile, JPanel cardSequencePanel) {
        initComponents();
        this.studentProfile = studentProfile;
        this.CardSequencePanel = cardSequencePanel;
     
        loadSemesters();   // 加载学期列表到ComboBox
        loadTranscript();  // 加载成绩单数据
    }
    
    // ==================== 加载学期列表 ====================
    /**
     * 加载学期到ComboBox
     */
   private void loadSemesters() {
        cbxSelectSemster.removeAllItems();
        cbxSelectSemster.addItem("All Semesters");
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            
            // 直接访问courseloadlist的keys（这些就是学期名称）
            // 通过getCourseLoadBySemester方法来推断有哪些学期
            // 或者遍历getCourseList()来收集学期
            
            ArrayList<SeatAssignment> allCourses = transcript.getCourseList();
            HashSet<String> semesters = new HashSet<>();
            
            for (SeatAssignment seat : allCourses) {
                CourseLoad courseLoad = seat.getCourseLoad();  // 直接访问属性
                if (courseLoad != null) {
                    String semester = courseLoad.getTerm();  // 直接访问semester属性
                    semesters.add(semester);
                }
            }
            
            for (String term : semesters) {
                cbxSelectSemster.addItem(term);
            }
            
            System.out.println("Loaded " + semesters.size() + " semesters");
            
        } catch (Exception e) {
            System.err.println("Error loading semesters: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
     // ==================== 加载成绩单 ====================
    /**
     * 加载成绩单数据到表格
     */
    private void loadTranscript() {
        DefaultTableModel model = (DefaultTableModel) tblTranscriptTable.getModel();
        model.setRowCount(0);
        
        try {
            String selectedSemester = (String) cbxSelectSemster.getSelectedItem();
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> allCourses = transcript.getCourseList();
            
            if (allCourses == null || allCourses.isEmpty()) {
                System.out.println("No courses found");
                return;
            }
            
            // 计算Overall GPA
            double overallGPA = calculateOverallGPA();
            
            // 按学期分组
            HashMap<String, ArrayList<SeatAssignment>> coursesBySemester = new HashMap<>();
            
            for (SeatAssignment seat : allCourses) {
                CourseLoad courseLoad = seat.getCourseLoad();
                if (courseLoad == null) continue;
                
                String term = courseLoad.getTerm();  // 直接访问semester属性
               
                if (!coursesBySemester.containsKey(term)) {
                    coursesBySemester.put(term, new ArrayList<>());
                }
                coursesBySemester.get(term).add(seat);
            }
            
            // 遍历每个学期
            for (String term : coursesBySemester.keySet()) {
                // 过滤学期
                if (selectedSemester != null && 
                    !selectedSemester.equals("All Semesters") && 
                    !term.equals(selectedSemester)) {
                    continue;
                }
                
                ArrayList<SeatAssignment> semesterCourses = coursesBySemester.get(term);
                double termGPA = calculateTermGPA(semesterCourses);
                String academicStanding = determineAcademicStanding(termGPA, overallGPA);
                
                // 添加每门课程到表格
                for (SeatAssignment seat : semesterCourses) {
                    CourseOffer offer = seat.getCourseOffer();
                    Course course = seat.getAssociatedCourse();  // 使用getAssociatedCourse()
                    
                    String courseId = course.getCourseNumber();
                    String courseName = course.getName();
                    
                    // getGrade()返回float，需要转换成字母成绩
                    float gradeValue = seat.getGrade();
                    String letterGrade = convertGradeToLetter(gradeValue);
                    
                    Object[] row = {
                        term,
                        academicStanding,
                        courseId,
                        courseName,
                        letterGrade,
                        String.format("%.2f", termGPA),
                        String.format("%.2f", overallGPA)
                    };
                    
                    model.addRow(row);
                }
            }
            
            System.out.println("Transcript loaded: " + model.getRowCount() + " courses");
            
        } catch (Exception e) {
            System.err.println("Error loading transcript: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // ==================== 成绩转换 ====================
    /**
     * 将数字成绩转换为字母成绩
     * float (4.0, 3.7, etc.) → String ("A", "A-", etc.)
     */
    private String convertGradeToLetter(float gradeValue) {
        if (gradeValue >= 4.0) return "A";
        if (gradeValue >= 3.7) return "A-";
        if (gradeValue >= 3.3) return "B+";
        if (gradeValue >= 3.0) return "B";
        if (gradeValue >= 2.7) return "B-";
        if (gradeValue >= 2.3) return "C+";
        if (gradeValue >= 2.0) return "C";
        if (gradeValue >= 1.7) return "C-";
        if (gradeValue > 0) return "F";
        return "N/A";  // 0.0 或没有成绩
    }
    
    // ==================== GPA 计算 ====================
    /**
     * 计算学期GPA
     */
   private double calculateTermGPA(ArrayList<SeatAssignment> seats) {
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        for (SeatAssignment seat : seats) {
            float gradeValue = seat.getGrade();  // 直接是GPA值
            
            if (gradeValue <= 0) {
                continue;  // 跳过没有成绩的课程
            }
            
            int credits = seat.getCreditHours();
            
            totalQualityPoints += gradeValue * credits;
            totalCredits += credits;
        }
        
        return totalCredits > 0 ? totalQualityPoints / totalCredits : 0.0;
    }
    
    /**
     * 计算总GPA（所有学期）
     */
    private double calculateOverallGPA() {
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<SeatAssignment> allCourses = transcript.getCourseList();
            
            for (SeatAssignment seat : allCourses) {
                float gradeValue = seat.getGrade();
                
                if (gradeValue <= 0) {
                    continue;
                }
                
                int credits = seat.getCreditHours();
                
                totalQualityPoints += gradeValue * credits;
                totalCredits += credits;
            }
            
            return totalCredits > 0 ? totalQualityPoints / totalCredits : 0.0;
            
        } catch (Exception e) {
            System.err.println("Error calculating overall GPA: " + e.getMessage());
            return 0.0;
        }
    }
    
    // ==================== Academic Standing 判定 ====================
    /**
     * 判定学业状态
     * 
     * 规则：
     * - Good Standing: Term GPA ≥ 3.0 AND Overall GPA ≥ 3.0
     * - Academic Warning: Term GPA < 3.0 (even if Overall GPA ≥ 3.0)
     * - Academic Probation: Overall GPA < 3.0 (regardless of Term GPA)
     */
    private String determineAcademicStanding(double termGPA, double overallGPA) {
        if (termGPA >= 3.0 && overallGPA >= 3.0) {
            return "Good Standing";
        } else if (termGPA < 3.0) {
            return "Academic Warning";
        } else if (overallGPA < 3.0) {
            return "Academic Probation";
        }
        return "Unknown";
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTittle = new javax.swing.JLabel();
        lblSelectSemster = new javax.swing.JLabel();
        cbxSelectSemster = new javax.swing.JComboBox<>();
        btnBack = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTranscriptTable = new javax.swing.JTable();

        lblTittle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTittle.setText(" My Transcript");

        lblSelectSemster.setText("Select semster:");

        cbxSelectSemster.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBack.setText("< < Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        tblTranscriptTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Term", "Academic Standing", "Course Name", "Grade", "Term GPA", "Overall GPA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblTranscriptTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSelectSemster)
                            .addComponent(btnBack))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTittle)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cbxSelectSemster, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(442, 442, 442)
                                .addComponent(btnRefresh)))))
                .addContainerGap(164, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTittle)
                    .addComponent(btnBack))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSelectSemster)
                    .addComponent(cbxSelectSemster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) CardSequencePanel.getLayout()).show(CardSequencePanel, "StudentMenu");
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
         loadTranscript();
        JOptionPane.showMessageDialog(this,
            "Transcript refreshed!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 学期选择ComboBox - 按学期过滤
     */
    private void cbxSelectSemsterActionPerformed(java.awt.event.ActionEvent evt) {
        // 当选择不同学期时，重新加载成绩单
        loadTranscript();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbxSelectSemster;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSelectSemster;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JTable tblTranscriptTable;
    // End of variables declaration//GEN-END:variables
}
