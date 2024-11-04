
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static final String[] sessionTimings = {
            "09:00 - 10:00",
            "10:00 - 11:00",
            "11:00 - 12:00",
            "12:00 - 13:00",
            "13:00 - 14:00",
            "14:00 - 15:00",
            "15:00 - 16:00",
            "16:00 - 17:00"
    };

    private static ArrayList<User> users;
    private static ArrayList<Appointment> appts;
    private static ArrayList<Schedule> schedules;
    private static ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords;
    private static ArrayList<Medication> inventory;
    private static ArrayList<ReplenishmentRequest> replenishmentRequests;
    private static Scanner sc = new Scanner(System.in);
    private static User user = null;
    private static ArrayList<Diagnosis> diagnoses;
    private static ArrayList<Treatment> treatments;

    public static void main(String[] args) throws Exception {

        users = CsvDB.readUsers();
        appts = CsvDB.readAppointments();
        schedules = CsvDB.readSchedules();
        apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
        diagnoses = CsvDB.readDiagnoses();
        treatments = CsvDB.readTreatments();

        AuthenticationService auth = new AuthenticationService(users);
        while (true) {

            if (user != null) {
                user.displayMenu();

                if (user instanceof Patient)
                    patientFunctions();

                else if (user instanceof Doctor)
                    doctorFunctions();

                else if (user instanceof Pharmacist)
                    pharmacistFunctions();

                else {
                    // Admin functions here
                }
            } else {
                user = auth.authenticate();
            }

            // Display user menu and functions logic here (to be removed)
            // while (user != null) {
            // // Display Patient Menu
            // if (user instanceof Patient) {
            // patientFunctions();
            // } else if (user instanceof Doctor) { // User is a Doctor instance
            // doctorFunctions();

            // } else if (user instanceof Pharmacist) { // User is a Pharmacist instance
            // pharmacistFunctions();
            // } else { // User is a Administrator instance
            // Administrator admin = (Administrator) user;
            // admin.displayMenu();
            // }
            // }

        }
    }

    // Patient actions
    public static void patientFunctions() throws IOException {
        Patient patient = (Patient) user;
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: // View personal medical record
                patient.viewMedicalRecord(apptOutcomeRecords, diagnoses, treatments);
                sc.nextLine();
                System.out.println("\nPress Enter to continue");
                sc.nextLine();
                break;

            case 2: // Update personal information
                patient.updatePersonalInformation(users);
                sc.nextLine();
                System.out.println("\nPress Enter to continue");
                sc.nextLine();
                break;
            case 3: // View Available Appointment Slots
                patient.viewAvailableAppointment(schedules, users);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 4: // Schedule an Appointment
                patient.scheduleAppointment(schedules, users, patient.getHospitalID());
                appts = CsvDB.readAppointments();
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 5: // Reschedule an Appointment
                patient.rescheduleAppointment(appts, schedules, users);
                appts = CsvDB.readAppointments(); // Reload appointments after rescheduling
                schedules = CsvDB.readSchedules(); // Reload schedules after rescheduling
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 6: // Cancel an Appointment
                appts = CsvDB.readAppointments();
                patient.cancelAppointment(appts, schedules, users);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 7:
                patient.viewScheduledAppointments(appts, users);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 8: // View Past Appointment Outcome Records
                patient.viewAppointmentOutcomeRecords(appts, apptOutcomeRecords);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 9: // Log out
                user = null;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }
    }

    // Doctor actions
    public static void doctorFunctions() {
        Doctor doctor = (Doctor) user;
        // ArrayList<Schedule> docSchedule = doctor.viewSchedule(doctor.getHospitalID(),
        // schedules);

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3: // View Personal Schedule based on week
                doctor.viewWeeklySchedule(schedules, users);
                break;
            case 4:
                doctor.setAvailability(schedules, appts);
                break;
            case 5:
                doctor.acceptOrDeclineAppointmentRequests(schedules, users, appts);
                break;
            case 6:
                doctor.viewUpcomingAppointments(schedules, users);
                break;
            case 7:
                doctor.recordAppointmentOutcome(appts);
                break;
            case 8:
                user = null;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }

    }

    // Pharmacist actions
    public static void pharmacistFunctions() {
        Pharmacist pharmacist = (Pharmacist) user;
        System.out.print("\nEnter your choice: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                try {
                    apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                    pharmacist.viewAppointmentOutcome(apptOutcomeRecords);
                    sc.nextLine();
                    System.out.println("\nPress Enter to continue");
                    sc.nextLine();
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                    inventory = CsvDB.readMedications();
                    pharmacist.prescribeAndUpdate(apptOutcomeRecords, inventory);
                    sc.nextLine();
                    System.out.println("\nPress Enter to continue");
                    sc.nextLine();
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }
                break;
            case 3:
                try {
                    inventory = CsvDB.readMedications();
                    pharmacist.viewInventory(inventory);
                    sc.nextLine();
                    System.out.println("\nPress Enter to continue");
                    sc.nextLine();
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }
                break;
            case 4:
                try {
                    inventory = CsvDB.readMedications();
                    replenishmentRequests = CsvDB.readRequest();
                    pharmacist.submitReplenishmentRequest(inventory, replenishmentRequests, pharmacist);
                    sc.nextLine();
                    System.out.println("\nPress Enter to continue");
                    sc.nextLine();
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }
                break;
            case 5:
                user = null;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }
    }

}
