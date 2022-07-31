package edu.usm.cos420.antenatal.service;

import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.view.PregnancySelectionView;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class PregnancySelectionService {

    PregnancySelectionView pregnancySelectionView;
    public PregnancySelectionService(PregnancySelectionView pregnancySelectionView){
        this.pregnancySelectionView = pregnancySelectionView;
    }

    public int handlePregnancySelection (MouseEvent e) {
        if (e.getClickCount() == 1) {
            JTable target = (JTable) e.getSource();
            int selection = JOptionPane.showConfirmDialog(null, "View this pregnancy?");
            if (selection == 0) {
                pregnancySelectionView.setVisible(false);
                return target.getSelectedRow();
            }
        }
        return -1;
    }

    public void handleGenerateReport () {
        MidWifeMonthlyReport.outputReport();
        JOptionPane.showMessageDialog(null, "Report Generated");
    }

    public void toggleVisibility () {
        pregnancySelectionView.setVisible(!pregnancySelectionView.isVisible());
    }

    public void displayPregnancies(PatientWrapper patient) {
        this.pregnancySelectionView.displayPregnancies(patient);
    }
}