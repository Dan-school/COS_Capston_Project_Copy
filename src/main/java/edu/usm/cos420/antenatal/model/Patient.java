package edu.usm.cos420.antenatal.model;

import java.io.Serializable;

/**
 *
 * @author Nick Doane<br>
 * Basic Implementation of a Person class, to be used as a static identifier of a person,
 * patient, or staff member.
 *
 */
public class Patient implements Serializable {

    private static final long serialVersionUID = 7526472295622771666L;
    private Long outpatientId;
    private Long nhisId; // Optional
    private static Long COUNTER = 0L;
    private String firstName;
    private String lastName;
    private String Address;
    private int phoneNumber;
    private String birthdate;    //Optional
    private char sex;
    private String[] guardians;        //Optional
    private String[] dependents;       //Optional


    /**
     * Default Constructor
     */
    public Patient(Long ID){
        outpatientId = ID;
    }



    public Patient(Long id, Long nhisId, String first, String last,
                   String Address, int phoneNumber, String birthdate, char sex) {
        this.outpatientId = id;
        this.nhisId= nhisId;
        this.firstName = first;
        this.lastName = last;
        this.Address = Address;
        this.phoneNumber = phoneNumber;

//    	this needs to be converted
        this.birthdate = birthdate.toString();

        this.sex = sex;
//    	this.guardian = guardian;
//    	this.dependent = dependent;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


    public Long getId(){
        return outpatientId;
    }

    public void setID(Long ID){
        this.outpatientId = ID;
    }

    public String getName() {
        return firstName.concat(" ").concat(lastName);
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }


    public void setName(String first, String last) {
        this.firstName = first;
        this.lastName = last;
    }


    public String getAddress() {
        return Address;
    }


    public void setAddress(String address) {
        Address = address;
    }


    public int getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getBirthdate() {
        return birthdate;
    }




    public char getSex() {
        return sex;
    }


    public void setSex(char sex) {
        this.sex = sex;
    }


    public String[] getGuardians() {
        return guardians;
    }


    public void setGuardians(String[] guardian) {
        this.guardians = guardian;
    }


    public String[] getDependents() {
        return dependents;
    }


    public void setDependents(String[] dependent) {
        this.dependents = dependent;
    }

    public Long getOutpatientId() {
        return outpatientId;
    }

    public void setOutpatientId(Long outpatientId) {
        this.outpatientId = outpatientId;
    }

    public Long getNhisId() {
        return nhisId;
    }

    public void setNhisId(Long nhisId) {
        this.nhisId = nhisId;
    }

    @Override
    public String toString() {
        return String.format("Patient [id=%d,name=%s,address=%s]",
                getId(), firstName.concat(" ").concat(lastName), Address);
    }


}