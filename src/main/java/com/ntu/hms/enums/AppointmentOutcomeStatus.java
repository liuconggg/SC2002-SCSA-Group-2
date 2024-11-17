package com.ntu.hms.enums;

/**
 * Represents the status of an appointment outcome.
 *
 * <p>This enumeration is used to track the completion state of an appointment. It can be utilized
 * within methods that handle the updating and recording of appointment outcomes, such as those in
 * the AppointmentManager class.
 *
 * <p>Enum Constants: - PENDING: Indicates that the outcome of the appointment has not been
 * finalized or recorded yet. - DISPENSED: Indicates that the outcome of the appointment has been
 * recorded and any prescribed medications have been dispensed.
 */
public enum AppointmentOutcomeStatus {
  PENDING,
  DISPENSED
}
