
import java.util.ArrayList;

public class AppointmentOutcomeRecord {

    private String appointmentID;
    private String typeOfService;
    private String consultationNotes;
    private ArrayList<MedicationItem> prescriptions;
    private String prescriptionStatus;

    public AppointmentOutcomeRecord() {

    }

    public AppointmentOutcomeRecord(String appointmentID, String typeOfService, String consultationNotes,
            ArrayList<MedicationItem> prescriptions, String prescriptionStatus) {
        this.appointmentID = appointmentID;
        this.typeOfService = typeOfService;
        this.consultationNotes = consultationNotes;
        this.prescriptions = prescriptions;
        this.prescriptionStatus = prescriptionStatus;
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

    public String getTypeOfService() {
        return this.typeOfService;
    }

    /**
     *
     * @param typeOfService
     */
    public void setTypeOfService(String typeOfService) {
        this.typeOfService = typeOfService;
    }

    public String getConsultationNotes() {
        return this.consultationNotes;
    }

    /**
     *
     * @param consultationNotes
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public ArrayList<MedicationItem> getPrescriptions() {
        return this.prescriptions;
    }

    public String getPrescriptionsAsString() {
        if (prescriptions == null || prescriptions.isEmpty()) {
            return "No prescriptions.";
        }

        StringBuilder prescriptionsStr = new StringBuilder();
        for (MedicationItem item : prescriptions) {
            prescriptionsStr.append(item.toString()).append("\n");
        }

        return prescriptionsStr.toString().trim();
    }

    /**
     *
     * @param prescriptions
     */
    public void setPrescriptions(ArrayList<MedicationItem> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getPrescriptionStatus() {
        return this.prescriptionStatus;
    }

    /**
     *
     * @param prescriptionStatus
     */
    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

}
