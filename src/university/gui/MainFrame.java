package university.gui;

import university.service.UniversityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    private final UniversityManager manager = UniversityManager.getInstance();

    private final StudentPanel studentPanel;
    private final ProfessorPanel professorPanel;
    private final StaffPanel staffPanel;
    private final CoursePanel coursePanel;
    private final PayrollPanel payrollPanel;

    public MainFrame() {
        super("University Management System");
        manager.loadFromFile();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        // each panel is constructed after `this`, so it can call back via getManager()/refreshAll()
        studentPanel = new StudentPanel(this);
        professorPanel = new ProfessorPanel(this);
        staffPanel = new StaffPanel(this);
        coursePanel = new CoursePanel(this);
        payrollPanel = new PayrollPanel(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Students", studentPanel);
        tabs.addTab("Professors", professorPanel);
        tabs.addTab("Staff", staffPanel);
        tabs.addTab("Courses", coursePanel);
        tabs.addTab("Payroll", payrollPanel);

        add(tabs, BorderLayout.CENTER);
        setJMenuBar(buildMenuBar());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndExit();
            }
        });
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveItem = new JMenuItem("Save Now");
        saveItem.addActionListener(e -> {
            manager.saveToFile();
            JOptionPane.showMessageDialog(this, "Data saved to data/ folder.");
        });

        JMenuItem exitItem = new JMenuItem("Save & Exit");
        exitItem.addActionListener(e -> saveAndExit());

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private void saveAndExit() {
        manager.saveToFile();
        dispose();
        System.exit(0);
    }

    /** Called by any panel after a mutating action so every tab reflects the change. */
    public void refreshAll() {
        studentPanel.refresh();
        professorPanel.refresh();
        staffPanel.refresh();
        coursePanel.refresh();
        payrollPanel.refresh();
    }

    public UniversityManager getManager() {
        return manager;
    }
}
