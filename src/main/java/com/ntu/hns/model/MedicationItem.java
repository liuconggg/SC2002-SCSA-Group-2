package com.ntu.hns.model;

public class MedicationItem {
  private String medicationID;
  private String medicationName;
  private int quantity;

  /** Default constructor required for OpenCSV to instantiate object. */
  public MedicationItem() {}

  public MedicationItem(String medicationID, String medicationName, int quantity) {
    this.medicationID = medicationID;
    this.medicationName = medicationName;
    this.quantity = quantity;
  }

  public String getMedicationID() {
    return medicationID;
  }

  public void setMedicationID(String medicationID) {
    this.medicationID = medicationID;
  }

  public String getMedicationName() {
    return medicationName;
  }

  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return medicationName + " (Quantity: " + quantity + ")";
  }
}
