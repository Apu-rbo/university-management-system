package university.gui;

import university.model.Course;
import university.model.Student;
import university.service.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class StudentPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UniversityManager manager;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField(10);
    private final JTextField emailField = new JTextField(12);
    private final JTextField ageField = new JTextField(3);
    private final JTextField majorField = new JTextField(10);

    private final JTextField enrollStudentIdField = new JTextField(4);
    private final JTextField enrollCourseIdField = new JTextField(4);

    public StudentPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.manager = mainFrame.getManager();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Age", "Major", "GPA", "Courses"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        refresh();
    }

    private JPanel buildFormPanel() {
        JPanel container = new JPanel(new GridLayout(2, 1, 4, 4));

        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRow.add(new JLabel("Name:")); addRow.add(nameField);
        addRow.add(new JLabel("Email:")); addRow.add(emailField);
        addRow.add(new JLabel("Age:")); addRow.add(ageField);
        addRow.add(new JLabel("Major:")); addRow.add(majorField);
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        addRow.add(addButton);

        JPanel enrollRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        enrollRow.add(new JLabel("Student ID:")); enrollRow.add(enrollStudentIdField);
        enrollRow.add(new JLabel("Course ID:")); enrollRow.add(enrollCourseIdField);
        JButton enrollButton = new JButton("Enroll in Course");
        enrollButton.addActionListener(e -> enrollStudent());
        enrollRow.add(enrollButton);

        container.add(addRow);
        container.add(enrollRow);
        return container;
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String major = majorField.getText().trim();

            Student s = new Student(name, email, age, major); // encapsulation validates email/age internally
            manager.addStudent(s);

            nameField.setText(""); emailField.setText(""); ageField.setText(""); majorField.setText("");
            mainFrame.refreshAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a whole number.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not add student", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enrollStudent() {
        try {
            int studentId = Integer.parseInt(enrollStudentIdField.getText().trim());
            int courseId = Integer.parseInt(enrollCourseIdField.getText().trim());
            Optional<Student> s = manager.findStudentById(studentId);
            Optional<Course> c = manager.findCourseById(courseId);
            if (s.isPresent() && c.isPresent()) {
                s.get().enroll(c.get());
                mainFrame.refreshAll();
            } else {
                JOptionPane.showMessageDialog(this, "Student or Course ID not found.", "Not found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be whole numbers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Student s : manager.getStudents()) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getAge(),
                    s.getMajor(), String.format("%.2f", s.getGpa()), s.getEnrolledCourses().size()
            });
        }
    }
}
