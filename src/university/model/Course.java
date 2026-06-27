package university.model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private static int idCounter = 100;

    private final int courseId;
    private String courseName;
    private int credits;
    private Professor instructor;          // null until assigned
    private final List<Student> enrolledStudents;

    public Course(String courseName, int credits) {
        this.courseId = idCounter++;
        this.courseName = courseName;
        this.credits = credits;
        this.enrolledStudents = new ArrayList<>();
    }

    /** Loading constructor - used by FileHandler to restore a saved Course. */
    public Course(int courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.enrolledStudents = new ArrayList<>();
        if (courseId >= idCounter) {
            idCounter = courseId + 1;
        }
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public Professor getInstructor() { return instructor; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }

    public void setInstructor(Professor instructor) {
        this.instructor = instructor;
    }

    void addStudent(Student s) {
        if (!enrolledStudents.contains(s)) enrolledStudents.add(s);
    }

    void removeStudent(Student s) {
        enrolledStudents.remove(s);
    }

    @Override
    public String toString() {
        String instr = (instructor != null) ? instructor.getName() : "Unassigned";
        return String.format("[Course %d] %s (%d credits) | Instructor: %s | Enrolled: %d",
                courseId, courseName, credits, instr, enrolledStudents.size());
    }
}
