package com.doctor.a247.model;

import java.io.Serializable;

public class Appoint extends Request implements Serializable {

    private String appointDate;
    private String appointTime;
    private String serial;


    public Appoint(){}

    
    public Appoint(String id, String patientName, String doctorId, String patientid, String appointDate, String appointTime, String serial) {
        super(id, patientName, doctorId, patientid);
        this.appointDate = appointDate;
        this.appointTime = appointTime;
        this.serial = serial;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
