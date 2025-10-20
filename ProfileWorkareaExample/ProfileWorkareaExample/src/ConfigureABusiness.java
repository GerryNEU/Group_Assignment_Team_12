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

        Department department = business.getDepartment();
        UserAccountDirectory uad = business.getUserAccountDirectory();

        
        PersonDirectory pd = department.getPersonDirectory();
        
        Person adminPerson = pd.newPerson("001");
        adminPerson.setName("Admin User");
        
        Person studentPerson = pd.newPerson("002");
        studentPerson.setName("Adam Student");
        
        Person facultyPerson = pd.newPerson("003");
        facultyPerson.setName("Kal Faculty");

        StudentDirectory sd = department.getStudentDirectory();
        StudentProfile studentProfile = sd.newStudentProfile(studentPerson);

        FacultyDirectory fd = department.getFacultyDirectory(); 
        FacultyProfile facultyProfile = fd.newFacultyProfile(facultyPerson);
        
        uad.newUserAccount(adminPerson, "admin", "****", "Admin");
        uad.newUserAccount(studentPerson, "adam", "****", "Student");
        uad.newUserAccount(facultyPerson, "prof", "****", "Faculty");

        Course c1 = department.newCourse("Application Engineering", "INFO 5100", 4);
        department.addCoreCourse(c1);
        department.newCourse("Data Science", "INFO 6100", 4);
        department.newCourse("Web Design", "INFO 6210", 4);
        
        CourseSchedule cs = department.newCourseSchedule("Fall 2025");
        
        CourseOffer co1 = cs.newCourseOffer("INFO 5100");
        co1.generatSeats(10);
        co1.assignFaculty(facultyProfile); // 分配教师

        CourseOffer co2 = cs.newCourseOffer("INFO 6100");
        co2.generatSeats(15);
        
        CourseOffer co3 = cs.newCourseOffer("INFO 6210");
        co3.generatSeats(20);
        
        CourseLoad cl_adam = studentProfile.newCourseLoad("Fall 2025");
        cl_adam.newSeatAssignment(co1);

        return business;
    }
}
