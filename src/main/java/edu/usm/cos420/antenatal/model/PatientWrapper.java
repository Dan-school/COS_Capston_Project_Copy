package edu.usm.cos420.antenatal.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientWrapper implements Serializable{

    private static final long serialVersionUID = 7526472295622776147L;
    private static long COUNTER = 0L;
    private long serialNumber;

    ArrayList<Pregnancy> pregnancies;
    Patient patient;

    public Long getId() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }


    public PatientWrapper(Patient patient, ArrayList<Pregnancy> pregnancies) {
        serialNumber = generateSerialNumber();
        this.pregnancies = pregnancies;
        this.patient = patient;
    }

    public List<Pregnancy> getPregnancies() {
        return pregnancies;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPregnancies(ArrayList<Pregnancy> pregnancies) {
        this.pregnancies = pregnancies;
    }
    

    // for auto-generation of serial numbers
    private long generateSerialNumber()
    {
        return COUNTER++;
    }

    

}
