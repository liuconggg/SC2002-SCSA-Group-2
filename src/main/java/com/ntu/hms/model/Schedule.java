package com.ntu.hms.model;

import com.ntu.hms.enums.ScheduleStatus;
import java.time.LocalDate;

/**
 * Represents a schedule for a doctor, including their ID, date, and session statuses.
 */
public class Schedule {
  private String doctorID;
  private LocalDate date;
  private String[] session = new String[8];

  public Schedule() {}

  /**
   * Constructs a new Schedule with the specified doctor ID, date, and session statuses.
   *
   * @param doctorID Unique identifier of the doctor for whom the schedule is created.
   * @param date The date for which the schedule is applicable.
   * @param session Array of session statuses for the schedule.
   */
  public Schedule(String doctorID, LocalDate date, String[] session) {
    this.doctorID = doctorID;
    this.date = date;
    this.session = session;
  }

  /**
   * Retrieves the unique identifier of the doctor for whom the schedule is created.
   *
   * @return the doctor's unique identifier as a String.
   */
  public String getDoctorID() {
    return this.doctorID;
  }

  /**
   * Sets the unique identifier for the doctor this schedule pertains to.
   *
   * @param doctorID the unique identifier of the doctor
   */
  public void setDoctorID(String doctorID) {
    this.doctorID = doctorID;
  }

  /**
   * Retrieves the date for which the schedule is applicable.
   *
   * @return the date of the schedule as a LocalDate.
   */
  public LocalDate getDate() {
    return this.date;
  }

  /**
   * Sets the date for the schedule.
   *
   * @param date The date to be set for the schedule.
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Retrieves the session statuses for the schedule.
   *
   * @return an array of Strings representing the session statuses.
   */
  public String[] getSession() {
    return this.session;
  }

  /**
   * Sets the session statuses for the schedule.
   *
   * @param session An array of Strings representing the session statuses.
   */
  public void setSession(String[] session) {
    this.session = session;
  }

  /**
   * Creates a default schedule for a specified doctor on a given date with all sessions marked as "Available".
   *
   * @param doctorID the unique identifier of the doctor for whom the schedule is created
   * @param date the date for which the schedule is applicable
   * @return a Schedule object with specified doctor ID, date, and all sessions marked as "Available"
   */
  public static Schedule createDefaultSchedule(String doctorID, LocalDate date) {
    String[] defaultSessions = new String[8];
    for (int i = 0; i < defaultSessions.length; i++) {
      defaultSessions[i] = "Available"; // Set all sessions as available by default
    }
    return new Schedule(doctorID, date, defaultSessions);
  }

  /**
   * Retrieves the status of a session at the given index.
   *
   * @param sessionIndex the index of the session whose status is to be retrieved.
   * @return the status of the session as a String, which could be "Available", "Unavailable", or specific status extracted from session info. Returns "Unknown" if the status cannot
   *  be determined.
   */
  // Helper Methods to Handle Session Status and com.ntu.hms.users.Patient ID
  public String getSessionStatus(int sessionIndex) {
    String sessionInfo = this.session[sessionIndex];
    if (sessionInfo.equalsIgnoreCase("Available") || sessionInfo.equalsIgnoreCase("Unavailable")) {
      return sessionInfo;
    }
    String[] parts = sessionInfo.split("-");
    return parts.length > 1 ? parts[1] : "Unknown";
  }

  /**
   * Retrieves the patient ID from the session information at the specified index.
   *
   * @param sessionIndex the index of the session from which to retrieve the patient ID
   * @return the patient ID as a String if available, otherwise returns null
   */
  public String getPatientIdFromSession(int sessionIndex) {
    String sessionInfo = this.session[sessionIndex];
    if (sessionInfo.contains("-")) {
      return sessionInfo.split("-")[0];
    }
    return null;
  }

  /**
   * Sets the status of a specific session identified by the given session index.
   * If the session at the specified index contains a patient ID, the status is appended to it.
   * Otherwise, if the status is "Available", it sets the session to "Available".
   *
   * @param sessionIndex the index of the session to update
   * @param status the new status for the session
   */
  public void setSessionStatus(int sessionIndex, String status) {
    String patientId = getPatientIdFromSession(sessionIndex);
    if (patientId != null) {
      this.session[sessionIndex] = patientId + "-" + status;
    } else if (status.equals("Available")) {
      this.session[sessionIndex] = "Available";
    }
  }

  /**
   * Accepts the appointment for a specific session and sets its status to "CONFIRMED".
   *
   * @param sessionIndex the index of the session to be accepted.
   */
  public void acceptAppointment(int sessionIndex) {
    setSessionStatus(sessionIndex, ScheduleStatus.CONFIRMED.name());
  }

  /**
   * Declines an appointment by marking the specified session as "Available".
   *
   * @param sessionIndex the index of the session to update
   */
  public void declineAppointment(int sessionIndex) {
    // add logic here
    this.session[sessionIndex] = "Available";
  }
}
