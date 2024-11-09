package com.ntu.hns.model;

import com.ntu.hns.enums.ScheduleStatus;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    @CsvBindByPosition(position = 0) private String doctorID;
    @CsvDate("dd/MM/yyyy")
    @CsvBindByPosition(position = 1)
    private LocalDate date;
    @CsvBindByPosition(position = 2) private String[] session = new String[8];

    /** Default constructor required for OpenCSV to instantiate object. */
    public Schedule() {}

    public Schedule(String doctorID, LocalDate date, String[] session) {
        this.doctorID = doctorID;
        this.date = date;
        this.session = session;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String[] getSession() {
        return this.session;
    }

    public void setSession(String[] session) {
        this.session = session;
    }

    public static Schedule createDefaultSchedule(String doctorID, LocalDate date) {
        String[] defaultSessions = new String[8];
        for (int i = 0; i < defaultSessions.length; i++) {
            defaultSessions[i] = "Available"; // Set all sessions as available by default
        }
        return new Schedule(doctorID, date, defaultSessions);
    }

    // Helper Methods to Handle Session Status and com.ntu.hms.users.Patient ID
    public String getSessionStatus(int sessionIndex) {
        String sessionInfo = this.session[sessionIndex];
        if (sessionInfo.equalsIgnoreCase("Available") || sessionInfo.equalsIgnoreCase("Unavailable")) {
            return sessionInfo;
        }
        String[] parts = sessionInfo.split("-");
        return parts.length > 1 ? parts[1] : "Unknown";
    }

    public String getPatientIdFromSession(int sessionIndex) {
        String sessionInfo = this.session[sessionIndex];
        if (sessionInfo.contains("-")) {
            return sessionInfo.split("-")[0];
        }
        return null;
    }

    public void setSessionStatus(int sessionIndex, String status) {
        String patientId = getPatientIdFromSession(sessionIndex);
        if (patientId != null) {
            this.session[sessionIndex] = patientId + "-" + status;
        } else if (status.equals("Available")) {
            this.session[sessionIndex] = "Available";
        }
    }

    public void acceptAppointment(int sessionIndex) {
        setSessionStatus(sessionIndex, ScheduleStatus.CONFIRMED.name());
    }

    public void declineAppointment(int sessionIndex) {
        // add logic here
        this.session[sessionIndex] = "Available";
    }

}
