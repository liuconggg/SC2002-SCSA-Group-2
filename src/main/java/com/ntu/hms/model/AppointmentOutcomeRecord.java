package com.ntu.hms.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the outcome record of an appointment, including details such as the appointment ID,
 * type of service, consultation notes, prescriptions, and prescription status.
 */
public class AppointmentOutcomeRecord {
  private String appointmentID;
  private String typeOfService;
  private String consultationNotes;
  private List<MedicationItem> prescriptions;
  private String prescriptionStatus;

  /** Default constructor for creating an instance of AppointmentOutcomeRecord. */
  public AppointmentOutcomeRecord() {}

  /**
   * Constructs a new AppointmentOutcomeRecord with the specified details.
   *
   * @param appointmentID the unique identifier for the appointment
   * @param typeOfService the type of service provided during the appointment
   * @param consultationNotes the notes recorded during the consultation
   * @param prescriptions the list of medications prescribed during the appointment
   * @param prescriptionStatus the status of the prescriptions
   */
  public AppointmentOutcomeRecord(
      String appointmentID,
      String typeOfService,
      String consultationNotes,
      List<MedicationItem> prescriptions,
      String prescriptionStatus) {
    this.appointmentID = appointmentID;
    this.typeOfService = typeOfService;
    this.consultationNotes = consultationNotes;
    this.prescriptions = prescriptions;
    this.prescriptionStatus = prescriptionStatus;
  }

  /**
   * Retrieves the unique identifier for the appointment.
   *
   * @return the unique identifier for the appointment.
   */
  public String getAppointmentID() {
    return this.appointmentID;
  }

  /**
   * Sets the unique identifier for the appointment.
   *
   * @param appointmentID the unique identifier for the appointment
   */
  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  /**
   * Retrieves the type of service provided during the appointment.
   *
   * @return the type of service as a String.
   */
  public String getTypeOfService() {
    return this.typeOfService;
  }

  /**
   * Sets the type of service for the appointment.
   *
   * @param typeOfService the type of service provided during the appointment
   */
  public void setTypeOfService(String typeOfService) {
    this.typeOfService = typeOfService;
  }

  /**
   * Retrieves the notes recorded during the consultation.
   *
   * @return the consultation notes as a String.
   */
  public String getConsultationNotes() {
    return this.consultationNotes;
  }

  /**
   * Sets the notes recorded during the consultation.
   *
   * @param consultationNotes the notes to be set for the consultation
   */
  public void setConsultationNotes(String consultationNotes) {
    this.consultationNotes = consultationNotes;
  }

  /**
   * Retrieves the list of medications prescribed during the appointment.
   *
   * @return a list of MedicationItem objects representing the prescriptions.
   */
  public List<MedicationItem> getPrescriptions() {
    return this.prescriptions;
  }

  /**
   * Converts the list of prescriptions into a single string representation, with each prescription
   * on a new line.
   *
   * @return a string representation of the prescriptions; returns "No prescriptions." if the
   *     prescriptions list is null or empty
   */
  public String getPrescriptionsAsString() {
    if (prescriptions == null || prescriptions.isEmpty()) {
      return "No prescriptions.";
    }

    StringBuilder prescriptionsStr = new StringBuilder();
    for (MedicationItem item : prescriptions) {
      prescriptionsStr.append(item.toString()).append("\n");
    }

    return prescriptionsStr.toString().trim();
  }

  /**
   * Sets the list of medications prescribed during the appointment.
   *
   * @param prescriptions the list of MedicationItem objects representing the prescriptions
   */
  public void setPrescriptions(ArrayList<MedicationItem> prescriptions) {
    this.prescriptions = prescriptions;
  }

  /**
   * Retrieves the status of the prescriptions associated with the appointment.
   *
   * @return the prescription status as a String.
   */
  public String getPrescriptionStatus() {
    return this.prescriptionStatus;
  }

  /**
   * Sets the status of the prescriptions associated with an appointment.
   *
   * @param prescriptionStatus the status of the prescriptions
   */
  public void setPrescriptionStatus(String prescriptionStatus) {
    this.prescriptionStatus = prescriptionStatus;
  }

  public static AppointmentOutcomeRecord findOutcomeByAppointmentID(
      ArrayList<AppointmentOutcomeRecord> outcomeRecords, String appointmentID) {
    for (AppointmentOutcomeRecord outcome : outcomeRecords) {
      if (outcome.getAppointmentID().equals(appointmentID)) {
        return outcome;
      }
    }
    return null;
  }
}
