package com.doctor.a247.model;

import java.io.Serializable;

public class Address implements Serializable {

    private String houseNo;
    private String roadNo;
    private String village;
    private String post;
    private String thana;
    private String zila;
    private String division;

    public Address(String houseNo, String roadNo, String village, String post, String thana, String zila, String division) {
        this.houseNo = houseNo;
        this.roadNo = roadNo;
        this.village = village;
        this.post = post;
        this.thana = thana;
        this.zila = zila;
        this.division = division;
    }

    public Address(){}

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getRoadNo() {
        return roadNo;
    }

    public void setRoadNo(String roadNo) {
        this.roadNo = roadNo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getZila() {
        return zila;
    }

    public void setZila(String zila) {
        this.zila = zila;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
