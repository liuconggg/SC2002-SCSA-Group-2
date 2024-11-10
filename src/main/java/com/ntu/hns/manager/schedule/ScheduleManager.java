package com.ntu.hns.manager.schedule;

import static com.ntu.hns.App.sessionTimings;
import static com.ntu.hns.manager.appointment.AppointmentManager.updateAppointment;

import com.ntu.hns.CsvDB;
import com.ntu.hns.enums.AppointmentStatus;
import com.ntu.hns.enums.ScheduleStatus;
import com.ntu.hns.model.Appointment;
import com.ntu.hns.model.Schedule;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;
import com.ntu.hns.util.UtilProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class ScheduleManager implements ScheduleManagerInterface {
    private final CsvDB csvDB;
    private final DateTimeFormatter dateFormatter;
    private final Scanner scanner;
    private final UtilProvider utilProvider;

    @Autowired
    public ScheduleManager(
            CsvDB csvDB,
            DateTimeFormatter dateTimeFormatter,
            Scanner scanner,
            UtilProvider utilProvider) {
        this.csvDB = csvDB;
        this.dateFormatter = dateTimeFormatter;
        this.scanner = scanner;
        this.utilProvider = utilProvider;
    }

    @Override
    public void showWeeklySchedule() {
        List<Doctor> doctors = csvDB.readDoctors();
        List<Schedule> schedules = csvDB.readSchedules();

        // Display the available doctors to the user
        System.out.println("\nAvailable Doctors: ");
        int doctorIndex = 0;
        for (Doctor doc : doctors) {
            System.out.println((++doctorIndex) + ". " + doc.getName());
        }

        // Prompt user to select a doctor by index
        System.out.print("Select a doctor to view available slots (Press Enter to return): ");
        String doctorInput = scanner.nextLine().trim();

        // Allow patient to exit viewing process
        if (doctorInput.isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        int selectedDoctorIndex;
        try {
            selectedDoctorIndex = Integer.parseInt(doctorInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        // Validate the doctor's selection
        if (selectedDoctorIndex < 1 || selectedDoctorIndex > doctors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // Get the selected doctor
        Doctor selectedDoctor = doctors.get(selectedDoctorIndex - 1);
        String doctorID = selectedDoctor.getHospitalID();

        // Prompt user for number of weeks in advance to view
        System.out.print("Enter the number of weeks you want to view in advance: ");
        String weeksInput = scanner.nextLine();

        int numberOfWeeks;
        try {
            numberOfWeeks = Integer.parseInt(weeksInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        // Calculate date range, starting from the next day
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = today.plusWeeks(numberOfWeeks);

        // Filter schedules for the specified doctor
        ArrayList<Schedule> doctorSchedule = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID)) {
                doctorSchedule.add(schedule);
            }
        }

        LocalDate currentDate = startDate;

        // Check each date within the range for availability or assume availability if
        // not in schedule
        while (!currentDate.isAfter(endDate)) {
            boolean dateInSchedule = false;

            for (Schedule schedule : doctorSchedule) {
                if (schedule.getDate().equals(currentDate)) {
                    dateInSchedule = true;

                    // Display available sessions with timings for this date
                    System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n",
                            selectedDoctor.getName(), currentDate.format(dateFormatter));

                    for (int i = 0; i < schedule.getSession().length; i++) {
                        if (schedule.getSession()[i].equals("Available")) {
                            System.out.printf("Session %d (%s)\n", i + 1, sessionTimings[i]);
                        }
                    }
                    break;
                }
            }

            // If date not found in schedule, assume all sessions are available
            if (!dateInSchedule) {
                System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n",
                        selectedDoctor.getName(), currentDate.format(dateFormatter));
                for (int i = 0; i < sessionTimings.length; i++) {
                    System.out.printf("Session %d (%s)\n", i + 1, sessionTimings[i]);
                }
            }

            // Move to the next day, skipping weekends
            currentDate = currentDate.plusDays(1);
            if (currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7) {
                currentDate = currentDate.plusDays(2); // Skip Saturday and Sunday
            }
        }
    }

    @Override
    public void showWeeklySchedule(Doctor doctor) {
        System.out.print("\nEnter the number of weeks you want to view in advance (Press Enter to return): ");
        String weeksInput = scanner.nextLine();

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
        List<Schedule> doctorSchedule = utilProvider.getScheduleByDoctorID(doctor.getHospitalID());
        List<Schedule> weeklySchedule = new ArrayList<>();

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
                Schedule newSchedule = Schedule.createDefaultSchedule(doctor.getHospitalID(), currentDate);
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
                String sessionTime = sessionTimings[i];

                if (sessionInfo.equals("Available") || sessionInfo.equals("Unavailable")) {
                    System.out.printf("Session %d (%s): %s\n", i + 1, sessionTime, sessionInfo);
                } else {
                    // Need to retrieve the patient name
                    String patientId = schedule.getPatientIdFromSession(i);
                    Patient scheduledPatient = (patientId != null) ? utilProvider.getPatientById(patientId, csvDB.readPatients()) : null;
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
        scanner.nextLine(); // Wait for the user to press Enter
    }

    @Override
    public void updateSchedule(Doctor doctor) {
        boolean exit = false;

        while (!exit) {
            List<Schedule> doctorSchedule = utilProvider.getScheduleByDoctorID(doctor.getHospitalID());
            List<String> pendingAppointments = new ArrayList<>();
            List<Schedule> pendingSchedules = new ArrayList<>();
            List<Integer> pendingSessionIndexes = new ArrayList<>();

            // Gather all pending appointments
            for (Schedule schedule : doctorSchedule) {
                if (schedule.getDate().isBefore(LocalDate.now())) {
                    continue; // Skip past dates
                }

                for (int i = 0; i < schedule.getSession().length; i++) {
                    String sessionInfo = schedule.getSession()[i];

                    if (sessionInfo.contains(ScheduleStatus.PENDING.name())) {
                        String patientId = sessionInfo.split("-")[0];
                        Patient scheduledPatient = utilProvider.getPatientById(patientId, csvDB.readPatients()); // Assuming static method

                        if (scheduledPatient != null) {
                            // Add pending appointment details to the list
                            String appointmentDetail = String.format("Date: %s, Time: %s, com.ntu.hms.users.Patient: %s",
                                    schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    sessionTimings[i],
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
            String choiceInput = scanner.nextLine();

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
                    String decisionInput = scanner.nextLine();

                    if (decisionInput.trim().isEmpty()) {
                        System.out.println("\nReturning to previous menu...");
                        continue;
                    }

                    char decision = decisionInput.toUpperCase().charAt(0);

                    List<Appointment> appointments = csvDB.readAppointments();
                    if (decision == 'A') {
                        chosenSchedule.acceptAppointment(sessionIndex);
                        updateAppointment(appointments, chosenSchedule, sessionIndex, AppointmentStatus.CONFIRMED.name());
                    } else if (decision == 'D') {
                        chosenSchedule.declineAppointment(sessionIndex);
                        updateAppointment(appointments, chosenSchedule, sessionIndex, AppointmentStatus.CANCELLED.name());
                    } else {
                        System.out.println("\nInvalid input, please enter 'A' or 'D'.");
                    }

                    CsvDB.saveSchedules(csvDB.readSchedules());

                } else {
                    System.out.println("\nInvalid choice. Please select a valid appointment number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a valid number.");
            }
        }
    }

    @Override
    public void setAvailability(Doctor doctor) {
        List<Schedule> schedules = csvDB.readSchedules();
        List<Appointment> appointments = csvDB.readAppointments();

        boolean exit = false;

        while (!exit) {
            // Ask for the date to change availability
            System.out.println(
                    "\nEnter the date in format (dd/MM/yyyy) to view and change availability (Press Enter to return):");
            String inputDate = scanner.nextLine();

            if (inputDate.trim().isEmpty()) {
                System.out.println("\nPress Enter to return");
                exit = true;
                continue;
            }

            try {
                LocalDate selectedDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Schedule chosenSchedule = null;
                List<Schedule> doctorSchedule = utilProvider.getScheduleByDoctorID(doctor.getHospitalID());

                // Find the schedule for the selected date
                for (Schedule schedule : doctorSchedule) {
                    if (schedule.getDate().equals(selectedDate)) {
                        chosenSchedule = schedule;
                        break;
                    }
                }

                if (chosenSchedule == null) {
                    // If no schedule found, create a default schedule with all sessions available
                    chosenSchedule = Schedule.createDefaultSchedule(doctor.getHospitalID(), selectedDate);
                    schedules.add(chosenSchedule);
                }

                boolean continueUpdating = true;
                while (continueUpdating) {
                    // Display sessions to update
                    System.out.printf("\ncom.ntu.hms.Schedule for %s:\n\n",
                            selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    for (int i = 0; i < chosenSchedule.getSession().length; i++) {
                        String sessionInfo = chosenSchedule.getSession()[i];
                        String sessionTime = sessionTimings[i];
                        System.out.printf("%d. Session %d (%s): %s\n", i + 1, i + 1, sessionTime, sessionInfo);
                    }

                    // Prompt for session to update
                    System.out.println(
                            "\nEnter the number of the session to update availability, or press Enter to return:");
                    String sessionChoiceInput = scanner.nextLine();

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
                                        "\ncom.ntu.hms.users.Patient (?) pending booking has been declined. Session %d has been updated to Unavailable as it was pending.\n",
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
                            CsvDB.saveAppointments(appointments);
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
}
