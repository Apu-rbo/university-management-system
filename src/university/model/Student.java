package university.model;

import university.core.Enrollable;

import java.util.ArrayList;
import java.util.List;

/**
 * INHERITANCE: extends Person, reuses id/name/email/age for free.
 * POLYMORPHISM: provides its OWN version of getRole()/displayInfo().
 */
public class Student extends Person implements Enrollable {

    private String major;
    private double gpa;
    private final List<Course> enrolledCourses;

    public Student(String name, String email, int age, String major) {
        super(name, email, age);
        this.major = major;
        this.gpa = 0.0;
        this.enrolledCourses = new ArrayList<>();
    }

    /** Loading constructor - used by FileHandler to restore a saved Student. */
    public Student(int id, String name, String email, int age, String major, double gpa) {
        super(id, name, email, age);
        this.major = major;
        this.enrolledCourses = new ArrayList<>();
        setGpa(gpa);
    }

    public void setGpa(double gpa) {
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        this.gpa = gpa;
    }

    public double getGpa() { return gpa; }
    public String getMajor() { return major; }
    public List<Course> getEnrolledCourses() { return enrolledCourses; }

    @Override
    public void enroll(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.addStudent(this);
        }
    }

    @Override
    public void dropCourse(Course course) {
        enrolledCourses.remove(course);
        course.removeStudent(this);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String displayInfo() {
        return toString() + String.format(" | Major:%s | GPA:%.2f | Courses:%d",
                major, gpa, enrolledCourses.size());
    }
}
