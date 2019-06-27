package com.doctor.a247.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Doctor extends User implements Serializable {

    private Experience experience;
    private String specalist;
    private String imageUrl;
    private String degination;

    public Doctor(String name, String id, int age, String fatherName, String motherName, String phoneNumber, Address presentAddress, Address permanantAddress, String gender, Experience experience, String specalist, String imageUrl, String degination) {
        super(name, id, age, fatherName, motherName, phoneNumber, presentAddress, permanantAddress, gender);
        this.experience = experience;
        this.specalist = specalist;
        this.imageUrl = imageUrl;
        this.degination = degination;
    }

    public Doctor(){}

    public String getDegination() {
        return degination;
    }

    public void setDegination(String degination) {
        this.degination = degination;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public String getSpecalist() {
        return specalist;
    }

    public void setSpecalist(String specalist) {
        this.specalist = specalist;
    }

}
