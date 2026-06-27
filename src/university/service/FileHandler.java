package university.service;

import university.model.*;

import java.io.*;
import java.util.List;

/**
 * Handles all reading/writing of plain-text files in /data.
 * Kept entirely separate from UniversityManager (single responsibility:
 * UniversityManager manages data in memory, FileHandler only knows about disk).
 *
 * File formats (pipe-delimited, course IDs comma-joined where a list is needed):
 *   courses.txt:     id|name|credits
 *   professors.txt:  id|name|email|age|department|baseSalary|courseId,courseId,...
 *   students.txt:    id|name|email|age|major|gpa|courseId,courseId,...
 *   staff.txt:       id|name|email|age|position|monthlySalary
 */
public class FileHandler {

    private static final String DATA_DIR = "data/";

    // ============================== SAVE ==============================

    public static void saveAll(UniversityManager manager) {
        try {
            new File(DATA_DIR).mkdirs();
            saveCourses(manager.getCourses());
            saveProfessors(manager.getProfessors());
            saveStudents(manager.getStudents());
            saveStaff(manager.getStaffList());
            System.out.println("Data saved to '" + DATA_DIR + "'");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void saveCourses(List<Course> courses) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "courses.txt"))) {
            for (Course c : courses) {
                bw.write(c.getCourseId() + "|" + c.getCourseName() + "|" + c.getCredits());
                bw.newLine();
            }
        }
    }

    private static void saveProfessors(List<Professor> professors) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "professors.txt"))) {
            for (Professor p : professors) {
                bw.write(p.getId() + "|" + p.getName() + "|" + p.getEmail() + "|" + p.getAge()
                        + "|" + p.getDepartment() + "|" + p.getBaseSalary()
                        + "|" + joinCourseIds(p.getCoursesTaught()));
                bw.newLine();
            }
        }
    }

    private static void saveStudents(List<Student> students) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "students.txt"))) {
            for (Student s : students) {
                bw.write(s.getId() + "|" + s.getName() + "|" + s.getEmail() + "|" + s.getAge()
                        + "|" + s.getMajor() + "|" + s.getGpa()
                        + "|" + joinCourseIds(s.getEnrolledCourses()));
                bw.newLine();
            }
        }
    }

    private static void saveStaff(List<Staff> staffList) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_DIR + "staff.txt"))) {
            for (Staff s : staffList) {
                bw.write(s.getId() + "|" + s.getName() + "|" + s.getEmail() + "|" + s.getAge()
                        + "|" + s.getPosition() + "|" + s.getMonthlySalary());
                bw.newLine();
            }
        }
    }

    private static String joinCourseIds(List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < courses.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(courses.get(i).getCourseId());
        }
        return sb.toString();
    }

    // ============================== LOAD ==============================

    public static void loadAll(UniversityManager manager) {
        loadCourses(manager);     // must come first - others link back to these
        loadProfessors(manager);
        loadStudents(manager);
        loadStaff(manager);
    }

    private static void loadCourses(UniversityManager manager) {
        File file = new File(DATA_DIR + "courses.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|");
                Course c = new Course(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]));
                manager.addCourse(c);
            }
        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }

    private static void loadProfessors(UniversityManager manager) {
        File file = new File(DATA_DIR + "professors.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|");
                Professor prof = new Professor(Integer.parseInt(p[0]), p[1], p[2],
                        Integer.parseInt(p[3]), p[4], Double.parseDouble(p[5]));
                if (p.length > 6 && !p[6].isBlank()) {
                    for (String courseIdStr : p[6].split(",")) {
                        manager.findCourseById(Integer.parseInt(courseIdStr))
                                .ifPresent(prof::assignCourse);
                    }
                }
                manager.addProfessor(prof);
            }
        } catch (IOException e) {
            System.out.println("Error loading professors: " + e.getMessage());
        }
    }

    private static void loadStudents(UniversityManager manager) {
        File file = new File(DATA_DIR + "students.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|");
                Student student = new Student(Integer.parseInt(p[0]), p[1], p[2],
                        Integer.parseInt(p[3]), p[4], Double.parseDouble(p[5]));
                if (p.length > 6 && !p[6].isBlank()) {
                    for (String courseIdStr : p[6].split(",")) {
                        manager.findCourseById(Integer.parseInt(courseIdStr))
                                .ifPresent(student::enroll);
                    }
                }
                manager.addStudent(student);
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }

    private static void loadStaff(UniversityManager manager) {
        File file = new File(DATA_DIR + "staff.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|");
                Staff staff = new Staff(Integer.parseInt(p[0]), p[1], p[2],
                        Integer.parseInt(p[3]), p[4], Double.parseDouble(p[5]));
                manager.addStaff(staff);
            }
        } catch (IOException e) {
            System.out.println("Error loading staff: " + e.getMessage());
        }
    }
}
