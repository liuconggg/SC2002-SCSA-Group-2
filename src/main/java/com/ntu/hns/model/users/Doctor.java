package com.ntu.hns.model.users;

import static com.ntu.hns.MenuDisplayer.displayDoctorMenu;

import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Doctor extends User {
  @Autowired private AppointmentManager appointmentManager;
  @Autowired private MedicalRecordManager medicalRecordManager;
  @Autowired private ScheduleManager scheduleManager;

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
}
