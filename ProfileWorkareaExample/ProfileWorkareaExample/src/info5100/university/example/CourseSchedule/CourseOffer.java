/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.Persona.Faculty.FacultyAssignment;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class CourseOffer {

    Course course;
    int number;
    ArrayList<Seat> seatlist;
    CourseSchedule courseSchedule;
    FacultyAssignment facultyprofile;

    public CourseOffer(CourseSchedule cs, Course c) {
        course = c;
        courseSchedule = cs;
        seatlist = new ArrayList();
    }
     
    public void AssignAsTeacher(FacultyProfile fp) {

        facultyprofile = new FacultyAssignment(fp, this);
    }

    public FacultyProfile getFacultyProfile() {
        return facultyprofile.getFacultyProfile();
    }

    public String getCourseNumber() {
        return course.getCourseNumber();
    }

    public void generatSeats(int n) {
        for (int i = 0; i < n; i++) {
            seatlist.add(new Seat(this, i));
        }

    }

    public Seat getEmptySeat() {
        for (Seat s : seatlist) {
            if (!s.isOccupied()) {
                return s;
            }
        }
        return null;
    }


    public SeatAssignment assignEmptySeat(CourseLoad cl) {

        Seat seat = getEmptySeat();
        if (seat == null) {
            return null;
        }
        SeatAssignment sa = seat.newSeatAssignment(cl); //seat is already linked to course offer
        cl.registerStudent(sa); //coures offer seat is now linked to student
        return sa;
    }

    public int getTotalCourseRevenues() {

        int sum = 0;

        for (Seat s : seatlist) {
            if (s.isOccupied() == true) {
                sum = sum + course.getCoursePrice();
            }

        }
        return sum;
    }
    public Course getSubjectCourse(){
        return course;
    }
    public int getCreditHours(){
        return course.getCredits();
    }

    public void assignFaculty(FacultyProfile fp) {
        this.facultyprofile = new FacultyAssignment(fp, this);
    }
   
    public java.util.ArrayList<Seat> getSeatList() {
        return seatlist;
    }

  
    public int getSeatCount() {
        return seatlist == null ? 0 : seatlist.size();
    }


    public int getEnrolledCount() {
        if (seatlist == null) return 0;
        int count = 0;
        for (Seat s : seatlist) {
            if (s.isOccupied()) count++;
        }
        return count;
    }

}
