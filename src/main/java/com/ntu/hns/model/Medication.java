package com.ntu.hns.model;

import com.opencsv.bean.CsvBindByPosition;

public class Medication {
    @CsvBindByPosition(position = 0) private String medicationID;
    @CsvBindByPosition(position = 1) private String medicationName;
    @CsvBindByPosition(position = 2) private String stockStatus;
    @CsvBindByPosition(position = 3) private boolean alert;
    @CsvBindByPosition(position = 4) private int totalQuantity;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Medication() {}

    public Medication(String medicationID, String medicationName, String stockStatus, boolean alert,
                      int totalQuantity) {
        this.medicationID = medicationID;
        this.medicationName = medicationName;
        this.stockStatus = stockStatus;
        this.alert = alert;
        this.totalQuantity = totalQuantity;
    }

    public String getMedicationID() {
        return this.medicationID;
    }

    /**
     *
     * @param medicationID
     */
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    /**
     *
     * @param medicationName
     */
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStockStatus() {
        return this.stockStatus;
    }

    /**
     *
     * @param stockStatus
     */
    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public boolean getAlert() {
        return this.alert;
    }

    /**
     *
     * @param alert
     */
    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public int getTotalQuantity() {
        return this.totalQuantity;
    }

    /**
     *
     * @param totalQuantity
     */
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%b,%d", getMedicationID(), getMedicationName(), getStockStatus(), getAlert(), getTotalQuantity());
    }
}
