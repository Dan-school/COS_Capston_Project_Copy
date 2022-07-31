package edu.usm.cos420.antenatal.service;

import edu.usm.cos420.antenatal.messengers.ControllerToInterface;
import edu.usm.cos420.antenatal.messengers.ErrorMessage;
import edu.usm.cos420.antenatal.messengers.PatientPregnancyMessage;
import edu.usm.cos420.antenatal.messengers.SubsequentVisitMessage;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.model.Visit;
import edu.usm.cos420.antenatal.view.PregnancyView;

import javax.swing.*;
import javax.validation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;

public class PregnancyService {

    private PatientWrapperService service;
    private PregnancyView pregnancyView;
    private PatientWrapper currentPatient;
    private int currentPregnancy = 0;

    private static final Validator validator;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateF = LocalDate.now();
    private String sDate = dateF.format(dateTimeFormatter);

    static {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    public PregnancyService(PregnancyView pregnancyView, PatientWrapperService service, PatientWrapper currentPatient){
        this.pregnancyView = pregnancyView;
        this.service = service;
        this.currentPatient = currentPatient;
    }

    public boolean handleInitialVisit() {
        return initialVisitHandler(pregnancyView.initialVisit());
    }

    public boolean handleSubsequentVisit() {
        return subsequentVisitHandler(pregnancyView.subsequentVisit(currentPatient.getPregnancies().get(currentPregnancy).getSubsequentVisits().size()+1));
    }

    /**
     *
     *
     * @param initialVisitMessage
     */
    public boolean initialVisitHandler(PatientPregnancyMessage initialVisitMessage) {
        ControllerToInterface controllerToInterface = validateInitialVisitMessage(initialVisitMessage);
        controllerToInterface.getErrorMessages().putAll(validateSubsequentVisitMessage(pregnancyView.subsequentVisit(1)).getErrorMessages());
        if (!controllerToInterface.isAllFieldsValid()) {
            pregnancyView.displayValidationErrors(controllerToInterface, 1);
        } else {
            addInitialVisit(initialVisitMessage, pregnancyView.subsequentVisit(1));
            JOptionPane.showMessageDialog(null, "Successfully recorded visit. Full consulting register integration not implemented yet. Returning to pregnancy selection");
            pregnancyView.setVisible(false);
            pregnancyView.resetPregnancyGUI();
        }
        return controllerToInterface.isAllFieldsValid();
    }

    /**
     *
     *
     * @param subsequentVisitMessage
     */
    public boolean subsequentVisitHandler(SubsequentVisitMessage subsequentVisitMessage) {
        ControllerToInterface controllerToInterface = validateSubsequentVisitMessage(subsequentVisitMessage);
        if (!controllerToInterface.isAllFieldsValid()) {
            pregnancyView.displayValidationErrors(controllerToInterface, currentPatient.getPregnancies().get(currentPregnancy).getSubsequentVisits().size()+1);
        } else {
            addSubsequentVisit(subsequentVisitMessage);
            JOptionPane.showMessageDialog(null, "Successfully recorded visit. Full consulting register integration not implemented yet. Returning to pregnancy selection");
            pregnancyView.resetPregnancyGUI();
            pregnancyView.setVisible(false);
        }
        return controllerToInterface.isAllFieldsValid();
    }

    /**
     * Takes a patient and a validated initial visit to create a new PatientWrapper
     *
     * @param initialVisitMessage validated initialVisitMessage
     */
    public void addInitialVisit(PatientPregnancyMessage initialVisitMessage, SubsequentVisitMessage subsequentVisitMessage){
        Pregnancy pregnancy = new Pregnancy(initialVisitMessage);
        pregnancy.addVisit(subsequentVisitMessage);
        currentPatient.getPregnancies().add(pregnancy);
        service.updatePatientWrapper(currentPatient);
    }

    /**
     * Takes a patient and a validated subsequent visit to create a new PatientWrapper
     *
     * @param subsequentVisitMessage validated SubsequentVisitMessage
     */
    public void addSubsequentVisit(SubsequentVisitMessage subsequentVisitMessage){
        currentPatient.getPregnancies().get(currentPregnancy).addVisit(subsequentVisitMessage);
        service.updatePatientWrapper(currentPatient);
    }

    public void initialPregnancy() {
        pregnancyView.initializePatientInformation(currentPatient.getPatient());
        pregnancyView.constructVisit(newVisit(), 1);
        pregnancyView.showRegistryForm();
        pregnancyView.disableNewVisit();
        pregnancyView.createComponentMap();
        pregnancyView.setVisible(true);
    }

    public void returningPregnancy(int currentPregnancy) {
        this.currentPregnancy = currentPregnancy;
        pregnancyView.initializePatientInformation(currentPatient.getPatient());
        initializeReturnVisits();
        pregnancyView.showRegistryForm();
        pregnancyView.initializePregnancyInformation(currentPatient.getPregnancies().get(currentPregnancy));
        pregnancyView.createComponentMap();
        pregnancyView.markAsSubsequentVisit();
        pregnancyView.setVisible(true);
    }

    private SubsequentVisitMessage newVisit(){
        SubsequentVisitMessage newVisit = new SubsequentVisitMessage();

        newVisit.setDate(sDate);
        newVisit.setFundalHeight("");
        newVisit.setWeight("");
        newVisit.setSystolic("");
        newVisit.setDiastolic("");
        newVisit.setGestationWeek("");
        newVisit.setPatientHeight("");
        //do not have to se the tt or ipt they init false as they should

        return newVisit;
    }

    public void initializeReturnVisits(){
        ArrayList<Visit> vList = currentPatient.getPregnancies().get(currentPregnancy).getSubsequentVisits();
        for(int visitIndex = 0; visitIndex < vList.size(); visitIndex++){
            pregnancyView.constructVisit(visitToSubsequentVisitMessage(vList.get(visitIndex)), visitIndex+1);
        }
    }

    public SubsequentVisitMessage visitToSubsequentVisitMessage(Visit v){
        SubsequentVisitMessage visitMessage = new SubsequentVisitMessage();

        visitMessage.setDate(v.getDate().format(dateTimeFormatter));
        visitMessage.setBloodFilm(v.getBloodFilm());
        visitMessage.setFundalHeight(String.valueOf(v.getFundalHeight()));
        visitMessage.setWeight(String.valueOf(v.getWeight()));
        visitMessage.setSystolic(String.valueOf(v.getBloodPressure().getSystolic()));
        visitMessage.setDiastolic(String.valueOf(v.getBloodPressure().getDiastolic()));
        visitMessage.setGestationWeek(String.valueOf(v.getGestationWeek()));
        visitMessage.setPatientHeight(String.valueOf(v.getPatientHeight()));

        visitMessage.setIptFirstDose(currentPatient.getPregnancies().get(currentPregnancy).getIpt().getFirstDose());
        visitMessage.setIptSecondDose(currentPatient.getPregnancies().get(currentPregnancy).getIpt().getSecondDose());
        visitMessage.setIptThirdDose(currentPatient.getPregnancies().get(currentPregnancy).getIpt().getThirdDose());

        visitMessage.setTtProtectedNotDosed(currentPatient.getPregnancies().get(currentPregnancy).getTt().getProtectedNotDosed());
        visitMessage.setTtFirstDose(currentPatient.getPregnancies().get(currentPregnancy).getTt().getFirstDose());
        visitMessage.setTtSecondDose(currentPatient.getPregnancies().get(currentPregnancy).getTt().getSecondDose());
        visitMessage.setTtThirdDose(currentPatient.getPregnancies().get(currentPregnancy).getTt().getThirdDose());

        visitMessage.setComplaints(currentPatient.getPregnancies().get(currentPregnancy).getComplaints());
        visitMessage.setRemarks(currentPatient.getPregnancies().get(currentPregnancy).getRemarks());

        return visitMessage;
    }

    public ControllerToInterface validateInitialVisitMessage(PatientPregnancyMessage initialVisit) {
        ControllerToInterface controllerToInterface = new ControllerToInterface();
        Set<ConstraintViolation<PatientPregnancyMessage>> violations = validator.validate(initialVisit);
        controllerToInterface.setAllFieldsValid(violations.isEmpty());
        violations.stream().forEach(v -> controllerToInterface.addErrorMessage(v.getPropertyPath().toString(), new ErrorMessage(false, v.getMessage())));

        return controllerToInterface;
    }

    public ControllerToInterface validateSubsequentVisitMessage(SubsequentVisitMessage subsequentVisit) {
        ControllerToInterface controllerToInterface = new ControllerToInterface();
        Set<ConstraintViolation<SubsequentVisitMessage>> violations = validator.validate(subsequentVisit);
        controllerToInterface.setAllFieldsValid(violations.isEmpty());
        violations.stream().forEach(v -> controllerToInterface.addErrorMessage(v.getPropertyPath().toString(), new ErrorMessage(false, v.getMessage())));

        return controllerToInterface;
    }

    public void createNewVisitTab() {
        pregnancyView.constructVisit(newVisit(), currentPatient.getPregnancies().get(currentPregnancy).getSubsequentVisits().size()+1);
        pregnancyView.createComponentMap();
        pregnancyView.disableNewVisit();
    }
}
