/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.SeatAssignment;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kal bugrara
 */
public class Transcript {

    StudentProfile student;
    HashMap<String, CourseLoad> courseloadlist;

    CourseLoad currentcourseload;

    public Transcript(StudentProfile sp) {
        student = sp;
        courseloadlist = new HashMap();

    }

    public int getStudentSatisfactionIndex() {
        //for each courseload 
        //get seatassigmnets; 
        //for each seatassignment add 1 if like =true;
        return 0;
    }

    public CourseLoad newCourseLoad(String sem) {

        currentcourseload = new CourseLoad(sem, student);
        courseloadlist.put(sem, currentcourseload);
        return currentcourseload;
    }

    public CourseLoad getCurrentCourseLoad() {

        return currentcourseload;

    }

    public CourseLoad getCourseLoadBySemester(String semester) {

        return courseloadlist.get(semester);

    }

    public float getStudentTotalScore() {

        float sum = 0;

        for (CourseLoad cl : courseloadlist.values()) {
            sum = sum + cl.getSemesterScore();

        }
        return sum;
    }
    //sat index means student rated their courses with likes;
    public int getStudentSatifactionIndex() {
        ArrayList<SeatAssignment> courseregistrations = getCourseList();
        int sum = 0;
        for (SeatAssignment sa : courseregistrations) {

            if (sa.getLike()) {
                sum = sum + 1;
            }
        }
        return sum;
    }
    //generate a list of all courses taken so far (seetassignments) 
    //from multiple semesters (course loads)
    //from seat assignments we will be able to access the course offers

    public ArrayList<SeatAssignment> getCourseList() {
        ArrayList temp2;
        temp2 = new ArrayList();

        for (CourseLoad cl : courseloadlist.values()) { //extract cl list as objects --ignore label
            temp2.addAll(cl.getSeatAssignments()); //merge one array list to another
        }

        return temp2;

    }
    
        private double convertGradeToPoint(String grade) {
        if (grade == null) {
            return 0.0;
        }
        // This assumes sa.getGrade() returns a String.
        // If sa.getGrade() returns a double, we need to change this logic.
        // For now, we follow the PDF's letter grade rule.
        switch (grade.toUpperCase()) {
            case "A":
                return 4.0;
            case "A-":
                return 3.7;
            case "B+":
                return 3.3;
            case "B":
                return 3.0;
            case "B-":
                return 2.7;
            case "C+":
                return 2.3;
            case "C":
                return 2.0;
            case "C-":
                return 1.7;
            case "F":
                return 0.0;
            default:
                return 0.0; // Default for "In Progress", "W", etc.
        }
    }
    
    public double calculateOverallGPA() {
        double totalQualityPoints = 0.0;
        int totalCreditHours = 0;

        // getCourseList() returns SeatAssignments from all semesters
        ArrayList<SeatAssignment> allAssignments = getCourseList(); 

        if (allAssignments == null) {
            return 0.0;
        }

        for (SeatAssignment sa : allAssignments) {
            // 1. Get letter grade (e.g., "A") and convert to point (e.g., 4.0)
            // We assume sa.getGrade() returns a String.
            double gradePoint = convertGradeToPoint(sa.getGrade()); 

            // 2. Get course credits
            if (sa.getCourseOffer() != null && sa.getCourseOffer().getCourse() != null) {
                int credits = sa.getCourseOffer().getCourse().getCredits();
                
                // 3. Calculate quality points
                totalQualityPoints += (gradePoint * credits);
                
                // 4. Accumulate total credit hours
                totalCreditHours += credits;
            }
        }

        // 5. Avoid division by zero
        if (totalCreditHours == 0) {
            return 0.0;
        }

        // 6. Return Overall GPA
        return totalQualityPoints / totalCreditHours;
    }

    /**
     * Private helper method to calculate GPA for a single CourseLoad (semester).
     * @param cl The CourseLoad to calculate
     * @return The Term GPA for that semester
     */
   private double calculateTermGPA(CourseLoad cl) {
        if (cl == null || cl.getSeatassignments() == null) {
            return 0.0;
        }
        
        double totalQualityPoints = 0.0;
        int totalCreditHours = 0;
        
        for (SeatAssignment sa : cl.getSeatassignments()) { // Iterate only through courses for this term
            if (sa.getCourseOffer() != null && sa.getCourseOffer().getCourse() != null) {
                // Get letter grade and convert to point
                double gradePoint = convertGradeToPoint(sa.getGrade());
                int credits = sa.getCourseOffer().getCourse().getCredits();
                
                totalQualityPoints += (gradePoint * credits);
                totalCreditHours += credits;
            }
        }
        
        if (totalCreditHours == 0) {
            return 0.0; // Avoid division by zero
        }
        
        return totalQualityPoints / totalCreditHours;
    }

    /**
     * Gets the student's academic standing.
     * Rules from assignment PDF [source: 86-88]:
     * - Academic Probation: Overall GPA < 3.0
     * - Academic Warning: Term GPA < 3.0 (even if Overall >= 3.0)
     * - Good Standing: Overall GPA >= 3.0 AND Term GPA >= 3.0
     * @return "Good Standing", "Academic Warning", or "Academic Probation"
     */
    public String getAcademicStatus() {
        double overallGPA = calculateOverallGPA();
        
        // 'currentcourseload' variable points to the last added semester
        double termGPA = calculateTermGPA(currentcourseload); 

        // Check for Academic Probation first
        if (overallGPA < 3.0) {
            return "Academic Probation";
        }
        
        // Then check for Academic Warning
        if (termGPA < 3.0) {
            return "Academic Warning";
        }
        
        // If both checks pass
        return "Good Standing";
    }

}
