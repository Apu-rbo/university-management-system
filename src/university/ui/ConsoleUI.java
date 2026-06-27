package university.ui;

import university.model.*;
import university.service.UniversityManager;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private final UniversityManager manager = UniversityManager.getInstance();

    public void start() {
        manager.loadFromFile();
        System.out.println("=== University Management System ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> addProfessor();
                case 3 -> addStaff();
                case 4 -> addCourse();
                case 5 -> enrollStudent();
                case 6 -> assignProfessor();
                case 7 -> viewAllPeople();
                case 8 -> viewAllCourses();
                case 9 -> viewPayroll();
                case 10 -> { manager.saveToFile(); running = false; }
                default -> System.out.println("Invalid option, try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Add Student");
        System.out.println("2. Add Professor");
        System.out.println("3. Add Staff");
        System.out.println("4. Add Course");
        System.out.println("5. Enroll Student in Course");
        System.out.println("6. Assign Professor to Course");
        System.out.println("7. View All People");
        System.out.println("8. View All Courses");
        System.out.println("9. View Payroll Report");
        System.out.println("10. Save & Exit");
    }

    // ---- input helpers ----
    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Please enter a whole number: ");
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Please enter a number: ");
        }
        double val = scanner.nextDouble();
        scanner.nextLine();
        return val;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // ---- menu actions ----
    private void addStudent() {
        try {
            String name = readLine("Name: ");
            String email = readLine("Email: ");
            int age = readInt("Age: ");
            String major = readLine("Major: ");
            Student s = new Student(name, email, age, major);
            manager.addStudent(s);
            System.out.println("Added: " + s);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add student: " + e.getMessage());
        }
    }

    private void addProfessor() {
        try {
            String name = readLine("Name: ");
            String email = readLine("Email: ");
            int age = readInt("Age: ");
            String department = readLine("Department: ");
            double salary = readDouble("Base salary: ");
            Professor p = new Professor(name, email, age, department, salary);
            manager.addProfessor(p);
            System.out.println("Added: " + p);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add professor: " + e.getMessage());
        }
    }

    private void addStaff() {
        try {
            String name = readLine("Name: ");
            String email = readLine("Email: ");
            int age = readInt("Age: ");
            String position = readLine("Position: ");
            double salary = readDouble("Monthly salary: ");
            Staff s = new Staff(name, email, age, position, salary);
            manager.addStaff(s);
            System.out.println("Added: " + s);
        } catch (IllegalArgumentException e) {
            System.out.println("Could not add staff: " + e.getMessage());
        }
    }

    private void addCourse() {
        String name = readLine("Course name: ");
        int credits = readInt("Credits: ");
        Course c = manager.addCourse(name, credits);
        System.out.println("Added: " + c);
    }

    private void enrollStudent() {
        int studentId = readInt("Student ID: ");
        int courseId = readInt("Course ID: ");
        Optional<Student> s = manager.findStudentById(studentId);
        Optional<Course> c = manager.findCourseById(courseId);
        if (s.isPresent() && c.isPresent()) {
            s.get().enroll(c.get());
            System.out.println(s.get().getName() + " enrolled in " + c.get().getCourseName());
        } else {
            System.out.println("Student or course ID not found.");
        }
    }

    private void assignProfessor() {
        int profId = readInt("Professor ID: ");
        int courseId = readInt("Course ID: ");
        Optional<Professor> p = manager.findProfessorById(profId);
        Optional<Course> c = manager.findCourseById(courseId);
        if (p.isPresent() && c.isPresent()) {
            p.get().assignCourse(c.get());
            System.out.println(p.get().getName() + " assigned to " + c.get().getCourseName());
        } else {
            System.out.println("Professor or course ID not found.");
        }
    }

    private void viewAllPeople() {
        System.out.println("--- All People ---");
        if (manager.getAllPeople().isEmpty()) {
            System.out.println("(none yet)");
            return;
        }
        // Polymorphism: same displayInfo() call, different output per actual subtype
        for (Person p : manager.getAllPeople()) {
            System.out.println(p.displayInfo());
        }
    }

    private void viewAllCourses() {
        System.out.println("--- All Courses ---");
        if (manager.getCourses().isEmpty()) {
            System.out.println("(none yet)");
            return;
        }
        for (Course c : manager.getCourses()) {
            System.out.println(c);
        }
    }

    private void viewPayroll() {
        System.out.println("--- Payroll Report ---");
        for (Professor p : manager.getProfessors()) {
            System.out.printf("%s (Professor): $%.2f%n", p.getName(), p.calculateSalary());
        }
        for (Staff s : manager.getStaffList()) {
            System.out.printf("%s (Staff): $%.2f%n", s.getName(), s.calculateSalary());
        }
        System.out.printf("TOTAL PAYROLL: $%.2f%n", manager.getTotalPayroll());
    }
}
