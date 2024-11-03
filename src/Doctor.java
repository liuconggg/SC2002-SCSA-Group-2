
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User {

    public Doctor() {

    }

    public Doctor(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    public void displayMenu() {
        // TODO - implement Doctor.displayMenu
        System.out.println("\n=== Doctor Menu ===");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointment");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout\n");
    }

    public Doctor getDoctorById(String doctorID, ArrayList<User> users) {
        Doctor doctorFound = null;
        for (User doc : users) {
            if (doc.getHospitalID().equals(doctorID) && doc instanceof Doctor) {
                doctorFound = (Doctor) doc;
                break;
            }
        }

        return doctorFound;
    }

    public ArrayList<Schedule> viewSchedule(String doctorID, ArrayList<Schedule> schedules) {
        ArrayList<Schedule> doctorSchedule = new Schedule().getScheduleByDoctorID(doctorID, schedules);
        return doctorSchedule;
    }

    public void viewWeeklySchedule(ArrayList<Schedule> schedules, ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of weeks you want to view in advance (Press Enter to return): ");
        String weeksInput = sc.nextLine();

        if (weeksInput.trim().isEmpty()) {
            System.out.println("Press Enter to return");
            return;  // Return to the previous menu
        }

        int numberOfWeeks;
        try {
            numberOfWeeks = Integer.parseInt(weeksInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(numberOfWeeks);  // Calculate the end date based on the number of weeks
        ArrayList<Schedule> doctorSchedule = viewSchedule(getHospitalID(), schedules);
        ArrayList<Schedule> weeklySchedule = new ArrayList<>();

        // Iterate from today until the end date, excluding weekends
        LocalDate currentDate = today;
        while (!currentDate.isAfter(endDate)) {
            // Skip weekends (Saturday and Sunday)
            if (currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7) {
                currentDate = currentDate.plusDays(1);
                continue;  // Skip Saturday (6) and Sunday (7)
            }

            boolean scheduleExists = false;

            // Check if there is an existing schedule for the current date
            for (Schedule existingSchedule : doctorSchedule) {
                if (existingSchedule.getDate().equals(currentDate)) {
                    weeklySchedule.add(existingSchedule);
                    scheduleExists = true;
                    break;
                }
            }

            // If no schedule exists for the current date, create a default schedule
            if (!scheduleExists) {
                Schedule newSchedule = Schedule.createDefaultSchedule(getHospitalID(), currentDate);
                weeklySchedule.add(newSchedule);
            }

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        // Sort schedules by date to ensure they are in chronological order
        weeklySchedule.sort((s1, s2) -> s1.getDate().compareTo(s2.getDate()));

        // Display the schedule for the specified number of weeks
        for (Schedule schedule : weeklySchedule) {
            System.out.printf("Your Personal Schedule on %s:\n",
                    schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            for (int i = 0; i < schedule.getSession().length; i++) {
                String sessionInfo = schedule.getSession()[i];
                String sessionTime = App.sessionTimings[i];

                if (sessionInfo.equals("Available") || sessionInfo.equals("Unavailable")) {
                    System.out.printf("Session %d (%s): %s\n", i + 1, sessionTime, sessionInfo);
                } else {
                    // Need to retrieve the patient name
                    String patientId = schedule.getPatientIdFromSession(i);
                    Patient scheduledPatient = (patientId != null) ? Patient.getPatientById(patientId, users) : null;
                    if (scheduledPatient != null) {
                        String status = schedule.getSessionStatus(i);
                        if (status.equals("Pending")) {
                            System.out.printf("Session %d (%s): Pending with %s\n", i + 1, sessionTime, scheduledPatient.getName());
                        } else {
                            System.out.printf("Session %d (%s): Booked with %s\n", i + 1, sessionTime, scheduledPatient.getName());
                        }
                    } else {
                        System.out.printf("Session %d (%s): Booked (Unknown Patient, Patient ID: %s)\n", i + 1, sessionTime, patientId);
                    }
                }
            }
            System.out.println();
        }

        System.out.println("Press Enter to continue");
        sc.nextLine();  // Wait for the user to press Enter
    }

    public void viewUpcomingAppointments(ArrayList<Schedule> schedules, ArrayList<User> users) {
        LocalDate today = LocalDate.now();
        ArrayList<Schedule> doctorSchedule = viewSchedule(getHospitalID(), schedules);

        // Create a list to store appointment information grouped by date
        ArrayList<String> appointmentDetails = new ArrayList<>();
        LocalDate lastDisplayedDate = null;

        // Iterate through the schedules to find upcoming confirmed appointments
        for (Schedule schedule : doctorSchedule) {
            if (schedule.getDate().isBefore(today)) {
                continue;  // Skip past dates, only consider today or future dates
            }

            for (int i = 0; i < schedule.getSession().length; i++) {
                String sessionInfo = schedule.getSession()[i];

                // Check if the session is booked with a patient
                if (sessionInfo.contains("-Confirmed")) {
                    String patientId = schedule.getPatientIdFromSession(i);
                    Patient scheduledPatient = (patientId != null) ? Patient.getPatientById(patientId, users) : null;
                    if (scheduledPatient != null) {
                        // Display a new date header if the date has changed
                        if (lastDisplayedDate == null || !lastDisplayedDate.equals(schedule.getDate())) {
                            lastDisplayedDate = schedule.getDate();
                            System.out.println("\nAppointments for " + lastDisplayedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ":");
                            System.out.println("---------------------------------------------------------------");
                            System.out.printf("%-5s %-15s %s\n", "No.", "Time", "Patient Name");
                        }

                        // Format the appointment details into a consistent string
                        String formattedAppointment = String.format("%-5d %-15s %s",
                                appointmentDetails.size() + 1,
                                App.sessionTimings[i],
                                scheduledPatient.getName());
                        appointmentDetails.add(formattedAppointment);

                        // Print the formatted appointment
                        System.out.println(formattedAppointment);
                    }
                }
            }
        }

        // If no appointments were found
        if (appointmentDetails.isEmpty()) {
            System.out.println("No upcoming confirmed appointments.");
        }

        System.out.println("\nPress Enter to continue");
        new Scanner(System.in).nextLine();  // Wait for the user to press Enter
    }

    public void viewPatientMedicaRecords() {
    }

    public void acceptOrDeclineAppointmentRequests(ArrayList<Schedule> schedules, ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            ArrayList<Schedule> doctorSchedule = viewSchedule(getHospitalID(), schedules);
            ArrayList<String> pendingAppointments = new ArrayList<>();
            ArrayList<Schedule> pendingSchedules = new ArrayList<>();
            ArrayList<Integer> pendingSessionIndexes = new ArrayList<>();

            // Gather all pending appointments
            for (Schedule schedule : doctorSchedule) {
                if (schedule.getDate().isBefore(LocalDate.now())) {
                    continue;  // Skip past dates
                }

                for (int i = 0; i < schedule.getSession().length; i++) {
                    String sessionInfo = schedule.getSession()[i];

                    if (sessionInfo.contains("-Pending")) {
                        String patientId = sessionInfo.split("-")[0];
                        Patient scheduledPatient = Patient.getPatientById(patientId, users); // Assuming static method

                        if (scheduledPatient != null) {
                            // Add pending appointment details to the list
                            String appointmentDetail = String.format("\n%d. Date: %s, Time: %s, Patient: %s",
                                    pendingAppointments.size() + 1,
                                    schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    App.sessionTimings[i],
                                    scheduledPatient.getName());
                            pendingAppointments.add(appointmentDetail);
                            pendingSchedules.add(schedule);
                            pendingSessionIndexes.add(i);
                        }
                    }
                }
            }

            if (pendingAppointments.isEmpty()) {
                System.out.println("No pending appointments.");
                exit = true;
            } else {
                // Display pending appointments
                for (String appointment : pendingAppointments) {
                    System.out.println(appointment);
                }

                // Prompt for action
                System.out.println("\nEnter the number of the appointment to accept or decline, or select the last option to return (Press Enter to return):");
                String choiceInput = sc.nextLine();

                if (choiceInput.trim().isEmpty()) {
                    System.out.println("Press Enter to return");
                    exit = true;
                    continue;
                }

                try {
                    int choice = Integer.parseInt(choiceInput);

                    if (choice > 0 && choice <= pendingAppointments.size()) {
                        // Retrieve the corresponding schedule and session index
                        Schedule chosenSchedule = pendingSchedules.get(choice - 1);
                        int sessionIndex = pendingSessionIndexes.get(choice - 1);

                        // Accept or decline the appointment
                        System.out.print("Enter 'A' to Accept or 'D' to Decline (Press Enter to return): ");
                        String decisionInput = sc.nextLine();

                        if (decisionInput.trim().isEmpty()) {
                            System.out.println("Press Enter to return");
                            continue;
                        }

                        char decision = decisionInput.toUpperCase().charAt(0);

                        if (decision == 'A') {
                            try {
                                chosenSchedule.acceptAppointment(sessionIndex);
                                ArrayList<Appointment> appointments = CsvDB.readAppointments();

                                String appointmentId = "A" + String.format("%04d", appointments.size() + 1);
                                Appointment appointment = new Appointment(appointmentId, chosenSchedule.getPatientIdFromSession(sessionIndex),
                                        chosenSchedule.getDoctorID(),
                                        //change here
                                        chosenSchedule.getDate(), sessionIndex + 1, "Confirmed"
                                );
                                appointments.add(appointment);
                                CsvDB.saveAppointments(appointments);
                                System.out.println("\nAppointment has been accepted.");
                            } catch (IOException e) {
                                System.out.println("An error occurred while saving the appointment: " + e.getMessage());
                            }
                        } else if (decision == 'D') {
                            // add logic here for appointment outcome
                            chosenSchedule.declineAppointment(sessionIndex);
                            System.out.println("\nAppointment has been declined. The patient will be notified.");

                            // Notify the patient
                            String patientId = chosenSchedule.getPatientIdFromSession(sessionIndex);
                            Patient declinedPatient = Patient.getPatientById(patientId, users);
                            if (declinedPatient != null) {
                                // Example: Just printing the notification here; in real-world you'd use an email or message service
                                System.out.printf("Notification sent to Patient %s: Your appointment on %s at %s has been declined.\n",
                                        declinedPatient.getName(),
                                        chosenSchedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                        App.sessionTimings[sessionIndex]);
                            }
                        } else {
                            System.out.println("Invalid input, please enter 'A' or 'D'.");
                        }
                        // Update CSV with the new status
                        CsvDB.saveSchedules(schedules);
                    } else {
                        System.out.println("\nInvalid choice. Please select a valid appointment number or return to exit.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }
    }

    public void setAvailability(ArrayList<Schedule> schedules) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Ask for the date to change availability
            System.out.println("Enter the date in format (dd/MM/yyyy) to view and change availability (Press Enter to return):");
            String inputDate = sc.nextLine();

            if (inputDate.trim().isEmpty()) {
                System.out.println("Press Enter to return");
                exit = true;
                continue;
            }

            try {
                LocalDate selectedDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Schedule chosenSchedule = null;
                ArrayList<Schedule> doctorSchedule = viewSchedule(getHospitalID(), schedules);

                // Find the schedule for the selected date
                for (Schedule schedule : doctorSchedule) {
                    if (schedule.getDate().equals(selectedDate)) {
                        chosenSchedule = schedule;
                        break;
                    }
                }

                if (chosenSchedule == null) {
                    // If no schedule found, create a default schedule with all sessions available
                    chosenSchedule = Schedule.createDefaultSchedule(getHospitalID(), selectedDate);
                    schedules.add(chosenSchedule);
                }

                boolean continueUpdating = true;
                while (continueUpdating) {
                    // Display sessions to update
                    System.out.printf("\nSchedule for %s:\n\n", selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    for (int i = 0; i < chosenSchedule.getSession().length; i++) {
                        String sessionInfo = chosenSchedule.getSession()[i];
                        String sessionTime = App.sessionTimings[i];
                        System.out.printf("%d. Session %d (%s): %s\n", i + 1, i + 1, sessionTime, sessionInfo);
                    }

                    // Prompt for session to update
                    System.out.println("\nEnter the number of the session to update availability, or press Enter to return:");
                    String sessionChoiceInput = sc.nextLine();

                    if (sessionChoiceInput.trim().isEmpty()) {
                        continueUpdating = false;
                        continue;
                    }

                    try {
                        int sessionChoice = Integer.parseInt(sessionChoiceInput);

                        if (sessionChoice > 0 && sessionChoice <= chosenSchedule.getSession().length) {
                            // Update availability
                            String currentStatus = chosenSchedule.getSession()[sessionChoice - 1];
                            if (currentStatus.contains("-Pending")) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Unavailable";
                                // To add on patient appointment outcome logic
                                System.out.printf("\nPatient (?) pending booking has been declined. Session %d has been updated to Unavailable as it was pending.\n", sessionChoice);
                            } else if (currentStatus.equals("Available")) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Unavailable";
                                System.out.printf("\nSession %d has been updated to Unavailable.\n", sessionChoice);
                            } else if (currentStatus.equals("Unavailable")) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Available";
                                System.out.printf("\nSession %d has been updated to Available.\n", sessionChoice);
                            } else {
                                System.out.println("\nYou cannot update this session as you have confirmed appointments.");
                                continue;
                            }

                            // Update CSV with the new status
                            CsvDB.saveSchedules(schedules);
                        } else {
                            System.out.println("\nInvalid choice. Please select a valid session number or press enter to exit.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input format. Please use the correct format (dd/MM/yyyy).");
            }
        }
    }

}
