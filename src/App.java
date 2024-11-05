
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

        //Load all CSV data
        AuthenticationService auth = new AuthenticationService();
        while (true) {

            if (user != null) {
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
                user = auth.authenticate(users);
            }

        }
    }

    // Patient actions
    public static void patientFunctions() throws IOException {
        Patient patient = (Patient) user;
        int choice = -1;

        while (choice != 9) {
            try {
                users = CsvDB.readUsers();
                apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                diagnoses = CsvDB.readDiagnoses();
                treatments = CsvDB.readTreatments();
                schedules = CsvDB.readSchedules();
                appts = CsvDB.readAppointments();

                patient.displayMenu();
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

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
                        sc.nextLine();
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;
                    case 5: // Reschedule an Appointment
                        patient.rescheduleAppointment(appts, schedules, users);
                        sc.nextLine();
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;
                    case 6: // Cancel an Appointment
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
                    case 9:
                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        }

        if (choice == 9) {
            System.out.println("You have logged out ");
            user = null;
        }
    }

    // Doctor actions
    public static void doctorFunctions() throws IOException {
        Doctor doctor = (Doctor) user;
        // ArrayList<Schedule> docSchedule = doctor.viewSchedule(doctor.getHospitalID(),
        // schedules);
        int choice = -1;

        while (choice != 8) {
            try {
                users = CsvDB.readUsers();
                appts = CsvDB.readAppointments();
                schedules = CsvDB.readSchedules();
                diagnoses = CsvDB.readDiagnoses();
                inventory = CsvDB.readMedications();
                apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                treatments = CsvDB.readTreatments();

                doctor.displayMenu();
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        doctor.viewPatientMedicalRecords(schedules, users, apptOutcomeRecords, diagnoses, treatments);
                        break;
                    case 2:
                        doctor.updateMedicalRecord(schedules, users, apptOutcomeRecords, diagnoses, treatments, inventory);
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
                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        }

        if (choice == 8) {
            user = null;
            System.out.println("You have logged out ");
        }

    }

    // Pharmacist actions
    public static void pharmacistFunctions() throws IOException {
        Pharmacist pharmacist = (Pharmacist) user;
        int choice = -1;

        while (choice != 5) {
            try {
                pharmacist.displayMenu();

                inventory = CsvDB.readMedications();
                apptOutcomeRecords = CsvDB.readAppointmentOutcomeRecords();
                replenishmentRequests = CsvDB.readRequest();

                System.out.print("\nEnter your choice: ");
                choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        pharmacist.viewAppointmentOutcome(apptOutcomeRecords);
                        sc.nextLine();
                        System.out.println("\nPress Enter to continue");
                        sc.nextLine();
                        break;
                    case 2:
                        pharmacist.prescribeAndUpdate(apptOutcomeRecords, inventory);
                        sc.nextLine();
                        System.out.println("\nPress Enter to continue");
                        sc.nextLine();
                        break;
                    case 3:
                        pharmacist.viewInventory(inventory);
                        sc.nextLine();
                        System.out.println("\nPress Enter to continue");
                        sc.nextLine();
                        break;
                    case 4:
                        pharmacist.submitReplenishmentRequest(inventory, replenishmentRequests, pharmacist);
                        sc.nextLine();
                        System.out.println("\nPress Enter to continue");
                        sc.nextLine();
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        }

        if (choice == 5) {
            user = null;
            System.out.println("You have logged out ");

        }

    }

    // All Administrator functions and logic
    public static void administratorFunctions() throws IOException {
        Administrator administrator = (Administrator) user;
        int choice = -1;

        while (choice != 5) {
            try {
                users = CsvDB.readUsers();
                appts = CsvDB.readAppointments();
                inventory = CsvDB.readMedications();
                replenishmentRequests = CsvDB.readRequest();

                administrator.displayMenu();
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

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
                                administrator.addUser(users);
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
                        ////////////////////// View and Manage Medication Inventory
                        ////////////////////// //////////////////////
                

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
                            administrator.approveReplenishmentRequests(replenishmentRequests, inventory);

                        break;

                    case 5:
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }
        }

        ////////////////////// Logout //////////////////////

        if (choice == 5) {
            user = null;
            System.out.println("You have logged out ");

        }

    }

}
