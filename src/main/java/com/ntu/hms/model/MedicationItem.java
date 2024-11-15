package com.ntu.hms.model;

/**
 * Represents an item of medication with its ID, name, and quantity.
 */
public class MedicationItem {
  private String medicationID;
  private String medicationName;
  private int quantity;

  /** Default constructor required for OpenCSV to instantiate object. */
  public MedicationItem() {}

  /**
   * Constructs a new MedicationItem with the specified ID, name, and quantity.
   *
   * @param medicationID the unique identifier for the medication
   * @param medicationName the name of the medication
   * @param quantity the quantity of the medication
   */
  public MedicationItem(String medicationID, String medicationName, int quantity) {
    this.medicationID = medicationID;
    this.medicationName = medicationName;
    this.quantity = quantity;
  }

  /**
   * Retrieves the unique identifier for the medication.
   *
   * @return the medication ID
   */
  public String getMedicationID() {
    return medicationID;
  }

  /**
   * Sets the unique identifier for the medication.
   *
   * @param medicationID the unique identifier for the medication
   */
  public void setMedicationID(String medicationID) {
    this.medicationID = medicationID;
  }

  /**
   * Retrieves the name of the medication.
   *
   * @return the medication name
   */
  public String getMedicationName() {
    return medicationName;
  }

  /**
   * Sets the name of the medication.
   *
   * @param medicationName the name of the medication to set
   */
  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  /**
   * Retrieves the quantity of the medication.
   *
   * @return the quantity of the medication
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity of the medication.
   *
   * @param quantity the quantity to set
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Returns a string representation of the medication item.
   *
   * @return a formatted string containing the name of the medication and its quantity
   */
  @Override
  public String toString() {
    return medicationName + " (Quantity: " + quantity + ")";
  }
}
