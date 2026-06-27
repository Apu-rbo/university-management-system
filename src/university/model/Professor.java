package university.model;

import university.core.Payable;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Person implements Payable {

    private String department;
    private double baseSalary;
    private final List<Course> coursesTaught;

    public Professor(String name, String email, int age, String department, double baseSalary) {
        super(name, email, age);
        this.department = department;
        this.baseSalary = baseSalary;
        this.coursesTaught = new ArrayList<>();
    }

    /** Loading constructor - used by FileHandler to restore a saved Professor. */
    public Professor(int id, String name, String email, int age, String department, double baseSalary) {
        super(id, name, email, age);
        this.department = department;
        this.baseSalary = baseSalary;
        this.coursesTaught = new ArrayList<>();
    }

    public double getBaseSalary() { return baseSalary; }

    public String getDepartment() { return department; }
    public List<Course> getCoursesTaught() { return coursesTaught; }

    public void assignCourse(Course course) {
        coursesTaught.add(course);
        course.setInstructor(this);
    }

    // POLYMORPHISM: same method name as Staff.calculateSalary(),
    // completely different formula underneath.
    @Override
    public double calculateSalary() {
        return baseSalary + (coursesTaught.size() * 200.0); // bonus per course
    }

    @Override
    public String getRole() {
        return "Professor";
    }

    @Override
    public String displayInfo() {
        return toString() + String.format(" | Dept:%s | Salary:$%.2f | Courses:%d",
                department, calculateSalary(), coursesTaught.size());
    }
}
