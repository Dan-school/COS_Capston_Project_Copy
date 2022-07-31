package edu.usm.cos420.antenatal.service;

import edu.usm.cos420.antenatal.model.Patient;
import edu.usm.cos420.antenatal.model.PatientWrapper;

import java.util.List;

public interface PatientWrapperService {

    /**
     * Adds PatientWrapper object
     * 
     * @param model PatientWrapper to be added
     */
    void addPatientWrapper(PatientWrapper model);

    /**
     * Updates PatientWrapper in database
     * @param model PatientWrapper to be updated
     */
    void updatePatientWrapper(PatientWrapper model);
    
    /**
     * Updates PatientWrapper in database based on ID
     * @param id PatientWrapper to be updated
     */
    void updatePatientWrapper(Long id);
    
    /**
     * @return list of all PatientWrappers in database
     */
    List<PatientWrapper> getPatientWrappers();

    /**
     * Gets the PatientWrapper for a specified patient
     * @param patient Patient to receive PatientWrapper for
     * @return PatientWrapper for a single patient in database
     */
    PatientWrapper getPatientWrapper(Patient patient);
    
    /**
     * @param id Patient to receive PatientWrapper for
     * @return PatientWrapper based on ID inputted, null if no PatientWrapper with that ID exists
     */
    PatientWrapper getPatientWrapper(Long id);

    /**
	 * Calculate the maximum ID value of elements in the repository
	 *
	 * @return the maximum id of an PatientWrapper in the repository
	 */
	Long maxItemId();
}
