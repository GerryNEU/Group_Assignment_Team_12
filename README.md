# Group Assignment 1: Profile Workarea Example - Digital University

## 1. Project Title
Profile Workarea Example - Implementing Access-Controlled Use Cases in a Digital University System

## 2. Team Information
* **Team:** Team 12
* **Members & Roles:**
    * [Guochang Su] - Role: [Administer], Responsibilities: [User Account Management, Person Management, Student&Faculty Management, Manager Profile, Analytics Dashboard], NUID: [002521648]
    * [Member Name 2] - Role: [Role 2 assigned from PDF], Responsibilities: [Specific features implemented], NUID: [NUID 2]
    * [Member Name 3] - Role: [Role 3 assigned from PDF], Responsibilities: [Specific features implemented], NUID: [NUID 3]
    * [Member Name 4 (if applicable)] - Role: [Role 4 assigned from PDF], Responsibilities: [Specific features implemented], NUID: [NUID 4]

---

## 3. Project Overview
**Purpose & Objectives:**
This project integrates a reference implementation of a Digital University System with an Access Control Layer. The primary objective is to enable proper user authentication and implement role-based access control for different university stakeholders (Admin, Faculty, Student, Registrar). Each role has a dedicated use case with specific functionalities reflecting their responsibilities within the university system.

**Key Features Implemented:**
* **Role-Based Access Control:** Secure login and distinct interfaces/permissions for Admin, Faculty, Student, and Registrar roles.
* **User & Personnel Management (Admin):** Creation, modification, and deletion of user accounts and person records (students, faculty, staff) with validation checks.
* **Academic Records Management (Admin):** Viewing, editing, deleting, and searching student/faculty records; assigning faculty to courses/departments.
* **Course Management (Faculty):** Viewing assigned courses, updating details, managing syllabi, and controlling enrollment status.
* **Student Management (Faculty):** Grading assignments, viewing enrolled students, accessing transcripts, ranking students, and calculating class GPA.
* **Course Registration & Academics (Student):** Searching/registering for courses within credit limits, dropping courses, viewing transcripts with GPA/standing, tracking graduation progress, and managing tuition payments.
* **Course Offering Management (Registrar):** Creating and managing course offerings per semester, including assigning faculty and setting capacity.
* **Profile Management:** All roles can view and update their personal profile information.
* **Analytics & Reporting:** Admins view a dashboard with user/course/enrollment/revenue summaries. Faculty generate course performance reports. Registrar generates institutional reports.

---

## 4. Installation & Setup Instructions

**Prerequisites:**
* **Java Development Kit (JDK):** Version 17 or higher (as specified in `project.properties`).
* **IDE:** Apache NetBeans IDE (Version 16 or compatible recommended, project uses NetBeans GUI builder and project structure).
* **Libraries:** Apache NetBeans AbsoluteLayout library (included in the `lib` folder).

**Setup Instructions:**
1.  **Clone Repository:** Clone the `Group_Assignment_Team_12` repository to your local machine using Git.
    ```bash
    git clone [repository URL]
    ```
    *(Replace `[repository URL]` with the actual URL)*
2.  **Open in NetBeans:**
    * Launch NetBeans IDE.
    * Go to `File` > `Open Project`.
    * Navigate to the directory where you cloned the repository.
    * Select the `ProfileWorkareaExample` folder within the repository.
    * Click `Open Project`.
3.  **Build Project:**
    * In the NetBeans `Projects` window, right-click on the `ProfileWorkareaExample` project.
    * Select `Clean and Build`. Wait for the build process to complete successfully.
4.  **Run Project:**
    * Right-click on the `ProfileWorkareaExample` project again.
    * Select `Run`. This will compile and launch the application, displaying the main login window (`ProfileWorkAreaMainFrame`).

---

## 5. Authentication & Access Control

* **Authentication Process:**
    * Users initiate login through the `ProfileWorkAreaMainFrame` interface by entering their **Username** and **Password**.
    * The `LoginButtonActionPerformed` method retrieves these credentials.
    * It calls the `AuthenticateUser(un, pw)` method of the `UserAccountDirectory` (within the `Business` object).
    * This method iterates through the `userAccountList`, checking for a match using `ua.IsValidUser(un, pw)`.
    * If a match is found, the corresponding `UserAccount` object is returned; otherwise, `null` is returned, and login fails.
    * Logout is implicitly handled by closing the application or could be added with a dedicated logout button returning to the login screen.

* **Authorization Rules:**
    * Upon successful authentication, the system retrieves the user's **role** from the `UserAccount` object (e.g., "Admin", "Faculty", "Student", "Registrar").
    * Based on this role string:
        * **Admin:** Gains access to `AdminRoleWorkAreaJPanel`, enabling user/personnel management, records administration, analytics, and profile editing.
        * **Faculty:** Gains access to `FacultyWorkAreaJPanel`, enabling management of assigned courses, student grading, performance reporting, profile editing, and tuition insights.
        * **Student:** Gains access to `StudentWorkAreaJPanel`, enabling course registration, coursework interaction, graduation audit checks, transcript viewing, profile editing, and financial management.
        * **Registrar:** Gains access to `RegistrarWorkAreaJPanel`, primarily enabling course offering management. (Other features might be limited based on implementation).
    * The `CardLayout` (`CardSequencePanel`) is used to display only the appropriate work area panel for the logged-in user's role, effectively restricting access to other roles' functionalities.

---

## 6. Features Implemented

*(Note: Assign specific team members clearly for each main feature/responsibility as required by the grading criteria)*

**Admin Role** (`AdminRoleWorkAreaJPanel`): *(Implemented by: [Guochang Su])*
* **User Account Management** (`ManageUserAccountsJPanel`): Create, view/edit, delete user accounts. Assigns roles based on person's profile. Requires `AdminUserAccount.java`.
* **Person Registration (HR)** (`ManagePersonsJPanel`): Add new persons (Student/Faculty), prevents duplicates by email, auto-generates sequential ID (S###/F###), view/edit basic info, delete. Requires `AdministerPersonJPanel.java`.
* **Student and Faculty Records Management** (`ManageRecordsJPanel`): Tabbed view for Students and Faculty. Allows viewing details, searching by ID, Name, Dept (3 methods implemented), editing contact info (`ViewEditStudentJPanel`, `ViewEditFacultyJPanel`), deleting records, and assigning faculty to courses (`AssignFacultyJPanel`).
* **My Profile** (`AdminManageProfileJPanel`): Allows logged-in admin to view/edit their own name, email, phone.
* **Analytics Dashboard** (`AnalyticsDashboardJPanel`): Displays summary tables for: Users by Role, Courses per Semester, Enrollment per Course, Total Tuition Revenue.

**Faculty Role** (`FacultyWorkAreaJPanel`): *(Implemented by: [Member Name(s)])*
* **Manage Courses** (`ManageCoursesJPanel`): View assigned courses (Fall 2025 hardcoded). Update course name, seats, schedule. Upload/Modify syllabus (via file chooser or text input). Toggle enrollment open/closed.
* **Manage Students Profiles** (`ManageStudentsJPanel`): Select course via dropdown. View enrolled students. Select student and update grade (0-100 score converted to GPA/Letter). View student transcript summary (`ViewTranscriptJPanel`). Rank students by GPA and show class average GPA.
* **My Profile** (`ManageProfileJPanel`): View and update own profile: Name, Office, Phone, Bio.
* **Performance Reports** (`PerformanceReportsJPanel`): Filter courses by semester. View report for selected course including Average Grade, Enrollment Count, Grade Distribution (A, B, C, D, F). Export report to CSV.
* **Tuition Insight** (`TuitionInsightJPanel`): View table showing assigned courses, enrolled count, tuition per student, and total tuition generated per course. Displays overall total tuition.

**Student Role** (`StudentWorkAreaJPanel`): *(Implemented by: [Member Name(s)])*
* **Course Work & Financial Management** (`CourseworkJPanel`): Displays current tuition balance based on enrolled credits. Shows course fee breakdown. Allows paying tuition (simulated, updates balance and history). Tracks and displays payment history. (Assignment submission is a placeholder `JTextArea`).
* **Manage Profile** (`ProfileJPanel`): View and Edit Name, Email, Phone number with basic validation.
* **Graduation Audit** (`GraduationAuditJPanel`): Calculates and displays progress towards 32-credit requirement. Checks for core course INFO 5100 completion. Calculates and displays Overall GPA. Shows "Ready to Graduate" status based on credits and core course.
* **Registration** (`CourseRegistrationJPanel`): Search available courses by Course ID, Teacher, or Course Name (3 methods). Displays results in table showing ID, Name, Instructor, Credits, Seats (Available/Total), Status (Open/Full). Allows enrolling if seats available and within 8-credit limit. Displays "My Courses" for the current semester. Allows dropping enrolled courses.
* **Transcript** (`TranscriptJPanel`): Displays full academic history. Allows filtering by semester via dropdown. Shows Term, Academic Standing (Good, Warning, Probation based on Term/Overall GPA), Course ID, Name, Letter Grade, Term GPA, and Overall GPA. GPA calculation follows standard rules (Grade Points * Credits).

**Registrar Role** (`RegistrarWorkAreaJPanel`): *(Implemented by: [Member Name(s)])*
* **Course Offering Management** (`ManageCourseOfferingsJPanel`): View list of course offerings for the current semester (Fall 2025). (Create, Edit, Delete buttons are present but may have limited functionality).
* *(Other buttons are placeholders as per the UI design)*.

---

## 7. Usage Instructions

1.  **Launch & Login:**
    * Run the application (`ProfileWorkAreaMainFrame`).
    * Enter the username and password for the desired role (see credentials in Section 8). Click `Login`.
2.  **Admin Workflow:**
    * **Add Person:** Click `Register Persons (HR)` > `Add New Person`. Enter Name, Email, select Role (Student/Faculty). Click `Save`. Note the generated ID.
    * **Create Account:** Click `Administer User Accounts` > `Create`. Select the newly added person from the dropdown. Enter Username, Password, Confirm Password. Role is auto-filled. Click `Save`.
    * **Manage Records:** Click `Manage Faculty & Student Records`. Use search fields/buttons (by ID, Name, Dept). Select a record, click `View / Edit Details` to update email/phone, or click `Delete Record`. To assign faculty, select faculty, click `Assign to Course/Dept`, choose a course, click `Confirm Assignment`. Use `Refresh Table` after searching/deleting.
    * **View Analytics:** Click `Analytics Dashboard`. Switch between tabs (Users by Role, Courses per Semester, Enrollment per Course, Tuition Revenue) to view summary data.
3.  **Faculty Workflow:**
    * **Manage Courses:** Click `Manage Courses`. View assigned courses. Select a row and modify Name, Seats, or Schedule in the table. Click `Save`. Click `Syllabus` to upload/enter text. Click `ToggleEnrollment` to open/close registration.
    * **Grade Students:** Click `Manage Students Profiles`. Select a course from the dropdown. Select a student row. Enter a score (0-100) in the text field below table. Click `Update Grade`. Click `Rank Students` to sort and see class GPA. Click `ViewTranscript` for a student's summary.
    * **Check Reports:** Click `Performance Reports`. Select Semester/Course. View metrics. Click `Export CSV` to save the report.
    * **View Tuition:** Click `Tuition Insight` to see revenue generated by courses.
4.  **Student Workflow:**
    * **Register:** Click `Registration`. Use the search bar and type dropdown, click `Search`, or click `Show all Courses`. Select a course from "Available Courses" table, click `Enroll`. Check "My Courses" table and credit count. Select a course in "My Courses", click `Drop`.
    * **View Academics:** Click `Transcript`. View all courses/grades. Use dropdown to select a specific semester. Note GPA and Academic Standing.
    * **Check Graduation:** Click `Graduation Audit`. Review credit progress, core course status, GPA, and overall readiness.
    * **Pay Tuition:** Click `Course Work`. View balance and fee breakdown. Click `Pay Tuition`, confirm amount, select method. View updated balance and payment history.
5.  **Registrar Workflow:**
    * Click `Course Offering Management`. View the list of courses for Fall 2025. (Further actions like Create/Edit depend on implementation).

---

## 8. Testing Guide

* **Authentication & Authorization Verification:**
    * Log in using credentials for each role (Admin: `admin`/`****`, Faculty: `prof1`/`1234`, Student: `student1`/`****`, Registrar: `registrar`/`regpass`). Verify the correct main menu/work area appears.
    * Attempt invalid logins (wrong password, wrong username). Verify login fails with no access granted.
    * Log in as Student. Ensure Admin/Faculty/Registrar specific buttons (e.g., "Administer User Accounts", "Manage Courses", "Course Offering Management") are *not* visible or accessible. Repeat for Faculty (ensure Admin/Student buttons are inaccessible).
* **Sample Test Cases:**
    * **Admin - User Cycle:**
        1. Login as `admin`.
        2. Go to `Register Persons (HR)` > `Add New Person`. Add "Test Student", email "test@uni.edu", role Student. Click Save. Note the generated ID (e.g., S021).
        3. Go to `Administer User Accounts` > `Create`. Select "Test Student (S021)" from dropdown. Set username "teststudent", password "testpass". Click Save.
        4. Logout/Relaunch application. Login as `teststudent`/`testpass`. Verify Student Work Area appears.
        5. Login as `admin`. Go to `Manage Faculty & Student Records` > Student tab. Search for ID "S021". Select the row, click `Delete Record`. Confirm.
        6. Logout/Relaunch. Attempt login as `teststudent`/`testpass`. Verify login fails.
    * **Student - Registration & Academics:**
        1. Login as `student1`/`****`.
        2. Go to `Registration`. Search for Course ID "INFO 5100". Select it, click `Enroll`. Search for "INFO 6100", select, click `Enroll`. Verify "My Courses" shows both and Credits is 8/8.
        3. Attempt to enroll in "INFO 6210". Verify credit limit error message.
        4. Go to `Transcript`. Verify both courses appear with "N/A" or initial grades. Check Term/Overall GPA.
        5. Go to `Graduation Audit`. Verify 8 credits completed, core course shown (may be incomplete if no grade yet), GPA updated.
        6. Go back to `Registration`. Select "INFO 6100" in "My Courses". Click `Drop`. Confirm. Verify course removed and credits updated to 4/8.
    * **Faculty - Grading & Reporting:**
        1. Login as `prof1`/`1234` (Kal Faculty).
        2. Go to `Manage Students Profiles`. Select course "INFO 5100" from dropdown.
        3. Select "Student 1" (S001). Enter "95" in grade field below table. Click `Update Grade`. Verify table shows Score 95.0, GPA 4.0, Letter A.
        4. Select "Student 2" (S002). Enter "88". Click `Update Grade`. Verify Score 88.0, GPA 3.3, Letter B+.
        5. Click `Rank Students`. Verify students are sorted by GPA, and Class GPA is calculated.
        6. Go to `Performance Reports`. Select Semester "Fall 2025", Course "INFO 5100". Verify report shows Avg Grade, Enrollment, Grade Distribution (1 A, 1 B+ etc.). Click `Export CSV`. Choose location, save. Verify CSV file contents.
    * **Student - Financials:**
        1. Login as `student1`/`****` (who has 4 credits enrolled).
        2. Go to `Course Work`. Verify balance shows $6000 (4 * 1500). Verify Course Fees table shows INFO 5100 with $6000.
        3. Click `Pay Tuition`. Confirm payment. Select "Credit Card". Verify success message.
        4. Verify Balance shows "$0.00 (Paid)" in green. Verify Payment History table shows the transaction. Verify `Pay Tuition` button is disabled.
* **Input Validation:**
    * Try creating Persons/Users with empty fields, invalid emails, short passwords. Verify error messages appear and operation fails.
    * Try entering non-numeric grades as Faculty. Verify error.
    * Try searching with empty search terms. Verify prompts appear.

---

## 9. Challenges & Solutions

* **Challenge:** Implementing Role-Based Access Control cleanly.
    * **Solution:** Utilized the provided Access Control package. Login determines the role string. A `CardLayout` in the main frame switches visibility to the correct role's primary `JPanel`, inherently restricting access to UI elements of other roles.
* **Challenge:** Ensuring data consistency across different panels and roles (e.g., Admin deletes a user, Student logs in).
    * **Solution:** Passed the central `Business` object containing directories (`UserAccountDirectory`, `PersonDirectory`, etc.) to necessary panels. Implemented refresh mechanisms (`populateTable`, `loadData`) called upon returning to list views or after data modifications to reflect changes.
* **Challenge:** Correctly calculating GPA, academic standing, and graduation requirements based on specific rules.
    * **Solution:** Encapsulated calculation logic within model classes (`Transcript.java` for GPA/Standing, `GraduationAuditJPanel.java` for graduation checks) to keep UI classes cleaner and ensure rules are applied consistently. Used standard GPA calculation formulas.
* **Challenge:** Implementing multiple search methods for Student/Faculty records and Course Registration.
    * **Solution:** Created separate button actions for each search type (ID, Name, Dept). Each action calls a specific search method in the relevant directory (`StudentDirectory`, `FacultyDirectory`, or iterates `CourseSchedule`) and refreshes the JTable with the filtered results using an overloaded `populateTable` method.
* **Challenge:** Managing state for editing operations (e.g., Profile editing).
    * **Solution:** Implemented `setViewMode()` and `setEditMode()` methods in profile panels (`ProfileJPanel`, `AdminManageProfileJPanel`, `ManageProfileJPanel`) to toggle field editability and button states (Edit, Save, Cancel). Stored original values before editing to allow cancellation.
* **Challenge:** Generating unique but sequential IDs for new Persons (S###, F###).
    * **Solution:** Implemented `generateNextPersonId` helper method in `AdministerPersonJPanel` which iterates through existing Person IDs with the same prefix (e.g., "S"), finds the highest number, increments it, and formats it with leading zeros.

---

## 10. Future Enhancements

* **Database Integration:** Replace in-memory ArrayLists with a database (e.g., MySQL, PostgreSQL) for data persistence.
* **Enhanced Registrar Role:** Fully implement course offering creation/editing, student enrollment/dropping from admin side, financial reporting, and institutional analytics.
* **Coursework Implementation:** Develop the assignment submission and detailed progress tracking for students.
* **Improved Faculty Grading:** Allow grading of specific assignments, weighted averages for final grades, and feedback mechanisms.
* **Notifications:** Implement a system for notifications (e.g., new grades available, tuition due, enrollment changes).
* **Detailed Financial Module:** Add features like scholarships, financial aid, different payment plans, and integration with external payment gateways.
* **Security Enhancements:** Implement more robust password hashing, session management, and potentially finer-grained permissions.
* **UI/UX Refinement:** Improve visual design, layout consistency, and user experience across all panels. Add features like sorting/filtering directly in tables.
* **Testing:** Introduce automated unit tests (JUnit) and potentially integration tests to ensure code quality and prevent regressions.
* **Internationalization (I18N):** Support for multiple languages in the user interface.

---

## 11. Contribution Breakdown

*(Please provide specific details for each member, following the structure below)*

* **[Guochang Su] (NUID: [002521648]):**
    * **Assigned Use Case:** [Admin]
    * **Coding:** Implemented [User Account Management, Person Management, Student&Faculty Management, Manager Profile, Analytics Dashboard]. Developed/Modified classes: [All Classes under Admin, and some of students, faculties, directions]. Addressed [5] Pull Request reviews.
    * **Documentation:** Authored README sections: [Project Overview, Installation, Usage Instructions]. Added code comments for [All Classes under Admin, and some of students, faculties, directions].
    * **Testing:** Performed testing for [Admin login, User creation/deletion, Records Part, Person creatrion/deletion, Assgin departments to faculties]. Verified authentication/authorization for Admin role.
    * **Commits/PRs:** Contributed [47] meaningful commits to personal branch, created [15] Pull Requests to main.
* **[Member Name 2] (NUID: [NUID 2]):**
    * **Assigned Use Case:** [Admin/Faculty/Student/Registrar]
    * **Coding:** Implemented [List specific features like Course Management, Student Grading, Performance Reports export, ManageStudentsJPanel ranking...]. Developed/Modified classes: [`ClassC.java`, `ClassD.java`...]. Addressed [Number] Pull Request reviews.
    * **Documentation:** Authored README sections: [e.g., Features Implemented (Faculty), Usage Instructions]. Added code comments for [Specific classes/methods].
    * **Testing:** Performed testing for [Faculty login, Grading functionality, Report generation...]. Verified authentication/authorization for Faculty role.
    * **Commits/PRs:** Contributed [Number] meaningful commits to personal branch, created [Number] Pull Requests to main.
* **[Member Name 3] (NUID: [NUID 3]):**
    * **Assigned Use Case:** [Admin/Faculty/Student/Registrar]
    * **Coding:** Implemented [List specific features like Course Registration, Graduation Audit, Transcript view, Financial Management panel...]. Developed/Modified classes: [`ClassE.java`, `ClassF.java`...]. Addressed [Number] Pull Request reviews.
    * **Documentation:** Authored README sections: [e.g., Authentication, Testing Guide]. Added code comments for [Specific classes/methods].
    * **Testing:** Performed testing for [Student login, Course enrollment/drop, GPA calculation, Tuition payment...]. Verified authentication/authorization for Student role.
    * **Commits/PRs:** Contributed [Number] meaningful commits to personal branch, created [Number] Pull Requests to main.
* **[Member Name 4 (if applicable)] (NUID: [NUID 4]):**
    * **Assigned Use Case:** [Admin/Faculty/Student/Registrar]
    * **Coding:** Implemented [List specific features like Registrar's Course Offering Management, Student Registration Admin-side...]. Developed/Modified classes: [`ClassG.java`, `ClassH.java`...]. Addressed [Number] Pull Request reviews.
    * **Documentation:** Authored README sections: [e.g., Challenges & Solutions, Future Enhancements]. Added code comments for [Specific classes/methods].
    * **Testing:** Performed testing for [Registrar login, Course Offering display...]. Verified authentication/authorization for Registrar role.
    * **Commits/PRs:** Contributed [Number] meaningful commits to personal branch, created [Number] Pull Requests to main.
