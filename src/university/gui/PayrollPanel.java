package university.gui;

import university.core.Payable;
import university.model.Professor;
import university.model.Staff;
import university.service.UniversityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollPanel extends JPanel {

    private final UniversityManager manager;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel = new JLabel("Total Payroll: $0.00");

    public PayrollPanel(MainFrame mainFrame) {
        this.manager = mainFrame.getManager();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tableModel = new DefaultTableModel(new Object[]{"Name", "Role", "Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 14f));
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalLabel, BorderLayout.WEST);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refresh());
        southPanel.add(refreshButton, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        double total = 0;

        // POLYMORPHISM: every Payable is processed identically here,
        // even though Professor and Staff calculate salary very differently.
        List<Object[]> rows = new ArrayList<>();
        for (Professor p : manager.getProfessors()) {
            rows.add(new Object[]{p.getName(), "Professor", p});
        }
        for (Staff s : manager.getStaffList()) {
            rows.add(new Object[]{s.getName(), "Staff", s});
        }

        for (Object[] row : rows) {
            Payable payable = (Payable) row[2];
            double salary = payable.calculateSalary();
            tableModel.addRow(new Object[]{row[0], row[1], String.format("$%.2f", salary)});
            total += salary;
        }

        totalLabel.setText(String.format("Total Payroll: $%.2f", total));
    }
}
