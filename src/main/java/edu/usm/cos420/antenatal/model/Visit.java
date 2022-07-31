package edu.usm.cos420.antenatal.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import edu.usm.cos420.antenatal.messengers.SubsequentVisitMessage;

public class Visit implements Serializable {

    private final transient DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final long serialVersionUID = 7526472295622776148L;
    private LocalDate date;
    private BloodPressure bloodPressure;
    private int weight;
    private int patientHeight;
    private int fundalHeight;
    private int gestationWeek;
    private boolean bloodFilm;
    private String refurred; // not well documented. Using string for now

    public Visit() {
        this.bloodPressure = new BloodPressure();
    }

    public Visit(LocalDate date, BloodPressure bloodPressure, int weight, int patientHeight, int fundalHeight, int gestationWeek, boolean bloodFilm,
            String refurred, IPT ipt) {
        this.date = date;
        this.bloodPressure = bloodPressure;
        this.weight = weight;
        this.fundalHeight = fundalHeight;
        this.bloodFilm = bloodFilm;
        this.patientHeight = patientHeight;
        this.gestationWeek = gestationWeek;
        this.refurred = refurred;
//        this.ipt = ipt;
    }

    /**
     * Constructor that takes a validated initialVisitMessage
     */
    public Visit(SubsequentVisitMessage subsequentVisitMessage) {
        /* Parsing data to SubsequesntVisitMessage for data verification.*/

        this.date = LocalDate.parse(subsequentVisitMessage.getDate(), dateTimeFormatter);
        this.bloodPressure = new BloodPressure((Integer.parseInt(subsequentVisitMessage.getSystolic())), (Integer.parseInt(subsequentVisitMessage.getDiastolic())));
        this.patientHeight = Integer.parseInt(subsequentVisitMessage.getPatientHeight());
        this.weight = Integer.parseInt(subsequentVisitMessage.getWeight());
        this.gestationWeek = Integer.parseInt(subsequentVisitMessage.getGestationWeek());
        this.fundalHeight = Integer.parseInt(subsequentVisitMessage.getFundalHeight());
        this.bloodFilm = subsequentVisitMessage.getBloodFilm();
        this.refurred = subsequentVisitMessage.getRefurred();
//        this.ipt = new IPT(subsequentVisitMessage.getIptFirstDose(), subsequentVisitMessage.getIptSecondDose(), subsequentVisitMessage.getIptThirdDose());


    }



    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BloodPressure getBloodPressure() {
        return this.bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPatientHeight() {
        return this.patientHeight;
    }

    public void setPatientHeight(int patientHeight) {
        this.patientHeight = patientHeight;
    }

    public int getFundalHeight() {
        return this.fundalHeight;
    }

    public void setFundalHeight(int fundalHeight) {
        this.fundalHeight = fundalHeight;
    }

    public int getGestationWeek() {
        return this.gestationWeek;
    }

    public void setGestationWeek(int gestationWeek) {
        this.gestationWeek = gestationWeek;
    }

    public boolean getBloodFilm() {
        return this.bloodFilm;
    }

    public void setBloodFilm(boolean bloodFilm) {
        this.bloodFilm = bloodFilm;
    }

    public String getRefurred() {
        return this.refurred;
    }

    public void setRefurred(String refurred) {
        this.refurred = refurred;
    }

}
