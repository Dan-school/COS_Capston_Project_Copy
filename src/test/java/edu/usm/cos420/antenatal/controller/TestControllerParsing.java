package edu.usm.cos420.antenatal.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.service.PregnancyService;
import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceDao;
import org.junit.Before;
import org.junit.Test;

import edu.usm.cos420.antenatal.messengers.ControllerToInterface;
import edu.usm.cos420.antenatal.messengers.PatientPregnancyMessage;
import edu.usm.cos420.antenatal.messengers.SubsequentVisitMessage;
import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.service.PatientWrapperService;
import edu.usm.cos420.antenatal.view.PregnancySelectionView;
import edu.usm.cos420.antenatal.view.PregnancyView;

import java.util.ArrayList;

public class TestControllerParsing {
    PatientWrapperService patientWrapperService;
    PregnancyView registerView;
    PatientPregnancyMessage patientPregnancyMessage;
    SubsequentVisitMessage subsequentVisitMessage;
    Patient patient;
    PregnancySelectionView pregnancySelectionView;
    PatientWrapper patientWrapper;
    PregnancyService pregnancyService;

    @Before
    public void setupData() {
        registerView = new PregnancyView();
        patientWrapperService = new PatientWrapperServiceDao();
        patient = new Patient(0L);
        pregnancySelectionView = new PregnancySelectionView();
        patientWrapper = new PatientWrapper(patient, new ArrayList<Pregnancy>());
        pregnancyService = new PregnancyService(registerView, patientWrapperService, patientWrapper);

        // Initial Visit Message setup
        patientPregnancyMessage = new PatientPregnancyMessage();


        patientPregnancyMessage.setGravidity("50");
        patientPregnancyMessage.setParity("50");
        patientPregnancyMessage.setExpectedDayOfDelivery("28/03/2021");
        patientPregnancyMessage.setHemoglobinReg("9");
        patientPregnancyMessage.setBloodGroup("A+");
        patientPregnancyMessage.setSickling(true);
        patientPregnancyMessage.setSicklingType("AS");
        patientPregnancyMessage.setMaleInvolvement(true);
        patientPregnancyMessage.setVDRL(true);

        // Subsequent Visit Message setup
        subsequentVisitMessage = new SubsequentVisitMessage();
        subsequentVisitMessage.setPatient("patient string");
        subsequentVisitMessage.setDate("28/03/2021");
        subsequentVisitMessage.setSystolic("500");
        subsequentVisitMessage.setDiastolic("500");
        subsequentVisitMessage.setWeight("500");
        subsequentVisitMessage.setFundalHeight("500");

        subsequentVisitMessage.setPatientHeight("500");
        subsequentVisitMessage.setGestationWeek("50");
        subsequentVisitMessage.setHemoglobin36Wks("9");
        subsequentVisitMessage.setBloodFilm(true);
        subsequentVisitMessage.setRefurred("Refurred String");

        subsequentVisitMessage.setTtFirstDose(false);
        subsequentVisitMessage.setTtProtectedNotDosed(true);
        subsequentVisitMessage.setTtSecondDose(false);
        subsequentVisitMessage.setTtThirdDose(false);

        subsequentVisitMessage.setIptFirstDose(true);
        subsequentVisitMessage.setIptSecondDose(true);
        subsequentVisitMessage.setIptThirdDose(true);

        subsequentVisitMessage.setPmtctARV(true);
        subsequentVisitMessage.setPmtctPostTestCounseling(true);
        subsequentVisitMessage.setPmtctPreTestCounseling(true);
        subsequentVisitMessage.setPmtctTestResult(true);
        subsequentVisitMessage.setItnUse(true);
        subsequentVisitMessage.setComplaints("Complaint string");
        subsequentVisitMessage.setRemarks("Remarks string");

    }

    @Test
    public void testInitialVisitMessageValidMessage() {
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertTrue("Incorrect validation of initial visit message, expected correct", response.isAllFieldsValid());
    }

    @Test
    public void testInitialVisitMessageInvalidMessage1() {
        patientPregnancyMessage.setParity("Invalid String");
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("parity").isValid());
    }

    @Test
    public void testInitialVisitMessageInvalidMessage2() {
        patientPregnancyMessage.setBloodGroup("Invalid String");
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("bloodGroup").isValid());
    }

    @Test
    public void testInitialVisitMessageInvalidMessage3() {
        patientPregnancyMessage.setSicklingType("Invalid String");
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("sicklingType").isValid());
    }

    @Test
    public void testInitialVisitMessageInvalidMessage4() {
        patientPregnancyMessage.setHemoglobinReg("Invalid String");
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("hemoglobinReg").isValid());
    }

    @Test
    public void testInitialVisitMessageInvalidMessage5() {
        patientPregnancyMessage.setExpectedDayOfDelivery("Invalid String");
        ControllerToInterface response = pregnancyService.validateInitialVisitMessage(patientPregnancyMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("expectedDayOfDelivery").isValid());
    }




    @Test
    public void testSubsequentVisitMessageValidMessage() {
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertTrue("Incorrect validation of subsequent visit message, expected correct", response.isAllFieldsValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage1() {
        subsequentVisitMessage.setSystolic("Invalid String");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of subsequent visit message, expected correct", response.getErrorMessages().get("systolic").isValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage2() {
        subsequentVisitMessage.setFundalHeight("Invalid String");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of subsequent visit message, expected correct", response.getErrorMessages().get("fundalHeight").isValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage3() {
        subsequentVisitMessage.setDate("Invalid Date");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of subsequent visit message, expected correct", response.getErrorMessages().get("date").isValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage4() {
        subsequentVisitMessage.setHemoglobin36Wks("Invalid String");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("hemoglobin36Wks").isValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage5() {
        subsequentVisitMessage.setGestationWeek("Invalid String");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("gestationWeek").isValid());
    }

    @Test
    public void testSubsequentVisitMessageInvalidMessage6() {
        subsequentVisitMessage.setPatientHeight("5000");
        ControllerToInterface response = pregnancyService.validateSubsequentVisitMessage(subsequentVisitMessage);
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.isAllFieldsValid());
        assertFalse("Incorrect validation of initial visit message, expected incorrect", response.getErrorMessages().get("patientHeight").isValid());
    }

}