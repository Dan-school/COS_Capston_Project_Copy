package edu.usm.cos420.antenatal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.usm.cos420.antenatal.messengers.PatientPregnancyMessage;
import edu.usm.cos420.antenatal.messengers.SubsequentVisitMessage;

/**
 * Antenatal Model for COS 420
 */
public class Pregnancy implements Serializable {
    private final transient DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Long getId() {
        return serialNumber; // placeholder for DAO
    }

    private static final long serialVersionUID = 7526472295622776147L;
    private long serialNumber;
    private static long COUNTER = 0L;
    private Parity parity; // Parity is the number of deliveries a woman has had, including both live and
                           // stillbirths.

    private LocalDate expectedDayOfDelivery;
    private double hemoglobinLevelAtReg; // google says this in an int in gm/dl (grams per deciliter)
    private double hemoglobinLevelAt36Wks;
    private int urineTestResult; // value of urine result
    private String bloodGroup; // Blood type as String. Could be expanded to another class
    private Sickling sickling = new Sickling(); // Sickling information. External class
    private boolean VDRL; // Venereal Disease Research Laboratory data. boolean reactive/non reactive
    private PMTCT pmtct = new PMTCT(); // PMTCT data. External class
    private ArrayList<Visit> subsequentVisits = new ArrayList<>();
    private TT tt = new TT(); // TT doses. External class.
    private IPT ipt = new IPT(); // IPT doses. External class
    private boolean itnUse; // ITN use, boolean
    private String complaints; // string field complaints. english text
    private String remarks; // string field remarks. english text
    private boolean maleInvolvement;

    /**
     * Default Constructor
     */
    public Pregnancy() {
        serialNumber = generateSerialNumber();
        parity = new Parity();
    }

    /**
     * Constructor with all fields
     */
    public Pregnancy(int parity, int gravidity, int systolic, int diastolic, int height, int weight, int gestation,
            int fundalHeight, LocalDate expectedDayOfDelivery, double hemoglobinLevelAtReg, double hemoglobin36Wks,
            int urineTestResult, String bloodGroup, Sickling sickling, boolean VDRL, PMTCT pmtct, TT tt, IPT ipt,
            boolean itnUse, boolean maleInvolvement, String complaints, String remarks) {
        this.expectedDayOfDelivery = expectedDayOfDelivery;
        this.hemoglobinLevelAtReg = hemoglobinLevelAtReg;
        this.hemoglobinLevelAt36Wks = hemoglobin36Wks;
        this.urineTestResult = urineTestResult;
        this.bloodGroup = bloodGroup;
        this.sickling = sickling;
        this.VDRL = VDRL;
        this.pmtct = pmtct;
        this.tt = tt;
        this.ipt = ipt;
        this.itnUse = itnUse;
        this.maleInvolvement = maleInvolvement;
        this.complaints = complaints;
        this.remarks = remarks;

        this.parity = new Parity(parity, gravidity);
    }

    /**
     * Constructor that takes a validated patientPregnancyMessage
     */
    public Pregnancy(PatientPregnancyMessage patientPregnancyMessage) {
        /*
         * Constructor that parses numbers from intilialVisitMessage.
         */
        serialNumber = generateSerialNumber();

        this.parity = new Parity(Integer.parseInt(patientPregnancyMessage.getParity()), Integer.parseInt(patientPregnancyMessage.getGravidity()));
        this.expectedDayOfDelivery = LocalDate.parse(patientPregnancyMessage.getExpectedDayOfDelivery(), dateTimeFormatter);
        this.hemoglobinLevelAtReg = Double.parseDouble(patientPregnancyMessage.getHemoglobinReg());
        // We won't have the 36 week hemo during initial visit
        //this.hemoglobinLevelAt36Wks = patientPregnancyMessage.getHemoglobin36Wks().isEmpty() ? 0 : Double.parseDouble(patientPregnancyMessage.getHemoglobin36Wks());
        // GUI has not added field yet. More clarification needed on urine test
        // this.urineTestResult = Integer.parseInt(patientPregnancyMessage.getUrineTest());
        this.bloodGroup = patientPregnancyMessage.getBloodGroup();
        this.sickling = new Sickling(patientPregnancyMessage.isSickling(),
                patientPregnancyMessage.getSicklingType());
        this.VDRL = patientPregnancyMessage.isVDRL();
        this.maleInvolvement = patientPregnancyMessage.isMaleInvolvement();

    }

    public void addVisit(SubsequentVisitMessage subsequentVisitMessage) {
        this.hemoglobinLevelAt36Wks = subsequentVisitMessage.getHemoglobin36Wks().isEmpty() ? 0 : Double.parseDouble(subsequentVisitMessage.getHemoglobin36Wks());
        this.pmtct = new PMTCT(subsequentVisitMessage.isPmtctPreTestCounseling(), subsequentVisitMessage.isPmtctTestResult(), subsequentVisitMessage.isPmtctPostTestCounseling(), subsequentVisitMessage.isPmtctARV());
        this.tt = new TT(subsequentVisitMessage.getTtFirstDose(), subsequentVisitMessage.getTtSecondDose(), subsequentVisitMessage.getTtThirdDose(), subsequentVisitMessage.getTtProtectedNotDosed());
        this.ipt = new IPT(subsequentVisitMessage.getIptFirstDose(), subsequentVisitMessage.getIptSecondDose(), subsequentVisitMessage.getIptThirdDose());
        this.itnUse = subsequentVisitMessage.getItnUse();
        this.complaints = subsequentVisitMessage.getComplaints();
        this.remarks = subsequentVisitMessage.getRemarks();
        addSubsequentVisit(new Visit(subsequentVisitMessage));
    }

    // for auto-generation of serial numbers
    private long generateSerialNumber() {
        return COUNTER++;
    }

    /**
     * Public Access Methods ( Get, Set, toString)
     */

    public long getSerialNumber() {
        return serialNumber;
    }

    public Parity getParity() {
        return parity;
    }
    public void setParity(int gravidity, int parity) {
        this.parity.setParity(parity);
        this.parity.setGravidity(gravidity);
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDate getExpectedDayOfDelivery() {
        return expectedDayOfDelivery;
    }

    public void setExpectedDayOfDelivery(LocalDate expectedDayOfDelivery) {
        this.expectedDayOfDelivery = expectedDayOfDelivery;
    }

    public void setExpectedDayOfDelivery(String expectedDayOfDelivery) {
        this.expectedDayOfDelivery = LocalDate.parse(expectedDayOfDelivery, dateTimeFormatter);
    }

    public double getHemoglobinLevelAtReg() {
        return hemoglobinLevelAtReg;
    }

    public void setHemoglobinLevelAtReg(double hemoglobinLevelAtReg) {
        this.hemoglobinLevelAtReg = hemoglobinLevelAtReg;
    }

    public double getHemoglobinLevelAt36Wks() {
        return hemoglobinLevelAt36Wks;
    }

    public void setHemoglobinLevelAt36Wks(double hemoglobinLevelAt36Wks) {
        this.hemoglobinLevelAt36Wks = hemoglobinLevelAt36Wks;
    }


    public int getUrineTestResult() {
        return this.urineTestResult;
    }

    public void setUrineTestResult(int urineTestResult) {
        this.urineTestResult = urineTestResult;
    }

    public String getBloodGroup() {
        return this.bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Sickling getSickling() {
        return this.sickling;
    }

    public void setSickling(Sickling sickling) {
        this.sickling = sickling;
    }

    public boolean isVDRL() {
        return this.VDRL;
    }

    public boolean getVDRL() {
        return this.VDRL;
    }

    public void setVDRL(boolean VDRL) {
        this.VDRL = VDRL;
    }

    public PMTCT getPmtct() {
        return this.pmtct;
    }

    public void setPmtct(PMTCT pmtct) {
        this.pmtct = pmtct;
    }
    public void setSubsequentVisits(ArrayList<Visit> subsequentVisits) {
        this.subsequentVisits = subsequentVisits;
    }

    public TT getTt() {
        return this.tt;
    }

    public void setTt(TT tt) {
        this.tt = tt;
    }

    public IPT getIpt() {
        return this.ipt;
    }

    public void setIpt(IPT ipt) {
        this.ipt = ipt;
    }

    public boolean isItnUse() {
        return this.itnUse;
    }

    public boolean getItnUse() {
        return this.itnUse;
    }

    public void setItnUse(boolean itnUse) {
        this.itnUse = itnUse;
    }

    public String getComplaints() {
        return this.complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    

    public ArrayList<Visit> getSubsequentVisits() {
        return subsequentVisits;
    }

    public void addSubsequentVisit(Visit visit) {
        subsequentVisits.add(visit);
    }

    @Override
    public String toString() {
        return "toString or not toString; that is what goes here";
    }
}