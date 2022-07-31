package edu.usm.cos420.antenatal.model;

import java.io.Serializable;


public class BloodPressure implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;
    
    private int systolic;
    private int diastolic;

    public BloodPressure() {
        this.systolic = 0;
        this.diastolic = 0;
    }

    public BloodPressure(int systolic, int diastolic) {
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    @Override
    public String toString() {
            return String.format("%d/%d mm Hg", systolic, diastolic);
        }
}
