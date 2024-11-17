package com.ntu.hms.manager.schedule;

import static com.ntu.hms.App.sessionTimings;
import static com.ntu.hms.manager.appointment.AppointmentManager.updateAppointment;
import static com.ntu.hms.util.UtilProvider.*;

import com.ntu.hms.CsvDB;
import com.ntu.hms.enums.AppointmentStatus;
import com.ntu.hms.enums.ScheduleStatus;
import com.ntu.hms.model.Appointment;
import com.ntu.hms.model.Schedule;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.util.ScannerWrapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * ScheduleManager class for managing and displaying schedules in a healthcare system. This class
 * manages the scheduling, availability, and updates for doctors. It provides functionalities to
 * show weekly schedules, update schedules, and set doctor availability. The class uses a
 * DateTimeFormatter for date formatting and a ScannerWrapper for reading inputs.
 */
public class ScheduleManager implements ScheduleManagerInterface {
  private final DateTimeFormatter dateFormatter;
  private final ScannerWrapper scanner;

  /**
   * Constructs a ScheduleManager with the specified DateTimeFormatter and ScannerWrapper.
   *
   * @param dateTimeFormatter the DateTimeFormatter to be used for formatting dates
   * @param scanner the ScannerWrapper for reading input
   */
  private ScheduleManager(DateTimeFormatter dateTimeFormatter, ScannerWrapper scanner) {
    this.dateFormatter = dateTimeFormatter;
    this.scanner = scanner;
  }

  /**
   * Displays the weekly schedule for a selected doctor.
   *
   * <p>This method outputs the list of doctors fetched from the database and prompts the user to
   * select a doctor and specify the number of weeks in advance they wish to view the schedule for.
   * Based on the selection, it displays the available sessions for the specified number of weeks,
   * skipping weekends.
   *
   * <p>Steps: 1. Reads the list of doctors and schedules from the database. 2. Displays the list of
   * available doctors to the user. 3. Prompts the user to select a doctor and the number of weeks
   * in advance they want to view. 4. Displays the available sessions for the selected doctor within
   * the specified date range.
   *
   * <p>Edge Cases: - If the input selection is empty, returns to the main menu. - If the input
   * selection is invalid, outputs an appropriate message. - If the weekday does not have a specific
   * schedule, all sessions are considered available.
   *
   * <p>Note: This method assumes all sessions are available if a specific date is not found in the
   * doctor's schedule.
   */
  @Override
  public void showWeeklySchedule() {
    List<Doctor> doctors = CsvDB.readDoctors();
    List<Schedule> schedules = CsvDB.readSchedules();

    // Display the available doctors to the user
    System.out.println("\nAvailable Doctors: ");
    IntStream.range(0, doctors.size())
        .forEach(index -> System.out.println((index + 1) + ". " + doctors.get(index).getName()));

    // Prompt user to select a doctor by index
    System.out.println("Select a doctor to view available slots (Press Enter to return): ");
    String doctorInput = scanner.nextLine().trim();
    System.out.println();

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
          System.out.printf(
              "\nAvailable Sessions for Dr. %s on %s:\n",
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
        System.out.printf(
            "\nAvailable Sessions for Dr. %s on %s:\n",
            selectedDoctor.getName(), currentDate.format(dateFormatter));
        for (int i = 0; i < sessionTimings.length; i++) {
          System.out.printf("Session %d (%s)\n", i + 1, sessionTimings[i]);
        }
      }

      // Move to the next day, skipping weekends
      currentDate = currentDate.plusDays(1);
      if (currentDate.getDayOfWeek().getValue() == 6
          || currentDate.getDayOfWeek().getValue() == 7) {
        currentDate = currentDate.plusDays(2); // Skip Saturday and Sunday
      }
    }
  }

  /**
   * Displays the weekly schedule for a specified doctor.
   *
   * <p>This method prompts the user to input the number of weeks they want to view in advance, and
   * shows the doctor's schedule for the specified period, excluding weekends. It displays
   * available, pending, and booked sessions for the doctor.
   *
   * @param doctor The doctor whose schedule is to be displayed.
   */
  @Override
  public void showWeeklySchedule(Doctor doctor) {
    System.out.print(
        "\nEnter the number of weeks you want to view in advance (Press Enter to return): ");
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
    LocalDate endDate =
        today.plusWeeks(numberOfWeeks); // Calculate the end date based on the number of weeks
    List<Schedule> doctorSchedule = getScheduleByDoctorID(doctor.getHospitalID());
    List<Schedule> weeklySchedule = new ArrayList<>();

    // Iterate from today until the end date, excluding weekends
    LocalDate currentDate = today;
    while (!currentDate.isAfter(endDate)) {
      // Skip weekends (Saturday and Sunday)
      if (currentDate.getDayOfWeek().getValue() == 6
          || currentDate.getDayOfWeek().getValue() == 7) {
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
      System.out.printf(
          "\nYour Personal Schedule on %s:\n",
          schedule.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

      for (int i = 0; i < schedule.getSession().length; i++) {
        String sessionInfo = schedule.getSession()[i];
        String sessionTime = sessionTimings[i];

        if (sessionInfo.equals("Available") || sessionInfo.equals("Unavailable")) {
          System.out.printf("Session %d (%s): %s\n", i + 1, sessionTime, sessionInfo);
        } else {
          // Need to retrieve the patient name
          String patientId = schedule.getPatientIdFromSession(i);
          Patient scheduledPatient =
              (patientId != null) ? getPatientById(patientId, CsvDB.readPatients()) : null;
          if (scheduledPatient != null) {
            String status = schedule.getSessionStatus(i);
            if (status.equals(ScheduleStatus.PENDING.name())) {
              System.out.printf(
                  "Session %d (%s): Pending with %s\n",
                  i + 1, sessionTime, scheduledPatient.getName());
            } else {
              System.out.printf(
                  "Session %d (%s): Booked with %s\n",
                  i + 1, sessionTime, scheduledPatient.getName());
            }
          } else {
            System.out.printf(
                "Session %d (%s): Booked (Unknown Patient, Patient ID: %s)\n",
                i + 1, sessionTime, patientId);
          }
        }
      }
      System.out.println();
    }

    System.out.println("\nPress Enter to continue");
    scanner.nextLine(); // Wait for the user to press Enter
  }

  /**
   * Updates the schedule of a specified doctor by processing pending appointments.
   *
   * <p>This method retrieves the current pending appointments for the given doctor and allows to
   * accept or decline them. It ensures that appointments from past dates are skipped and only valid
   * upcoming appointments are presented for action.
   *
   * @param doctor The doctor whose schedule is to be updated.
   */
  @Override
  public void updateSchedule(Doctor doctor) {
    boolean exit = false;

    while (!exit) {
      List<Schedule> doctorSchedule = getScheduleByDoctorID(doctor.getHospitalID());
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
            Patient scheduledPatient =
                getPatientById(patientId, CsvDB.readPatients()); // Assuming static method

            if (scheduledPatient != null) {
              // Add pending appointment details to the list
              String appointmentDetail =
                  String.format(
                      "Date: %s, Time: %s, Patient: %s",
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

      sortedIndices.sort(
          (index1, index2) -> {
            Schedule schedule1 = pendingSchedules.get(index1);
            Schedule schedule2 = pendingSchedules.get(index2);
            int dateComparison = schedule1.getDate().compareTo(schedule2.getDate());
            if (dateComparison != 0) {
              return dateComparison;
            }
            // If dates are equal, compare based on session time
            return Integer.compare(
                pendingSessionIndexes.get(index1), pendingSessionIndexes.get(index2));
          });

      // Display sorted pending appointments
      System.out.println("\n=== Pending Appointments ===");
      for (int i = 0; i < sortedIndices.size(); i++) {
        int sortedIndex = sortedIndices.get(i);
        System.out.printf("%d. %s\n", i + 1, pendingAppointments.get(sortedIndex));
      }

      // Prompt for action
      System.out.println(
          "\nEnter the number of the appointment to accept or decline, or press Enter to return:");
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

          List<Appointment> appointments = CsvDB.readAppointments();
          if (decision == 'A') {
            chosenSchedule.acceptAppointment(sessionIndex);
            updateAppointment(
                appointments, chosenSchedule, sessionIndex, AppointmentStatus.CONFIRMED.name());
            System.out.println("\nBooking has been accepted");
          } else if (decision == 'D') {
            chosenSchedule.declineAppointment(sessionIndex);
            updateAppointment(
                appointments, chosenSchedule, sessionIndex, AppointmentStatus.CANCELLED.name());
            System.out.println("\nBooking has been declined");
          } else {
            System.out.println("\nInvalid input, please enter 'A' or 'D'.");
          }

          CsvDB.saveSchedules(doctorSchedule);

        } else {
          System.out.println("\nInvalid choice. Please select a valid appointment number.");
        }
      } catch (NumberFormatException e) {
        System.out.println("\nInvalid input. Please enter a valid number.");
      }
    }
  }

  /**
   * Sets the availability of the doctor's schedule by allowing modifications to their session
   * statuses.
   *
   * <p>This method prompts the user to input a date for which they want to modify the availability,
   * displays the current status of each session for that date, and allows the user to update the
   * session statuses to either "Available" or "Unavailable". It ensures the schedule and
   * appointments are subsequently saved to the database.
   *
   * @param doctor The doctor whose schedule availability is being set.
   */
  @Override
  public void setAvailability(Doctor doctor) {
    List<Schedule> schedules = CsvDB.readSchedules();
    List<Appointment> appointments = CsvDB.readAppointments();

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
        LocalDate selectedDate =
            LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Schedule chosenSchedule = null;
        List<Schedule> doctorSchedule = getScheduleByDoctorID(doctor.getHospitalID());

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
          System.out.printf(
              "\nSchedule for %s:\n\n",
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

                Appointment selectedAcAppointment =
                    Appointment.getAppointmentByScheduleAndSession(
                        chosenSchedule, sessionChoice - 1, appointments);
                selectedAcAppointment.setStatus(AppointmentStatus.CANCELLED.name());
                System.out.printf(
                    "\nBooking has been declined. Session %d has been updated to Unavailable as it was pending.\n",
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
              for (int i = 0; i < schedules.size(); i++) {
                Schedule currentSchedule = schedules.get(i);
                if (currentSchedule.getDoctorID().equals(chosenSchedule.getDoctorID())
                    && currentSchedule.getDate().equals(chosenSchedule.getDate())) {
                  schedules.set(i, chosenSchedule);
                  break;
                }
              }
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

  /**
   * Initializes and returns a builder for creating instances of ScheduleManager.
   *
   * <p>The ScheduleManagerBuilder allows for the setting of required components such as
   * DateTimeFormatter and ScannerWrapper, ensuring these are properly configured before building a
   * ScheduleManager instance.
   *
   * @return a new instance of ScheduleManagerBuilder for building ScheduleManager objects
   */
  public static ScheduleManagerBuilder scheduleManagerBuilder() {
    return new ScheduleManagerBuilder();
  }

  /**
   * Builder class for creating instances of ScheduleManager.
   *
   * <p>Supports method chaining to set various properties needed for ScheduleManager construction.
   */
  // Static inner Builder class
  public static class ScheduleManagerBuilder {
    private DateTimeFormatter dateTimeFormatter;
    private ScannerWrapper scanner;

    /**
     * Setter method for DateTimeFormatter.
     *
     * @param dateTimeFormatter the DateTimeFormatter to be used
     * @return the ScheduleManagerBuilder instance for chaining
     */
    // Setter method for DateTimeFormatter
    public ScheduleManagerBuilder setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
      this.dateTimeFormatter = dateTimeFormatter;
      return this; // Return the builder for chaining
    }

    /**
     * Setter method for ScannerWrapper.
     *
     * @param scanner the ScannerWrapper to be used
     * @return the ScheduleManagerBuilder instance for chaining
     */
    // Setter method for ScannerWrapper
    public ScheduleManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    /**
     * Builds and returns an instance of ScheduleManager.
     *
     * <p>Validates that all required fields are set before creating the ScheduleManager instance.
     *
     * @return a new instance of ScheduleManager configured with the provided DateTimeFormatter and
     *     ScannerWrapper.
     * @throws IllegalArgumentException if any required fields are not set.
     */
    // Method to build a ScheduleManager instance
    public ScheduleManager build() {
      // Add validation to ensure required fields are set
      if (dateTimeFormatter == null) {
        throw new IllegalArgumentException("DateTimeFormatter must not be null.");
      }
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must not be null.");
      }
      return new ScheduleManager(dateTimeFormatter, scanner);
    }
  }
}
