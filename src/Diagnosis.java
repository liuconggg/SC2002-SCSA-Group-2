
public class Diagnosis {

    String patientID;
    String diagnosis;
    String appointmentID;

    public Diagnosis() {
    }

    public Diagnosis(String appointmentID, String patientID, String diagnosis) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;

        this.diagnosis = diagnosis;
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

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

}
