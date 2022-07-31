package edu.usm.cos420.antenatal.messengers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.*;

public class PatientPregnancyMessage {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Parity    
    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,2}$", message =  "Invalid value. Range 0 - 99")
    private String gravidity;

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,2}$", message =  "Invalid value. Range 0 - 99")
    private String parity;

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$", message = "Invalid date. Date format DD/MM/YYYY")
    private String expectedDayOfDelivery;                // unparsed date 

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^([0-9]{1,3}|[0-9]{0,3}\\.[0-9]{1,2})$", message = "Invalid value")
    private String hemoglobinReg;                       

    //@NotBlank(message = "Field required")                // Need more info https://www.drmyhill.co.uk/wiki/Urine_MULTISTIX_analysis_interpretation
    private String urineTest;

   @NotBlank(message = "Field required")                // Per manual for registers it's another name for blood type
   @Pattern(regexp="^A\\+$|^A\\-$|^B\\+$|^B\\-$|^O\\+$|^O\\-$|^AB\\+$|^AB\\-$", message = "Enter a valid blood type")
    private String bloodGroup;

    // Sickling
    private boolean sickling;

    @Pattern(regexp = "^AS$|^SS$|^SC$|^$")
    private String sicklingType;                         // unparsed sickling type


    private boolean maleInvolvement;

    private boolean VDRL;

    @AssertTrue(message = "If sickling trait indicate which kind")
    private boolean issicklingGroup() {
        return (!sickling && sicklingType.isBlank()) || (sickling && !sicklingType.isBlank());
    } 

    @AssertTrue(message = "Invalid date. Date format DD/MM/YYYY")
    private boolean isexpectedDayOfDelivery() {
        try {
            LocalDate.parse(expectedDayOfDelivery, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    } 

    public String getGravidity() {
        return gravidity;
    }

    public void setGravidity(String gravidity) {
        this.gravidity = gravidity;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getExpectedDayOfDelivery() {
        return expectedDayOfDelivery;
    }

    public void setExpectedDayOfDelivery(String expectedDayOfDelivery) {
        this.expectedDayOfDelivery = expectedDayOfDelivery;
    }

    public String getHemoglobinReg() {
        return hemoglobinReg;
    }

    public void setHemoglobinReg(String hemoglobinReg) {
        this.hemoglobinReg = hemoglobinReg;
    }

    public String getUrineTest() {
        return urineTest;
    }

    public void setUrineTest(String urineTest) {
        this.urineTest = urineTest;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public boolean isSickling() {
        return sickling;
    }

    public void setSickling(boolean sickling) {
        this.sickling = sickling;
    }

    public String getSicklingType() {
        return sicklingType;
    }

    public void setSicklingType(String sicklingType) {
        this.sicklingType = sicklingType;
    }

    public boolean isVDRL() {
        return VDRL;
    }

    public boolean isMaleInvolvement() {
        return this.maleInvolvement;
    }

    public boolean getMaleInvolvement() {
        return this.maleInvolvement;
    }

    public void setMaleInvolvement(boolean maleInvolvement) {
        this.maleInvolvement = maleInvolvement;
    }

    public void setVDRL(boolean vDRL) {
        VDRL = vDRL;
    }

}
