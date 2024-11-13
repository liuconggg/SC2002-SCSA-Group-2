package com.ntu.hms.manager.appointment;

import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;

public interface AppointmentManagerInterface {

  void scheduleAppointment(Patient patient);

  void cancelAppointment(Patient patient);

  void rescheduleAppointment(Patient patient);

  void showScheduledAppointments(Patient patient);

  void showAppointmentOutcome(Patient patient);

  void showUpcomingAppointments(Doctor doctor);

  void updateAppointmentOutcome(Doctor doctor);

  void showAppointmentOutcome();
}
