package com.ntu.hns.model.users;

import static com.ntu.hns.MenuDisplayer.displayPatientMenu;

import com.ntu.hns.CsvDB;
import com.ntu.hns.InfoUpdater;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;
import com.ntu.hns.util.ScannerWrapper;
import java.util.*;

public class Patient extends User implements InfoUpdater {
  private String dateOfBirth;
  private String phoneNumber;
  private String email;
  private String bloodType;

  private ScannerWrapper scanner;
  private AppointmentManager appointmentManager;
  private MedicalRecordManager medicalRecordManager;
  private ScheduleManager scheduleManager;

  /** Default constructor required for OpenCSV to instantiate object. */
  public Patient() {}

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

  public void displayMenu() {
    displayPatientMenu();
  }

  public String getDateOfBirth() {
    return this.dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBloodType() {
    return this.bloodType;
  }

  public void setBloodType(String bloodType) {
    this.bloodType = bloodType;
  }

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

  public void viewWeeklySchedule() {
    scheduleManager.showWeeklySchedule();
  }

  public void viewMedicalRecord() {
    medicalRecordManager.showMedicalRecord(this);
  }

  public void scheduleAppointment() {
    appointmentManager.scheduleAppointment(this);
  }

  public void cancelAppointment() {
    appointmentManager.cancelAppointment(this);
  }

  public void rescheduleAppointment() {
    appointmentManager.rescheduleAppointment(this);
  }

  public void viewScheduledAppointments() {
    appointmentManager.showScheduledAppointments(this);
  }

  public void viewAppointmentOutcome() {
    appointmentManager.showAppointmentOutcome(this);
  }

  public void setScanner(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }

  public void setMedicalRecordManager(MedicalRecordManager medicalRecordManager) {
    this.medicalRecordManager = medicalRecordManager;
  }

  public void setScheduleManager(ScheduleManager scheduleManager) {
    this.scheduleManager = scheduleManager;
  }
}
