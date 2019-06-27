package com.doctor.a247.Pojos;

public class Medicine {

    private String appointmentId;
    private String patientId;
    private String medicineName;
    private String totalMedicine;
    private String medicineRules;
    private String medicineId;

    public Medicine(String appointmentId, String patientId, String medicineName, String totalMedicine, String medicineRules, String medicineId) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.medicineName = medicineName;
        this.totalMedicine = totalMedicine;
        this.medicineRules = medicineRules;
        this.medicineId = medicineId;
    }

    public Medicine() {
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

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getTotalMedicine() {
        return totalMedicine;
    }

    public void setTotalMedicine(String totalMedicine) {
        this.totalMedicine = totalMedicine;
    }

    public String getMedicineRules() {
        return medicineRules;
    }

    public void setMedicineRules(String medicineRules) {
        this.medicineRules = medicineRules;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }
}
