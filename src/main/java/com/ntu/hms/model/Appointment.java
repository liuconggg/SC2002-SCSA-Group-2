package com.ntu.hms.model;

import com.ntu.hms.enums.AppointmentStatus;
import com.ntu.hms.enums.ScheduleStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an appointment in a medical scheduling system. It contains information
 * about the appointment ID, patient ID, doctor ID, date, session, and status.
 */
public class Appointment {
  private String appointmentID;
  private String patientID;
  private String doctorID;
  private LocalDate date;
  private int session;
  private String status;

  /**
   * Default constructor for creating an instance of the Appointment class. Initializes the object
   * with default values.
   */
  public Appointment() {}

  /**
   * Constructs an Appointment instance with provided details.
   *
   * @param appointmentID the unique ID of the appointment
   * @param patientID the ID of the patient making the appointment
   * @param doctorID the ID of the doctor with whom the appointment is made
   * @param date the date on which the appointment is scheduled
   * @param session the session number for the appointment
   * @param status the current status of the appointment
   */
  public Appointment(
      String appointmentID,
      String patientID,
      String doctorID,
      LocalDate date,
      int session,
      String status) {
    this.appointmentID = appointmentID;
    this.patientID = patientID;
    this.doctorID = doctorID;
    this.date = date;
    this.session = session;
    this.status = status;
  }

  /**
   * Sets the current status of the appointment.
   *
   * @param status the new status to be set for the appointment
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Retrieves the current status of the appointment.
   *
   * @return the status of the appointment as a String
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the unique identifier for the appointment.
   *
   * @param appointmentID the unique identifier for the appointment
   */
  public void setAppointmentID(String appointmentID) {
    this.appointmentID = appointmentID;
  }

  /**
   * Retrieves the unique identifier for the appointment.
   *
   * @return the appointment ID as a String
   */
  public String getAppointmentID() {
    return appointmentID;
  }

  /**
   * Retrieves the ID of the patient associated with this appointment.
   *
   * @return the patient's ID as a String
   */
  public String getPatientID() {
    return this.patientID;
  }

  /**
   * Sets the unique identifier for the patient associated with the appointment.
   *
   * @param patientID the unique identifier of the patient
   */
  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  /**
   * Retrieves the ID of the doctor associated with this appointment.
   *
   * @return the doctor's ID as a String
   */
  public String getDoctorID() {
    return this.doctorID;
  }

  /**
   * Sets the unique identifier for the doctor associated with this appointment.
   *
   * @param doctorID the unique identifier of the doctor
   */
  public void setDoctorID(String doctorID) {
    this.doctorID = doctorID;
  }

  /**
   * Retrieves the session number for the appointment.
   *
   * @return the session number as an integer
   */
  public int getSession() {
    return this.session;
  }

  /**
   * Sets the session number for the appointment.
   *
   * @param session the session number to be set for the appointment
   */
  public void setSession(int session) {
    this.session = session;
  }

  /**
   * Retrieves the date of the appointment.
   *
   * @return the date of the appointment as a LocalDate object
   */
  public LocalDate getDate() {
    return this.date;
  }

  /**
   * Sets the date for the appointment.
   *
   * @param date the date to be set for the appointment, represented as a LocalDate object
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Retrieves an appointment based on the provided schedule and session index.
   *
   * @param chosenSchedule The chosen schedule which includes the doctor ID and date.
   * @param sessionIndex The session index for which the appointment needs to be found.
   * @param appointments The list of appointment objects to search through.
   * @return The matching appointment if found, otherwise null.
   * @throws IllegalArgumentException If the chosenSchedule or appointments list is null.
   */
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

  /**
   * Retrieves a list of appointments for a given patient ID, excluding completed and cancelled
   * appointments.
   *
   * @param patientID the ID of the patient whose appointments are to be retrieved
   * @param appointments the list of all available appointments to be filtered
   * @return a list of appointments for the given patient ID that are either confirmed or pending
   */
  public ArrayList<Appointment> getAppointmentsByPatientID(
      String patientID, ArrayList<Appointment> appointments) {
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

  /**
   * Retrieves a list of appointments for a specified doctor ID.
   *
   * @param doctorID the ID of the doctor whose appointments are to be retrieved
   * @param appointments the list of all available appointments to be filtered
   * @return a list of appointments for the given doctor ID
   */
  public ArrayList<Appointment> getAppointmentsByDoctorID(
      String doctorID, ArrayList<Appointment> appointments) {
    ArrayList<Appointment> filteredAppt = new ArrayList<Appointment>();

    // fetch all appointments for the doctor
    for (Appointment appointment : appointments) {
      if (appointment.getDoctorID().equals(doctorID)) {
        filteredAppt.add(appointment);
      }
    }

    return filteredAppt;
  }

  /**
   * Retrieves a list of confirmed appointments for a specific doctor.
   *
   * @param doctorID the ID of the doctor whose confirmed appointments are to be retrieved
   * @param appointments the list of all available appointments to be filtered
   * @return a list of confirmed appointments for the given doctor ID
   */
  public static ArrayList<Appointment> getConfirmedAppointmentsByDoctorID(
      String doctorID, List<Appointment> appointments) {
    ArrayList<Appointment> filteredAppointments = new ArrayList<>();

    for (Appointment appointment : appointments) {
      if (appointment.getDoctorID().equals(doctorID)
          && appointment.getStatus().equals(ScheduleStatus.CONFIRMED.name())) {
        filteredAppointments.add(appointment);
      }
    }
    return filteredAppointments;
  }

  /**
   * Retrieves an appointment from the provided list based on the given appointment ID.
   *
   * @param appointmentID the unique ID of the appointment to find
   * @param appointments the list of appointments to search through
   * @return the appointment that matches the given ID, or null if not found
   */
  public Appointment getAppointmentByAppointmentID(
      String appointmentID, ArrayList<Appointment> appointments) {
    Appointment found = null;
    for (Appointment appt : appointments) {
      if (appt.getAppointmentID().equals(appointmentID)) {
        found = appt;
        break;
      }
    }
    return found;
  }

  /**
   * Generates a string representation of the Appointment object.
   *
   * @return a comma-separated string containing: appointment ID, patient ID, doctor ID, formatted
   *     date (dd/MM/yyyy), session number, and status.
   */
  @Override
  public String toString() {
    return String.format(
        "%s,%s,%s,%s,%d,%s",
        getAppointmentID(),
        getPatientID(),
        getDoctorID(),
        getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        getSession(),
        getStatus());
  }
}
