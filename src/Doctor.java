
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User
        implements DoctorAppointmentManager, ScheduleManager, MedicalRecordManager, AppointmentOutcomeManager,
        AppointmentOutcomeViewer {

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
        System.out.print("\nEnter the number of weeks you want to view in advance (Press Enter to return): ");
        String weeksInput = sc.nextLine();

        if (weeksInput.trim().isEmpty()) {
            System.out.println("\nPress Enter to return");
            return; // Return to the previous menu
        }

        int numberOfWeeks;
        try {
            numberOfWeeks = Integer.parseInt(weeksInput);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input. Please enter a valid number.");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusWeeks(numberOfWeeks); // Calculate the end date based on the number of weeks
        ArrayList<Schedule> doctorSchedule = viewSchedule(getHospitalID(), schedules);
        ArrayList<Schedule> weeklySchedule = new ArrayList<>();

        // Iterate from today until the end date, excluding weekends
        LocalDate currentDate = today;
        while (!currentDate.isAfter(endDate)) {
            // Skip weekends (Saturday and Sunday)
            if (currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7) {
                currentDate = currentDate.plusDays(1);
                continue; // Skip Saturday (6) and Sunday (7)
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
            System.out.printf("\nYour Personal Schedule on %s:\n",
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
                        if (status.equals(ScheduleStatus.PENDING.name())) {
                            System.out.printf("Session %d (%s): Pending with %s\n", i + 1, sessionTime,
                                    scheduledPatient.getName());
                        } else {
                            System.out.printf("Session %d (%s): Booked with %s\n", i + 1, sessionTime,
                                    scheduledPatient.getName());
                        }
                    } else {
                        System.out.printf("Session %d (%s): Booked (Unknown Patient, Patient ID: %s)\n", i + 1,
                                sessionTime, patientId);
                    }
                }
            }
            System.out.println();
        }

        System.out.println("\nPress Enter to continue");
        sc.nextLine(); // Wait for the user to press Enter
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
                continue; // Skip past dates, only consider today or future dates
            }

            for (int i = 0; i < schedule.getSession().length; i++) {
                String sessionInfo = schedule.getSession()[i];

                // Check if the session is booked with a patient
                if (sessionInfo.contains("-" + ScheduleStatus.CONFIRMED.name())) {
                    String patientId = schedule.getPatientIdFromSession(i);
                    Patient scheduledPatient = (patientId != null) ? Patient.getPatientById(patientId, users) : null;
                    if (scheduledPatient != null) {
                        // Display a new date header if the date has changed
                        if (lastDisplayedDate == null || !lastDisplayedDate.equals(schedule.getDate())) {
                            lastDisplayedDate = schedule.getDate();
                            System.out.println("\nAppointments for "
                                    + lastDisplayedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ":");
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
            System.out.println("\nNo upcoming confirmed appointments.");
        }

        System.out.println("\nPress Enter to continue");
        new Scanner(System.in).nextLine(); // Wait for the user to press Enter
    }

    public void updateAppointment(ArrayList<Appointment> appts, Schedule chosenSchedule, int sessionIndex,
            String status) {
        Appointment selectedAcAppointment = Appointment
                .getAppointmentByScheduleAndSession(chosenSchedule, sessionIndex, appts);
        if (selectedAcAppointment != null) {
            selectedAcAppointment.setStatus(status);
        } else {
            System.err.println(
                    "\nAppointment not found for the chosen schedule and session index. Please check the appointments list.");
        }

        try {
            CsvDB.saveAppointments(appts);
        } catch (IOException e) {
            System.err.println("\nError while saving appointments: " + e.getMessage());
        }

    }

    public void updateSchedule(ArrayList<Schedule> schedules, ArrayList<User> users, ArrayList<Appointment> appts) {
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
                    continue; // Skip past dates
                }

                for (int i = 0; i < schedule.getSession().length; i++) {
                    String sessionInfo = schedule.getSession()[i];

                    if (sessionInfo.contains(ScheduleStatus.PENDING.name())) {
                        String patientId = sessionInfo.split("-")[0];
                        Patient scheduledPatient = Patient.getPatientById(patientId, users); // Assuming static method

                        if (scheduledPatient != null) {
                            // Add pending appointment details to the list
                            String appointmentDetail = String.format("Date: %s, Time: %s, Patient: %s",
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

            // If no pending appointments, inform the user and exit
            if (pendingAppointments.isEmpty()) {
                System.out.println("\nNo pending appointments.");
                exit = true;
                continue;
            }

            // Sort the schedules and session indexes based on date and time
            ArrayList<Integer> sortedIndices = new ArrayList<>();
            for (int i = 0; i < pendingSchedules.size(); i++) {
                sortedIndices.add(i);
            }

            sortedIndices.sort((index1, index2) -> {
                Schedule schedule1 = pendingSchedules.get(index1);
                Schedule schedule2 = pendingSchedules.get(index2);
                int dateComparison = schedule1.getDate().compareTo(schedule2.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }
                // If dates are equal, compare based on session time
                return Integer.compare(pendingSessionIndexes.get(index1), pendingSessionIndexes.get(index2));
            });

            // Display sorted pending appointments
            System.out.println("\n=== Pending Appointments ===");
            for (int i = 0; i < sortedIndices.size(); i++) {
                int sortedIndex = sortedIndices.get(i);
                System.out.printf("%d. %s\n", i + 1, pendingAppointments.get(sortedIndex));
            }

            // Prompt for action
            System.out.println("\nEnter the number of the appointment to accept or decline, or press Enter to return:");
            String choiceInput = sc.nextLine();

            if (choiceInput.trim().isEmpty()) {
                System.out.println("\nReturning to previous menu...");
                exit = true;
                continue;
            }

            try {
                int choice = Integer.parseInt(choiceInput);

                if (choice > 0 && choice <= sortedIndices.size()) {
                    // Retrieve the corresponding schedule and session index
                    int sortedIndex = sortedIndices.get(choice - 1);
                    Schedule chosenSchedule = pendingSchedules.get(sortedIndex);
                    int sessionIndex = pendingSessionIndexes.get(sortedIndex);

                    // Accept or decline the appointment
                    System.out.print("\nEnter 'A' to Accept or 'D' to Decline (Press Enter to return): ");
                    String decisionInput = sc.nextLine();

                    if (decisionInput.trim().isEmpty()) {
                        System.out.println("\nReturning to previous menu...");
                        continue;
                    }

                    char decision = decisionInput.toUpperCase().charAt(0);

                    if (decision == 'A') {
                        chosenSchedule.acceptAppointment(sessionIndex);
                        updateAppointment(appts, chosenSchedule, sessionIndex, AppointmentStatus.CONFIRMED.name());
                    } else if (decision == 'D') {
                        chosenSchedule.declineAppointment(sessionIndex);
                        updateAppointment(appts, chosenSchedule, sessionIndex, AppointmentStatus.CANCELLED.name());
                    } else {
                        System.out.println("\nInvalid input, please enter 'A' or 'D'.");
                    }

                    CsvDB.saveSchedules(schedules);

                } else {
                    System.out.println("\nInvalid choice. Please select a valid appointment number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
            }
        }
    }

    public void setAvailability(ArrayList<Schedule> schedules, ArrayList<Appointment> appointments) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Ask for the date to change availability
            System.out.println(
                    "\nEnter the date in format (dd/MM/yyyy) to view and change availability (Press Enter to return):");
            String inputDate = sc.nextLine();

            if (inputDate.trim().isEmpty()) {
                System.out.println("\nPress Enter to return");
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
                    System.out.printf("\nSchedule for %s:\n\n",
                            selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    for (int i = 0; i < chosenSchedule.getSession().length; i++) {
                        String sessionInfo = chosenSchedule.getSession()[i];
                        String sessionTime = App.sessionTimings[i];
                        System.out.printf("%d. Session %d (%s): %s\n", i + 1, i + 1, sessionTime, sessionInfo);
                    }

                    // Prompt for session to update
                    System.out.println(
                            "\nEnter the number of the session to update availability, or press Enter to return:");
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
                            if (currentStatus.contains("-" + ScheduleStatus.PENDING.name())) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Unavailable";
                                // To add on patient appointment outcome logic

                                Appointment selectedAcAppointment = Appointment.getAppointmentByScheduleAndSession(
                                        chosenSchedule, sessionChoice - 1, appointments);
                                selectedAcAppointment.setStatus(AppointmentStatus.CANCELLED.name());
                                System.out.printf(
                                        "\nPatient (?) pending booking has been declined. Session %d has been updated to Unavailable as it was pending.\n",
                                        sessionChoice);
                            } else if (currentStatus.equals("Available")) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Unavailable";
                                System.out.printf("\nSession %d has been updated to Unavailable.\n", sessionChoice);
                            } else if (currentStatus.equals("Unavailable")) {
                                chosenSchedule.getSession()[sessionChoice - 1] = "Available";
                                System.out.printf("\nSession %d has been updated to Available.\n", sessionChoice);
                            } else {
                                System.out.println(
                                        "\nYou cannot update this session as you have confirmed appointments.");
                                continue;
                            }

                            // Update CSV with the new status
                            CsvDB.saveSchedules(schedules);
                            try {
                                CsvDB.saveAppointments(appointments);
                            } catch (IOException e) {
                                System.err.println("\nError while saving appointments: " + e.getMessage());
                            }
                        } else {
                            System.out.println(
                                    "\nInvalid choice. Please select a valid session number or press enter to exit.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input. Please enter a valid number.");
                    }
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input format. Please use the correct format (dd/MM/yyyy).");
            }
        }
    }

    public void updateAppointmentOutcome(ArrayList<Appointment> appointments, ArrayList<Medication> inventory,
            ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords, ArrayList<Diagnosis> diagnoses,
            ArrayList<Treatment> treatments) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Step 1: Filter and display confirmed appointments
            int appointmentIndex = 1;

            System.out.println("\n=== Confirmed Appointments ===");

            ArrayList<Appointment> confirmedAppointments = Appointment
                    .getConfirmedAppointmentsByDoctorID(getHospitalID(), appointments);

            for (Appointment appointment : confirmedAppointments) {
                System.out.printf("%d. Appointment ID: %s, Date: %s, Session: %s, Patient ID: %s\n",
                        appointmentIndex, appointment.getAppointmentID(),
                        appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        App.sessionTimings[appointment.getSession() - 1],
                        appointment.getPatientID());
                appointmentIndex++;
            }

            if (confirmedAppointments.isEmpty()) {
                System.out.println("No confirmed appointments found.");
                break;
            }

            // Step 2: Get the user's selection for which appointment they want to modify
            System.out.println("\nSelect the appointment to record the outcome (or press Enter to return):");
            String input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("\nReturning to main menu...");
                exit = true;
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice > 0 && choice <= confirmedAppointments.size()) {
                    // Retrieve the chosen appointment
                    Appointment selectedAppointment = confirmedAppointments.get(choice - 1);

                    LocalDate appointmentDate = selectedAppointment.getDate();
                    LocalTime currentTime = LocalTime.now();
                    LocalDate currentDate = LocalDate.now();

                    String sessionTimeString = App.sessionTimings[selectedAppointment.getSession() - 1].substring(0, 5);
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime sessionTime = LocalTime.parse(sessionTimeString, timeFormatter);

                    if (appointmentDate.isAfter(currentDate)
                            || (appointmentDate.isEqual(currentDate) && sessionTime.isAfter(currentTime))) {
                        System.out.println(
                                "\nThe appointment time has not passed yet. You cannot record the outcome at this time.");
                        continue;
                    }

                    // Step 3: Prompt the user to record the outcome using Y/N input
                    System.out.print("\nEnter the outcome for this appointment ('Y' for Completed or 'N' for No-Show): ");
                    String outcome = sc.nextLine().trim().toUpperCase();

                    if (outcome.equals("Y")) {
                        String consultationNotes = "";
                        String typeOfService = "";
                        String diagnosisNotes = "";
                        String treatmentNotes = "";

                        System.out.println("\nEnter type of service (enter na if none): ");
                        typeOfService = sc.nextLine();

                        System.out.println("\nEnter consultation notes (enter na if none): ");
                        consultationNotes = sc.nextLine();

                        System.out.println("\nEnter diagnosis (enter na if none): ");
                        diagnosisNotes = sc.nextLine();

                        System.out.println("\nEnter treatment (enter na if none): ");
                        treatmentNotes = sc.nextLine();

                        // Step 4: Display medication inventory and allow the user to prescribe
                        // medicines
                        ArrayList<MedicationItem> prescribedMedicines = new ArrayList<>();
                        boolean addingMedicines = true;

                        while (addingMedicines) {
                            System.out.println("\nAvailable Medicines:");
                            int medIndex = 1;
                            for (Medication med : inventory) {
                                System.out.printf("%d. %s (Available: %d units)\n", medIndex, med.getMedicationName(),
                                        med.getTotalQuantity());
                                medIndex++;
                            }

                            System.out
                                    .println("\nSelect a medicine by number to prescribe (or press Enter to finish): ");
                            String medInput = sc.nextLine();

                            if (medInput.trim().isEmpty()) {
                                addingMedicines = false;
                                continue;
                            }

                            try {
                                int medChoice = Integer.parseInt(medInput);

                                if (medChoice > 0 && medChoice <= inventory.size()) {
                                    Medication selectedMed = inventory.get(medChoice - 1);
                                    System.out.printf("\nEnter quantity for %s: ", selectedMed.getMedicationName());
                                    String quantityInput = sc.nextLine();
                                    int quantity = Integer.parseInt(quantityInput);

                                    if (quantity > 0 && quantity <= selectedMed.getTotalQuantity()) {
                                        System.out.println("\nCurrent Prescribing List:");
                                        MedicationItem prescribedMed = new MedicationItem();
                                        prescribedMed.setMedicationID(selectedMed.getMedicationID());
                                        prescribedMed.setMedicationName(selectedMed.getMedicationName());
                                        prescribedMed.setQuantity(quantity);
                                        prescribedMedicines.add(prescribedMed);
                                        for (MedicationItem med : prescribedMedicines) {
                                            System.out.printf("- %s: %d units\n", med.getMedicationName(),
                                                    med.getQuantity());
                                        }
                                    } else {
                                        System.out.println(
                                                "\nInvalid quantity. Please enter a valid amount within the available units.");
                                    }
                                } else {
                                    System.out.println("\nInvalid choice. Please select a valid medicine number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("\nInvalid input. Please enter a valid number.");
                            }
                        }

                        // Save the details to an outcome record including prescribed medications
                        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(
                                selectedAppointment.getAppointmentID(), typeOfService, consultationNotes,
                                prescribedMedicines, AppointmentOutcomeStatus.PENDING.name());

                        Treatment treatment = new Treatment(selectedAppointment.getAppointmentID(),
                                selectedAppointment.getPatientID(), treatmentNotes);
                        Diagnosis diagnosis = new Diagnosis(selectedAppointment.getAppointmentID(),
                                selectedAppointment.getPatientID(), diagnosisNotes);

                        treatments.add(treatment);
                        diagnoses.add(diagnosis);
                        apptOutcomeRecords.add(outcomeRecord);

                        CsvDB.saveAppointmentOutcomeRecords(apptOutcomeRecords);
                        CsvDB.saveDiagnosis(diagnoses);
                        CsvDB.saveTreatment(treatments);

                        // CsvDB.saveAppointmentOutcomeRecords(apptOutcomeRecords);
                        selectedAppointment.setStatus(AppointmentStatus.COMPLETED.name());
                        System.out.println("\nAppointment outcome recorded successfully as 'Completed'.");

                    } else if (outcome.equals("N")) {
                        selectedAppointment.setStatus(AppointmentStatus.NO_SHOW.name());
                        System.out.println("\nAppointment outcome recorded successfully as 'No-Show'.");
                    } else {
                        System.out.println("\nInvalid input. Please enter either 'Y' for Completed or 'N' for No-Show.");
                        continue; // Continue to allow user to retry entering a valid input
                    }

                    // Save the updated appointment list to CSV
                    try {
                        CsvDB.saveAppointments(appointments);
                    } catch (IOException e) {
                        System.err.println("\nError while saving appointments: " + e.getMessage());
                    }
                } else {
                    System.out.println("\nInvalid choice. Please select a valid appointment number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void viewMedicalRecord(ArrayList<Schedule> schedules, ArrayList<User> users,
            ArrayList<AppointmentOutcomeRecord> outcomeRecords, ArrayList<Diagnosis> diagnoses,
            ArrayList<Treatment> treatments) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> uniquePatientIds = new ArrayList<>();
        ArrayList<User> patientsUnderCare = new ArrayList<>();
        Patient currentPatient = null;

        // Step 1: Filter all confirmed schedules for the logged-in doctor
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(getHospitalID())) {
                for (int i = 0; i < schedule.getSession().length; i++) {
                    String sessionInfo = schedule.getSession()[i];
                    if (sessionInfo.contains(ScheduleStatus.CONFIRMED.name())) {
                        String patientId = sessionInfo.split("-")[0]; // Extract the patient ID
                        if (!uniquePatientIds.contains(patientId)) {
                            uniquePatientIds.add(patientId);
                        }
                    }
                }
            }
        }

        // Step 2: Retrieve all user (patient) objects based on unique patient IDs
        for (String patientId : uniquePatientIds) {
            for (User user : users) {
                if (user.getHospitalID().equals(patientId) && user instanceof Patient) {
                    patientsUnderCare.add(user);
                    break;
                }
            }
        }

        // Step 3: Display the list of patients under the doctor's care
        if (patientsUnderCare.isEmpty()) {
            System.out.println("\nNo patients are currently under your care.");
            return;
        }

        boolean viewingRecords = true;
        while (viewingRecords) {
            System.out.println("\n=== Patients Under Your Care ===");
            int index = 1;
            for (User patient : patientsUnderCare) {
                System.out.printf("%d. Patient Name: %s, Age: %d, Gender: %s\n", index, patient.getName(),
                        patient.getAge(), patient.getGender());
                index++;
            }

            // Step 4: Let the doctor select a patient to view their medical records
            System.out.println("\nSelect a patient to view their medical records (or press Enter to return):");
            String input = sc.nextLine();

            if (input.trim().isEmpty()) {
                System.out.println("\nReturning to previous menu...");
                viewingRecords = false; // Exit the loop
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice > 0 && choice <= patientsUnderCare.size()) {
                    // Retrieve the chosen patient object
                    User selectedPatient = patientsUnderCare.get(choice - 1);

                    // Display the patient's medical record information (assuming a method exists)
                    System.out.println("\n=== Medical Records for " + selectedPatient.getName() + " ===");
                    if (selectedPatient instanceof Patient) {
                        currentPatient = (Patient) selectedPatient;
                        System.out.println("\n==================== Medical Record ====================");
                        System.out.printf("ID            : %s\n", currentPatient.getHospitalID());
                        System.out.printf("Name          : %s\n", currentPatient.getName());
                        System.out.printf("Date of Birth : %s\n", currentPatient.getDateOfBirth());
                        System.out.printf("Gender        : %s\n", currentPatient.getGender());
                        System.out.printf("Contact       : %s\n", currentPatient.getPhoneNumber());
                        System.out.printf("Email         : %s\n", currentPatient.getEmail());
                        System.out.printf("Blood Type    : %s\n", currentPatient.getBloodType());
                        System.out.println("=======================================================");

                        // Display diagnosis and treatment information with prescriptions only
                        System.out.println("\n================= Diagnoses and Treatments =============");
                        int recordNo = 1;
                        boolean hasRecords = false;

                        for (Diagnosis diagnosis : diagnoses) {
                            for (Treatment treatment : treatments) {
                                if (diagnosis.getPatientID().equals(currentPatient.getHospitalID())
                                        && treatment.getPatientID().equals(currentPatient.getHospitalID())
                                        && diagnosis.getAppointmentID().equals(treatment.getAppointmentID())) {

                                    // Display basic record information
                                    System.out.printf("Record %d:\n", recordNo++);
                                    System.out.printf("  Appointment ID : %s\n", diagnosis.getAppointmentID());
                                    System.out.printf("  Diagnosis      : %s\n", diagnosis.getDiagnosis());
                                    System.out.printf("  Treatment      : %s\n", treatment.getTreatment());

                                    // Find and display only the prescriptions
                                    AppointmentOutcomeRecord outcome = getOutcomeByAppointmentID(outcomeRecords,
                                            diagnosis.getAppointmentID());
                                    if (outcome != null && !outcome.getPrescriptions().isEmpty()) {
                                        String prescriptions = outcome.getPrescriptions().stream()
                                                .map(MedicationItem::toString)
                                                .reduce((p1, p2) -> p1 + ", " + p2)
                                                .orElse("No prescriptions.");
                                        System.out.printf("  Prescriptions  : %s\n", prescriptions);
                                    } else {
                                        System.out.println("  Prescriptions  : No prescriptions available.");
                                    }

                                    System.out.println("-------------------------------------------------------");
                                    hasRecords = true;
                                }
                            }
                        }

                        if (!hasRecords) {
                            System.out.println("\nNo diagnoses or treatments found for this patient.");
                        }
                        System.out.println("=======================================================");
                    }
                } else {
                    System.out.println("\nInvalid choice. Please select a valid patient number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
            }
        }
    }

    public AppointmentOutcomeRecord getOutcomeByAppointmentID(ArrayList<AppointmentOutcomeRecord> outcomeRecords,
            String appointmentID) {
        for (AppointmentOutcomeRecord outcome : outcomeRecords) {
            if (outcome.getAppointmentID().equals(appointmentID)) {
                return outcome;
            }
        }
        return null;
    }

    public void updateMedicalRecord(ArrayList<Schedule> schedules, ArrayList<User> users,
            ArrayList<AppointmentOutcomeRecord> outcomeRecords, ArrayList<Diagnosis> diagnoses,
            ArrayList<Treatment> treatments, ArrayList<Medication> inventory) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            // Display the appointment outcomes for user selection
            System.out.println("\nSelect an Appointment Outcome by number:\n");
            for (int i = 0; i < outcomeRecords.size(); i++) {
                AppointmentOutcomeRecord outcome = outcomeRecords.get(i);
                System.out.println((i + 1) + ". Appointment ID: " + outcome.getAppointmentID());
            }

            // Get user input for appointment outcome selection
            System.out.print("\nEnter the number of the Appointment Outcome (or press Enter to return): ");
            String selectedOutcomeIndexInput = sc.nextLine();

            if (selectedOutcomeIndexInput.trim().isEmpty()) {
                System.out.println("Returning to previous menu...");
                return;
            }

            int selectedOutcomeIndex;

            try {
                selectedOutcomeIndex = Integer.parseInt(selectedOutcomeIndexInput) - 1;
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
                continue;
            }

            if (selectedOutcomeIndex < 0 || selectedOutcomeIndex >= outcomeRecords.size()) {
                System.out.println("\nInvalid selection. Please select a valid number.");
                continue;
            }

            // Get the selected appointment outcome
            AppointmentOutcomeRecord selectedOutcome = outcomeRecords.get(selectedOutcomeIndex);
            String appointmentId = selectedOutcome.getAppointmentID();

            // Find the corresponding diagnosis and treatment
            Diagnosis selectedDiagnosis = null;
            for (Diagnosis diagnosis : diagnoses) {
                if (diagnosis.getAppointmentID().equals(appointmentId)) {
                    selectedDiagnosis = diagnosis;
                    break;
                }
            }

            Treatment selectedTreatment = null;
            for (Treatment treatment : treatments) {
                if (treatment.getAppointmentID().equals(appointmentId)) {
                    selectedTreatment = treatment;
                    break;
                }
            }

            // Ask the user if they want to update diagnosis
            if (selectedDiagnosis != null) {
                System.out.println("\nCurrent Diagnosis: " + selectedDiagnosis.getDiagnosis());
                String updateDiagnosisResponse;
                while (true) {
                    System.out.print("\nDo you want to update the diagnosis? (y/n or press Enter to return to main menu): ");
                    updateDiagnosisResponse = sc.nextLine();

                    if (updateDiagnosisResponse.isEmpty()) {
                        System.out.println("\nReturning to main menu...");
                        return; // Exit the method and return to the main menu
                    }

                    if (updateDiagnosisResponse.equalsIgnoreCase("y") || updateDiagnosisResponse.equalsIgnoreCase("n")) {
                        break;
                    }

                    System.out.println("\nInvalid input. Please enter 'y' to update, 'n' to skip, or press Enter to return to the main menu.");
                }

                if (updateDiagnosisResponse.equalsIgnoreCase("y")) {
                    System.out.print("\nEnter new diagnosis details (or press Enter to cancel): ");
                    String newDiagnosisDetails = sc.nextLine();
                    if (!newDiagnosisDetails.trim().isEmpty()) {
                        String currentDiagnosis = selectedDiagnosis.getDiagnosis();
                        String updatedDiagnosis;

                        // Check if current diagnosis already contains " - Updated:"
                        if (currentDiagnosis.contains(" - Updated:")) {
                            // Extract the original part before " - Updated:"
                            int updatedIndex = currentDiagnosis.indexOf(" - Updated:");
                            String originalDiagnosis = currentDiagnosis.substring(0, updatedIndex);
                            updatedDiagnosis = originalDiagnosis + " - Updated: " + newDiagnosisDetails;
                        } else {
                            // Append " - Updated:" for the first time
                            updatedDiagnosis = currentDiagnosis + " - Updated: " + newDiagnosisDetails;
                        }

                        selectedDiagnosis.setDiagnosis(updatedDiagnosis);
                        System.out.println("\nDiagnosis updated.");
                    } else {
                        System.out.println("\nDiagnosis update cancelled.");
                    }
                }
            } else {
                System.out.println("\nNo diagnosis found for this appointment.");
            }

            // Ask the user if they want to update treatment
            if (selectedTreatment != null) {
                System.out.println("\nCurrent Treatment: " + selectedTreatment.getTreatment());
                String updateTreatmentResponse;
                while (true) {
                    System.out.print("\nDo you want to update the treatment? (y/n or press Enter to return to main menu): ");
                    updateTreatmentResponse = sc.nextLine();

                    if (updateTreatmentResponse.isEmpty()) {
                        System.out.println("\nReturning to main menu...");
                        return; // Exit the method and return to the main menu
                    }

                    if (updateTreatmentResponse.equalsIgnoreCase("y") || updateTreatmentResponse.equalsIgnoreCase("n")) {
                        break;
                    }

                    System.out.println("\nInvalid input. Please enter 'y' to update, 'n' to skip, or press Enter to return to the main menu.");
                }

                if (updateTreatmentResponse.equalsIgnoreCase("y")) {
                    System.out.print("\nEnter new treatment details (or press Enter to cancel): ");
                    String newTreatmentDetails = sc.nextLine();
                    if (!newTreatmentDetails.trim().isEmpty()) {
                        String currentTreatment = selectedTreatment.getTreatment();
                        String updatedTreatment;

                        // Check if current treatment already contains " - Updated:"
                        if (currentTreatment.contains(" - Updated:")) {
                            // Extract the original part before " - Updated:"
                            int updatedIndex = currentTreatment.indexOf(" - Updated:");
                            String originalTreatment = currentTreatment.substring(0, updatedIndex);
                            updatedTreatment = originalTreatment + " - Updated: " + newTreatmentDetails;
                        } else {
                            // Append " - Updated:" for the first time
                            updatedTreatment = currentTreatment + " - Updated: " + newTreatmentDetails;
                        }

                        selectedTreatment.setTreatment(updatedTreatment);
                        System.out.println("\nTreatment updated.");
                    } else {
                        System.out.println("\nTreatment update cancelled.");
                    }
                }
            } else {
                System.out.println("\nNo treatment found for this appointment.");
            }

            if (AppointmentStatus.PENDING.name().equalsIgnoreCase(selectedOutcome.getPrescriptionStatus())) {
                System.out.println("\nPrescription Status: " + selectedOutcome.getPrescriptionStatus());
                String editPrescriptionResponse;

                while (true) {
                    System.out.print("\nDo you want to edit the prescriptions? (y/n or press Enter to skip): ");
                    editPrescriptionResponse = sc.nextLine();

                    if (editPrescriptionResponse.isEmpty()) {
                        System.out.println("Skipping prescription edit...");
                        return; // Exit to the main menu or continue to the next section
                    }

                    if (editPrescriptionResponse.equalsIgnoreCase("y") || editPrescriptionResponse.equalsIgnoreCase("n")) {
                        break;
                    }

                    System.out.println("\nInvalid input. Please enter 'y' to edit, 'n' to skip, or press Enter to return.");
                }

                if (editPrescriptionResponse.equalsIgnoreCase("y")) {
                    System.out.print("\nEnter new prescription details (or press Enter to cancel): ");
                    ArrayList<MedicationItem> prescribedMedicines = new ArrayList<>();
                    boolean addingMedicines = true;

                    while (addingMedicines) {
                        System.out.println("\nAvailable Medicines:");
                        int medIndex = 1;
                        for (Medication med : inventory) {
                            System.out.printf("%d. %s (Available: %d units)\n", medIndex, med.getMedicationName(),
                                    med.getTotalQuantity());
                            medIndex++;
                        }

                        System.out.print("\nSelect a medicine by number to prescribe (or press Enter to finish): ");
                        String medInput = sc.nextLine();

                        if (medInput.trim().isEmpty()) {
                            addingMedicines = false;
                            continue;
                        }

                        try {
                            int medChoice = Integer.parseInt(medInput);

                            if (medChoice > 0 && medChoice <= inventory.size()) {
                                Medication selectedMed = inventory.get(medChoice - 1);
                                System.out.printf("\nEnter quantity for %s: ", selectedMed.getMedicationName());
                                String quantityInput = sc.nextLine();
                                int quantity = Integer.parseInt(quantityInput);

                                if (quantity > 0 && quantity <= selectedMed.getTotalQuantity()) {
                                    System.out.println("\nCurrent Prescribing List:");
                                    MedicationItem prescribedMed = new MedicationItem();
                                    prescribedMed.setMedicationID(selectedMed.getMedicationID());
                                    prescribedMed.setMedicationName(selectedMed.getMedicationName());
                                    prescribedMed.setQuantity(quantity);
                                    prescribedMedicines.add(prescribedMed);
                                    for (MedicationItem med : prescribedMedicines) {
                                        System.out.printf("- %s: %d units\n", med.getMedicationName(),
                                                med.getQuantity());
                                    }
                                } else {
                                    System.out.println("\nInvalid quantity. Please enter a valid amount within the available units.");
                                }
                            } else {
                                System.out.println("\nInvalid choice. Please select a valid medicine number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\nInvalid input. Please enter a valid number.");
                        }
                    }

                    selectedOutcome.setPrescriptions(prescribedMedicines);
                } else {
                    System.out.println("\nPlease enter valid input");
                }
            }

            try {
                CsvDB.saveAppointmentOutcomeRecords(outcomeRecords);
                CsvDB.saveDiagnosis(diagnoses);
                CsvDB.saveTreatment(treatments);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
