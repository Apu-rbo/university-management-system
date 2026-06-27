package university.gui;

import university.model.Staff;
import university.service.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffPanel extends JPanel {

    private final MainFrame mainFrame;
    private final UniversityManager manager;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField(10);
    private final JTextField emailField = new JTextField(12);
    private final JTextField ageField = new JTextField(3);
    private final JTextField positionField = new JTextField(10);
    private final JTextField salaryField = new JTextField(6);

    public StaffPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.manager = mainFrame.getManager();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Age", "Position", "Salary"}, 0) {
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
        addRow.add(new JLabel("Name:")); addRow.add(nameField);
        addRow.add(new JLabel("Email:")); addRow.add(emailField);
        addRow.add(new JLabel("Age:")); addRow.add(ageField);
        addRow.add(new JLabel("Position:")); addRow.add(positionField);
        addRow.add(new JLabel("Monthly Salary:")); addRow.add(salaryField);
        JButton addButton = new JButton("Add Staff");
        addButton.addActionListener(e -> addStaff());
        addRow.add(addButton);
        return addRow;
    }

    private void addStaff() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String position = positionField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());

            Staff s = new Staff(name, email, age, position, salary);
            manager.addStaff(s);

            nameField.setText(""); emailField.setText(""); ageField.setText("");
            positionField.setText(""); salaryField.setText("");
            mainFrame.refreshAll();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age and salary must be numbers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not add staff", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Staff s : manager.getStaffList()) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(), s.getAge(),
                    s.getPosition(), String.format("$%.2f", s.calculateSalary())
            });
        }
    }
}
