package edu.usm.cos420.antenatal.view;

import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.model.PatientWrapper;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PregnancySelectionView extends JFrame {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton createPregnancy;
    private JButton generateReport;
    private String[][] records;
    private String header[] = { "Due Date", "Visits",  "Parity" };
    final private int GUIwidth = 850,
            GUIheight = 700;
    PatientWrapper pregnancies;

    public PregnancySelectionView () {
        initUI();
    }

    public void initUI() {
        this.setTitle("Pregnancy Selection");

        mainPanel = new JPanel();
        this.setDefaultCloseOperation(3);
        this.setSize(GUIwidth, GUIheight);

        scrollPane = new JScrollPane();
        pregnancies = new PatientWrapper(null, new ArrayList<Pregnancy>());
        displayPregnancies(pregnancies);
        mainPanel.add(scrollPane);
        createPregnancy = new JButton("New");
        mainPanel.add(createPregnancy);
        generateReport = new JButton("Generate Report");
        mainPanel.add(generateReport);
        this.add(mainPanel);
        this.setVisible(true);
    }

    public void displayPregnancies(PatientWrapper selectedPatient) {
        if (selectedPatient.getPregnancies().isEmpty()) {
            records = new String[0][0];
            table = new JTable(records, header);

            scrollPane.setViewportView(table);
        } else {
            records = new String[selectedPatient.getPregnancies().size()][3];
            for (int i = 0; i < selectedPatient.getPregnancies().size(); i++) {
                selectedPatient.getPregnancies().get(i).getExpectedDayOfDelivery();
                records[i][0] = selectedPatient.getPregnancies().get(i).getExpectedDayOfDelivery().format(dateTimeFormatter);
                records[i][1] = "Hemoglobin At Registration: " + selectedPatient.getPregnancies().get(i).getHemoglobinLevelAtReg();
                records[i][2] = selectedPatient.getPregnancies().get(i).getSickling() + "";

            }
            table = new JTable(records, header);

            scrollPane.setViewportView(table);
        }
    }

    public void addTableMouseAdapter (MouseAdapter mouseAdapter) {
        table.addMouseListener(mouseAdapter);
    }

    public void addCreatePregnancyListener(ActionListener actionListener) {
        createPregnancy.addActionListener(actionListener);
    }

    public void addGenerateReportListener(ActionListener actionListener) {
        generateReport.addActionListener(actionListener);
    }


    public static void main(String[] args) {
        System.out.println("HERE WE GO");
        new PregnancySelectionView();
    }
}
