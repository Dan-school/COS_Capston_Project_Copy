package edu.usm.cos420.antenatal.model;

import java.io.Serializable;

public class PMTCT implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    private boolean preTestCounseling;
    private boolean testResult;
    private boolean postTestCounseling;
    private boolean ARV;

    public PMTCT() {}

    public PMTCT(boolean preTestCounseling, boolean testResult, boolean postTestCounseling, boolean ARV) {
        this.preTestCounseling = preTestCounseling;
        this.testResult = testResult;
        this.postTestCounseling = postTestCounseling;
        this.ARV = ARV;
    }

    public boolean getPreTestCounseling() {
        return this.preTestCounseling;
    }

    public void setPreTestCounseling(boolean ptc) {
        this.preTestCounseling = ptc;
    }

    public boolean getTestResult() {
        return this.testResult;
    }

    public void setTestResult(boolean tr) {
        this.testResult = tr;
    }

    public boolean getPostTestCounseling() {
        return this.postTestCounseling;
    }

    public void setPostTestCounseling(boolean ptc) {
        this.postTestCounseling = ptc;
    }

    public boolean getARV() {
        return this.ARV;
    }

    public void setARV(boolean arv) {
        this.ARV = arv;
    }

    
}
