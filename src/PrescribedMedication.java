
public class PrescribedMedication {

    private String appointmentID;
    private String medicationName;
    private String status;
    private int quantity;

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

    public String getStatus() {
        return this.status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointmentID() {
        return this.appointmentID;
    }

    /**
     *
     * @param appointmentID
     */
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getQuantity() {
        return this.quantity;
    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @param appointmentID
     * @param medicationName
     * @param status
     * @param quantity
     */
    public PrescribedMedication(String appointmentID, String medicationName, String status, int quantity) {
        // TODO - implement PrescribedMedication.PrescribedMedication
        throw new UnsupportedOperationException();
    }

    public PrescribedMedication() {
        // TODO - implement PrescribedMedication.PrescribedMedication
        throw new UnsupportedOperationException();
    }

}
