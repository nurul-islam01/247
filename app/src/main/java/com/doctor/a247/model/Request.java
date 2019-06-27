package com.doctor.a247.model;

import java.io.Serializable;

public class Request implements Serializable {
    private String  id;
    private String patientName;
    private String doctorId;
    private String patientid;

    public Request (){}


    public Request(String id, String patientName, String doctorId, String patientid) {
        this.id = id;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.patientid = patientid;
    }


    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }
}
