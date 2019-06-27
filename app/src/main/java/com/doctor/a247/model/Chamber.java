package com.doctor.a247.model;

import java.util.ArrayList;

public class Chamber {

    private String id;
    private String hostpitalName;
    private String chamberAddress;
    private String stayTime;
    private ArrayList<Integer> chamberDays;

    public Chamber( String id, String hostpitalName, String chamberAddress, String stayTime, ArrayList<Integer>chamberDays) {
        this.hostpitalName = hostpitalName;
        this.chamberAddress = chamberAddress;
        this.stayTime = stayTime;
        this.chamberDays = chamberDays;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chamber(){}

    public String getHostpitalName() {
        return hostpitalName;
    }

    public void setHostpitalName(String hostpitalName) {
        this.hostpitalName = hostpitalName;
    }

    public String getChamberAddress() {
        return chamberAddress;
    }

    public void setChamberAddress(String chamberAddress) {
        this.chamberAddress = chamberAddress;
    }

    public String getStayTime() {
        return stayTime;
    }

    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }

    public ArrayList<Integer> getChamberDays() {
        return chamberDays;
    }

    public void setChamberDays(ArrayList<Integer> chamberDays) {
        this.chamberDays = chamberDays;
    }


}
