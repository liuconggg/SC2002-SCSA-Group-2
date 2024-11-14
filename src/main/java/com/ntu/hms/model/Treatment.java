package com.ntu.hms.model;

public class Treatment {
  String appointmentID;
  String patientID;
  String treatment;

  /** Default constructor required for OpenCSV to instantiate object. */
  public Treatment() {}

  public Treatment(String appointmentID, String patientID, String treatment) {
    this.appointmentID = appointmentID;
    this.patientID = patientID;
    this.treatment = treatment;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  public String getAppointmentID() {
    return appointmentID;
  }

  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  public String getTreatment() {
    return treatment;
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s", getAppointmentID(), getPatientID(), getTreatment());
  }
}
