package edu.usm.cos420.antenatal.controller;

import edu.usm.cos420.antenatal.service.*;
import edu.usm.cos420.antenatal.view.*;
import edu.usm.cos420.antenatal.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.event.*;

import java.time.LocalDate;

/**
 * AntenatalController
 */
public class AntenatalController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PatientWrapperService service;
    private PregnancyService pregnancyService;
    private PregnancySelectionService pregnancySelectionService;
    private PregnancyView pregnancyView;
    private PregnancySelectionView pregnancySelectionView;
    private Patient patient;
    private LocalDate dateF = LocalDate.now();
	private String sDate = dateF.format(dateTimeFormatter);
    private PatientWrapper currentPatient = null; // We're going to need a view to select the pregnancy from a patient
    private int currentPregnancy;


    /**
     * DEFAULT CONSTRUCTOR
     * @param service model service
     * @param pregnancyView Pregnancy registration view
     */
    public AntenatalController(PatientWrapperService service, PregnancyView pregnancyView, PregnancySelectionView pregnancySelectionView, Patient patient){
        this.service = service;
        this.pregnancyView = pregnancyView;
        this.pregnancyView.addVisitSubmissionListener(new VisitSubmissionListener());
        this.pregnancyView.addSubsequentVisitListener(new SubsequentVisitListener());
        this.patient = patient;
        initCurrentPatient();
        this.pregnancySelectionView = pregnancySelectionView;
        this.pregnancySelectionView.displayPregnancies(currentPatient);
        this.pregnancySelectionView.addCreatePregnancyListener(new CreatePregnancyListener());
        this.pregnancySelectionView.addTableMouseAdapter(new PregnancySelectionListener());
        this.pregnancySelectionView.addGenerateReportListener(new GenerateReportListener());
        this.pregnancySelectionService = new PregnancySelectionService(pregnancySelectionView);
        this.pregnancyService = new PregnancyService(pregnancyView, service, currentPatient);
    }

    private void initCurrentPatient() {
        if (service.getPatientWrapper(patient) != null) {
            currentPatient = service.getPatientWrapper(patient);
        } else {
            currentPatient = new PatientWrapper(patient, new ArrayList<Pregnancy>());
            service.addPatientWrapper(currentPatient);
        }
    }

    private void resetListeners(){
        this.pregnancySelectionView.addCreatePregnancyListener(new CreatePregnancyListener());
        this.pregnancySelectionView.addTableMouseAdapter(new PregnancySelectionListener());
        this.pregnancySelectionView.addGenerateReportListener(new GenerateReportListener());
    }


    private class VisitSubmissionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("initial")) {
                if(pregnancyService.handleInitialVisit()) {
                    pregnancySelectionService.displayPregnancies(currentPatient);
                    resetListeners();
                    pregnancySelectionService.toggleVisibility();
                }
            } else {
                if (e.getActionCommand().equals("subsequent")) {
                    if(pregnancyService.handleSubsequentVisit()) {
                        pregnancySelectionService.displayPregnancies(currentPatient);
                        resetListeners();
                        pregnancySelectionService.toggleVisibility();
                    }
                }
            }
        }
    }

    private class CreatePregnancyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // This is a placeholder for how I suggested it should work. Dan might have a chain of controller methods here
            //rView = new RegisterView(new PatientWrapper(patient, new AntenatalModel()));
            pregnancySelectionService.toggleVisibility();
            pregnancyService.initialPregnancy();
        }
    }

    private class GenerateReportListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            pregnancySelectionService.handleGenerateReport();
        }
    }

    private class PregnancySelectionListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            currentPregnancy = pregnancySelectionService.handlePregnancySelection(e);
            if (currentPregnancy >= 0) {
                pregnancyService.returningPregnancy(currentPregnancy);
            }
        }
    }

    private class SubsequentVisitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            pregnancyService.createNewVisitTab();
        }
    }
}
