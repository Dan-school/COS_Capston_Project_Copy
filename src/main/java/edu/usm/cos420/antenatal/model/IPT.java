package edu.usm.cos420.antenatal.model;

import java.io.Serializable;


public class IPT implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;

    private boolean firstDose;
    private boolean secondDose;
    private boolean thirdDose;

    public IPT() {}

    public IPT(boolean firstDose, boolean secondDose, boolean thirdDose) {
        this.firstDose = firstDose;
        this.secondDose = secondDose;
        this.thirdDose = thirdDose;
    }

    public boolean getFirstDose() {
        return this.firstDose;
    }

    public void setFirstDose(boolean d1) {
        this.firstDose = d1;
    }

    public boolean getSecondDose() {
        return this.secondDose;
    }

    public void setSecondDose(boolean d2) {
        this.secondDose = d2;
    }

    public boolean getThirdDose() {
        return this.thirdDose;
    }

    public void setThirdDose(boolean d3) {
        this.thirdDose = d3;
    }

}
