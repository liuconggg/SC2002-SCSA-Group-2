package com.ntu.hns.model;

import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeRecord {
  private String appointmentID;
  private String typeOfService;
  private String consultationNotes;
  private List<MedicationItem> prescriptions;
  private String prescriptionStatus;

  /** Default constructor required for OpenCSV to instantiate object. */
  public AppointmentOutcomeRecord() {}

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

  public String getAppointmentID() {
    return this.appointmentID;
  }

  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  public String getTypeOfService() {
    return this.typeOfService;
  }

  public void setTypeOfService(String typeOfService) {
    this.typeOfService = typeOfService;
  }

  public String getConsultationNotes() {
    return this.consultationNotes;
  }

  public void setConsultationNotes(String consultationNotes) {
    this.consultationNotes = consultationNotes;
  }

  public List<MedicationItem> getPrescriptions() {
    return this.prescriptions;
  }

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

  public void setPrescriptions(ArrayList<MedicationItem> prescriptions) {
    this.prescriptions = prescriptions;
  }

  public String getPrescriptionStatus() {
    return this.prescriptionStatus;
  }

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
