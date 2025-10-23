/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

*/
import Business.Business;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.Department.Department;
import info5100.university.example.Persona.Person;
import info5100.university.example.Persona.PersonDirectory;
import info5100.university.example.Persona.StudentDirectory;
import info5100.university.example.Persona.StudentProfile;
import info5100.university.example.Persona.Faculty.FacultyDirectory;
import info5100.university.example.Persona.Faculty.FacultyProfile;

/**
 *
 * @author kal bugrara
 */
class ConfigureABusiness {

    static Business initialize() {
        Business business = new Business("Digital University");

        // Get the top-level directiories
        Department department = business.getDepartment();
        UserAccountDirectory uad = business.getUserAccountDirectory();
        PersonDirectory pd = department.getPersonDirectory();
        StudentDirectory sd = department.getStudentDirectory();
        FacultyDirectory fd = department.getFacultyDirectory();

        // Admin
        // Create Admin Person
        Person adminPerson = pd.newPerson("001");
        adminPerson.setName("Admin User");
        uad.newUserAccount(adminPerson, "admin", "****", "Admin");
        
        // Faculty
        // Create 10 Faculty Persons and Profiles
        FacultyProfile[] faculties = new FacultyProfile[10];
        for (int i = 0; i < 10; i++) {
            Person facultyPerson = pd.newPerson("F00" + (i + 1));
            facultyPerson.setName(i == 0 ? "Kal Faculty" : "Faculty " + (i + 1));
            faculties[i] = fd.newFacultyProfile(facultyPerson);
            // Create Faculty User Account 
            uad.newUserAccount(facultyPerson, "prof" + (i + 1), "1234", "Faculty");
        }
        System.out.println("Faculty accounts created:");
        for (UserAccount ua : uad.getUserAccountList()) {
            System.out.println("  Username=" + ua.getUserLoginName() 
                + "  ->  PersonID=" + ua.getAssociatedPerson().getPersonId() 
                + ", Name=" + ua.getAssociatedPerson().getName());
        }
        // Assign the original "Kal Faculty" name to the first faculty for consistency
        faculties[0].getPerson().setName("Kal Faculty");
        
        
        // Studnets
        // Create 10 Students and Profiles
        StudentProfile[] students = new StudentProfile[20];
        for (int i = 0; i < 20; i++) {
            Person studentPerson = pd.newPerson("S00" + (i + 1));
            studentPerson.setName("Student " + (i + 1));
            students[i] = sd.newStudentProfile(studentPerson);
            // Create Student User Account
            uad.newUserAccount(studentPerson, "student" + (i + 1), "****", "Student");
        }
        
        // TODO: 1 Register
        // Wait for Teammate Define the Register Class
        
        // ----------------------------------------------------------------------
        // Course Part
        // Create Courese
        Course c1 = department.newCourse("Application Engineering", "INFO 5100", 4);
        Course c2 = department.newCourse("Data Science", "INFO 6100", 4);
        Course c3 = department.newCourse("Web Design", "INFO 6210", 4);
        Course c4 = department.newCourse("Data Management", "INFO 5200", 4);
        Course c5 = department.newCourse("Cloud Computing", "INFO 6300", 4);
        
        // Add core course requirement for Student graduation audit
        department.addCoreCourse(c1);
        
        // Create Course Schedule for "Fall 2025"
        CourseSchedule is = department.newCourseSchedule("Fall 2025");
        
        // Create 5 Course Offers and assign faculty
        CourseOffer co1 = is.newCourseOffer("INFO 5100");
        co1.generatSeats(50);
        co1.assignFaculty(faculties[0]); // Kal Faculty
        
        CourseOffer co2 = is.newCourseOffer("INFO 6100");
        co2.generatSeats(25);
        co2.assignFaculty(faculties[1]); // Faculty 2

        CourseOffer co3 = is.newCourseOffer("INFO 6210");
        co3.generatSeats(20);
        co3.assignFaculty(faculties[2]); // Faculty 3

        CourseOffer co4 = is.newCourseOffer("INFO 5200");
        co4.generatSeats(20);
        co4.assignFaculty(faculties[3]); // Faculty 4

        CourseOffer co5 = is.newCourseOffer("INFO 6300");
        co5.generatSeats(15);
        co5.assignFaculty(faculties[4]); // Faculty 5
        
        // ---------------------------------------------------------------------------
        // Students with seat assignments
        // Each course is 4 credits. 8-credit limit per semester = 2 courses max
        
        // Student 1 takes 2 courses
        // Student 1 (Adam) takes 2 courses (8 credits)
        CourseLoad cl_s1 = students[0].newCourseLoad("Fall 2025");
        cl_s1.newSeatAssignment(co1); // INFO 5100
        cl_s1.newSeatAssignment(co2); // INFO 6100

        // Student 2 takes 1 course (4 credits)
        CourseLoad cl_s2 = students[1].newCourseLoad("Fall 2025");
        cl_s2.newSeatAssignment(co1); // INFO 5100

        // Student 3 takes 2 courses (8 credits)
        CourseLoad cl_s3 = students[2].newCourseLoad("Fall 2025");
        cl_s3.newSeatAssignment(co3); // INFO 6210
        cl_s3.newSeatAssignment(co4); // INFO 5200

        // Student 4 takes 1 course (4 credits)
        CourseLoad cl_s4 = students[3].newCourseLoad("Fall 2025");
        cl_s4.newSeatAssignment(co5); // INFO 6300

        // Student 5 takes 2 courses (8 credits)
        CourseLoad cl_s5 = students[4].newCourseLoad("Fall 2025");
        cl_s5.newSeatAssignment(co1); // INFO 5100
        cl_s5.newSeatAssignment(co3); // INFO 6210

        // Student 6 takes 1 course
        CourseLoad cl_s6 = students[5].newCourseLoad("Fall 2025");
        cl_s6.newSeatAssignment(co2); // INFO 6100
        
        // Student 7 takes 2 courses
        CourseLoad cl_s7 = students[6].newCourseLoad("Fall 2025");
        cl_s7.newSeatAssignment(co4); // INFO 5200
        cl_s7.newSeatAssignment(co5); // INFO 6300
        
        // Student 8 takes 1 course
        CourseLoad cl_s8 = students[7].newCourseLoad("Fall 2025");
        cl_s8.newSeatAssignment(co1); // INFO 5100
        
        // Student 9 takes 1 course
        CourseLoad cl_s9 = students[8].newCourseLoad("Fall 2025");
        cl_s9.newSeatAssignment(co2); // INFO 6100

        // Student 10 takes 2 courses
        CourseLoad cl_s10 = students[9].newCourseLoad("Fall 2025");
        cl_s10.newSeatAssignment(co3); // INFO 6210
        cl_s10.newSeatAssignment(co1); // INFO 5100
        
        // Student 11 takes 2 courses (8 credits)
        CourseLoad cl_s11 = students[10].newCourseLoad("Fall 2025");
        cl_s11.newSeatAssignment(co1); // INFO 5100
        cl_s11.newSeatAssignment(co5); // INFO 6300

        // Student 12 takes 1 course (4 credits)
        CourseLoad cl_s12 = students[11].newCourseLoad("Fall 2025");
        cl_s12.newSeatAssignment(co2); // INFO 6100

        // Student 13 takes 2 courses (8 credits)
        CourseLoad cl_s13 = students[12].newCourseLoad("Fall 2025");
        cl_s13.newSeatAssignment(co3); // INFO 6210
        cl_s13.newSeatAssignment(co4); // INFO 5200

        // Student 14 takes 1 course (4 credits)
        CourseLoad cl_s14 = students[13].newCourseLoad("Fall 2025");
        cl_s14.newSeatAssignment(co1); // INFO 5100

        // Student 15 takes 2 courses (8 credits)
        CourseLoad cl_s15 = students[14].newCourseLoad("Fall 2025");
        cl_s15.newSeatAssignment(co2); // INFO 6100
        cl_s15.newSeatAssignment(co5); // INFO 6300

        // Student 16 takes 1 course
        CourseLoad cl_s16 = students[15].newCourseLoad("Fall 2025");
        cl_s16.newSeatAssignment(co4); // INFO 5200
        
        // Student 17 takes 2 courses
        CourseLoad cl_s17 = students[16].newCourseLoad("Fall 2025");
        cl_s17.newSeatAssignment(co1); // INFO 5100
        cl_s17.newSeatAssignment(co3); // INFO 6210
        
        // Student 18 takes 1 course
        CourseLoad cl_s18 = students[17].newCourseLoad("Fall 2025");
        cl_s18.newSeatAssignment(co5); // INFO 6300
        
        // Student 19 takes 1 course
        CourseLoad cl_s19 = students[18].newCourseLoad("Fall 2025");
        cl_s19.newSeatAssignment(co2); // INFO 6100

        // Student 20 takes 2 courses
        CourseLoad cl_s20 = students[19].newCourseLoad("Fall 2025");
        cl_s20.newSeatAssignment(co4); // INFO 5200
        cl_s20.newSeatAssignment(co3); // INFO 6210

        return business;
    }
}
