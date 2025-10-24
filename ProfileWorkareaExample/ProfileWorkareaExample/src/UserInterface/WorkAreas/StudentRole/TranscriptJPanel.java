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
        
        initGradeMap();    // 初始化成绩映射
        loadSemesters();   // 加载学期列表到ComboBox
        loadTranscript();  // 加载成绩单数据
    }
    
    // ==================== 初始化成绩映射表 ====================
    /**
     * 创建成绩到GPA分数的映射
     */
    private void initGradeMap() {
        gradeMap = new HashMap<>();
        gradeMap.put("A", 4.0);
        gradeMap.put("A-", 3.7);
        gradeMap.put("B+", 3.3);
        gradeMap.put("B", 3.0);
        gradeMap.put("B-", 2.7);
        gradeMap.put("C+", 2.3);
        gradeMap.put("C", 2.0);
        gradeMap.put("C-", 1.7);
        gradeMap.put("F", 0.0);
    }
    
    // ==================== 加载学期列表 ====================
    /**
     * 加载学期到ComboBox
     */
    private void loadSemesters() {
        cbxSelectSemster.removeAllItems();
        
        // 添加"All Semesters"选项
        cbxSelectSemster.addItem("All Semesters");
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<CourseLoad> courseLoads = transcript.getCourseLoadList();
            
            // 添加所有学期
            for (CourseLoad load : courseLoads) {
                String term = load.getTerm();
                cbxSelectSemster.addItem(term);
            }
            
            System.out.println("Loaded " + courseLoads.size() + " semesters");
            
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
        // 获取表格模型
        DefaultTableModel model = (DefaultTableModel) tblTranscriptTable.getModel();
        model.setRowCount(0);  // 清空表格
        
        try {
            // 获取选中的学期
            String selectedSemester = (String) cbxSelectSemster.getSelectedItem();
            
            // 获取成绩单
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<CourseLoad> courseLoads = transcript.getCourseLoadList();
            
            // 计算Overall GPA（用于所有行）
            double overallGPA = calculateOverallGPA();
            
            // 遍历每个学期
            for (CourseLoad load : courseLoads) {
                String term = load.getTerm();
                
                // 如果选择了特定学期，只显示该学期
                if (selectedSemester != null && 
                    !selectedSemester.equals("All Semesters") && 
                    !term.equals(selectedSemester)) {
                    continue;  // 跳过其他学期
                }
                
                // 计算该学期的GPA
                double termGPA = calculateTermGPA(load);
                
                // 判断Academic Standing
                String academicStanding = determineAcademicStanding(termGPA, overallGPA);
                
                // 获取该学期的所有课程
                ArrayList<SeatAssignment> seats = load.getSeatAssignments();
                
                if (seats == null || seats.isEmpty()) {
                    System.out.println("No courses in term: " + term);
                    continue;
                }
                
                // 遍历每门课程
                for (SeatAssignment seat : seats) {
                    CourseOffer offer = seat.getCourseOffer();
                    Course course = offer.getCoursesubject();
                    
                    String courseId = course.getCourseNumber();    // "INFO 5100"
                    String courseName = course.getName();          // "Application Engineering"
                    String grade = seat.getGrade();                // "A"
                    
                    // 如果没有成绩，显示"N/A"
                    if (grade == null || grade.isEmpty()) {
                        grade = "N/A";
                    }
                    
                    // 格式化GPA（保留2位小数）
                    String termGPAStr = String.format("%.2f", termGPA);
                    String overallGPAStr = String.format("%.2f", overallGPA);
                    
                    // 添加到表格
                    Object[] row = {
                        term,
                        academicStanding,
                        courseId + " - " + courseName,  // 合并Course ID和Name
                        grade,
                        termGPAStr,
                        overallGPAStr
                    };
                    
                    model.addRow(row);
                }
            }
            
            System.out.println("Transcript loaded: " + model.getRowCount() + " courses");
            
        } catch (Exception e) {
            System.err.println("Error loading transcript: " + e.getMessage());
            e.printStackTrace();
            
            // 显示错误消息
            JOptionPane.showMessageDialog(this,
                "Error loading transcript: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== GPA 计算 ====================
    /**
     * 计算学期GPA
     */
    private double calculateTermGPA(CourseLoad courseLoad) {
        double totalQualityPoints = 0.0;  // 总质量分
        int totalCredits = 0;              // 总学分
        
        try {
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            
            for (SeatAssignment seat : seats) {
                // 获取成绩
                String grade = seat.getGrade();
                
                // 如果没有成绩，跳过
                if (grade == null || grade.isEmpty() || !gradeMap.containsKey(grade)) {
                    continue;
                }
                
                // 获取学分
                Course course = seat.getCourseOffer().getCoursesubject();
                int credits = course.getCredits();
                
                // 获取成绩分数
                double gradePoints = gradeMap.get(grade);
                
                // 累加质量分
                totalQualityPoints += gradePoints * credits;
                totalCredits += credits;
            }
            
            // 计算GPA
            if (totalCredits > 0) {
                return totalQualityPoints / totalCredits;
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating term GPA: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    /**
     * 计算总GPA（所有学期）
     */
    private double calculateOverallGPA() {
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            ArrayList<CourseLoad> courseLoads = transcript.getCourseLoadList();
            
            // 遍历所有学期
            for (CourseLoad load : courseLoads) {
                ArrayList<SeatAssignment> seats = load.getSeatAssignments();
                
                for (SeatAssignment seat : seats) {
                    String grade = seat.getGrade();
                    
                    if (grade == null || grade.isEmpty() || !gradeMap.containsKey(grade)) {
                        continue;
                    }
                    
                    Course course = seat.getCourseOffer().getCoursesubject();
                    int credits = course.getCredits();
                    double gradePoints = gradeMap.get(grade);
                    
                    totalQualityPoints += gradePoints * credits;
                    totalCredits += credits;
                }
            }
            
            if (totalCredits > 0) {
                return totalQualityPoints / totalCredits;
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating overall GPA: " + e.getMessage());
        }
        
        return 0.0;
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

        btnRefresh.setText("Refresh");

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
                .addContainerGap(64, Short.MAX_VALUE))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cbxSelectSemster;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblSelectSemster;
    private javax.swing.JLabel lblTittle;
    private javax.swing.JTable tblTranscriptTable;
    // End of variables declaration//GEN-END:variables
}
