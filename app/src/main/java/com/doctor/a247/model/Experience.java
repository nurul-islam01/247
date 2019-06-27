package com.doctor.a247.model;

import java.io.Serializable;

public class Experience implements Serializable {

    private String hostpitalName;
    private String deptName;
    private String startDate;
    private String endDate;

    public Experience(String hostpitalName, String deptName, String startDate, String endDate) {
        this.hostpitalName = hostpitalName;
        this.deptName = deptName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Experience(){}

    public String getHostpitalName() {
        return hostpitalName;
    }

    public void setHostpitalName(String hostpitalName) {
        this.hostpitalName = hostpitalName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
