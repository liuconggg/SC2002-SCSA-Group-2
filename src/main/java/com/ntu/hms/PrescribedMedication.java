package com.ntu.hms;

/** Represents a medication prescribed during an appointment. */
public class PrescribedMedication {
  private String appointmentID;
  private String medicationName;
  private String status;
  private int quantity;

  /**
   * Retrieves the name of the prescribed medication.
   *
   * @return the name of the medication
   */
  public String getMedicationName() {
    return this.medicationName;
  }

  /**
   * Sets the name of the prescribed medication.
   *
   * @param medicationName the name of the medication to set
   */
  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  /**
   * Retrieves the status of the prescribed medication.
   *
   * @return the status of the medication
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Sets the status of the prescribed medication.
   *
   * @param status the status to set for the medication
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Retrieves the ID of the appointment associated with the prescribed medication.
   *
   * @return the appointment ID
   */
  public String getAppointmentID() {
    return this.appointmentID;
  }

  /**
   * Sets the ID of the appointment associated with the prescribed medication.
   *
   * @param appointmentID the ID of the appointment to set
   */
  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  /**
   * Retrieves the quantity of the prescribed medication.
   *
   * @return the quantity of the medication
   */
  public int getQuantity() {
    return this.quantity;
  }

  /**
   * Sets the quantity of the prescribed medication.
   *
   * @param quantity the quantity to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Constructs a new PrescribedMedication with the specified details.
   *
   * @param appointmentID the ID of the appointment during which the medication was prescribed
   * @param medicationName the name of the medication prescribed
   * @param status the current status of the medication (e.g., prescribed, administered)
   * @param quantity the quantity of the medication prescribed
   */
  public PrescribedMedication(
      String appointmentID, String medicationName, String status, int quantity) {
    // TODO - implement com.ntu.hms.PrescribedMedication.com.ntu.hms.PrescribedMedication
    throw new UnsupportedOperationException();
  }

  /**
   * Constructs a new PrescribedMedication with default values.
   *
   * <p>This constructor is intended to create an instance of PrescribedMedication without
   * initializing its fields. It is useful in scenarios where the fields will be set at a later time
   * using setter methods.
   *
   * <p>Note: This constructor currently throws UnsupportedOperationException indicating that it is
   * not yet implemented.
   */
  public PrescribedMedication() {
    // TODO - implement com.ntu.hms.PrescribedMedication.com.ntu.hms.PrescribedMedication
    throw new UnsupportedOperationException();
  }
}
