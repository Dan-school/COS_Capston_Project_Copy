package edu.usm.cos420.antenatal.service;

import static org.junit.Assert.*;

import edu.usm.cos420.antenatal.model.*;
import edu.usm.cos420.antenatal.dao.*;
import edu.usm.cos420.antenatal.dao.domain.*;


import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

import edu.usm.cos420.antenatal.service.impl.PatientWrapperServiceDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestService {
	GenericDao<Long, PatientWrapper> dao;
	PatientWrapperDao patientWrapperDao;
    PatientWrapperService patientWrapperService;
    
	@Before
	public void setupData() {
	   dao = new ObjectStreamDao<Long, PatientWrapper>("_test2.ser");
	   patientWrapperDao = new PatientWrapperDao(dao);
	   patientWrapperService = new PatientWrapperServiceDao(patientWrapperDao);
	}
	
	@Test
    public void testAddPatientWrapper() {
        PatientWrapper retrievedItem;
        PatientWrapper input = new PatientWrapper(new Patient(0L), new ArrayList<Pregnancy>());
        
        patientWrapperService.addPatientWrapper(input);
        
        retrievedItem = patientWrapperService.getPatientWrapper(input.getId());
                
        assertNotNull("Dao returns a null item.", retrievedItem);
		assertEquals("Patient name does not match sent patient name", retrievedItem.getPatient().getId(), input.getPatient().getId());
	}
	
	

	@After
	public void tearDown()
	{

		Path path = FileSystems.getDefault().getPath(".", "_test2.ser");
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

