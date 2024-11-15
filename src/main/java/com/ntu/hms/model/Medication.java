package com.ntu.hms.model;

/**
 * Represents a medication with its details and inventory status.
 */
public class Medication {
  private String medicationID;
  private String medicationName;
  private String stockStatus;
  private boolean alert;
  private int totalQuantity;

  /**
   * Default constructor for the Medication class.
   * Initializes a new instance of the Medication class with default field values.
   */
  public Medication() {}

  /**
   * Constructs a new Medication instance with the specified details.
   *
   * @param medicationID The unique identifier for the medication.
   * @param medicationName The name of the medication.
   * @param stockStatus The current stock status of the medication (e.g., LOW, MEDIUM, HIGH).
   * @param alert Indicates if there is an alert for stocking (true if stock is low, false otherwise).
   * @param totalQuantity The total quantity of the medication in stock.
   */
  public Medication(
      String medicationID,
      String medicationName,
      String stockStatus,
      boolean alert,
      int totalQuantity) {
    this.medicationID = medicationID;
    this.medicationName = medicationName;
    this.stockStatus = stockStatus;
    this.alert = alert;
    this.totalQuantity = totalQuantity;
  }

  /**
   * Retrieves the unique identifier for the medication.
   *
   * @return the medication ID as a String.
   */
  public String getMedicationID() {
    return this.medicationID;
  }

  /**
   * Sets the unique identifier for the medication.
   *
   * @param medicationID The unique identifier for the medication.
   */
  public void setMedicationID(String medicationID) {
    this.medicationID = medicationID;
  }

  /**
   * Retrieves the name of the medication.
   *
   * @return the medication name as a String.
   */
  public String getMedicationName() {
    return this.medicationName;
  }

  /**
   * Sets the name of the medication.
   *
   * @param medicationName The name of the medication.
   */
  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  /**
   * Retrieves the current stock status of the medication.
   *
   * @return the stock status as a String.
   */
  public String getStockStatus() {
    return this.stockStatus;
  }

  /**
   * Sets the current stock status of the medication.
   *
   * @param stockStatus The current stock status of the medication (e.g., LOW, MEDIUM, HIGH).
   */
  public void setStockStatus(String stockStatus) {
    this.stockStatus = stockStatus;
  }

  /**
   * Retrieves the alert status of the medication.
   *
   * @return true if there is an alert for low stock, false otherwise.
   */
  public boolean getAlert() {
    return this.alert;
  }

  /**
   * Sets the alert status for the medication.
   *
   * @param alert Indicates if there is an alert for stocking (true if stock is low, false otherwise).
   */
  public void setAlert(boolean alert) {
    this.alert = alert;
  }

  /**
   * Retrieves the total quantity of the medication in stock.
   *
   * @return the total quantity of the medication as an integer.
   */
  public int getTotalQuantity() {
    return this.totalQuantity;
  }

  /**
   * Sets the total quantity of the medication in stock.
   *
   * @param totalQuantity The total quantity to be set for the medication.
   */
  public void setTotalQuantity(int totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  /**
   * Returns a string representation of the Medication object.
   *
   * @return a string containing the medication ID, name, stock status, alert status, and total quantity, separated by commas.
   */
  @Override
  public String toString() {
    return String.format(
        "%s,%s,%s,%b,%d",
        getMedicationID(), getMedicationName(), getStockStatus(), getAlert(), getTotalQuantity());
  }
}
