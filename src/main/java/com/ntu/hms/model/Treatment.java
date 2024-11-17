package com.ntu.hms.model;

/** Represents a treatment for a patient within a medical appointment system. */
public class Treatment {
  String appointmentID;
  String patientID;
  String treatment;

  /**
   * Default constructor for the Treatment class. Initializes a new instance of the Treatment class
   * without setting any fields.
   */
  public Treatment() {}

  /**
   * Constructs a new Treatment instance with the specified appointment ID, patient ID, and
   * treatment.
   *
   * @param appointmentID the ID of the appointment.
   * @param patientID the ID of the patient receiving the treatment.
   * @param treatment the description or details of the treatment provided.
   */
  public Treatment(String appointmentID, String patientID, String treatment) {
    this.appointmentID = appointmentID;
    this.patientID = patientID;
    this.treatment = treatment;
  }

  /**
   * Sets the ID of the patient receiving the treatment.
   *
   * @param patientID the ID of the patient.
   */
  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  /**
   * Retrieves the ID of the patient receiving the treatment.
   *
   * @return the ID of the patient.
   */
  public String getPatientID() {
    return patientID;
  }

  /**
   * Sets the ID of the appointment.
   *
   * @param appointmentID the ID of the appointment.
   */
  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  /**
   * Retrieves the ID of the appointment associated with this treatment.
   *
   * @return the ID of the appointment.
   */
  public String getAppointmentID() {
    return appointmentID;
  }

  /**
   * Sets the description or details of the treatment provided.
   *
   * @param treatment the description or details of the treatment.
   */
  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  /**
   * Retrieves the description or details of the treatment provided.
   *
   * @return the description or details of the treatment.
   */
  public String getTreatment() {
    return treatment;
  }

  /**
   * Returns a string representation of the Treatment object. The string is formatted as
   * "appointmentID,patientID,treatment".
   *
   * @return a string containing the appointment ID, patient ID, and treatment details.
   */
  @Override
  public String toString() {
    return String.format("%s,%s,%s", getAppointmentID(), getPatientID(), getTreatment());
  }
}
