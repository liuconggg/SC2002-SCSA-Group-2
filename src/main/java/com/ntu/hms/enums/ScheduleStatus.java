package com.ntu.hms.enums;

/**
 * Represents the status of a scheduled appointment. The status can be one of the following:
 *
 * <ul>
 *   <li>CONFIRMED - Indicates that the appointment is confirmed and will take place as scheduled.
 *   <li>PENDING - Indicates that the appointment is awaiting confirmation and has not yet been
 *       confirmed.
 *   <li>CANCELLED - Indicates that the appointment has been cancelled and will not take place.
 * </ul>
 */
public enum ScheduleStatus {
  CONFIRMED,
  PENDING,
  CANCELLED
}
