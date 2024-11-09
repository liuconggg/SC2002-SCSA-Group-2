package com.ntu.hns.manager.appointment;

import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;

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
