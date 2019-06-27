package com.doctor.a247.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String id;
    private int age;
    private String fatherName;
    private String motherName;
    private String phoneNumber;
    private Address presentAddress;
    private Address permanantAddress;
    private String gender;

    public User(){}

    public User(String name, String id, int age, String fatherName, String motherName, String phoneNumber, Address presentAddress, Address permanantAddress, String gender) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.phoneNumber = phoneNumber;
        this.presentAddress = presentAddress;
        this.permanantAddress = permanantAddress;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(Address presentAddress) {
        this.presentAddress = presentAddress;
    }

    public Address getPermanantAddress() {
        return permanantAddress;
    }

    public void setPermanantAddress(Address permanantAddress) {
        this.permanantAddress = permanantAddress;
    }
}
