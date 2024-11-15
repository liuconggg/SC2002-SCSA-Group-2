package com.ntu.hms.doctor;

import static com.ntu.hms.TestManager.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The type Doctor actions test.
 * This class contains test cases for various actions that can be performed by a doctor,
 * including viewing and updating patient medical records, managing personal schedules,
 * and handling appointment requests.
 */
public class DoctorActionsTest {

  /**
   * Test Case 9: View Patient Medical Records
   * Doctor views medical records of patients under their care.
   * Verify that the patient's medical record is displayed, including all relevant medical history.
   */
  @Test
  @DisplayName("Test Case 9: View Patient Medical Record")
  public void testViewPatientMedicalRecord() {
    provideInput("login\nD0001\n123\n1\n1\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 10: Update Patient Medical Records
   * Doctor adds a new diagnosis and treatment plan to a patient's medical record.
   * Verify that the medical record is updated successfully, reflecting the new information.
   */
  @Test
  @DisplayName("Test Case 10: Update Patient Medical Records")
  public void testUpdatePatientMedicalRecord() {
    provideInput("login\nD0001\n123\n2\n1\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 11: View Personal Schedule
   * Doctor views their personal appointment schedule.
   * Verify that the system displays the doctor's upcoming appointments and availability slots.
   */
  @Test
  @DisplayName("Test Case 11: View Personal Schedule")
  public void testViewPersonalSchedule() {
    provideInput("login\nD0001\n123\n3\n2\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 12: Set Availability for Appointments
   * Doctor sets or updates their availability for patient appointments.
   * Verify that the doctor's availability is updated, and patients can see the new slots when scheduling appointments.
   */
  @Test
  @DisplayName("Test Case 12: Set Availability for Appointments")
  public void testSetAvailability() {
    provideInput("login\nD0001\n123\n4\n07/11/2024\n1\n\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 13: Accept or Decline Appointment Requests
   * Doctor accepts or declines an appointment request from a patient.
   * Verify that the appointment status changes to "confirmed" when accepted or "cancelled" when declined,
   * and the patient is able to see the updated status of the appointment.
   */
  @Test
  @DisplayName("Test Case 13: Accept or Decline Appointment Requests")
  public void testAcceptOrDeclineAppointmentRequests() {
    provideInput("login\nD0001\n123\n5\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 14: View Upcoming Appointments
   * Doctor views all upcoming confirmed appointments.
   * Verify that the system displays a list of all upcoming appointments with patient details and appointment times.
   */
  @Test
  @DisplayName("Test Case 14: View Upcoming Appointments")
  public void testViewUpcomingAppointments() {
    provideInput("login\nD0001\n123\n6\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 15: Record Appointment Outcome
   * Doctor records the outcome of a completed appointment.
   * Verify that the appointment outcome is recorded, and relevant updates are visible to the patient under
   * "View Past Appointment Outcome Records".
   */
  @Test
  @DisplayName("Test Case 15: Record Appointment Outcome")
  public void testRecordAppointmentOutcome() {
    provideInput("login\nD0001\n123\n7\n8\nexit\n");
    startApplication();
  }
}