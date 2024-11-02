
public class MedicationItem {

    private String medicationID;
    private String medicationName;
    private int quantity;

    public MedicationItem() {

    }

    public MedicationItem(String medicationID, String medicationName, int quantity) {
        this.medicationID = medicationID;
        this.medicationName = medicationName;
        this.quantity = quantity;
    }

    public String getMedicationID() {
        return medicationID;
    }

    /**
     *
     * @param medicationID
     */
    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public String getMedicationName() {
        return medicationName;
    }

    /**
     *
     * @param medicationName
     */
    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
