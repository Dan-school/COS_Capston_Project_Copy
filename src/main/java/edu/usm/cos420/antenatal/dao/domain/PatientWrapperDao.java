package edu.usm.cos420.antenatal.dao.domain;

import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import edu.usm.cos420.antenatal.dao.GenericDao;
import edu.usm.cos420.antenatal.dao.JsonDao;
import edu.usm.cos420.antenatal.model.PatientWrapper;


public class PatientWrapperDao {
    private GenericDao<Long, PatientWrapper> patientWrapperDao;

    /**
     * Default constructor creates an Json file called customers.json TypeToken allows
     * the GSON parser to map to/from JSON to objects
     */
    public PatientWrapperDao() {
        Type t = new TypeToken<Map<Long, PatientWrapper>>() {
        }.getType();
        patientWrapperDao = new JsonDao<>("antenatalModel.json", t);
    }

    /**
     * Constructor where the filename is provided
     */
    public PatientWrapperDao(String fileName) {
        Type t = new TypeToken<Map<Long, PatientWrapper>>() {
        }.getType();
        patientWrapperDao = new JsonDao<>(fileName, t);
    }

    /**
     * Support for other DAOs is provided
     * 
     * @param dao a Data Access Object class that implements GenericDao<Long,PatientWrapper>
     */
    public PatientWrapperDao(GenericDao<Long, PatientWrapper> dao) {
        patientWrapperDao = dao;
    }

    /**
     * Returns the DAO used in the class
     * 
     * @return a DAO that implements GenericDao<Long,PatientWrapper>
     */
    public GenericDao<Long, PatientWrapper> getAntenatalModelDao() {
        return patientWrapperDao;
    }

    /**
     * Add a PatientWrapper to the DAO repository
     * 
     * @param entity any PatientWrapper object
     */
    public void add(PatientWrapper entity) {
        patientWrapperDao.add(entity.getId(), entity);
    }

    /**
     * Update a PatientWrapper in the DAO repository
     * 
     * @param entity any PatientWrapper object
     */
    public void update(PatientWrapper entity) {
        patientWrapperDao.update(entity.getId(), entity);
    }

    /**
     * Remove a PatientWrapper in the DAO repository
     * 
     * @param id of the PatientWrapper object to remove
     */

    public void remove(Long id) {
        patientWrapperDao.remove(id);
    }

    /**
     * Find a PatientWrapper in the DAO repository
     * 
     * @param id of the PatientWrapper object to locate
     * @return the PatientWrapper with id field equal to key
     */
    public PatientWrapper find(Long key) {
        return patientWrapperDao.find(key);
    }

    /**
     * Generate a list of PatientWrapper in the DAO repository
     * 
     * @return List of PatientWrapper
     */

    public List<PatientWrapper> list() {
        return patientWrapperDao.list();
    }
}
