package edu.usm.cos420.antenatal.controller;

import com.google.gson.reflect.TypeToken;
import edu.usm.cos420.antenatal.dao.JsonDao;
import edu.usm.cos420.antenatal.service.PregnancyService;
import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.usm.cos420.antenatal.controller.AntenatalController;
import edu.usm.cos420.antenatal.dao.GenericDao;
import edu.usm.cos420.antenatal.dao.ObjectStreamDao;
import edu.usm.cos420.antenatal.dao.domain.PatientWrapperDao;
import edu.usm.cos420.antenatal.messengers.PatientPregnancyMessage;
import edu.usm.cos420.antenatal.messengers.SubsequentVisitMessage;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Sickling;
import edu.usm.cos420.antenatal.model.TT;
import edu.usm.cos420.antenatal.model.Visit;
import edu.usm.cos420.antenatal.service.PatientWrapperService;
import edu.usm.cos420.antenatal.view.PregnancySelectionView;
import edu.usm.cos420.antenatal.view.PregnancyView;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.model.BloodPressure;
import edu.usm.cos420.antenatal.model.IPT;
import edu.usm.cos420.antenatal.model.PMTCT;
import edu.usm.cos420.antenatal.model.Parity;
import edu.usm.cos420.antenatal.model.Patient;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;


public class TestControllerData {

    GenericDao<Long, PatientWrapper> dao;
	PatientWrapperDao patientWrapperDao;
    PatientWrapperService patientWrapperService;
    PregnancyView registerView;
    PatientPregnancyMessage patientPregnancyMessage;
    SubsequentVisitMessage subsequentVisitMessage;
    SubsequentVisitMessage subsequentVisitMessage2;
    Patient patient;
    PregnancySelectionView pregnancySelectionView;
    PatientWrapper patientWrapper;
    PregnancyService pregnancyService;


    @Before
    public void setupData() {
        Type t = new TypeToken<Map<Long, PatientWrapper>>() {
        }.getType();
        dao = new JsonDao<>("test.json", t);
	    patientWrapperDao = new PatientWrapperDao(dao);
	    patientWrapperService = new PatientWrapperServiceDao(patientWrapperDao);
        // This back and forth with view and controller is due to the planned listener being in the view instead of the controller
        // The controller needs to reference the view and the view needs to reference the controller
        registerView = new PregnancyView();

        patient = new Patient(5L);
        pregnancySelectionView = new PregnancySelectionView();
        patientWrapper = new PatientWrapper(patient, new ArrayList<Pregnancy>());
        pregnancyService = new PregnancyService(registerView, patientWrapperService, patientWrapper);
        //patientWrapperService.addPatientWrapper(patientWrapper);

        // Initial Visit Message setup
        patientPregnancyMessage = new PatientPregnancyMessage();

        patientPregnancyMessage.setGravidity("50");
        patientPregnancyMessage.setParity("50");
        patientPregnancyMessage.setExpectedDayOfDelivery("28/03/2021");
        patientPregnancyMessage.setHemoglobinReg("9.0");
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
        subsequentVisitMessage.setHemoglobin36Wks("9.0");
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

        // Subsequent Visit Message 2 setup
        subsequentVisitMessage2 = new SubsequentVisitMessage();
        subsequentVisitMessage2.setPatient("new patient string");
        subsequentVisitMessage2.setDate("28/04/2021");
        subsequentVisitMessage2.setSystolic("550");
        subsequentVisitMessage2.setDiastolic("530");
        subsequentVisitMessage2.setWeight("350");
        subsequentVisitMessage2.setFundalHeight("300");

        subsequentVisitMessage2.setPatientHeight("530");
        subsequentVisitMessage2.setGestationWeek("60");
        subsequentVisitMessage2.setHemoglobin36Wks("7.0");
        subsequentVisitMessage2.setBloodFilm(false);
        subsequentVisitMessage2.setRefurred("Refurred String 2");

        subsequentVisitMessage2.setTtFirstDose(true);
        subsequentVisitMessage2.setTtProtectedNotDosed(false);
        subsequentVisitMessage2.setTtSecondDose(true);
        subsequentVisitMessage2.setTtThirdDose(false);

        subsequentVisitMessage2.setIptFirstDose(true);
        subsequentVisitMessage2.setIptSecondDose(true);
        subsequentVisitMessage2.setIptThirdDose(false);

        subsequentVisitMessage2.setPmtctARV(true);
        subsequentVisitMessage2.setPmtctPostTestCounseling(true);
        subsequentVisitMessage2.setPmtctPreTestCounseling(true);
        subsequentVisitMessage2.setPmtctTestResult(true);
        subsequentVisitMessage2.setItnUse(false);
        subsequentVisitMessage2.setComplaints("Complaint string 2");
        subsequentVisitMessage2.setRemarks("Remarks string 2");
    }

    @Test
    public void testControllerAddInitialVisit() {
        // this is the best way to test these - creating another patient wrapper would require duplication
        // and overhead, and although this is long, it works so that if field data is changed above this logic
        // will still function


        // tests the adding of initial visit data with the add initial visit method in the controller


        pregnancyService.addInitialVisit(patientPregnancyMessage, subsequentVisitMessage);

        PatientWrapper patientWrapper = patientWrapperService.getPatientWrapper(patient);
        assertEquals(1, patientWrapperService.getPatientWrapper(patient).getPregnancies().size());
        Pregnancy antenatalModel = patientWrapper.getPregnancies().get(0);

        Parity parity = new Parity(Integer.parseInt(patientPregnancyMessage.getParity()), Integer.parseInt(patientPregnancyMessage.getGravidity()));
        assertEquals("Retrieved parity object does not match set parity information", parity.toString(), antenatalModel.getParity().toString());

        // date format from toString different than sent date above
        assertEquals("Retrieved date does not match set date", "2021-03-28", antenatalModel.getExpectedDayOfDelivery().toString());
        assertEquals("Retrieved HemoglobinReg does not match set HemoglobinReg", patientPregnancyMessage.getHemoglobinReg(), ""+antenatalModel.getHemoglobinLevelAtReg());
        assertEquals("Retrieved blood group does not match set blood group", patientPregnancyMessage.getBloodGroup(), antenatalModel.getBloodGroup());

        Sickling sickling = antenatalModel.getSickling();
        assertEquals("Retrieved sickling state does not match set sickling state", patientPregnancyMessage.isSickling(), sickling.getSickling());
        assertEquals("Retrieved sickling type does not match set sickling type", patientPregnancyMessage.getSicklingType(), sickling.getType());

        assertEquals("Retrived male involvement does not match set male involvement", patientPregnancyMessage.getMaleInvolvement(), antenatalModel.isMaleInvolvement());
        assertEquals("Retrieved VDRL does not match set VDRL", patientPregnancyMessage.isVDRL(), antenatalModel.getVDRL());

    }

    @Test
    public void testControllerAddInitialVisitSubsequentVisitData() {
        // tests the adding of subsequent visit data with the add initial visit method in the controller

        pregnancyService.addInitialVisit(patientPregnancyMessage, subsequentVisitMessage);
        PatientWrapper patientWrapper = patientWrapperService.getPatientWrapper(patient);
        Pregnancy antenatalModel = patientWrapper.getPregnancies().get(0);
        Visit subsequentVisit = antenatalModel.getSubsequentVisits().get(0);

        // date format from toString different than sent date above
        assertEquals("Retrieved date does not match set date", "2021-03-28", subsequentVisit.getDate().toString());
        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(subsequentVisitMessage.getSystolic()), Integer.parseInt(subsequentVisitMessage.getDiastolic()));
        assertEquals("Retrieved visit blood pressure object does not match set blood pressure", bloodPressure.toString(), subsequentVisit.getBloodPressure().toString());
        assertEquals("Retrieved visit weight does not match set weight", subsequentVisitMessage.getWeight(), ""+subsequentVisit.getWeight());
        assertEquals("Retrieved visit fundal height does not match set fundal height", subsequentVisitMessage.getFundalHeight(), ""+subsequentVisit.getFundalHeight());

        assertEquals("Retrieved visit gestation week does not match set gestation week", subsequentVisitMessage.getGestationWeek(), ""+subsequentVisit.getGestationWeek());

        assertEquals("Retrieved visit gestation week does not match set gestation week", subsequentVisitMessage.getHemoglobin36Wks(), ""+antenatalModel.getHemoglobinLevelAt36Wks());
        assertEquals("Retrieved visit blood film does not match set blood film", subsequentVisitMessage.getBloodFilm(), subsequentVisit.getBloodFilm());
        assertEquals("Retrieved visit refurred does not match set refurred", subsequentVisitMessage.getRefurred(), subsequentVisit.getRefurred());

        TT tt = antenatalModel.getTt();
        assertEquals("Retrieved tt first dose does not match set tt first dose", subsequentVisitMessage.getTtFirstDose(), tt.getFirstDose());
        assertEquals("Retrieved tt second dose does not match set tt second dose", subsequentVisitMessage.getTtSecondDose(), tt.getSecondDose());
        assertEquals("Retrieved tt third dose does not match set tt third dose", subsequentVisitMessage.getTtThirdDose(), tt.getThirdDose());
        assertEquals("Retrieved tt protected does not match set tt protected", subsequentVisitMessage.getTtProtectedNotDosed(), tt.getProtectedNotDosed());

        IPT ipt = antenatalModel.getIpt();
        assertEquals("Retrieved IPT first dose not match set IPT first dose", subsequentVisitMessage.getIptFirstDose(), ipt.getFirstDose());
        assertEquals("Retrieved IPT second dose not match set IPT second dose", subsequentVisitMessage.getIptSecondDose(), ipt.getSecondDose());
        assertEquals("Retrieved IPT third dose not match set IPT third dose", subsequentVisitMessage.getIptThirdDose(), ipt.getThirdDose());

        PMTCT pmtct = antenatalModel.getPmtct();
        assertEquals("Retrieved PMTCT ARV dose not match set PMTCT ARV", subsequentVisitMessage.isPmtctARV(), pmtct.getARV());
        assertEquals("Retrieved PMTCT post test not match set PMTCT post test", subsequentVisitMessage.isPmtctPostTestCounseling(), pmtct.getPostTestCounseling());
        assertEquals("Retrieved PMTCT pre test not match set PMTCT pre test", subsequentVisitMessage.isPmtctPreTestCounseling(), pmtct.getPreTestCounseling());
        assertEquals("Retrieved PMTCT test result not match set PMTCT test result", subsequentVisitMessage.isPmtctTestResult(), pmtct.getTestResult());

        assertEquals("Retrieved ITN use does not match set itn use", subsequentVisitMessage.getItnUse(), antenatalModel.getItnUse());

        assertEquals("Retrieved complaints does not match set complaints", subsequentVisitMessage.getComplaints(), antenatalModel.getComplaints());
        assertEquals("Retrieved remarks does not match set remarks", subsequentVisitMessage.getRemarks(), antenatalModel.getRemarks());

    }

    @Test
    public void testControllerAddSubsequentVisit() {
        // tests the adding of subsequent visit data with the add subsequent visit method in the controller, and proper changing of data in the model

        pregnancyService.addInitialVisit(patientPregnancyMessage, subsequentVisitMessage);
        pregnancyService.addSubsequentVisit(subsequentVisitMessage2);
        PatientWrapper patientWrapper = patientWrapperService.getPatientWrapper(patient);
        Pregnancy antenatalModel = patientWrapper.getPregnancies().get(0);
        Visit subsequentVisit = antenatalModel.getSubsequentVisits().get(1);

        // date format from toString different than sent date above
        assertEquals("Retrieved date does not match set date", "2021-04-28", subsequentVisit.getDate().toString());
        BloodPressure bloodPressure = new BloodPressure(Integer.parseInt(subsequentVisitMessage2.getSystolic()), Integer.parseInt(subsequentVisitMessage2.getDiastolic()));
        assertEquals("Retrieved visit blood pressure object does not match set blood pressure", bloodPressure.toString(), subsequentVisit.getBloodPressure().toString());
        assertEquals("Retrieved visit weight does not match set weight", subsequentVisitMessage2.getWeight(), ""+subsequentVisit.getWeight());
        assertEquals("Retrieved visit fundal height does not match set fundal height", subsequentVisitMessage2.getFundalHeight(), ""+subsequentVisit.getFundalHeight());

        assertEquals("Retrieved visit gestation week does not match set gestation week", subsequentVisitMessage2.getGestationWeek(), ""+subsequentVisit.getGestationWeek());

        assertEquals("Retrieved visit gestation week does not match set gestation week", subsequentVisitMessage2.getHemoglobin36Wks(), ""+antenatalModel.getHemoglobinLevelAt36Wks());
        assertEquals("Retrieved visit blood film does not match set blood film", subsequentVisitMessage2.getBloodFilm(), subsequentVisit.getBloodFilm());
        assertEquals("Retrieved visit refurred does not match set refurred", subsequentVisitMessage2.getRefurred(), subsequentVisit.getRefurred());

        TT tt = antenatalModel.getTt();
        assertEquals("Retrieved tt first dose does not match set tt first dose", subsequentVisitMessage2.getTtFirstDose(), tt.getFirstDose());
        assertEquals("Retrieved tt second dose does not match set tt second dose", subsequentVisitMessage2.getTtSecondDose(), tt.getSecondDose());
        assertEquals("Retrieved tt third dose does not match set tt third dose", subsequentVisitMessage2.getTtThirdDose(), tt.getThirdDose());
        assertEquals("Retrieved tt protected does not match set tt protected", subsequentVisitMessage2.getTtProtectedNotDosed(), tt.getProtectedNotDosed());

        IPT ipt = antenatalModel.getIpt();
        assertEquals("Retrieved IPT first dose not match set IPT first dose", subsequentVisitMessage2.getIptFirstDose(), ipt.getFirstDose());
        assertEquals("Retrieved IPT second dose not match set IPT second dose", subsequentVisitMessage2.getIptSecondDose(), ipt.getSecondDose());
        assertEquals("Retrieved IPT third dose not match set IPT third dose", subsequentVisitMessage2.getIptThirdDose(), ipt.getThirdDose());

        PMTCT pmtct = antenatalModel.getPmtct();
        assertEquals("Retrieved PMTCT ARV dose not match set PMTCT ARV", subsequentVisitMessage2.isPmtctARV(), pmtct.getARV());
        assertEquals("Retrieved PMTCT post test not match set PMTCT post test", subsequentVisitMessage2.isPmtctPostTestCounseling(), pmtct.getPostTestCounseling());
        assertEquals("Retrieved PMTCT pre test not match set PMTCT pre test", subsequentVisitMessage2.isPmtctPreTestCounseling(), pmtct.getPreTestCounseling());
        assertEquals("Retrieved PMTCT test result not match set PMTCT test result", subsequentVisitMessage2.isPmtctTestResult(), pmtct.getTestResult());

        assertEquals("Retrieved ITN use does not match set itn use", subsequentVisitMessage2.getItnUse(), antenatalModel.getItnUse());

        assertEquals("Retrieved complaints does not match set complaints", subsequentVisitMessage2.getComplaints(), antenatalModel.getComplaints());
        assertEquals("Retrieved remarks does not match set remarks", subsequentVisitMessage2.getRemarks(), antenatalModel.getRemarks());

    }

    @After
	public void tearDown() throws FileNotFoundException {
        // java.nio.file.FileSystemException The process cannot access the file because it is being used by another process
        new PrintWriter("test.json").append("{}").close();
		Path path = FileSystems.getDefault().getPath(".", "test.json");
		try {
            Files.delete(path);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", path);
		} catch (IOException x) {
		    System.err.println(x);
		}
	}
}
