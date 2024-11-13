package com.ntu.hns.model;

public class Diagnosis {
  private String appointmentId;
  private String patientId;
  private String diagnosis;

  /** Default constructor required for OpenCSV to instantiate object. */
  public Diagnosis() {}

  public Diagnosis(String appointmentID, String patientID, String diagnosis) {
    this.appointmentId = appointmentID;
    this.patientId = patientID;
    this.diagnosis = diagnosis;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  public String getAppointmentId() {
    return appointmentId;
  }

  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  public String getDiagnosis() {
    return diagnosis;
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s", getAppointmentId(), getPatientId(), getDiagnosis());
  }
}
