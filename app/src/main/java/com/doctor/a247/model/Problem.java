package com.doctor.a247.model;

import java.io.Serializable;

public class Problem implements Serializable {

    private String id;
    private String problemText;

    public Problem(String id, String problemText) {
        this.id = id;
        this.problemText = problemText;
    }

    public Problem(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemText() {
        return problemText;
    }

    public void setProblemText(String problemText) {
        this.problemText = problemText;
    }
}
