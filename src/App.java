
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
                    Pharmacist pharmacist = (Pharmacist) userLoggedIn;
                    pharmacist.displayMenu();

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
                boolean changing = true;
                int action;
                while (changing) {
                    System.out.println("Select the information to update");
                    System.out.println("1. Name");
                    System.out.println("2. Phone Number");
                    System.out.println("3. Email Address");
                    System.out.println("4. Confirm Changes & Exit");
                    System.out.print("Your Choice:");
                    action = sc.nextInt();
                    sc.nextLine();
                    switch (action) {
                        case 1:
                            System.out.print("Enter your new name: ");
                            String newName = sc.nextLine();
                            patient.setName(newName);
                            break;
                        case 2:
                            System.out.print("Enter your new phone number: ");
                            String newPhoneNumber = sc.nextLine();
                            patient.setPhoneNumber(newPhoneNumber);
                            break;
                        case 3:
                            System.out.print("Enter your new email address: ");
                            String newEmail = sc.nextLine();
                            patient.setEmail(newEmail);
                            break;
                        case 4:
                            CsvDB.saveUsers(users);
                            ; // Save changes upon exiting
                            changing = false;
                            break;
                    }
                }
                break;
            case 3: // View Available Appointment Slots
                patient.viewAvailableAppointment(schedules, users);
                break;
            case 4: // Schedule an Appointment
                patient.scheduleAppointment(schedules, users, patient.getHospitalID());
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 5: // Reschedule an Appointment
                break;
            case 6: // Cancel an Appointment
                // if (patientAppointments.size() > 0) {
                //     int apptCounter = 0;
                //     int appointmentChoice;
                //     Doctor doc;
                //     System.out.println("Which appointment do you want to cancel?");
                //     for (Appointment patientAppt : patientAppointments) {
                //         doc = new Doctor().getDoctorById(patientAppt.getDoctorID(), users);
                //         if (doc != null) {
                //             System.out.printf("%d. Appointment with Dr %s on %s is %s\n", ++apptCounter,
                //                     doc.getName(),
                //                     patientAppt.getDateTime()
                //                             .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                //                     patientAppt.getStatus());
                //         }

                //     }
                //     System.out.print("Your choice: ");
                //     appointmentChoice = sc.nextInt();
                //     if (appointmentChoice <= patientAppointments.size()) {
                //         doc = new Doctor().getDoctorById(
                //                 patientAppointments.get(appointmentChoice - 1).getDoctorID(), users);
                //         patientAppointments.get(appointmentChoice - 1).setStatus("Cancelled");
                //         CsvDB.saveAppointments(appts);
                //         System.out.printf("Your appointment with %s has been cancelled\n", doc.getName());
                //     }
                // } else {
                //     System.out.println("You have no scheduled appointments");
                // }
                // sc.nextLine();
                // System.out.println("Press Enter to continue...");
                // sc.nextLine();
                break;
            case 7: // View scheduled appointments
                // if (patientAppointments.size() > 0) {
                //     int apptCounter = 0;
                //     Doctor doc;

                //     System.out.println("Your scheduled appointment(s):");
                //     for (Appointment patientAppt : patientAppointments) {
                //         doc = new Doctor().getDoctorById(patientAppt.getDoctorID(), users);
                //         if (doc != null) {
                //             System.out.printf("%d. Appointment with Dr %s on %s is %s\n", ++apptCounter,
                //                     doc.getName(),
                //                     patientAppt.getDateTime()
                //                             .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                //                     patientAppt.getStatus());
                //         }
                //     }
                // } else {
                //     System.out.println("You have no scheduled appointments");
                // }
                // sc.nextLine();
                // System.out.println("Press Enter to continue...");
                // sc.nextLine();
                patient.viewScheduledAppointments(appts, users);
                sc.nextLine();
                System.out.println("Press Enter to continue...");
                sc.nextLine();
                break;
            case 8: // View Past Appointment Outcome Records
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
                doctor.setAvailability(schedules);
                break;
            case 5:
                doctor.acceptOrDeclineAppointmentRequests(schedules, users);
                break;
            case 6:
                doctor.viewUpcomingAppointments(schedules, users);
                break;
            case 7:
                break;
            case 8:
                userLoggedIn = null;
                loggedOut = true;
                System.out.println("You have logged out");
                sc.nextLine();
                break;
        }

    }

}
