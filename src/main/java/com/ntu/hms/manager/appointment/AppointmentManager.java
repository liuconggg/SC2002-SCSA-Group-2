package com.ntu.hms.manager.appointment;

import static com.ntu.hms.App.sessionTimings;
import static com.ntu.hms.enums.AppointmentStatus.*;
import static com.ntu.hms.model.Appointment.getAppointmentByScheduleAndSession;
import static com.ntu.hms.model.Schedule.createDefaultSchedule;
import static com.ntu.hms.util.UtilProvider.*;

import com.ntu.hms.CsvDB;
import com.ntu.hms.enums.AppointmentOutcomeStatus;
import com.ntu.hms.enums.AppointmentStatus;
import com.ntu.hms.enums.ScheduleStatus;
import com.ntu.hms.model.*;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.util.ScannerWrapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The AppointmentManager class is responsible for managing patient appointments. It includes
 * methods for scheduling, rescheduling, and canceling appointments, as well as displaying
 * appointment outcomes.
 */
public class AppointmentManager implements AppointmentManagerInterface {
  private final DateTimeFormatter dateFormatter;
  private final ScannerWrapper scanner;

  /**
   * Constructs an AppointmentManager with the specified DateTimeFormatter and ScannerWrapper.
   *
   * @param dateTimeFormatter the DateTimeFormatter to be used for formatting dates.
   * @param scanner the ScannerWrapper to be used for reading user input.
   */
  private AppointmentManager(DateTimeFormatter dateTimeFormatter, ScannerWrapper scanner) {
    this.dateFormatter = dateTimeFormatter;
    this.scanner = scanner;
  }

  /**
   * Schedules an appointment for a patient by allowing the patient to select a doctor, choose an
   * available date, and book a session.
   *
   * @param patient The patient for whom the appointment is being scheduled.
   */
  @Override
  public void scheduleAppointment(Patient patient) {
    // Display the available doctors to the user
    System.out.println("\nAvailable Doctors: ");
    List<Doctor> doctors = CsvDB.readDoctors();
    IntStream.range(0, doctors.size())
        .forEach(index -> System.out.printf("%d. %s%n", index + 1, doctors.get(index).getName()));

    // Prompt user to select a doctor by index
    System.out.print("\nSelect a doctor to schedule an appointment (Press Enter to return): ");
    String doctorInput = scanner.nextLine().trim();

    // Allow patient to exit scheduling process
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

    if (selectedDoctorIndex < 1 || selectedDoctorIndex > doctors.size()) {
      System.out.println("Invalid selection.");
      return;
    }

    // Get the selected doctor
    Doctor selectedDoctor = doctors.get(selectedDoctorIndex - 1);
    String doctorID = selectedDoctor.getHospitalID();

    // Prompt user for appointment date
    System.out.print("Enter the appointment date (dd/MM/yyyy) (Press Enter to return): ");
    String dateInput = scanner.nextLine().trim();

    // Allow patient to exit if they choose
    if (dateInput.isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    LocalDate appointmentDate;
    try {
      appointmentDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } catch (Exception e) {
      System.out.println("Invalid date format. Please use dd/MM/yyyy.");
      return;
    }

    // Find or create a schedule for the chosen date
    Schedule scheduleForDate = null;
    List<Schedule> schedules = CsvDB.readSchedules();
    for (Schedule schedule : schedules) {
      if (schedule.getDoctorID().equals(doctorID) && schedule.getDate().equals(appointmentDate)) {
        scheduleForDate = schedule;
        break;
      }
    }

    if (scheduleForDate == null) {
      scheduleForDate = Schedule.createDefaultSchedule(doctorID, appointmentDate);
      schedules.add(scheduleForDate);
    }

    // Display available sessions and allow patient to select
    System.out.println("\nAvailable sessions:");
    String[] session = scheduleForDate.getSession();
    boolean hasAvailableSession = false;
    for (int i = 0; i < session.length; i++) {
      if (session[i].equals("Available")) {
        System.out.printf("Session %d: Available\n", i + 1);
        hasAvailableSession = true;
      }
    }

    if (!hasAvailableSession) {
      System.out.println("No available sessions on this date.");
      return;
    }

    // Allow user to return without booking a session
    System.out.print("Select a session number to book (Press Enter to return): ");
    String sessionInput = scanner.nextLine().trim();

    if (sessionInput.isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    int sessionNumber;
    try {
      sessionNumber = Integer.parseInt(sessionInput);
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid session number.");
      return;
    }

    if (sessionNumber < 1
        || sessionNumber > session.length
        || !session[sessionNumber - 1].equals("Available")) {
      System.out.println("Invalid session selection or session not available.");
      return;
    }

    // Mark the session as booked with patient ID and Pending status
    session[sessionNumber - 1] = patient.getHospitalID() + "-" + ScheduleStatus.PENDING.name();
    scheduleForDate.setSession(session);

    // Save updated schedules to the CSV file
    CsvDB.saveSchedules(schedules);

    List<Appointment> appointments = CsvDB.readAppointments();
    String appointmentID = "A" + String.format("%04d", appointments.size() + 1);
    Appointment newAppointment =
        new Appointment(
            appointmentID,
            patient.getHospitalID(),
            doctorID,
            appointmentDate,
            sessionNumber,
            AppointmentStatus.PENDING.name());
    appointments.add(newAppointment);
    CsvDB.saveAppointments(
        appointments); // Update com.ntu.hms.Appointment.csv with the new appointment

    System.out.printf(
        "Appointment booked for Dr. %s on %s, Session %d.\n",
        selectedDoctor.getName(),
        appointmentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        sessionNumber);
  }

  /**
   * Reschedules an existing appointment for a patient. The patient can choose from a list of their
   * pending or confirmed appointments and select a new date and session for the appointment.
   *
   * @param patient The patient for whom the appointment is being rescheduled.
   */
  @Override
  public void rescheduleAppointment(Patient patient) {
    List<Doctor> doctors = CsvDB.readDoctors();
    List<Appointment> appointments = CsvDB.readAppointments();
    List<Schedule> schedules = CsvDB.readSchedules();

    // Filter to show only pending or confirmed appointments
    List<Appointment> reschedulableAppointments =
        getAppointmentsForPatient(patient.getHospitalID())
            .stream()
            .filter(
                appointment ->
                    appointment.getStatus().equalsIgnoreCase(PENDING.name())
                        || appointment.getStatus().equalsIgnoreCase(CONFIRMED.name()))
            .collect(Collectors.toList());

    // Check if there are any appointments to reschedule
    if (reschedulableAppointments.isEmpty()) {
      System.out.println("\nYou have no pending or confirmed appointments to reschedule.");
      return;
    }

    // Display reschedulable appointments
    System.out.println("Which appointment would you like to reschedule?");
    int apptCounter = 0;
    for (Appointment appt : reschedulableAppointments) {
      Doctor doctor = getDoctorById(appt.getDoctorID());
      if (doctor != null) {
        System.out.printf(
            "%d. Appointment with Dr. %s on %s at %s - Status: %s\n",
            ++apptCounter,
            doctor.getName(),
            appt.getDate().format(dateFormatter),
            sessionTimings[appt.getSession() - 1],
            appt.getStatus());
      }
    }

    System.out.println("Which appointment would you like to reschedule? (Press Enter to return): ");
    String appointmentChoiceInput = scanner.nextLine().trim();

    // Allow the patient to exit the rescheduling process
    if (appointmentChoiceInput.isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    int appointmentChoice;
    try {
      appointmentChoice = Integer.parseInt(appointmentChoiceInput);
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return;
    }

    // Validate the choice
    if (appointmentChoice < 1 || appointmentChoice > reschedulableAppointments.size()) {
      System.out.println("Invalid choice.");
      return;
    }

    // Get the chosen appointment
    Appointment chosenAppointment = reschedulableAppointments.get(appointmentChoice - 1);
    Doctor selectedDoctor = getDoctorById(chosenAppointment.getDoctorID());
    if (selectedDoctor == null) {
      System.out.println("Doctor not found.");
      return;
    }

    // Prompt for new date
    System.out.print("Enter the new appointment date (dd/MM/yyyy): ");
    String newDateInput = scanner.nextLine().trim();
    LocalDate newAppointmentDate;
    try {
      newAppointmentDate = LocalDate.parse(newDateInput, dateFormatter);
    } catch (Exception e) {
      System.out.println("Invalid date format. Please use dd/MM/yyyy.");
      return;
    }

    // Find or create schedule for the new date
    Optional<Schedule> optionalSchedule =
        schedules
            .stream()
            .filter(schedule -> schedule.getDoctorID().equals(selectedDoctor.getHospitalID()))
            .filter(schedule -> schedule.getDate().equals(newAppointmentDate))
            .findFirst();
    Schedule newScheduleForDate =
        optionalSchedule.orElseGet(
            () -> {
              Schedule schedule =
                  createDefaultSchedule(selectedDoctor.getHospitalID(), newAppointmentDate);
              schedules.add(schedule);
              return schedule;
            });

    // Display available sessions for new date
    System.out.println(
        "\nAvailable sessions for Dr. "
            + selectedDoctor.getName()
            + " on "
            + newAppointmentDate.format(dateFormatter)
            + ":");
    String[] newSession = newScheduleForDate.getSession();
    boolean hasAvailableSession = false;
    for (int i = 0; i < newSession.length; i++) {
      if (newSession[i].equals("Available")) {
        System.out.printf("Session %d: Available\n", i + 1);
        hasAvailableSession = true;
      }
    }

    if (!hasAvailableSession) {
      System.out.println("No available sessions on this date.");
      return;
    }

    // Select a new session
    System.out.print("Select a session number for the new date: ");
    int newSessionNumber = scanner.nextInt();
    // scanner.nextLine(); // consume newline character

    if (newSessionNumber < 1
        || newSessionNumber > newSession.length
        || !newSession[newSessionNumber - 1].equals("Available")) {
      System.out.println("Invalid session selection or session not available.");
      return;
    }

    // Update old schedule slot to "Available"
    String doctorID = chosenAppointment.getDoctorID();
    LocalDate oldAppointmentDate = chosenAppointment.getDate();
    int oldSessionNumber = chosenAppointment.getSession();
    for (Schedule schedule : schedules) {
      if (schedule.getDoctorID().equals(doctorID)
          && schedule.getDate().equals(oldAppointmentDate)) {
        String[] oldSessionSlots = schedule.getSession();
        oldSessionSlots[oldSessionNumber - 1] = "Available";
        schedule.setSession(oldSessionSlots);
        break;
      }
    }

    // Update appointment details and mark the new session as "Pending"
    chosenAppointment.setDate(newAppointmentDate);
    chosenAppointment.setSession(newSessionNumber);
    if (chosenAppointment.getStatus().equalsIgnoreCase(CONFIRMED.name())) {
      chosenAppointment.setStatus(PENDING.name()); // Change confirmed to pending if rescheduled
    }

    newSession[newSessionNumber - 1] =
        patient.getHospitalID() + "-" + ScheduleStatus.PENDING.name();
    newScheduleForDate.setSession(newSession);

    // Update the original appointments list with the chosen appointment's new details
    for (int i = 0; i < appointments.size(); i++) {
      if (appointments.get(i).getAppointmentID().equals(chosenAppointment.getAppointmentID())) {
        appointments.set(i, chosenAppointment);
        break;
      }
    }

    // Save updated appointments and schedules to CSV files
    CsvDB.saveAppointments(appointments); // Save the full list of appointments
    CsvDB.saveSchedules(schedules);

    System.out.printf(
        "Your appointment with Dr. %s has been rescheduled to %s, Session %d.\n",
        selectedDoctor.getName(), newAppointmentDate.format(dateFormatter), newSessionNumber);
  }

  /**
   * Cancels a patient's appointment by allowing the patient to select from their pending or
   * confirmed appointments.
   *
   * @param patient The patient for whom the appointment is being cancelled.
   */
  @Override
  public void cancelAppointment(Patient patient) {
    List<Appointment> appointments = CsvDB.readAppointments();
    List<Schedule> schedules = CsvDB.readSchedules();
    List<Doctor> doctors = CsvDB.readDoctors();

    // Filter to show only pending or confirmed appointments
    List<Appointment> cancellableAppointments =
        getAppointmentsForPatient(patient.getHospitalID())
            .stream()
            .filter(
                appointment ->
                    appointment.getStatus().equalsIgnoreCase(PENDING.name())
                        || appointment.getStatus().equalsIgnoreCase(CONFIRMED.name()))
            .collect(Collectors.toList());

    // Check if there are any appointments to cancel
    if (cancellableAppointments.isEmpty()) {
      System.out.println("You have no pending or confirmed appointments to cancel.");
      return;
    }

    // Display cancellable appointments
    System.out.println("Which appointment would you like to cancel?");
    int apptCounter = 0;
    for (Appointment appt : cancellableAppointments) {
      Doctor doctor = getDoctorById(appt.getDoctorID());
      if (doctor != null) {
        System.out.printf(
            "%d. Appointment with Dr. %s on %s at %s - Status: %s\n",
            ++apptCounter,
            doctor.getName(),
            appt.getDate().format(dateFormatter),
            sessionTimings[appt.getSession() - 1],
            appt.getStatus());
      }
    }

    // Get user choice
    System.out.println("Which appointment would you like to cancel? (Press Enter to return): ");
    String appointmentChoiceInput = scanner.nextLine().trim();

    // Allow the patient to exit the cancellation process
    if (appointmentChoiceInput.isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    int appointmentChoice;
    try {
      appointmentChoice = Integer.parseInt(appointmentChoiceInput);
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return;
    }

    // Validate the choice
    if (appointmentChoice < 1 || appointmentChoice > cancellableAppointments.size()) {
      System.out.println("Invalid choice.");
      return;
    }

    // Validate the choice
    if (appointmentChoice >= 1 && appointmentChoice <= cancellableAppointments.size()) {
      // Get the chosen appointment and update status
      Appointment chosenAppointment = cancellableAppointments.get(appointmentChoice - 1);
      chosenAppointment.setStatus(CANCELLED.name());

      // Update the schedule slot to "Available"
      String doctorID = chosenAppointment.getDoctorID();
      LocalDate apptDate = chosenAppointment.getDate();
      int sessionNumber = chosenAppointment.getSession();
      for (Schedule schedule : schedules) {
        if (schedule.getDoctorID().equals(doctorID) && schedule.getDate().equals(apptDate)) {
          String[] sessionSlots = schedule.getSession();
          sessionSlots[sessionNumber - 1] = "Available";
          schedule.setSession(sessionSlots);
          break;
        }
      }

      chosenAppointment.setStatus(CANCELLED.name());

      // Update the original appointments list with the chosen appointment's new status
      for (int i = 0; i < appointments.size(); i++) {
        if (appointments.get(i).getAppointmentID().equals(chosenAppointment.getAppointmentID())) {
          appointments.set(i, chosenAppointment);
          break;
        }
      }

      // Save the changes
      CsvDB.saveAppointments(appointments);
      CsvDB.saveSchedules(schedules);
      System.out.printf(
          "Your appointment with Dr. %s on %s has been cancelled.\n",
          getDoctorById(doctorID).getName(), chosenAppointment.getDate().format(dateFormatter));
    } else {
      System.out.println("Invalid choice.");
    }
  }

  /**
   * Displays the outcomes of the completed appointments for the given patient.
   *
   * @param patient The patient whose appointment outcomes are to be shown.
   */
  @Override
  public void showAppointmentOutcome(Patient patient) {
    // Filter to show only the patient's completed appointments
    List<Appointment> completedAppointments =
        CsvDB.readAppointments()
            .stream()
            .filter(appointment -> appointment.getPatientID().equals(patient.getHospitalID()))
            .filter(appointment -> appointment.getStatus().equalsIgnoreCase(COMPLETED.name()))
            .collect(Collectors.toList());

    // Check if there are any completed appointments for this patient
    if (completedAppointments.isEmpty()) {
      System.out.println("You have no completed appointments with recorded outcomes.");
      return;
    }

    // Display completed appointments and allow the patient to choose one
    System.out.println("Select a completed appointment to view its outcome record:");
    int apptCounter = 0;
    for (Appointment appt : completedAppointments) {
      int session = appt.getSession();
      String sessionTime =
          (session > 0 && session <= sessionTimings.length)
              ? sessionTimings[session - 1]
              : "Unknown time";

      System.out.printf(
          "%d. Appointment ID: %s, Date: %s, Time: %s\n",
          ++apptCounter,
          appt.getAppointmentID(),
          appt.getDate().format(dateFormatter),
          sessionTime);
    }

    // Get user selection
    System.out.print(
        "Enter the number of the appointment you want to view (Press Enter to return): ");
    String choiceInput = scanner.nextLine().trim();

    // Allow the patient to exit the outcome viewing process
    if (choiceInput.isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    int choice;
    try {
      choice = Integer.parseInt(choiceInput);
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a number.");
      return;
    }

    // Validate the choice
    if (choice < 1 || choice > completedAppointments.size()) {
      System.out.println("Invalid selection.");
      return;
    }

    // Find the outcome record for the selected completed appointment
    Appointment selectedAppointment = completedAppointments.get(choice - 1);
    boolean foundOutcome = false;

    for (AppointmentOutcomeRecord record : CsvDB.readAppointmentOutcomeRecords()) {
      if (record.getAppointmentID().equals(selectedAppointment.getAppointmentID())) {
        // Display the outcome record details
        System.out.println("\n=== Appointment Outcome Record ===");
        System.out.println("Appointment ID: " + record.getAppointmentID());
        System.out.println("Type of Service: " + record.getTypeOfService());
        System.out.println("Consultation Notes: " + record.getConsultationNotes());
        System.out.println("Prescriptions: " + record.getPrescriptionsAsString());
        System.out.println("Prescription Status: " + record.getPrescriptionStatus());
        foundOutcome = true;
        break;
      }
    }

    if (!foundOutcome) {
      System.out.println("No outcome record found for the selected appointment.");
    }
  }

  /**
   * Displays the outcomes of all completed appointments.
   *
   * <p>This method reads appointment outcome records from a CSV database, and prints to the console
   * each appointment's ID and prescription status. For each appointment, it lists the prescribed
   * medications if available, including the medication ID, name, and quantity. If no prescriptions
   * are available, it indicates that there are none.
   */
  @Override
  public void showAppointmentOutcome() {
    List<AppointmentOutcomeRecord> appointmentOutcomeRecords =
        CsvDB.readAppointmentOutcomeRecords();
    System.out.println("\n=== Appointment Outcome Records ===");
    for (AppointmentOutcomeRecord record : appointmentOutcomeRecords) {
      System.out.printf(
          "\nAppointment ID:%s\tPrescription Status:%s\n",
          record.getAppointmentID(), record.getPrescriptionStatus());

      // Print prescriptions
      System.out.println("Prescriptions:");
      List<MedicationItem> prescriptions = record.getPrescriptions();
      if (prescriptions.isEmpty()) {
        System.out.println("None");
      } else {
        for (MedicationItem item : prescriptions) {
          System.out.printf(
              "Medication ID:%s\tMedication Name:%s\tQuantity:%d\n",
              item.getMedicationID(), item.getMedicationName(), item.getQuantity());
        }
      }
    }
  }

  /**
   * Displays the user's chosen type of appointments.
   *
   * <p>This method prompts the user for the type of appointments they wish to view from a set of
   * predefined categories: Completed, Cancelled, Pending, No Show, and View All. Based on the
   * user's input, it filters appointments from the database and displays them accordingly. If the
   * input is invalid or empty, it returns to the main menu.
   */
  public void showAppointment() {
    // Prompt the user for the type of appointment they want to view
    System.out.println("\nWhat type of appointments would you like to view?");
    System.out.println("1. Completed");
    System.out.println("2. Cancelled");
    System.out.println("3. Pending");
    System.out.println("4. No Show");
    System.out.println("5. View All");
    System.out.print("Enter your choice (or press enter to return to main menu): ");

    String input = scanner.nextLine(); // Capture the input as a string
    // If user presses "Enter" only, return to main menu
    if (input.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    // If input is not empty, try to parse it as an integer
    int choice;
    try {
      choice = Integer.parseInt(input); // Convert input to integer
    } catch (NumberFormatException e) {
      System.out.println("Invalid choice. Please enter a valid number.");
      return;
    }

    // Filter and display appointments based on user's choice
    List<Appointment> appointments = CsvDB.readAppointments();
    switch (choice) {
      case 1:
        System.out.println("\n=== Completed Appointments ===");
        filterAndDisplayAppointments(appointments, AppointmentStatus.COMPLETED.name());
        break;
      case 2:
        System.out.println("\n=== Cancelled Appointments ===");
        filterAndDisplayAppointments(appointments, AppointmentStatus.CANCELLED.name());
        break;
      case 3:
        System.out.println("\n=== Pending Appointments ===");
        filterAndDisplayAppointments(appointments, AppointmentStatus.PENDING.name());
        break;
      case 4:
        System.out.println("\n=== No Show Appointments ===");
        filterAndDisplayAppointments(appointments, AppointmentStatus.NO_SHOW.name());
        break;
      case 5:
        System.out.println("\n=== All Appointments ===");
        displayAllAppointments(appointments);
        break;
      default:
        System.out.println("Invalid choice. Returning to the main menu...");
    }
  }

  /**
   * Displays the scheduled appointments for a specific patient.
   *
   * <p>This method filters the confirmed and pending appointments for the specified patient and
   * prints the details of each appointment. If no appointments are found, a message is displayed
   * indicating that no scheduled appointments exist.
   *
   * @param patient The patient whose scheduled appointments are to be shown.
   */
  @Override
  public void showScheduledAppointments(Patient patient) {
    // Filter confirmed appointments for this patient
    List<Appointment> patientAppointments =
        CsvDB.readAppointments()
            .stream()
            .filter(appointment -> appointment.getPatientID().equals(patient.getHospitalID()))
            .filter(appointment -> !appointment.getStatus().equalsIgnoreCase(CANCELLED.name()))
            .filter(appointment -> !appointment.getStatus().equalsIgnoreCase(COMPLETED.name()))
            .collect(Collectors.toList());

    if (patientAppointments.isEmpty()) {
      System.out.println("\nYou have no scheduled appointments.");
    } else {
      System.out.println("\nYour scheduled appointments:");

      int apptCounter = 0;
      // Display each appointment's details
      for (Appointment appt : patientAppointments) {
        Doctor doctor = getDoctorById(appt.getDoctorID());

        if (doctor != null) {
          System.out.printf(
              "%d. Appointment with Dr. %s on %s at %s %s\n",
              ++apptCounter,
              doctor.getName(),
              appt.getDate().format(dateFormatter),
              sessionTimings[appt.getSession() - 1],
              appt.getStatus());
        }
      }
    }
  }

  /**
   * Displays the upcoming confirmed appointments for a given doctor.
   *
   * <p>This method retrieves the doctor's schedule, filters out past dates, and prints the upcoming
   * confirmed appointments, organized by dates. If no upcoming confirmed appointments are found, it
   * displays a message indicating the absence of such appointments.
   *
   * @param doctor The doctor for whom the upcoming appointments are to be shown.
   */
  @Override
  public void showUpcomingAppointments(Doctor doctor) {
    List<Patient> patients = CsvDB.readPatients();
    LocalDate today = LocalDate.now();
    List<Schedule> doctorSchedule = getScheduleByDoctorID(doctor.getHospitalID());

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
          Patient scheduledPatient =
              (patientId != null) ? getPatientById(patientId, patients) : null;
          if (scheduledPatient != null) {
            // Display a new date header if the date has changed
            if (lastDisplayedDate == null || !lastDisplayedDate.equals(schedule.getDate())) {
              lastDisplayedDate = schedule.getDate();
              System.out.println(
                  "\nAppointments for "
                      + lastDisplayedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                      + ":");
              System.out.println("---------------------------------------------------------------");
              System.out.printf("%-5s %-15s %s\n", "No.", "Time", "Patient Name");
            }

            // Format the appointment details into a consistent string
            String formattedAppointment =
                String.format(
                    "%-5d %-15s %s",
                    appointmentDetails.size() + 1, sessionTimings[i], scheduledPatient.getName());
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
    scanner.nextLine(); // Wait for the user to press Enter
  }

  /**
   * Updates the outcome for a doctor's appointment, allowing the doctor to record whether the
   * appointment was completed or a no-show, add consultation notes, prescribe medications, and save
   * these records to the database.
   *
   * @param doctor The doctor who is updating the appointment outcome. Contains the hospital ID
   *     needed to filter the appointments.
   */
  @Override
  public void updateAppointmentOutcome(Doctor doctor) {
    List<Appointment> appointments = CsvDB.readAppointments();
    List<Medication> medications = CsvDB.readMedications();
    List<AppointmentOutcomeRecord> appointmentOutcomeRecords =
        CsvDB.readAppointmentOutcomeRecords();
    List<Diagnosis> diagnoses = CsvDB.readDiagnoses();
    List<Treatment> treatments = CsvDB.readTreatments();

    boolean exit = false;

    while (!exit) {
      // Step 1: Filter and display confirmed appointments
      int appointmentIndex = 1;

      System.out.println("\n=== Confirmed Appointments ===");

      ArrayList<Appointment> confirmedAppointments =
          Appointment.getConfirmedAppointmentsByDoctorID(doctor.getHospitalID(), appointments);

      for (Appointment appointment : confirmedAppointments) {
        System.out.printf(
            "%d. Appointment ID: %s, Date: %s, Session: %s, Patient ID: %s\n",
            appointmentIndex,
            appointment.getAppointmentID(),
            appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            sessionTimings[appointment.getSession() - 1],
            appointment.getPatientID());
        appointmentIndex++;
      }

      if (confirmedAppointments.isEmpty()) {
        System.out.println("No confirmed appointments found.");
        break;
      }

      // Step 2: Get the user's selection for which appointment they want to modify
      System.out.println(
          "\nSelect the appointment to record the outcome (or press Enter to return):");
      String input = scanner.nextLine();

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

          String sessionTimeString =
              sessionTimings[selectedAppointment.getSession() - 1].substring(0, 5);
          DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
          LocalTime sessionTime = LocalTime.parse(sessionTimeString, timeFormatter);

          if (appointmentDate.isAfter(currentDate)
              || (appointmentDate.isEqual(currentDate) && sessionTime.isAfter(currentTime))) {
            System.out.println(
                "\nThe appointment time has not passed yet. You cannot record the outcome at this time.");
            continue;
          }

          // Step 3: Prompt the user to record the outcome using Y/N input
          System.out.print(
              "\nEnter the outcome for this appointment ('Y' for Completed or 'N' for No-Show): ");
          String outcome = scanner.nextLine().trim().toUpperCase();

          if (outcome.equals("Y")) {
            String consultationNotes = "";
            String typeOfService = "";
            String diagnosisNotes = "";
            String treatmentNotes = "";

            System.out.println("\nEnter type of service (enter na if none): ");
            typeOfService = scanner.nextLine();

            System.out.println("\nEnter consultation notes (enter na if none): ");
            consultationNotes = scanner.nextLine();

            System.out.println("\nEnter diagnosis (enter na if none): ");
            diagnosisNotes = scanner.nextLine();

            System.out.println("\nEnter treatment (enter na if none): ");
            treatmentNotes = scanner.nextLine();

            // Step 4: Display medication inventory and allow the user to prescribe
            // medicines
            ArrayList<MedicationItem> prescribedMedicines = new ArrayList<>();
            boolean addingMedicines = true;

            while (addingMedicines) {
              System.out.println("\nAvailable Medicines:");
              int medIndex = 1;
              for (Medication med : medications) {
                System.out.printf(
                    "%d. %s (Available: %d units)\n",
                    medIndex, med.getMedicationName(), med.getTotalQuantity());
                medIndex++;
              }

              System.out.println(
                  "\nSelect a medicine by number to prescribe (or press Enter to finish): ");
              String medInput = scanner.nextLine();

              if (medInput.trim().isEmpty()) {
                addingMedicines = false;
                continue;
              }

              try {
                int medChoice = Integer.parseInt(medInput);

                if (medChoice > 0 && medChoice <= medications.size()) {
                  Medication selectedMed = medications.get(medChoice - 1);
                  System.out.printf("\nEnter quantity for %s: ", selectedMed.getMedicationName());
                  String quantityInput = scanner.nextLine();
                  int quantity = Integer.parseInt(quantityInput);

                  if (quantity > 0 && quantity <= selectedMed.getTotalQuantity()) {
                    System.out.println("\nCurrent Prescribing List:");
                    MedicationItem prescribedMed =
                        new MedicationItem(
                            selectedMed.getMedicationID(),
                            selectedMed.getMedicationName(),
                            quantity);
                    prescribedMedicines.add(prescribedMed);
                    for (MedicationItem med : prescribedMedicines) {
                      System.out.printf(
                          "- %s: %d units\n", med.getMedicationName(), med.getQuantity());
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
            AppointmentOutcomeRecord outcomeRecord =
                new AppointmentOutcomeRecord(
                    selectedAppointment.getAppointmentID(),
                    typeOfService,
                    consultationNotes,
                    prescribedMedicines,
                    AppointmentOutcomeStatus.PENDING.name());

            Treatment treatment =
                new Treatment(
                    selectedAppointment.getAppointmentID(),
                    selectedAppointment.getPatientID(),
                    treatmentNotes);
            Diagnosis diagnosis =
                new Diagnosis(
                    selectedAppointment.getAppointmentID(),
                    selectedAppointment.getPatientID(),
                    diagnosisNotes);

            treatments.add(treatment);
            diagnoses.add(diagnosis);
            appointmentOutcomeRecords.add(outcomeRecord);

            CsvDB.saveAppointmentOutcomeRecords(appointmentOutcomeRecords);
            CsvDB.saveDiagnosis(diagnoses);
            CsvDB.saveTreatment(treatments);

            // saveAppointmentOutcomeRecords(apptOutcomeRecords);
            selectedAppointment.setStatus(AppointmentStatus.COMPLETED.name());
            System.out.println("\nAppointment outcome recorded successfully as 'Completed'.");

          } else if (outcome.equals("N")) {
            selectedAppointment.setStatus(AppointmentStatus.NO_SHOW.name());
            System.out.println("\nAppointment outcome recorded successfully as 'No-Show'.");
          } else {
            System.out.println(
                "\nInvalid input. Please enter either 'Y' for Completed or 'N' for No-Show.");
            continue; // Continue to allow user to retry entering a valid input
          }

          // Save the updated appointment list to CSV
          CsvDB.saveAppointments(appointments);
        } else {
          System.out.println("\nInvalid choice. Please select a valid appointment number.");
        }
      } catch (NumberFormatException e) {
        System.out.println("\nInvalid input. Please enter a valid number.");
      }
    }
  }

  /**
   * Retrieves the list of appointments for a specified patient.
   *
   * @param patientID The ID of the patient whose appointments are to be fetched.
   * @return A list of appointments that belong to the specified patient.
   */
  public List<Appointment> getAppointmentsForPatient(String patientID) {
    return CsvDB.readAppointments()
        .stream()
        .filter(appointment -> appointment.getPatientID().equals(patientID))
        .collect(Collectors.toList());
  }

  /**
   * Updates the status of an appointment for a given schedule and session index.
   *
   * @param appts The list of appointments to be updated.
   * @param chosenSchedule The schedule for which the appointment is to be updated.
   * @param sessionIndex The session index within the chosen schedule.
   * @param status The new status to be set for the appointment.
   */
  public static void updateAppointment(
      List<Appointment> appts, Schedule chosenSchedule, int sessionIndex, String status) {
    Appointment selectedAcAppointment =
        getAppointmentByScheduleAndSession(chosenSchedule, sessionIndex, appts);
    if (selectedAcAppointment != null) {
      selectedAcAppointment.setStatus(status);
    } else {
      System.err.println(
          "\nAppointment not found for the chosen schedule and session index. Please check the appointments list.");
    }

    CsvDB.saveAppointments(appts);
  }

  /**
   * Filters and displays appointments based on their status.
   *
   * <p>This method filters appointments by the given status and prints the appointment details. If
   * the status is "Completed", it also retrieves and displays the relevant appointment outcome
   * records. If no appointments are found with the specified status, a corresponding message is
   * displayed.
   *
   * @param appointments the list of all appointments to be filtered and displayed
   * @param status the status by which to filter the appointments (e.g., "Completed", "Pending")
   */
  // Modified Method to filter and display appointments based on their status
  private void filterAndDisplayAppointments(List<Appointment> appointments, String status) {
    boolean found = false;

    // Read Appointment Outcome Records (only if status is Completed)
    List<AppointmentOutcomeRecord> outcomeRecords = new ArrayList<>();
    if (status.equalsIgnoreCase(AppointmentStatus.COMPLETED.name())) {
      try {
        outcomeRecords = CsvDB.readAppointmentOutcomeRecords();
      } catch (Exception e) {
        System.out.println("Error reading appointment outcome records: " + e.getMessage());
        return;
      }
    }

    for (Appointment appt : appointments) {
      if (appt.getStatus().equalsIgnoreCase(status)) {
        // Display the basic appointment details
        System.out.printf(
            "Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Session: %d | Status: %s\n",
            appt.getAppointmentID(),
            appt.getPatientID(),
            appt.getDoctorID(),
            appt.getDate(),
            appt.getSession(),
            appt.getStatus());
        found = true;

        // If the appointment is "Completed", display additional outcome details
        if (status.equalsIgnoreCase(AppointmentStatus.COMPLETED.name())) {
          // Find the corresponding appointment outcome record
          AppointmentOutcomeRecord outcomeRecord =
              getOutcomeByAppointmentID(outcomeRecords, appt.getAppointmentID());
          if (outcomeRecord != null) {
            // System.out.printf(" com.ntu.hms.Diagnosis: %s\n", outcomeRecord.getDiagnosis());
            // System.out.printf(" com.ntu.hms.Treatment: %s\n", outcomeRecord.getTreatment());
            System.out.printf(
                "Appointment ID: %s | Type of Service: %s | Consultation Notes: %s | Prescriptions: %s | Prescription Status: %s\n",
                outcomeRecord.getAppointmentID(),
                outcomeRecord.getTypeOfService(),
                outcomeRecord.getConsultationNotes(),
                outcomeRecord.getPrescriptions(),
                outcomeRecord.getPrescriptionStatus());
          } else {
            System.out.println("  No outcome record available for this appointment.");
          }
        }

        System.out.println("-------------------------------------------------------");
      }
    }

    if (!found) {
      System.out.println("No appointments found with status: " + status);
    }
  }

  /**
   * Displays all appointments from the provided list. If the list is empty, a message indicating no
   * appointments will be shown. Otherwise, it prints the details of each appointment including
   * appointment ID, patient ID, doctor ID, date, session, and status.
   *
   * @param appointments the list of appointments to be displayed
   */
  // Method to display all appointments
  private void displayAllAppointments(List<Appointment> appointments) {
    if (appointments.isEmpty()) {
      System.out.println("No appointments available.");
      return;
    }

    for (Appointment appt : appointments) {
      System.out.printf(
          "Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Session: %d | Status: %s\n",
          appt.getAppointmentID(),
          appt.getPatientID(),
          appt.getDoctorID(),
          appt.getDate(),
          appt.getSession(),
          appt.getStatus());
    }
  }

  public AppointmentOutcomeRecord getOutcomeByAppointmentID(
      List<AppointmentOutcomeRecord> outcomeRecords, String appointmentID) {
    for (AppointmentOutcomeRecord outcome : outcomeRecords) {
      if (outcome.getAppointmentID().equals(appointmentID)) {
        return outcome;
      }
    }
    return null;
  }

  /**
   * Creates a new instance of AppointmentManagerBuilder, which is used to construct
   * AppointmentManager instances with specified configurations.
   *
   * @return a new AppointmentManagerBuilder instance for building AppointmentManager objects.
   */
  public static AppointmentManagerBuilder appointmentManagerBuilder() {
    return new AppointmentManagerBuilder();
  }

  /** Builder class to construct instances of `AppointmentManager`. */
  // Static inner Builder class
  public static class AppointmentManagerBuilder {
    private DateTimeFormatter dateTimeFormatter;
    private ScannerWrapper scanner;

    /**
     * Sets the DateTimeFormatter to be used by the AppointmentManager.
     *
     * @param dateTimeFormatter the DateTimeFormatter to be set
     * @return the current instance of AppointmentManagerBuilder for method chaining
     */
    // Setter method for DateTimeFormatter
    public AppointmentManagerBuilder setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
      this.dateTimeFormatter = dateTimeFormatter;
      return this; // Return the builder for chaining
    }

    /**
     * Sets the ScannerWrapper instance to be used by the AppointmentManagerBuilder.
     *
     * @param scanner the ScannerWrapper instance used for reading input
     * @return the current AppointmentManagerBuilder instance for chaining
     */
    // Setter method for ScannerWrapper
    public AppointmentManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    /**
     * Constructs and returns a new instance of AppointmentManager.
     *
     * @return a new instance of AppointmentManager configured with the current state of the builder
     * @throws IllegalArgumentException if dateTimeFormatter or scanner is null
     */
    // Method to build an AppointmentManager instance
    public AppointmentManager build() {
      // Add validation to ensure non-null fields if necessary
      if (dateTimeFormatter == null) {
        throw new IllegalArgumentException("DateTimeFormatter must not be null.");
      }
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must not be null.");
      }
      return new AppointmentManager(dateTimeFormatter, scanner);
    }
  }
}
