package com.ntu.hms.enums;

/**
 * Enumeration representing the status of an appointment.
 *
 * <p>PENDING: Indicates that the appointment is yet to be confirmed. CANCELLED: Indicates that the
 * appointment has been cancelled. COMPLETED: Indicates that the appointment has been completed.
 * CONFIRMED: Indicates that the appointment has been confirmed. NO_SHOW: Indicates that the patient
 * did not show up for the appointment.
 */
public enum AppointmentStatus {
  PENDING,
  CANCELLED,
  COMPLETED,
  CONFIRMED,
  NO_SHOW
}
