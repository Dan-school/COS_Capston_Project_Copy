package edu.usm.cos420.antenatal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import edu.usm.cos420.antenatal.controller.AntenatalController;
import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.model.Pregnancy;
import edu.usm.cos420.antenatal.service.PatientWrapperService;
import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceDao;
import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceSQL;
import edu.usm.cos420.antenatal.view.PregnancySelectionView;
import edu.usm.cos420.antenatal.view.PregnancyView;

/**
 * Top level application class
 */
public class AntenatalApp {

    private enum persistenceMode {
        LOCAL_POSTGRES,
        REMOTE_POSTGRES,
        LOCAL_JSON
    }

    private static final persistenceMode persistence = persistenceMode.REMOTE_POSTGRES;

    public static void main(String[] args) {
        PatientWrapperService patientWrapperService = null;
        // this is used to load differenet properties going to have to pass it or make
        // it global
        Properties dbProp = new Properties();
        if (persistence == persistenceMode.LOCAL_POSTGRES) {
            try (InputStream in = new FileInputStream("src" + File.separator + "main" + File.separator + "java" + File.separator + "edu" + File.separator + "usm" + File.separator + "cos420" + File.separator + "antenatal" + File.separator + "resources" + File.separator + "databaseLocal.properties")) {
                dbProp.load(in);
                patientWrapperService = new PatientWrapperServiceSQL(dbProp.getProperty("sql.instanceName"), dbProp.getProperty("sql.dbName"), dbProp.getProperty("sql.userName"), dbProp.getProperty("sql.password"), true);
            } catch (IOException e) {
                System.out.println("No DB properties exception shown: " + e);
            } catch (SQLException e) {
                System.err.println("Invalid properties in databaseLocal.properties: " + e);
            }
        } else if (persistence == persistenceMode.REMOTE_POSTGRES) {
            try (InputStream in = new FileInputStream("src" + File.separator + "main" + File.separator + "java" + File.separator + "edu" + File.separator + "usm" + File.separator + "cos420" + File.separator + "antenatal" + File.separator + "resources" + File.separator + "databaseServer.properties")) {
                dbProp.load(in);
                patientWrapperService = new PatientWrapperServiceSQL(dbProp.getProperty("sql.instanceName"), dbProp.getProperty("sql.dbName"), dbProp.getProperty("sql.userName"), dbProp.getProperty("sql.password"), false);
            } catch (IOException e) {
                System.out.println("No DB properties exception shown: " + e);
            } catch (SQLException e) {
                System.err.println("Invalid properties in databaseServer.properties: " + e);
            }
        } else if (persistence == persistenceMode.LOCAL_JSON) {
            patientWrapperService = new PatientWrapperServiceDao();
        }

        // all of this is just here because we can not integrate the Consulting register
        // in its current state

        Scanner sc = new Scanner(System.in);
        // Awaiting full consulting register integration
        // The current plan if for consulting register to pass a valid patient
        // This placeholder is a valid patient who likes their privacy. Same patient
        // every request
        ArrayList<Patient> patients = new ArrayList<Patient>();
        patients.add(new Patient(0L, 4647348126L, "Alice", "Jones", "1 Main Street, Portland, Maine", 2075555592,
                "19/04/1994", 'F'));
        patients.add(new Patient(1L, 4367357377L, "Bob", "Smith", "87 Main Street, Portland, Maine", 2075555591,
                "15/05/1996", 'M'));
        patients.add(new Patient(2L, 4239867000L, "Charlie", "Sebastian", "278 Main Street, Portland, Maine",
                2075555500, "16/04/1990", 'M'));

        System.out.println("Please select a patient");
        for (Patient p : patients) {
            System.out.println(p.toString());
        }
        int selection = 0;
        String userInput;
        boolean passes = false;
        while (!passes) { // input verification only works if IDs are 0-n
            userInput = sc.nextLine();
            try {
                selection = Integer.parseInt(userInput);
                passes = true;
            } catch (Exception e) {
                System.out.println("Not a number. Please try again.");
                continue;
            }

            if (selection < 0 || selection > patients.size() - 1) {
                System.out.println("Invalid ID. Please try again.");
                passes = false;
            }
        }

        PatientWrapper pW = new PatientWrapper(patients.get(selection), new ArrayList<Pregnancy>());

        PatientWrapper controllerPatient = patientWrapperService.getPatientWrapper(patients.get(selection));

        if (controllerPatient == null) {
            patientWrapperService.addPatientWrapper(pW);
            controllerPatient = pW;
        }

        PregnancySelectionView pregnancySelectionView = new PregnancySelectionView();
        PregnancyView registerView = new PregnancyView();
        AntenatalController controller = new AntenatalController(patientWrapperService, registerView,
                pregnancySelectionView, controllerPatient.getPatient());
    }
}
