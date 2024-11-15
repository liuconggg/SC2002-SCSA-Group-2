package com.ntu.hms.model;

/**
 * Represents a medical diagnosis with associated appointment and patient information.
 */
public class Diagnosis {
  private String appointmentId;
  private String patientId;
  private String diagnosis;

  /**
   * Default constructor for the Diagnosis class.
   * Initializes a new instance of the Diagnosis class with default values.
   */
  public Diagnosis() {}

  /**
   * Initializes a new instance of the Diagnosis class with specified appointment ID, patient ID, and diagnosis details.
   *
   * @param appointmentID The unique identifier for the appointment associated with this diagnosis.
   * @param patientID The unique identifier for the patient associated with this diagnosis.
   * @param diagnosis The detailed description of the diagnosis for the patient.
   */
  public Diagnosis(String appointmentID, String patientID, String diagnosis) {
    this.appointmentId = appointmentID;
    this.patientId = patientID;
    this.diagnosis = diagnosis;
  }

  /**
   * Sets the patient ID associated with this diagnosis.
   *
   * @param patientId The unique patient identifier to be set.
   */
  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  /**
   * Retrieves the unique identifier for the patient associated with this diagnosis.
   *
   * @return The unique patient identifier.
   */
  public String getPatientId() {
    return patientId;
  }

  /**
   * Sets the unique identifier for the appointment associated with this diagnosis.
   *
   * @param appointmentId The unique appointment identifier to be set.
   */
  public void setAppointmentId(String appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * Retrieves the unique identifier for the appointment associated with this diagnosis.
   *
   * @return The unique appointment identifier.
   */
  public String getAppointmentId() {
    return appointmentId;
  }

  /**
   * Sets the detailed description of the diagnosis for the patient.
   *
   * @param diagnosis The diagnosis details to be set.
   */
  public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
  }

  /**
   * Retrieves the detailed description of the diagnosis for the patient.
   *
   * @return The diagnosis details.
   */
  public String getDiagnosis() {
    return diagnosis;
  }

  /**
   * Returns a string representation of the Diagnosis object.
   * The format is "appointmentId,patientId,diagnosis".
   *
   * @return A string consisting of the appointment ID, patient ID, and diagnosis details, separated by commas.
   */
  @Override
  public String toString() {
    return String.format("%s,%s,%s", getAppointmentId(), getPatientId(), getDiagnosis());
  }
}
