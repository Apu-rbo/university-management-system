package university.gui;

import university.model.Course;
import university.service.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePanel extends JPanel {

    private final MainFrame mainFrame;
    private final UniversityManager manager;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField(14);
    private final JTextField creditsField = new JTextField(3);

    public CoursePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.manager = mainFrame.getManager();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Course Name", "Credits", "Instructor", "Enrolled"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        refresh();
    }

    private JPanel buildFormPanel() {
        JPanel addRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRow.add(new JLabel("Course Name:")); addRow.add(nameField);
        addRow.add(new JLabel("Credits:")); addRow.add(creditsField);
        JButton addButton = new JButton("Add Course");
        addButton.addActionListener(e -> addCourse());
        addRow.add(addButton);
        return addRow;
    }

    private void addCourse() {
        try {
            String name = nameField.getText().trim();
            int credits = Integer.parseInt(creditsField.getText().trim());
            manager.addCourse(name, credits);

            nameField.setText(""); creditsField.setText("");
            mainFrame.refreshAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credits must be a whole number.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Course c : manager.getCourses()) {
            String instructor = (c.getInstructor() != null) ? c.getInstructor().getName() : "Unassigned";
            tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getCredits(),
                    instructor, c.getEnrolledStudents().size()
            });
        }
    }
}
