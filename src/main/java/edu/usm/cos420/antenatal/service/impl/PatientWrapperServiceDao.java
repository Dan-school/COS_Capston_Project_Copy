package edu.usm.cos420.antenatal.service.impl;

import edu.usm.cos420.antenatal.dao.domain.PatientWrapperDao;
import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;
import edu.usm.cos420.antenatal.service.PatientWrapperService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatientWrapperServiceDao implements PatientWrapperService {

    PatientWrapperDao dao;

    /**
     * Default Constructor creates a default PatientWrapperDao object
     */
    public PatientWrapperServiceDao() {
        this.dao = new PatientWrapperDao();
    }

    /**
     * Constructor with the DAO provided
     *
     * @param dao Data Access Object to use in the service
     */
    public PatientWrapperServiceDao(PatientWrapperDao dao) {
        this.dao = dao;
    }

    /**
     * Adds PatientWrapper object
     *
     * @param model PatientWrapper to be added
     */
    public void addPatientWrapper(PatientWrapper model) {
        model.setSerialNumber(maxItemId()+1);
        dao.add(model);
    }

    /**
     * Updates PatientWrapper in database
     * @param model PatientWrapper to be updated
     */
    public void updatePatientWrapper(PatientWrapper model) {
        dao.update(model);
    }

    /**
     * Updates PatientWrapper in database based on ID
     * @param id PatientWrapper to be updated
     */
    public void updatePatientWrapper(Long id) {
        dao.update(getPatientWrapper(id));
    }

    /**
     * @return list of all PatientWrappers in database
     */
    public List<PatientWrapper> getPatientWrappers() {
        return dao.list();
    }

    /**
     * Gets the PatientWrapper for a specified patient
     * @param patient Patient to receive PatientWrapper for
     * @return PatientWrapper for a single patient in database
     */
    public PatientWrapper getPatientWrapper(Patient patient) {
        for (PatientWrapper patientWrapper : dao.list()) {
            if (patientWrapper.getPatient().getId().equals(patient.getId()))
                return patientWrapper;
        }
        return null;
    }

    /**
     * @param id
     * @return PatientWrapper based on ID inputted, null if no PatientWrapper with that ID exists
     */
    public PatientWrapper getPatientWrapper(Long id) {
        for (PatientWrapper patient : dao.list()) {
            if (patient.getId().compareTo(id) == 0)
                return patient;
        }
        return null;
    }

    /**
     * Calculate the maximum ID value of elements in the repository
     *
     * @return the maximum id of an PatientWrapper in the repository
     */
    public Long maxItemId() {
        List<PatientWrapper> patientList = dao.list();
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
