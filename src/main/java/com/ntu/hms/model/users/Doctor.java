package com.ntu.hms.model.users;

import static com.ntu.hms.MenuDisplayer.displayDoctorMenu;

import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hms.manager.schedule.ScheduleManager;

/**
 * Represents a doctor in the hospital system, extending the functionality of the User class.
 * This class manages appointments, medical records, and schedules specific to a doctor.
 */
public class Doctor extends User {
  private AppointmentManager appointmentManager;
  private MedicalRecordManager medicalRecordManager;
  private ScheduleManager scheduleManager;

  /**
   * Default constructor for the Doctor class.
   *
   * <p>This constructor initializes a new instance of the Doctor class, setting up default
   * values and extending the User class. It sets up the doctor's basic structure without
   * initializing any specific attributes like hospitalID, password, name, age, or gender.
   */
  public Doctor() {}

  /**
   * Constructs a new instance of the Doctor class with the specified details.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   * @param password the doctor's password for authentication
   * @param name the doctor's full name
   * @param age the doctor's age
   * @param gender the doctor's gender
   */
  public Doctor(String hospitalID, String password, String name, int age, String gender) {
    super(hospitalID, password, name, age, gender);
  }

  /**
   * Displays the menu options available specifically to a doctor.
   * This method delegates to {@link #displayDoctorMenu()} to present
   * various actions a doctor can perform in the hospital system.
   * Actions include viewing and updating medical records, managing the
   * schedule, and handling appointments.
   */
  public void displayMenu() {
    displayDoctorMenu();
  }

  /**
   * Displays the doctor's weekly schedule.
   *
   * This method calls the `showWeeklySchedule` method of `ScheduleManager`
   * to display the weekly schedule for the current doctor instance.
   * The display includes information about the sessions for each day,
   * detailing availability and any scheduled appointments.
   */
  public void viewWeeklySchedule() {
    scheduleManager.showWeeklySchedule(this);
  }

  /**
   * Displays the upcoming confirmed appointments for the doctor.
   *
   * This method calls the `showUpcomingAppointments` method of `AppointmentManager`
   * to display the list of confirmed appointments that are scheduled to occur in the future.
   *
   * The method filters and organizes the appointments by date and session time,
   * and provides a formatted list of the appointment details including the sequence number,
   * time, and patient name.
   */
  public void viewUpcomingAppointments() {
    appointmentManager.showUpcomingAppointments(this);
  }

  /**
   * Updates the doctor's schedule by interacting with the ScheduleManager.
   *
   * This method delegates the responsibility of updating the schedule to the
   * `updateSchedule` method of the ScheduleManager. The ScheduleManager
   * handles all the necessary logic to update the schedule based on the
   * doctor's appointments and availability.
   */
  public void updateSchedule() {
    scheduleManager.updateSchedule(this);
  }

  /**
   * Sets the doctor's availability in the schedule.
   *
   * This method delegates the task of setting the doctor's availability to the `setAvailability`
   * method of the `ScheduleManager`. It updates the doctor's schedule by marking sessions as available
   * or unavailable based on the specified criteria.
   */
  public void setAvailability() {
    scheduleManager.setAvailability(this);
  }

  /**
   * Updates the outcome of an appointment.
   *
   * This method delegates to the `updateAppointmentOutcome` method of the
   * `AppointmentManager` to handle the logic for updating the outcome
   * of a specific appointment. It utilizes the current instance of
   * the `Doctor` class to perform this update.
   */
  public void updateAppointmentOutcome() {
    appointmentManager.updateAppointmentOutcome(this);
  }

  /**
   * Views the medical record of a patient under the care of the doctor.
   *
   * This method displays a list of patients who have confirmed appointments
   * with the doctor. Once a patient is selected, detailed medical record
   * information of the patient is shown, including general patient details and
   * specific diagnosis and treatment information, with a focus on displaying
   * prescriptions.
   *
   * The method relies on the `MedicalRecordManager` to filter and retrieve
   * the relevant patient and medical record data. It interacts with the
   * underlying database to read schedules and patient details.
   */
  public void viewMedicalRecord() {
    medicalRecordManager.showMedicalRecord(this);
  }

  /**
   * Updates the medical record by delegating the task to the `updateMedicalRecord`
   * method of the `MedicalRecordManager`.
   *
   * This method ensures that the medical record associated with the current instance
   * of the Doctor class is updated by leveraging the MedicalRecordManager's update
   * functionality. It simplifies the process by encapsulating the update logic within
   * the responsible manager.
   */
  public void updateMedicalRecord() {
    medicalRecordManager.updateMedicalRecord();
  }

  /**
   * Sets the AppointmentManager for this doctor.
   *
   * @param appointmentManager the AppointmentManager to be assigned
   */
  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }

  /**
   * Sets the MedicalRecordManager for this doctor.
   *
   * @param medicalRecordManager the MedicalRecordManager to be assigned
   */
  public void setMedicalRecordManager(MedicalRecordManager medicalRecordManager) {
    this.medicalRecordManager = medicalRecordManager;
  }

  /**
   * Sets the ScheduleManager for this doctor.
   *
   * @param scheduleManager the ScheduleManager to be assigned
   */
  public void setScheduleManager(ScheduleManager scheduleManager) {
    this.scheduleManager = scheduleManager;
  }
}
