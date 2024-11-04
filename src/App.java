
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static final String DEFAULT_PASSWORD = "password";
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
    private static User userLoggedIn = null;
    private static boolean loggedOut = false;

    public static void main(String[] args) throws Exception {

        String id, password;
        users = CsvDB.readUsers();
        appts = CsvDB.readAppointments();
        schedules = CsvDB.readSchedules();

        System.out.println("Hospital Management System");
        userLoggedIn = null;

        while (true) {
            loggedOut = false;
            System.out.print("Hospital ID:");
            id = sc.nextLine();
            System.out.print("Password:");
            password = sc.nextLine();

            for (User user : users) {
                if (user.getHospitalID().equals(id) && user.getPassword().equals(password)) { // Check valid credentials
                    if (user.getPassword().equals(DEFAULT_PASSWORD)) { // User still using default password ("password")

                        System.out.println("Please change your password first");
                        do {
                            System.out.print("New Password: ");
                            String newPassword = sc.nextLine();
                            System.out.print("Confirm New Password: ");
                            String cfmNewPassword = sc.nextLine();
                            if (newPassword.equals(cfmNewPassword)) {
                                user.setPassword(newPassword);
                                CsvDB.saveUsers(users);
                                System.out.println("Password changed successfully!");

                                break;
                            } else {
                                System.out.println("Password does not match! Please try again!");
                            }
                        } while (true);

                    }
                    userLoggedIn = user;
                    System.out.println("Logged In successfully!");
                    break;
                }
            }

            // Display user menu and functions logic here
            while (userLoggedIn != null) {
                // Display Patient Menu
                if (userLoggedIn instanceof Patient) {
                    patientFunctions();
                } else if (userLoggedIn instanceof Doctor) { // User is a Doctor instance
                    doctorFunctions();

                } else if (userLoggedIn instanceof Pharmacist) { // User is a Pharmacist instance
                    pharmacistFunctions();
                } else { // User is a Administrator instance
                    Administrator admin = (Administrator) userLoggedIn;
                    admin.displayMenu();
                }
            }

            if (userLoggedIn == null && !loggedOut) {
                System.out.println("Login failed! Please try again!");
            }

        }
    }

    //All Patient function & logic
    public static void patientFunctions() throws IOException {
        Patient patient = (Patient) userLoggedIn;
        ArrayList<Appointment> patientAppointments = patient.viewAppointments(patient.getHospitalID(),
                appts);
        patient.displayMenu();
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1: // View personal medical record
                patient.viewMedicalRecord();
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
                appts = CsvDB.readAppointments();      // Reload appointments after rescheduling
                schedules = CsvDB.readSchedules();     // Reload schedules after rescheduling
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
                ArrayList<AppointmentOutcomeRecord> outcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                patient.viewAppointmentOutcomeRecords(appts, outcomeRecords);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            break;
            case 9: // Log out
                userLoggedIn = null;
                loggedOut = true;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }
    }

    //All doctor functions and logic
    public static void doctorFunctions() {
        Doctor doctor = (Doctor) userLoggedIn;
        // ArrayList<Schedule> docSchedule = doctor.viewSchedule(doctor.getHospitalID(), schedules);

        doctor.displayMenu();
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
                userLoggedIn = null;
                loggedOut = true;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }

    }

    public static void pharmacistFunctions() {
        Pharmacist pharmacist = (Pharmacist) userLoggedIn;
        pharmacist.displayMenu();
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
                userLoggedIn = null;
                loggedOut = true;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }
    }

}
