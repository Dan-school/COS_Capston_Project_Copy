package edu.usm.cos420.antenatal.service.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import edu.usm.cos420.antenatal.dao.domain.PatientWrapperSQLDao;
import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.service.PatientWrapperService;

public class PatientWrapperServiceSQL implements PatientWrapperService {

    PatientWrapperSQLDao dao;
    private final String URL_FORMAT_LOCAL = "jdbc:postgresql://%s/%s?user=%s&password=%s";
    private final String URL_FORMAT_REMOTE = "jdbc:postgresql://google/%s?cloudSqlInstance=%s&socketFactory=com.google.cloud.sql.postgres.SocketFactory&user=%s&password=%s"; // TBD

     
    public PatientWrapperServiceSQL(String instanceName, String dbName, String user, String pass, boolean local) throws SQLException {
        if (local)
            this.dao = new PatientWrapperSQLDao(String.format(URL_FORMAT_LOCAL, instanceName, dbName, user, pass));
        else
            this.dao = new PatientWrapperSQLDao(String.format(URL_FORMAT_REMOTE,
                    dbName, instanceName, user, pass));//TBD
        
    }

    /**
     * Constructor with the DAO provided
     *
     * @param dao Data Access Object to use in the service
     */
    public PatientWrapperServiceSQL(PatientWrapperSQLDao dao) {
        this.dao = dao;
    }

    /**
     * Adds PatientWrapper object
     *
     * @param model PatientWrapper to be added
     */
    public void addPatientWrapper(PatientWrapper model) {
        model.setSerialNumber(maxItemId()+1);
        try {
            dao.createPatientWrapper(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates PatientWrapper in database
     * @param model PatientWrapper to be updated
     */
    public void updatePatientWrapper(PatientWrapper model) {
        try {
            dao.updatePatientWrapper(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates PatientWrapper in database based on ID
     * @param id PatientWrapper to be updated
     */
    public void updatePatientWrapper(Long id) {
        try { // this method is really dumb and doesn't appear to do anything
        // but I can't get rid of it because of the interface
            dao.updatePatientWrapper(getPatientWrapper(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the PatientWrapper for a specified patient
     * @param patient Patient to receive PatientWrapper for
     * @return PatientWrapper for a single patient in database
     */
    public PatientWrapper getPatientWrapper(Patient patient) {
        List<PatientWrapper> patientWrappers;
        try {
            patientWrappers = dao.listWrappers();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }        
        for (PatientWrapper patientWrapper : patientWrappers) {
            if (patientWrapper.getPatient().getId().equals(patient.getId()))
                return patientWrapper;
        }
        return null;
    }

    /**
     * @return list of all PatientWrappers in database
     */
    public List<PatientWrapper> getPatientWrappers() {
        try {
            return dao.listWrappers();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
    }

    /**
     * Gets the PatientWrapper for a specified patient
     * @param patient Patient to receive PatientWrapper for
     * @return PatientWrapper for a single patient in database
     */
    public PatientWrapper getPatientWrapper(PatientWrapper patient) {
        return this.getPatientWrapper(patient.getId());
    }

    /**
     * @param id
     * @return PatientWrapper based on ID inputted, null if no PatientWrapper with that ID exists
     */
    public PatientWrapper getPatientWrapper(Long id) {
        PatientWrapper patientWrapper = null;
        try {
            patientWrapper = dao.readPatientWrapper(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientWrapper;
    }

    /**
     * Calculate the maximum ID value of elements in the repository
     *
     * @return the maximum id of an PatientWrapper in the repository
     */
    public Long maxItemId() {

        List<PatientWrapper> patientList = null;
        try {
            patientList = dao.listWrappers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Long max = 0L;
        if (patientList.isEmpty())
            return max;
        else {
            Iterator<PatientWrapper> iter = patientList.iterator();
            max = iter.next().getId();
            while (iter.hasNext())
            {
                PatientWrapper aPatient = iter.next();
                if (aPatient.getId() > max)
                    max = aPatient.getId();
            }
            return max;
        }
    }
}
