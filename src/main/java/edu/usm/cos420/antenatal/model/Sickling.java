package edu.usm.cos420.antenatal.model;

import java.io.Serializable;

public class Sickling implements Serializable {

    private static final long serialVersionUID = 7526472295622776147L;

    private boolean sicklingPositive;
    public String type;

    public Sickling() {}

    public Sickling(boolean sickling, String type) {
        this.sicklingPositive = sickling;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getSickling() {
        return sicklingPositive;
    }

    public void setSickling(boolean sickling) {
        this.sicklingPositive = sickling;        
    }

    public String toString() {
        return "Sicking " + sicklingPositive + (sicklingPositive ? type : "");
    }

}
