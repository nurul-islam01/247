package com.doctor.a247.Pojos;

import java.io.Serializable;

public class Pescribe implements Serializable {

    private String appointmentId;
    private String pescribeId;
    private String imageLink;
    private String patientId;

    public Pescribe(String appointmentId, String pescribeId, String imageLink, String patientId) {
        this.appointmentId = appointmentId;
        this.pescribeId = pescribeId;
        this.imageLink = imageLink;
        this.patientId = patientId;
    }

    public Pescribe() {
    }


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPescribeId() {
        return pescribeId;
    }

    public void setPescribeId(String pescribeId) {
        this.pescribeId = pescribeId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
