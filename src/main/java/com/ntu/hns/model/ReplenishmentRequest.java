package com.ntu.hns.model;

import com.opencsv.bean.CsvBindByPosition;
import java.util.List;

public class ReplenishmentRequest {
    @CsvBindByPosition(position = 0) private String requestID;
    @CsvBindByPosition(position = 1) private List<MedicationItem> medicationBatch;
    @CsvBindByPosition(position = 2) private String status;
    @CsvBindByPosition(position = 3) private String pharmacistID;

    /** Default constructor required for OpenCSV to instantiate object. */
    public ReplenishmentRequest() {}

    public ReplenishmentRequest(
            String requestID,
            List<MedicationItem> medicationBatch,
            String status,
            String pharmacistID) {
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
