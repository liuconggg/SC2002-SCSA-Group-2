import java.util.ArrayList;

public interface AppointmentOutcomeInterface {

    void viewAppointmentOutcome(ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords);

    void updateAppointmentOutcome(AppointmentOutcomeRecord appointmentOutcomeRecord, String appointmentID,
            String typeOfService, String consultationNotes, ArrayList<MedicationItem> prescriptions,
            String prescriptionStatus);
}
