package info5100.university.example.Persona.Faculty;

import info5100.university.example.Persona.Person;
import info5100.university.example.CourseSchedule.CourseOffer;
import java.util.ArrayList;
import java.util.List;

/**
 * FacultyProfile 
 * Handles faculty teaching assignments and overall ratings.
 */
public class FacultyProfile {

    private final Person person;
    private final ArrayList<FacultyAssignment> facultyAssignments;

    public FacultyProfile(Person p) {
        this.person = p;
        this.facultyAssignments = new ArrayList<>();
    }

    /**
     * Assign this faculty as the teacher of a course.
     * This also links the course to the faculty (bi-directional association).
     */
    public FacultyAssignment assignToCourse(CourseOffer co) {
        FacultyAssignment fa = new FacultyAssignment(this, co);
        facultyAssignments.add(fa);
        co.assignFaculty(this); // connect faculty and course
        return fa;
    }

    /** Get the list of all teaching assignments for this faculty */
    public List<FacultyAssignment> getFacultyAssignments() {
        return facultyAssignments;
    }

    /** Calculate the average teaching rating across all courses */
    public double getProfAverageOverallRating() {
        if (facultyAssignments.isEmpty()) return 0.0;
        double sum = 0.0;
        for (FacultyAssignment fa : facultyAssignments) {
            sum += fa.getRating();
        }
        return sum / facultyAssignments.size();
    }

    /** Return the faculty's Person object */
    public Person getPerson() {
        return person;
    }

    /** Utility method to check if a person ID matches this faculty */
    public boolean isMatch(String id) {
        return person != null && person.getPersonId().equals(id);
    }

    /** Return the faculty name for display */
    @Override
    public String toString() {
        return (person == null ? "N/A" : person.getName());
    }
}