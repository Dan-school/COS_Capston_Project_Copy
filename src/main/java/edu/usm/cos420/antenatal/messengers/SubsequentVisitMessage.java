package edu.usm.cos420.antenatal.messengers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.*;
public class SubsequentVisitMessage {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String patient;                 // Until we receive more information from consulting register we'll need to pass a patient field ourselves

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$", message = "Date format DD/MM/YYYY")
    private String date;

    // Blood Pressure
    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,3}$", message =  "Invalid value. Range 0 - 999")
    private String systolic;

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,3}$", message =  "Invalid value. Range 0 - 999")
    private String diastolic;

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,3}$", message =  "Invalid value. Range 0 - 999")
    private String weight;

    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,3}$", message =  "Invalid value. Range 0 - 999")
    private String fundalHeight;

    //TODO add the pattern 
    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-9]{1,3}$", message =  "Invalid value. Range 0 - 999")
    private String patientHeight; 
    
    //TODO add the pattern
    // Here's how to actually do 0-59
    @NotBlank(message = "Field required")
    @Pattern(regexp = "^[0-5]{1}[0-9]{1}$|^[0-9]{1}$", message =  "Invalid value. Range 0 - 59")
    private String gestationWeek;

    //    @NotBlank(message = "Field required")
    @Pattern(regexp = "^([0-9]{1,3}|[0-9]{0,3}\\.[0-9]{1,2})$|^$", message = "Invalid value")
    private String hemoglobin36Wks;                      // This seems unlikely to be filled out in an initial visit

    private boolean bloodFilm;              

    private String refurred; // unsure of spec on this


    // TT
    private boolean ttFirstDose;

    private boolean ttSecondDose;

    private boolean ttThirdDose;

    private boolean ttProtectedNotDosed;

    // IPT
    private boolean iptFirstDose;

    private boolean iptSecondDose;

    private boolean iptThirdDose;

    //PMTCT
    private boolean pmtctPreTestCounseling;

    private boolean pmtctTestResult;

    private boolean pmtctPostTestCounseling;

    private boolean pmtctARV;

    private boolean itnUse;

    //@NotBlank(message = "Field required")
    private String complaints;

    //@NotBlank(message = "Field required")
    private String remarks;


    @AssertTrue(message = "Invalid date")
    private boolean isdate() {
        try {
            LocalDate.parse(date, dateTimeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    } 
    
    public String getPatientHeight() {
        return this.patientHeight;
    }

    public void setPatientHeight(String patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getGestationWeek() {
        return this.gestationWeek;
    }

    public void setGestationWeek(String gestationWeek) {
        this.gestationWeek = gestationWeek;
    }

    public String getHemoglobin36Wks() {
        return hemoglobin36Wks;
    }

    public void setHemoglobin36Wks(String hemoglobin36Wks) {
        this.hemoglobin36Wks = hemoglobin36Wks;
    }

    public boolean getTtFirstDose() {
        return this.ttFirstDose;
    }

    public void setTtFirstDose(boolean ttFirstDose) {
        this.ttFirstDose = ttFirstDose;
    }

    public boolean getTtSecondDose() {
        return this.ttSecondDose;
    }

    public void setTtSecondDose(boolean ttSecondDose) {
        this.ttSecondDose = ttSecondDose;
    }

    public boolean getTtThirdDose() {
        return this.ttThirdDose;
    }

    public void setTtThirdDose(boolean ttThirdDose) {
        this.ttThirdDose = ttThirdDose;
    }

    public boolean getTtProtectedNotDosed() {
        return this.ttProtectedNotDosed;
    }

    public void setTtProtectedNotDosed(boolean ttProtectedNotDosed) {
        this.ttProtectedNotDosed = ttProtectedNotDosed;
    }

    public boolean getIptFirstDose() {
        return this.iptFirstDose;
    }

    public void setIptFirstDose(boolean iptFirstDose) {
        this.iptFirstDose = iptFirstDose;
    }

    public boolean getIptSecondDose() {
        return this.iptSecondDose;
    }

    public void setIptSecondDose(boolean iptSecondDose) {
        this.iptSecondDose = iptSecondDose;
    }


    public boolean getIptThirdDose() {
        return this.iptThirdDose;
    }

    public void setIptThirdDose(boolean iptThirdDose) {
        this.iptThirdDose = iptThirdDose;
    }


    public boolean getItnUse() {
        return this.itnUse;
    }

    public void setItnUse(boolean itnUse) {
        this.itnUse = itnUse;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSystolic() {
        return systolic;
    }

    public void setSystolic(String systolic) {
        this.systolic = systolic;
    }

    public String getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(String diastolic) {
        this.diastolic = diastolic;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFundalHeight() {
        return fundalHeight;
    }

    public void setFundalHeight(String fundalHeight) {
        this.fundalHeight = fundalHeight;
    }

    public boolean getBloodFilm() {
        return bloodFilm;
    }

    public void setBloodFilm(boolean bloodFilm) {
        this.bloodFilm = bloodFilm;
    }

    public boolean isPmtctPreTestCounseling() {
        return pmtctPreTestCounseling;
    }

    public void setPmtctPreTestCounseling(boolean pmtctPreTestCounseling) {
        this.pmtctPreTestCounseling = pmtctPreTestCounseling;
    }

    public boolean isPmtctTestResult() {
        return pmtctTestResult;
    }

    public void setPmtctTestResult(boolean pmtctTestResult) {
        this.pmtctTestResult = pmtctTestResult;
    }

    public boolean isPmtctPostTestCounseling() {
        return pmtctPostTestCounseling;
    }

    public void setPmtctPostTestCounseling(boolean pmtctPostTestCounseling) {
        this.pmtctPostTestCounseling = pmtctPostTestCounseling;
    }

    public boolean isPmtctARV() {
        return pmtctARV;
    }

    public void setPmtctARV(boolean pmtctARV) {
        this.pmtctARV = pmtctARV;
    }

    public String getRefurred() {
        return refurred;
    }

    public void setRefurred(String refurred) {
        this.refurred = refurred;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
