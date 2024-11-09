package com.ntu.hns.model;

import com.ntu.hns.enums.AppointmentStatus;
import com.ntu.hns.enums.ScheduleStatus;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    @CsvBindByPosition(position = 0) private String appointmentID;
    @CsvBindByPosition(position = 1) private String patientID;
    @CsvBindByPosition(position = 2) private String doctorID;
    @CsvDate("dd/MM/yyyy")
    @CsvBindByPosition(position = 3)
    private LocalDate date;
    @CsvBindByPosition(position = 4) private int session;
    @CsvBindByPosition(position = 5) private String status;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Appointment() {}

    public Appointment(String appointmentID, String patientID, String doctorID, LocalDate date, int session,
            String status) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.session = session;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public int getSession() {
        return this.session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static Appointment getAppointmentByScheduleAndSession(
            Schedule chosenSchedule, int sessionIndex, List<Appointment> appointments) {
        if (chosenSchedule == null || appointments == null) {
            throw new IllegalArgumentException("Schedule or appointment list cannot be null.");
        }

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(chosenSchedule.getDoctorID())
                    && appointment.getDate().equals(chosenSchedule.getDate())
                    && (appointment.getSession() == sessionIndex + 1)
                    && (appointment.getStatus().equalsIgnoreCase(AppointmentStatus.PENDING.name()))) {
                return appointment;
            }
        }
        return null; // Return null if no matching appointment is found
    }

    public ArrayList<Appointment> getAppointmentsByPatientID(String patientID, ArrayList<Appointment> appointments) {
        ArrayList<Appointment> filteredAppt = new ArrayList<Appointment>();

        // Only fetch the confirmed and pending appointments for the patients
        for (Appointment appointment : appointments) {
            if (appointment.getPatientID().equals(patientID)
                    && !(appointment.getStatus().equals(AppointmentStatus.COMPLETED.name()))
                    && !(appointment.getStatus().equals(AppointmentStatus.CANCELLED.name()))) {
                filteredAppt.add(appointment);
            }
        }
        return filteredAppt;
    }

    public ArrayList<Appointment> getAppointmentsByDoctorID(String doctorID, ArrayList<Appointment> appointments) {
        ArrayList<Appointment> filteredAppt = new ArrayList<Appointment>();

        // fetch all appointments for the doctor
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID)) {
                filteredAppt.add(appointment);
            }
        }

        return filteredAppt;
    }

    public static ArrayList<Appointment> getConfirmedAppointmentsByDoctorID(String doctorID,
            List<Appointment> appointments) {
        ArrayList<Appointment> filteredAppointments = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID)
                    && appointment.getStatus().equals(ScheduleStatus.CONFIRMED.name())) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    public Appointment getAppointmentByAppointmentID(String appointmentID, ArrayList<Appointment> appointments) {
        Appointment found = null;
        for (Appointment appt : appointments) {
            if (appt.getAppointmentID().equals(appointmentID)) {
                found = appt;
                break;
            }
        }
        return found;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%d,%s", getAppointmentID(), getPatientID(), getDoctorID(), getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), getSession(), getStatus());
    }
}
