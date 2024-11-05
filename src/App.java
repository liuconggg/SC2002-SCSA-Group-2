
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
    private static ArrayList<Treatment> treatments;
    private static ArrayList<Diagnosis> diagnoses;
    private static Scanner sc = new Scanner(System.in);
    private static User user = null;

    public static void main(String[] args) throws Exception {

        users = CsvDB.readUsers();
        appts = CsvDB.readAppointments();
        schedules = CsvDB.readSchedules();
        diagnoses = CsvDB.readDiagnoses();
        inventory = CsvDB.readMedications();
        apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
        treatments = CsvDB.readTreatments();

        AuthenticationService auth = new AuthenticationService(users);
        while (true) {

            if (user != null) {
                user.displayMenu();

                if (user instanceof Patient) {
                    patientFunctions();
                } else if (user instanceof Doctor) {
                    doctorFunctions();
                } else if (user instanceof Pharmacist) {
                    pharmacistFunctions();
                } else if (user instanceof Administrator) { // User is a Administrator instance
                    administratorFunctions();
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
                ArrayList<AppointmentOutcomeRecord> outcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                patient.viewAppointmentOutcomeRecords(appts, outcomeRecords);
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
                doctor.viewPatientMedicalRecords(schedules, users, apptOutcomeRecords, diagnoses, treatments);
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
                doctor.recordAppointmentOutcome(appts, inventory, apptOutcomeRecords, diagnoses, treatments);
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

    // All Administrator functions and logic
    public static void administratorFunctions() {
        Administrator administrator = (Administrator) user;
        //administrator.displayMenu();
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                ////////////////////// View and Manage Hospital Staff //////////////////////
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Add Staff");
                System.out.println("2. View Staff");
                System.out.println("3. Update Staff");
                System.out.println("4. Delete Staff");
                System.out.print("Enter your choice: ");

                int staffChoice = sc.nextInt();
                sc.nextLine();
                switch (staffChoice) {
                    case 1: // Add Staff
                        administrator.addUser(users);;
                        break;
                    case 2: // View Staff
                        administrator.viewUsers();
                        break;
                    case 3: // Update Staff
                        administrator.updateUser();
                        break;
                    case 4: // Delete Staff
                        administrator.deleteUser();
                        break;
                    default:
                        System.out.println("Invalid choice. Returning to main menu...");
                        break;
                }

                System.out.println();
                System.out.println("Press Enter to continue");
                sc.nextLine();
                break;
            case 2:
                ////////////////////// View Appointment details //////////////////////
                break;
            case 3:
                ////////////////////// View and Manage Medication Inventory //////////////////////
                try {
                    inventory = CsvDB.readMedications();
                } catch (IOException e) {
                    System.out.println("Error loading inventory: " + e.getMessage());
                    return; // Exit the method if loading fails
                }

                System.out.println("What would you like to do?");
                System.out.println("1. View Inventory");
                System.out.println("2. Add or Update Inventory Item");
                System.out.println("3. Delete Inventory Item");
                System.out.print("Enter your choice: ");

                int subChoice = sc.nextInt(); // Get user input for sub-choice
                sc.nextLine(); // Consume newline left after nextInt()

                switch (subChoice) {
                    case 1:
                        // View Inventory
                        administrator.viewInventory(inventory);
                        break;

                    case 2:
                        // Update Inventory
                        System.out.print("Enter the name of the medication to update: ");
                        String medicationName = sc.nextLine();

                        System.out.print("Enter the quantity to add: ");
                        while (!sc.hasNextInt()) {
                            System.out.print("Please enter a valid integer for the quantity: ");
                            sc.next(); // Discard invalid input
                        }
                        int quantity = sc.nextInt();
                        sc.nextLine(); // Consume newline left after nextInt()

                        // Update the inventory in memory
                        administrator.updateInventory(inventory, medicationName, quantity);

                        // Save the updated inventory to the CSV file
                        try {
                            CsvDB.saveMedications(inventory);
                            System.out.println("Inventory updated successfully.");
                        } catch (IOException e) {
                            System.out.println("Error saving inventory: " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Delete Inventory Item
                        System.out.print("Enter the name of the medication to delete: ");
                        String medicationToDelete = sc.nextLine();
                        administrator.deleteInventory(inventory, medicationToDelete);
                        break;

                    default:
                        System.out.println("Invalid choice. Returning to main menu...");
                        break;
                }

                System.out.println();
                System.out.println("Press Enter to continue");
                sc.nextLine();
                break;
            case 4:
                ////////////////////// Approve Replenishment Requests //////////////////////
                try {
                    inventory = CsvDB.readMedications();
                    replenishmentRequests = CsvDB.readRequest();
                    administrator.approveReplenishmentRequests(replenishmentRequests, inventory);
                } catch (IOException e) {
                    System.out.println("Error loading inventory or replenishment requests: " + e.getMessage());
                }
                break;
            case 5:
                ////////////////////// Logout //////////////////////
                user = null;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }
    }

}
