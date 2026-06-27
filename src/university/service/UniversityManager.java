package university.service;

import university.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Central in-memory store and business logic for the whole application.
 *
 * DESIGN PATTERN - Singleton: there is exactly one UniversityManager for
 * the lifetime of the program, reached via getInstance(). This avoids
 * passing the same lists around to every class that needs them.
 */
public class UniversityManager {

    private static UniversityManager instance;

    private final List<Student> students = new ArrayList<>();
    private final List<Professor> professors = new ArrayList<>();
    private final List<Staff> staffList = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();

    private UniversityManager() {
        // private constructor: nobody outside this class can do `new UniversityManager()`
    }

    public static UniversityManager getInstance() {
        if (instance == null) {
            instance = new UniversityManager();
        }
        return instance;
    }

    // ---- adding new records ----
    public void addStudent(Student s) { students.add(s); }
    public void addProfessor(Professor p) { professors.add(p); }
    public void addStaff(Staff s) { staffList.add(s); }

    public Course addCourse(String name, int credits) {
        Course c = new Course(name, credits);
        courses.add(c);
        return c;
    }

    /** Overload used by FileHandler when re-registering a Course already built from a saved line. */
    public void addCourse(Course c) {
        courses.add(c);
    }

    // ---- read access ----
    public List<Student> getStudents() { return students; }
    public List<Professor> getProfessors() { return professors; }
    public List<Staff> getStaffList() { return staffList; }
    public List<Course> getCourses() { return courses; }

    public List<Person> getAllPeople() {
        List<Person> all = new ArrayList<>();
        all.addAll(students);
        all.addAll(professors);
        all.addAll(staffList);
        return all;
    }

    // ---- lookups ----
    public Optional<Student> findStudentById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst();
    }

    public Optional<Professor> findProfessorById(int id) {
        return professors.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Optional<Course> findCourseById(int id) {
        return courses.stream().filter(c -> c.getCourseId() == id).findFirst();
    }

    // ---- reports ----
    public double getTotalPayroll() {
        double total = 0;
        for (Professor p : professors) total += p.calculateSalary(); // polymorphism
        for (Staff s : staffList) total += s.calculateSalary();      // polymorphism
        return total;
    }

    // ---- persistence ----
    public void loadFromFile() {
        FileHandler.loadAll(this);
    }

    public void saveToFile() {
        FileHandler.saveAll(this);
    }
}
