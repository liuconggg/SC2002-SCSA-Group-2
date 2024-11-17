package com.ntu.hms.model;

import java.util.List;

/**
 * This class represents a replenishment request for medications. It contains details about the
 * request such as the request ID, the batch of medications to be replenished, the status of the
 * request, and the ID of the pharmacist who created the request.
 */
public class ReplenishmentRequest {
  private String requestID;
  private List<MedicationItem> medicationBatch;
  private String status;
  private String pharmacistID;

  /** Default constructor required for OpenCSV to instantiate object. */
  public ReplenishmentRequest() {}

  /**
   * Constructs a new ReplenishmentRequest with the given details.
   *
   * @param requestID The unique identifier for the replenishment request.
   * @param medicationBatch The list of medication items that are to be replenished.
   * @param status The current status of the replenishment request.
   * @param pharmacistID The identifier of the pharmacist who created the request.
   */
  public ReplenishmentRequest(
      String requestID, List<MedicationItem> medicationBatch, String status, String pharmacistID) {
    this.requestID = requestID;
    this.medicationBatch = medicationBatch;
    this.status = status;
    this.pharmacistID = pharmacistID;
  }

  /**
   * Retrieves the unique identifier for the replenishment request.
   *
   * @return the unique identifier for the replenishment request.
   */
  public String getRequestID() {
    return this.requestID;
  }

  /**
   * Sets the unique identifier for the replenishment request.
   *
   * @param requestID the unique identifier for the replenishment request
   */
  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }

  /**
   * Retrieves the batch of medications required for the replenishment request.
   *
   * @return a list of MedicationItem objects that represents the batch of medications to be
   *     replenished.
   */
  public List<MedicationItem> getMedicationBatch() {
    return this.medicationBatch;
  }

  /**
   * Retrieves the current status of the replenishment request.
   *
   * @return the current status of the replenishment request.
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * Sets the current status of the replenishment request.
   *
   * @param status the current status of the replenishment request.
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Retrieves the unique identifier for the pharmacist who created the replenishment request.
   *
   * @return the unique identifier of the pharmacist.
   */
  public String getPharmacistID() {
    return this.pharmacistID;
  }
}
