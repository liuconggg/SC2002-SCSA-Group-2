package com.ntu.hms.model.users;

import static com.ntu.hms.MenuDisplayer.displayPatientMenu;

import com.ntu.hms.CsvDB;
import com.ntu.hms.InfoUpdater;
import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hms.manager.schedule.ScheduleManager;
import com.ntu.hms.util.ScannerWrapper;
import java.util.*;

/**
 * Represents a patient in the hospital management system. Inherits basic user attributes such as
 * hospital ID, password, name, age, and gender from the User class. This class also implements the
 * InfoUpdater interface to allow updating personal information.
 */
public class Patient extends User implements InfoUpdater {
  private String dateOfBirth;
  private String phoneNumber;
  private String email;
  private String bloodType;

  private ScannerWrapper scanner;
  private AppointmentManager appointmentManager;
  private MedicalRecordManager medicalRecordManager;
  private ScheduleManager scheduleManager;

  /**
   * Default constructor for the Patient class. This constructor initializes a new instance of the
   * Patient class with default values.
   */
  public Patient() {}

  /**
   * Constructs a new Patient with the specified details.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   * @param password the patient's password for authentication
   * @param name the patient's full name
   * @param age the patient's age
   * @param gender the patient's gender
   * @param dateOfBirth the patient's date of birth
   * @param phoneNumber the patient's contact phone number
   * @param email the patient's email address
   * @param bloodType the patient's blood type
   */
  public Patient(
      String hospitalID,
      String password,
      String name,
      int age,
      String gender,
      String dateOfBirth,
      String phoneNumber,
      String email,
      String bloodType) {
    super(hospitalID, password, name, age, gender);
    this.dateOfBirth = dateOfBirth;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.bloodType = bloodType;
  }

  /**
   * Displays the menu options available to the patient. This method delegates the task of
   * displaying the patient menu to the displayPatientMenu method, which includes options such as
   * viewing medical records, updating personal information, and managing appointments.
   */
  public void displayMenu() {
    displayPatientMenu();
  }

  /**
   * Returns the patient's date of birth.
   *
   * @return the date of birth as a String
   */
  public String getDateOfBirth() {
    return this.dateOfBirth;
  }

  /**
   * Sets the patient's date of birth.
   *
   * @param dateOfBirth the patient's date of birth to set
   */
  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Retrieves the patient's contact phone number.
   *
   * @return the patient's phone number as a String
   */
  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  /**
   * Sets the patient's contact phone number.
   *
   * @param phoneNumber the patient's contact phone number to set
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Retrieves the patient's email address.
   *
   * @return the patient's email as a String
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Sets the patient's email address.
   *
   * @param email the patient's email address to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Retrieves the patient's blood type.
   *
   * @return the patient's blood type as a String
   */
  public String getBloodType() {
    return this.bloodType;
  }

  /**
   * Sets the patient's blood type.
   *
   * @param bloodType the patient's blood type to set
   */
  public void setBloodType(String bloodType) {
    this.bloodType = bloodType;
  }

  /**
   * Updates the personal information of the patient.
   *
   * <p>This method prompts the user to choose between updating their phone number or email address,
   * and makes the corresponding changes after the user inputs the new information. It reads the
   * list of patients from a CSV database, updates the selected details, and saves the changes back
   * to the CSV file.
   *
   * <p>It loops through the options until the user decides to exit the update process.
   *
   * <p>If the user inputs an invalid choice or non-numeric input, appropriate error messages will
   * be displayed and the user will be prompted again.
   */
  @Override
  public void updatePersonalInformation() {
    List<Patient> patients = CsvDB.readPatients();
    boolean changing = true;
    int action = -1;

    while (changing) {
      System.out.println("Select the information to update:");
      System.out.println("1. Phone Number");
      System.out.println("2. Email Address");
      System.out.println("3. Exit");
      System.out.print("Your Choice: ");
      try {
        action = scanner.nextInt();
        switch (action) {
          case 1:
            System.out.print("Enter your new phone number: ");
            String newPhoneNumber = scanner.nextLine();
            this.setPhoneNumber(newPhoneNumber);
            System.out.println("Phone number has been updated");
            break;
          case 2:
            System.out.print("Enter your new email address: ");
            String newEmail = scanner.nextLine();
            this.setEmail(newEmail);
            System.out.println("Email address has been updated");
            break;
          case 3:
            changing = false;
            break;
          default:
            System.out.println("Invalid option. Please try again.");
        }

        // Update the Patient object in the ArrayList<User> users
        if ((action > 0) && (action != 3)) {
          for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getHospitalID().equals(this.getHospitalID())) {
              patients.set(i, this);
              break;
            }
          }

          // Save all users
          CsvDB.savePatients(patients);
          //          CsvDB.saveUsers(patients);
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.nextLine();
      }
    }
  }

  /**
   * Displays the weekly schedule for the patient.
   *
   * <p>This method delegates the action of showing the weekly schedule to the scheduleManager,
   * which handles the process of displaying the schedule based on the availability and
   * appointments.
   */
  public void viewWeeklySchedule() {
    scheduleManager.showWeeklySchedule();
  }

  /**
   * Allows a patient to view their own medical record.
   *
   * <p>This method delegates the task of displaying the medical record to the medicalRecordManager,
   * which handles the details of fetching and presenting the patient's medical information.
   */
  public void viewMedicalRecord() {
    medicalRecordManager.showMedicalRecord(this);
  }

  /**
   * Schedules an appointment for the patient.
   *
   * <p>This method delegates the task of scheduling an appointment to the appointmentManager. It
   * utilizes the appointmentManager's capabilities to handle the details of booking an appointment,
   * such as checking availability and setting the appointment date.
   */
  public void scheduleAppointment() {
    appointmentManager.scheduleAppointment(this);
  }

  /**
   * Cancels an appointment for the patient.
   *
   * <p>This method delegates the task of cancelling an appointment to the appointmentManager. It
   * calls the appointmentManager's cancelAppointment method, passing the current patient instance
   * as an argument. This includes checking which appointments are eligible for cancellation,
   * allowing the user to select an appointment to cancel, and updating the status of the chosen
   * appointment to "Cancelled".
   */
  public void cancelAppointment() {
    appointmentManager.cancelAppointment(this);
  }

  /**
   * Reschedules an existing appointment for the patient.
   *
   * <p>This method delegates the task of rescheduling the appointment to the appointmentManager,
   * which handles the details of finding a new available time slot and updating the appointment
   * record.
   */
  public void rescheduleAppointment() {
    appointmentManager.rescheduleAppointment(this);
  }

  /**
   * Displays the scheduled appointments for the patient.
   *
   * <p>This method delegates the task of showing the list of scheduled appointments to the
   * appointmentManager, which handles the retrieval and display of the appointments that are
   * confirmed and not yet completed or canceled for the patient.
   */
  public void viewScheduledAppointments() {
    appointmentManager.showScheduledAppointments(this);
  }

  /**
   * Allows the patient to view the outcome of their completed appointments.
   *
   * <p>This method delegates the task of displaying the outcome of completed appointments to the
   * appointmentManager. It involves fetching completed appointments for the patient and displaying
   * the details of the selected appointment's outcome, including service type, consultation notes,
   * and prescriptions.
   */
  public void viewAppointmentOutcome() {
    appointmentManager.showAppointmentOutcome(this);
  }

  /**
   * Sets the ScannerWrapper instance for the patient.
   *
   * @param scanner the ScannerWrapper instance to set
   */
  public void setScanner(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  /**
   * Sets the appointment manager for the patient.
   *
   * @param appointmentManager the appointment manager to set
   */
  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }

  /**
   * Sets the medical record manager for the patient.
   *
   * @param medicalRecordManager the MedicalRecordManager instance to set
   */
  public void setMedicalRecordManager(MedicalRecordManager medicalRecordManager) {
    this.medicalRecordManager = medicalRecordManager;
  }

  /**
   * Sets the schedule manager for the patient.
   *
   * @param scheduleManager the ScheduleManager instance to set
   */
  public void setScheduleManager(ScheduleManager scheduleManager) {
    this.scheduleManager = scheduleManager;
  }
}
