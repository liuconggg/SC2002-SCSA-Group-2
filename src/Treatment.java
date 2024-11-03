
public class Treatment {

    String patientID;
    String treatment;
    String appointmentID;

    public Treatment() {
    }

    public Treatment(String patientID, String appointmentID, String treatment) {
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.treatment = treatment;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getTreatment() {
        return treatment;
    }

}
