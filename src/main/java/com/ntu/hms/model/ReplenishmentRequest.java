package com.ntu.hms.model;

import java.util.List;

public class ReplenishmentRequest {
  private String requestID;
  private List<MedicationItem> medicationBatch;
  private String status;
  private String pharmacistID;

  /** Default constructor required for OpenCSV to instantiate object. */
  public ReplenishmentRequest() {}

  public ReplenishmentRequest(
      String requestID, List<MedicationItem> medicationBatch, String status, String pharmacistID) {
    this.requestID = requestID;
    this.medicationBatch = medicationBatch;
    this.status = status;
    this.pharmacistID = pharmacistID;
  }

  public String getRequestID() {
    return this.requestID;
  }

  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }

  public List<MedicationItem> getMedicationBatch() {
    return this.medicationBatch;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPharmacistID() {
    return this.pharmacistID;
  }
}
