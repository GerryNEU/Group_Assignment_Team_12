# Group_Assignment_Team_12
This repository is for group assignment of Team 12

# Profile Workarea Example - Digital University

## 1. Project Title
Profile Workarea Example - A Digital University Management System

## 2. Team Information
* **Team:** Team 12
* **Members & Roles:**
    * [Member Name 1] - Role: [Role 1], Responsibilities: [Specific tasks], NUID: [NUID 1]
    * [Member Name 2] - Role: [Role 2], Responsibilities: [Specific tasks], NUID: [NUID 2]
    * [Member Name 3] - Role: [Role 3], Responsibilities: [Specific tasks], NUID: [NUID 3]
    * [Member Name 4] - Role: [Role 4], Responsibilities: [Specific tasks], NUID: [NUID 4]
    *(Please fill in the actual names, roles, responsibilities, and NUIDs)*

---

## 3. Project Overview
This project simulates a **Digital University Management System** using Java Swing. It provides distinct work areas (portals) for different user roles within a university: **Admin**, **Faculty**, and **Student**, plus a **Registrar** role. The system allows users to manage profiles, courses, registration, academics, and administrative tasks based on their roles.

**Key Features:**
* Role-based access control.
* User account management (Admin).
* Personnel (Person) registration (Admin).
* Faculty and Student record management (Admin).
* Course offering management (Registrar, Faculty view).
* Student course registration and viewing transcripts.
* Faculty management of assigned courses and student grades.
* Profile management for all roles.
* Analytics dashboard for administrative overview.
* Graduation audit for students.
* Financial/Tuition management features for students and faculty insights.

---

## 4. Installation & Setup Instructions
**Prerequisites:**
* **Java Development Kit (JDK):** Version 17 or higher.
* **IDE:** NetBeans IDE (recommended, as the project uses NetBeans project structure and GUI builder). Version 16 or compatible.
* **Libraries:** Apache NetBeans AbsoluteLayout library (included in the `lib` folder).

**Setup Instructions:**
1.  **Clone Repository:** Clone the `Group_Assignment_Team_12` repository to your local machine.
    ```bash
    git clone [repository URL]
    ```
2.  **Open in NetBeans:**
    * Launch NetBeans IDE.
    * Go to `File` > `Open Project`.
    * Navigate to the cloned repository folder and select the `ProfileWorkareaExample` project folder.
    * Click `Open Project`.
3.  **Build Project:**
    * Right-click on the `ProfileWorkareaExample` project in the Projects window.
    * Select `Clean and Build`.
4.  **Run Project:**
    * Right-click on the `ProfileWorkareaExample` project.
    * Select `Run`. This will launch the application's main login frame.

---

## 5. Authentication & Access Control
* **Authentication:** Users log in via the main application window (`ProfileWorkAreaMainFrame`) using a **Username** and **Password**. The system authenticates credentials against the `UserAccountDirectory`.
* **Authorization:** Upon successful login, the system identifies the user's **role** (`Admin`, `Faculty`, `Student`, `Registrar`). Based on the role, the corresponding work area panel (e.g., `AdminRoleWorkAreaJPanel`, `FacultyWorkAreaJPanel`, `StudentWorkAreaJPanel`, `RegistrarWorkAreaJPanel`) is displayed, granting access only to functionalities relevant to that role.

---

## 6. Features Implemented

*(Note: Assign specific team members to each feature)*

**Admin Role** (`AdminRoleWorkAreaJPanel`):
* **Administer User Accounts** (`ManageUserAccountsJPanel`): Create, view/edit, and delete user accounts. Assign roles to persons. *(Implemented by: [Member Name])*
* **Register Persons (HR)** (`ManagePersonsJPanel`): Add new persons (potential students/faculty) to the system, view/edit basic details, and delete persons (with associated profiles/accounts). *(Implemented by: [Member Name])*
* **Manage Faculty & Student Records** (`ManageRecordsJPanel`): View, search (by ID, Name, Dept), edit contact details, delete faculty/student records, and assign faculty to courses. *(Implemented by: [Member Name])*
* **My Profile** (`AdminManageProfileJPanel`): View and edit own admin profile contact details (email, phone). *(Implemented by: [Member Name])*
* **Analytics Dashboard** (`AnalyticsDashboardJPanel`): View summary reports on users by role, courses per semester, enrollment per course, and total tuition revenue. *(Implemented by: [Member Name])*

**Faculty Role** (`FacultyWorkAreaJPanel`):
* **Manage Courses** (`ManageCoursesJPanel`): View assigned courses, update course details (name, seats, schedule), assign faculty (if permissions allowed, currently shows dropdown), manage syllabus, and toggle enrollment status. *(Implemented by: [Member Name])*
* **Manage Students Profiles** (`ManageStudentsJPanel`): View students enrolled in selected courses, update student grades (scores 0-100), rank students by GPA, calculate class GPA, and view individual student transcripts. *(Implemented by: [Member Name])*
* **My Profile** (`ManageProfileJPanel`): View and edit own faculty profile details (Name, Office, Phone, Bio). *(Implemented by: [Member Name])*
* **Performance Reports** (`PerformanceReportsJPanel`): View performance metrics (average grade, enrollment, grade distribution) for selected courses and semesters, with an option to export to CSV. *(Implemented by: [Member Name])*
* **Tuition Insight** (`TuitionInsightJPanel`): View tuition generated from enrolled students in assigned courses. *(Implemented by: [Member Name])*

**Student Role** (`StudentWorkAreaJPanel`):
* **Course Work & Financial Management** (`CourseworkJPanel`): View tuition balance, course fee breakdown for the current semester, payment history, and pay tuition. (Coursework submission section is a placeholder). *(Implemented by: [Member Name])*
* **Manage Profile** (`ProfileJPanel`): View and edit personal profile information (Name, Email, Phone). *(Implemented by: [Member Name])*
* **Graduation Audit** (`GraduationAuditJPanel`): Check progress towards graduation requirements (total credits, core course completion, overall GPA) and view readiness status. *(Implemented by: [Member Name])*
* **Registration** (`CourseRegistrationJPanel`): Search available courses (by ID, Teacher, Name), view course details (instructor, credits, seats, status), enroll in courses (up to credit limit), view currently enrolled courses, and drop courses. *(Implemented by: [Member Name])*
* **Transcript** (`TranscriptJPanel`): View academic transcript, filter by semester, see course grades (letter), term GPA, overall GPA, and academic standing. *(Implemented by: [Member Name])*

**Registrar Role** (`RegistrarWorkAreaJPanel`):
* **Course Offering Management** (`ManageCourseOfferingsJPanel`): View, create, edit, and delete course offerings for a specific semester (e.g., Fall 2025). *(Implemented by: [Member Name])*
* *(Other buttons like Student Registration, Manage Profile, Tuition, Reporting are placeholders in this role's panel)*.

---

## 7. Usage Instructions
1.  **Launch:** Run the `ProfileWorkAreaMainFrame.java` file.
2.  **Login:** Use the predefined credentials to log in as different roles:
    * **Admin:** Username: `admin`, Password: `****`
    * **Faculty:** Username: `prof1` (for Kal Faculty), `prof2`, etc., Password: `1234`
    * **Student:** Username: `student1`, `student2`, etc., Password: `****`
    * **Registrar:** Username: `registrar`, Password: `regpass`
3.  **Navigate:** Once logged in, the respective work area panel will appear. Use the buttons within the panel to access different features.
4.  **Admin Example:**
    * Click `Register Persons (HR)` to add a new person. Fill in details and select a role (Student/Faculty).
    * Click `Administer User Accounts` to create a login account for a person without one. Select the person, set username/password.
    * Click `Manage Faculty & Student Records` to view lists, search, edit contact info, or delete records.
5.  **Student Example:**
    * Click `Registration` to search for and enroll in courses. Select a course from "Available Courses" and click "Enroll". Check credit limits.
    * Click `Transcript` to view grades and GPA. Use the dropdown to filter by semester.
    * Click `Graduation Audit` to see progress towards degree requirements.
    * Click `Course Work` to view tuition and pay fees.
6.  **Faculty Example:**
    * Click `Manage Courses` to see assigned courses. Select a course to edit details or toggle enrollment.
    * Click `Manage Students Profiles`. Select a course from the dropdown, then select a student to update their grade (0-100 score). Click `Rank Students` to sort by GPA.
    * Click `Performance Reports` to view class statistics.
7.  **Registrar Example:**
    * Click `Course Offering Management` to view the list of courses offered for the semester. Use Create/View/Edit/Delete buttons (functionality might be partially implemented).

---

## 8. Testing Guide
* **Login:**
    * Test logging in with valid credentials for each role (Admin, Faculty, Student, Registrar). Verify the correct work area panel appears.
    * Test logging in with invalid credentials. Verify login fails.
* **Authorization:**
    * Log in as Student. Attempt to access Admin functions (e.g., user management). Verify access is denied (buttons/menus should not be visible).
    * Log in as Faculty. Attempt to access Admin functions. Verify access is denied.
    * Log in as Admin. Verify access to all administrative functions.
* **Sample Test Cases:**
    * **Admin:** Create a new Person (Student), then create a User Account for them. Log in as that student. Delete the student record via Admin panel and verify the user can no longer log in.
    * **Student:** Enroll in 2 courses (total 8 credits). Attempt to enroll in a third course and verify the credit limit error appears. View transcript and verify courses appear. View Graduation Audit. Drop one course and verify it's removed from "My Courses".
    * **Faculty:** Log in as `prof1`. Select `Manage Students Profiles`. Choose the INFO 5100 course. Select a student and update their grade to 85. Verify the grade updates in the table. Click `Rank Students` and verify sorting.
    * **Registrar:** Log in. Access `Course Offering Management`. Verify the course list for Fall 2025 is displayed. (Test Create/Edit/Delete if fully implemented).

---

## 9. Challenges & Solutions
* **Challenge:** Integrating diverse functionalities for multiple roles within a single Swing application while maintaining separation of concerns.
    * **Solution:** Used a `CardLayout` panel (`CardSequencePanel`) in the main frame to switch between different role-specific `JPanel`s. Each role panel encapsulates its own logic and UI components.
* **Challenge:** Managing shared data (like Person directory, Course schedules) accessibly across different parts of the application.
    * **Solution:** Centralized core data structures within the `Business` and `Department` classes, passing the `Business` object down to panels that require access.
* **Challenge:** Implementing specific business rules like credit limits, GPA calculations, and academic standing determination accurately.
    * **Solution:** Encapsulated calculation logic within relevant classes (e.g., `Transcript` for GPA and academic standing, `CourseRegistrationJPanel` for credit limit checks).
* **Challenge:** Ensuring UI updates correctly after data modifications (e.g., refreshing tables after adding/deleting records).
    * **Solution:** Implemented explicit refresh methods (e.g., `populateTable()`, `loadMyCourses()`) and called them after relevant actions or when navigating back to list views.

---

## 10. Future Enhancements
* Implement full CRUD (Create, Read, Update, Delete) functionality for all manageable entities (Courses, Offerings, etc.).
* Add more detailed financial management features (payment methods, fee types, reporting).
* Implement the "Course Work" submission feature for students.
* Develop more sophisticated analytics and reporting features.
* Add persistence layer (e.g., database connectivity) to save and load application state.
* Improve error handling and user feedback messages.
* Implement features for other potential roles (e.g., Department Head, Advisor).
* Refine the UI/UX for better usability.
* Add automated unit and integration tests.

---

## 11. Contribution Breakdown

*(Please fill this section with specific contributions)*

* **[Member Name 1] (NUID: [NUID 1]):**
    * **Coding:** Implemented features [Feature A, Feature B], developed classes [Class X, Class Y].
    * **Documentation:** Wrote sections [Section 1, Section 2] of the README.
    * **Testing:** Tested functionalities related to [Role Z].
    * **Other:** [Any other contributions, e.g., UI design, project setup].
* **[Member Name 2] (NUID: [NUID 2]):**
    * **Coding:** Implemented features [Feature C, Feature D], bug fixing in [Module P].
    * **Documentation:** Created UML diagrams, wrote section [Section 3].
    * **Testing:** Performed integration testing for [Use Case Q].
    * **Other:** [e.g., Version control management].
* **[Member Name 3] (NUID: [NUID 3]):**
    * **Coding:** Developed UI panels for [Role R], implemented [Feature E].
    * **Documentation:** Wrote Usage Instructions and Testing Guide sections.
    * **Testing:** Conducted usability testing.
    * **Other:** [e.g., Presentation preparation].
* **[Member Name 4] (NUID: [NUID 4]):**
    * **Coding:** Implemented data models [Model S, Model T], worked on [Feature F].
    * **Documentation:** Wrote Project Overview and Future Enhancements sections.
    * **Testing:** Tested Admin functionalities.
    * **Other:** [e.g., Code reviews].