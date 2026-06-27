package university.gui;

import university.model.Course;
import university.model.Professor;
import university.service.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

public class ProfessorPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UniversityManager manager;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField(10);
    private final JTextField emailField = new JTextField(12);
    private final JTextField ageField = new JTextField(3);
    private final JTextField deptField = new JTextField(8);
    private final JTextField salaryField = new JTextField(6);

    private final JTextField assignProfIdField = new JTextField(4);
    private final JTextField assignCourseIdField = new JTextField(4);

    public ProfessorPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.manager = mainFrame.getManager();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Age", "Department", "Salary", "Courses"}, 0) {
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
        addRow.add(new JLabel("Dept:")); addRow.add(deptField);
        addRow.add(new JLabel("Base Salary:")); addRow.add(salaryField);
        JButton addButton = new JButton("Add Professor");
        addButton.addActionListener(e -> addProfessor());
        addRow.add(addButton);

        JPanel assignRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignRow.add(new JLabel("Professor ID:")); assignRow.add(assignProfIdField);
        assignRow.add(new JLabel("Course ID:")); assignRow.add(assignCourseIdField);
        JButton assignButton = new JButton("Assign to Course");
        assignButton.addActionListener(e -> assignProfessor());
        assignRow.add(assignButton);

        container.add(addRow);
        container.add(assignRow);
        return container;
    }

    private void addProfessor() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String dept = deptField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());

            Professor p = new Professor(name, email, age, dept, salary);
            manager.addProfessor(p);

            nameField.setText(""); emailField.setText(""); ageField.setText("");
            deptField.setText(""); salaryField.setText("");
            mainFrame.refreshAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age and salary must be numbers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not add professor", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignProfessor() {
        try {
            int profId = Integer.parseInt(assignProfIdField.getText().trim());
            int courseId = Integer.parseInt(assignCourseIdField.getText().trim());
            Optional<Professor> p = manager.findProfessorById(profId);
            Optional<Course> c = manager.findCourseById(courseId);
            if (p.isPresent() && c.isPresent()) {
                p.get().assignCourse(c.get());
                mainFrame.refreshAll();
            } else {
                JOptionPane.showMessageDialog(this, "Professor or Course ID not found.", "Not found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "IDs must be whole numbers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Professor p : manager.getProfessors()) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getEmail(), p.getAge(),
                    p.getDepartment(), String.format("$%.2f", p.calculateSalary()), p.getCoursesTaught().size()
            });
        }
    }
}
