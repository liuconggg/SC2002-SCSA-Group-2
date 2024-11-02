

public class Medication {

    private String medicationID;
    private String medicationName;
    private String stockStatus;
    private boolean alert;
    private int totalQuantity;

    public Medication() { // default constructor

    }

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

}
