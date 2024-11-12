package com.ntu.hns.model.users;

import static com.ntu.hns.MenuDisplayer.displayDoctorMenu;

import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;

public class Doctor extends User {
  private AppointmentManager appointmentManager;
  private MedicalRecordManager medicalRecordManager;
  private ScheduleManager scheduleManager;

  /** Default constructor required for OpenCSV to instantiate object. */
  public Doctor() {}

  public Doctor(String hospitalID, String password, String name, int age, String gender) {
    super(hospitalID, password, name, age, gender);
  }

  public void displayMenu() {
    displayDoctorMenu();
  }

  public void viewWeeklySchedule() {
    scheduleManager.showWeeklySchedule(this);
  }

  public void viewUpcomingAppointments() {
    appointmentManager.showUpcomingAppointments(this);
  }

  public void updateSchedule() {
    scheduleManager.updateSchedule(this);
  }

  public void setAvailability() {
    scheduleManager.setAvailability(this);
  }

  public void updateAppointmentOutcome() {
    appointmentManager.updateAppointmentOutcome(this);
  }

  public void viewMedicalRecord() {
    medicalRecordManager.showMedicalRecord(this);
  }

  public void updateMedicalRecord() {
    medicalRecordManager.updateMedicalRecord();
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
