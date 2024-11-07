package com.ntu.hns.model.users;

import com.ntu.hns.Displayable;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Doctor extends User implements Displayable{
    @Autowired private AppointmentManager appointmentManager;
    @Autowired private MedicalRecordManager medicalRecordManager;
    @Autowired private ScheduleManager scheduleManager;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Doctor() {}

    public Doctor(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Doctor Menu ===");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointment");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout\n");
        System.out.print("Enter your choice: ");
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
