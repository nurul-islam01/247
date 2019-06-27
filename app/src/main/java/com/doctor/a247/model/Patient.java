package com.doctor.a247.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Patient extends User implements Serializable {

    private String bloodGroup;
    private int weight;

    public Patient(String name, String id, int age, String fatherName, String motherName, String phoneNumber, Address presentAddress, Address permanantAddress, String gender, String bloodGroup, int weight) {
        super(name, id, age, fatherName, motherName, phoneNumber, presentAddress, permanantAddress, gender);
        this.bloodGroup = bloodGroup;
        this.weight = weight;
    }

    public Patient(){}

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
