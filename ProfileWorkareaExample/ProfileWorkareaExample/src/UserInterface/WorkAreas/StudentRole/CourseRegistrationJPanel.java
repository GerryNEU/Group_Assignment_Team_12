/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package UserInterface.WorkAreas.StudentRole;

import Business.Business;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.CourseSchedule.SeatAssignment;
import info5100.university.example.Department.Department;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Transcript;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hug0_
 */
public class CourseRegistrationJPanel extends javax.swing.JPanel {
    private Business business;
    private StudentProfile studentProfile;
    private JPanel CardSequencePanel;
    
    private static final int MAX_CREDITS_PER_SEMESTER = 8;
    private static final String CURRENT_SEMESTER = "Fall 2025";

    /**
     * Creates new form CourseRegistrationJPanel
     */
    public CourseRegistrationJPanel(Business business, StudentProfile studentProfile,JPanel cardSequencePanel) {
        this.business = business;
        this.studentProfile = studentProfile;
        this.CardSequencePanel = cardSequencePanel;
        initComponents();
        
        setupSearchComboBox();
        loadAvailableCourses();
        loadMyCourses();
        updateCreditsDisplay();
    }
    
    
     // ==================== 初始化搜索下拉菜单 ====================
    /**
     * 设置搜索类型ComboBox
     */
    private void setupSearchComboBox() {
        cmbSearchType.removeAllItems();
        cmbSearchType.addItem("Course ID");
        cmbSearchType.addItem("Teacher");
        cmbSearchType.addItem("Course Name");
    }
    
    
    
     // ==================== 加载可选课程 ====================
    /**
     * 加载Fall 2025的所有可选课程
     */
    private void loadAvailableCourses() {
        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);  // 清空表格
        
        try {
            Department dept = business.getDepartment();
            CourseSchedule schedule = dept.getCourseSchedule(CURRENT_SEMESTER);
            
            if (schedule == null) {
                System.out.println("No course schedule found for " + CURRENT_SEMESTER);
                return;
            }
            
            ArrayList<CourseOffer> offers = schedule.getSchedule();
            
            if (offers == null || offers.isEmpty()) {
                System.out.println("No course offers available");
                return;
            }
            
            // 添加每门课程到表格
            for (CourseOffer offer : offers) {
                Course course = offer.getSubjectCourse();
                FacultyProfile faculty = offer.getFacultyProfile();
                
                String courseId = course.getCourseNumber();
                String courseName = course.getName();
                String instructor = faculty != null ? faculty.getPerson().getName() : "TBA";
                int credits = course.getCredits();
                
                // 计算可用座位
                int totalSeats = offer.getSeatCount();
                int occupiedSeats = offer.getEnrolledCount();
                int availableSeats = totalSeats - occupiedSeats;
                
                String status = availableSeats > 0 ? "Open" : "Full";
                
                Object[] row = {
                    courseId,
                    courseName,
                    instructor,
                    credits,
                    availableSeats + "/" + totalSeats,
                    status
                };
                
                model.addRow(row);
            }
            
            System.out.println("Loaded " + offers.size() + " available courses");
            
        } catch (Exception e) {
            System.err.println("Error loading available courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
      // ==================== 加载已选课程 ====================
    /**
     * 加载学生已选的课程
     */
    private void loadMyCourses() {
        DefaultTableModel model = (DefaultTableModel) tblMyCourses.getModel();
        model.setRowCount(0);
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) {
                System.out.println("No course load for current semester");
                return;
            }
            
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            
            if (seats == null || seats.isEmpty()) {
                System.out.println("No courses enrolled");
                return;
            }
            
            for (SeatAssignment seat : seats) {
                Course course = seat.getAssociatedCourse();
                CourseOffer offer = seat.getCourseOffer();
                FacultyProfile faculty = offer.getFacultyProfile();
                
                String courseId = course.getCourseNumber();
                String courseName = course.getName();
                String instructor = faculty != null ? faculty.getPerson().getName() : "TBA";
                int credits = course.getCredits();
                
                Object[] row = {
                    courseId,
                    courseName,
                    instructor,
                    credits
                };
                
                model.addRow(row);
            }
            
            System.out.println("Loaded " + seats.size() + " enrolled courses");
            
        } catch (Exception e) {
            System.err.println("Error loading my courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
     // ==================== 更新学分显示 ====================
    /**
     * 更新当前学分显示
     */
    private void updateCreditsDisplay() {
        try {
            int currentCredits = calculateCurrentCredits();
            
            lblCurrentCredits.setText("Current Credits: " + currentCredits + "/" + MAX_CREDITS_PER_SEMESTER);
            
            // 根据学分设置颜色
            if (currentCredits >= MAX_CREDITS_PER_SEMESTER) {
                lblCurrentCredits.setForeground(new Color(200, 0, 0));  // 红色（已满）
            } else if (currentCredits >= 4) {
                lblCurrentCredits.setForeground(new Color(0, 100, 0));  // 绿色（有课）
            } else {
                lblCurrentCredits.setForeground(Color.BLACK);  // 黑色（正常）
            }
            
        } catch (Exception e) {
            lblCurrentCredits.setText("Current Credits: 0/" + MAX_CREDITS_PER_SEMESTER);
        }
    }
    
    
    
     /**
     * 计算当前学期学分
     */
    private int calculateCurrentCredits() {
        int total = 0;
        
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad != null) {
                ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
                
                for (SeatAssignment seat : seats) {
                    total += seat.getCreditHours();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error calculating credits: " + e.getMessage());
        }
        
        return total;
    }
    
    
    
    // ==================== 搜索功能 ====================
    /**
     * 搜索课程 - 3种方法
     */
    private void searchCourses() {
        String searchType = (String) cmbSearchType.getSelectedItem();
        String searchText = txtSearch.getText().trim();
        
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter search text!", 
                "Search Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);
        
        try {
            Department dept = business.getDepartment();
            CourseSchedule schedule = dept.getCourseSchedule(CURRENT_SEMESTER);
            
            if (schedule == null) return;
            
            ArrayList<CourseOffer> offers = schedule.getSchedule();
            int foundCount = 0;
            
            for (CourseOffer offer : offers) {
                boolean matches = false;
                
                Course course = offer.getSubjectCourse();
                FacultyProfile faculty = offer.getFacultyProfile();
                
                // ========== 3种搜索方法 ==========
                if (searchType.equals("Course ID")) {
                    // 搜索方法1：按Course ID
                    matches = course.getCourseNumber().toLowerCase().contains(searchText.toLowerCase());
                    
                } else if (searchType.equals("Teacher")) {
                    // 搜索方法2：按Teacher
                    if (faculty != null) {
                        String teacherName = faculty.getPerson().getName();
                        matches = teacherName.toLowerCase().contains(searchText.toLowerCase());
                    }
                    
                } else if (searchType.equals("Course Name")) {
                    // 搜索方法3：按Course Name
                    matches = course.getName().toLowerCase().contains(searchText.toLowerCase());
                }
                // ==================================
                
                if (matches) {
                    String courseId = course.getCourseNumber();
                    String courseName = course.getName();
                    String instructor = faculty != null ? faculty.getPerson().getName() : "TBA";
                    int credits = course.getCredits();
                    
                    int totalSeats = offer.getSeatCount();
                    int occupiedSeats = offer.getEnrolledCount();
                    int availableSeats = totalSeats - occupiedSeats;
                    String status = availableSeats > 0 ? "Open" : "Full";
                    
                    Object[] row = {
                        courseId,
                        courseName,
                        instructor,
                        credits,
                        availableSeats + "/" + totalSeats,
                        status
                    };
                    
                    model.addRow(row);
                    foundCount++;
                }
            }
            
            if (foundCount == 0) {
                JOptionPane.showMessageDialog(this,
                    "No courses found matching: " + searchText,
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Found " + foundCount + " courses");
            }
            
        } catch (Exception e) {
            System.err.println("Error searching courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    // ==================== 选课功能 ====================
    /**
     * 选课
     */
    private void enrollCourse() {
        // 1. 检查是否选中了课程
        int selectedRow = tblAvailableCourses.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course to enroll!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // 2. 获取选中课程的信息
            String courseId = (String) tblAvailableCourses.getValueAt(selectedRow, 0);
            String courseName = (String) tblAvailableCourses.getValueAt(selectedRow, 1);
            int credits = (int) tblAvailableCourses.getValueAt(selectedRow, 3);
            String status = (String) tblAvailableCourses.getValueAt(selectedRow, 5);
            
            // 3. 检查课程是否已满
            if (status.equals("Full")) {
                JOptionPane.showMessageDialog(this,
                    "This course is full!",
                    "Enrollment Failed",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 4. 检查学分限制
            int currentCredits = calculateCurrentCredits();
            
            if (currentCredits + credits > MAX_CREDITS_PER_SEMESTER) {
                JOptionPane.showMessageDialog(this,
                    "Cannot enroll! You can only take " + MAX_CREDITS_PER_SEMESTER + " credits per semester.\n" +
                    "Current credits: " + currentCredits + "\n" +
                    "Course credits: " + credits + "\n" +
                    "Total would be: " + (currentCredits + credits),
                    "Credit Limit Exceeded",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 5. 检查是否已经注册
            if (isAlreadyEnrolled(courseId)) {
                JOptionPane.showMessageDialog(this,
                    "You are already enrolled in this course!",
                    "Already Enrolled",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 6. 执行选课
            boolean success = performEnrollment(courseId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Successfully enrolled in " + courseId + " - " + courseName,
                    "Enrollment Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // 刷新显示
                loadAvailableCourses();
                loadMyCourses();
                updateCreditsDisplay();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to enroll. Please try again.",
                    "Enrollment Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("Error enrolling: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 检查是否已经注册该课程
     */
    private boolean isAlreadyEnrolled(String courseId) {
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) return false;
            
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            
            for (SeatAssignment seat : seats) {
                Course course = seat.getAssociatedCourse();
                if (course.getCourseNumber().equals(courseId)) {
                    return true;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error checking enrollment: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * 执行选课操作
     */
    private boolean performEnrollment(String courseId) {
        try {
            // 获取CourseOffer
            Department dept = business.getDepartment();
            CourseSchedule schedule = dept.getCourseSchedule(CURRENT_SEMESTER);
            CourseOffer offer = schedule.getCourseOfferByNumber(courseId);
            
            if (offer == null) {
                System.err.println("CourseOffer not found: " + courseId);
                return false;
            }
            
            // 获取或创建CourseLoad
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) {
                courseLoad = transcript.newCourseLoad(CURRENT_SEMESTER);
            }
            
            // 注册课程
            SeatAssignment seat = courseLoad.newSeatAssignment(offer);
            
            if (seat == null) {
                System.err.println("Failed to create seat assignment");
                return false;
            }
            
            System.out.println("Successfully enrolled in " + courseId);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error performing enrollment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
      // ==================== 退课功能 ====================
    /**
     * 退课
     */
    private void dropCourse() {
        // 1. 检查是否选中了课程
        int selectedRow = tblMyCourses.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a course to drop!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // 2. 获取课程信息
            String courseId = (String) tblMyCourses.getValueAt(selectedRow, 0);
            String courseName = (String) tblMyCourses.getValueAt(selectedRow, 1);
            
            // 3. 确认退课
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to drop:\n" + courseId + " - " + courseName + "?",
                "Confirm Drop",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // 4. 执行退课
            boolean success = performDrop(courseId);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Successfully dropped " + courseId,
                    "Drop Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // 刷新显示
                loadAvailableCourses();
                loadMyCourses();
                updateCreditsDisplay();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to drop course.",
                    "Drop Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("Error dropping course: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 执行退课操作
     */
    private boolean performDrop(String courseId) {
        try {
            Transcript transcript = studentProfile.getTranscript();
            CourseLoad courseLoad = transcript.getCourseLoadBySemester(CURRENT_SEMESTER);
            
            if (courseLoad == null) return false;
            
            ArrayList<SeatAssignment> seats = courseLoad.getSeatAssignments();
            
            // 找到要退的课程
            for (int i = 0; i < seats.size(); i++) {
                SeatAssignment seat = seats.get(i);
                Course course = seat.getAssociatedCourse();
                
                if (course.getCourseNumber().equals(courseId)) {
                    // 移除这门课
                    seats.remove(i);
                    System.out.println("Dropped course: " + courseId);
                    return true;
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error performing drop: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
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
        lblSearch = new javax.swing.JLabel();
        cmbSearchType = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblAvailableCourses = new javax.swing.JLabel();
        scrollPane1 = new javax.swing.JScrollPane();
        tblAvailableCourses = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblCurrentCredits = new javax.swing.JLabel();
        lblMyCourses = new javax.swing.JLabel();
        scrollPane2 = new javax.swing.JScrollPane();
        tblMyCourses = new javax.swing.JTable();
        btnEnroll = new javax.swing.JButton();
        btnDrop = new javax.swing.JButton();

        btnBack.setText("< <Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        lblTitle.setText("Course Registration — Fall 2025");

        lblSearch.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblSearch.setText("Search:");

        cmbSearchType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnShowAll.setText("Show all Courses");
        btnShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblAvailableCourses.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblAvailableCourses.setText("Available Courses ( Fall 2025 )");

        tblAvailableCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Course ID", "Course Name", "Instructor", "Credits", "Available Seats", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane1.setViewportView(tblAvailableCourses);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(339, 339, 339)
                .addComponent(lblAvailableCourses)
                .addContainerGap(344, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblAvailableCourses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCurrentCredits.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblCurrentCredits.setText("Current Credits : 0/8");

        lblMyCourses.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblMyCourses.setText("My Courses( Fall 2025 )");

        tblMyCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Course ID", "Title 2", "Instructor", "Credits"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane2.setViewportView(tblMyCourses);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 360, Short.MAX_VALUE)
                        .addComponent(lblMyCourses)
                        .addGap(207, 207, 207)
                        .addComponent(lblCurrentCredits)
                        .addGap(15, 15, 15)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMyCourses))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblCurrentCredits)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        btnEnroll.setText("Enroll");
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEnroll, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnBack)
                                    .addGap(223, 223, 223))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(lblSearch)
                                    .addGap(35, 35, 35)
                                    .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(89, 89, 89)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtSearch))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSearch)
                            .addGap(26, 26, 26)
                            .addComponent(btnShowAll))))
                .addGap(0, 65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSearch)
                    .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnShowAll))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnroll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDrop)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         ((java.awt.CardLayout) CardSequencePanel.getLayout()).show(CardSequencePanel, "StudentMenu");
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchCourses();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllActionPerformed
        // TODO add your handling code here:
        loadAvailableCourses();
    }//GEN-LAST:event_btnShowAllActionPerformed

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
        // TODO add your handling code here:
        enrollCourse();
    }//GEN-LAST:event_btnEnrollActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        // TODO add your handling code here:
        dropCourse();
    }//GEN-LAST:event_btnDropActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JComboBox<String> cmbSearchType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAvailableCourses;
    private javax.swing.JLabel lblCurrentCredits;
    private javax.swing.JLabel lblMyCourses;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JTable tblAvailableCourses;
    private javax.swing.JTable tblMyCourses;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
