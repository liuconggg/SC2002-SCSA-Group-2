package com.ntu.hns.doctor;

import static com.ntu.hns.TestManager.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DoctorActionsTest {

  /**
   * Test Case 9: View Patient Medical Records
   *
   * <ul>
   *   <li>Doctor views medical records of patients under their care.
   *   <li>Verify that the patient's medical record is displayed, including all relevant medical
   *       history.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 9: View Patient Medical Record")
  public void testViewPatientMedicalRecord() {
    provideInput("login\nD0001\n123\n1\n1\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 10: Update Patient Medical Records
   *
   * <ul>
   *   <li>Doctor adds a new diagnosis and treatment plan to a patient's medical record.
   *   <li>Verify that the medical record is updated successfully, reflecting the new information.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 10: Update Patient Medical Records")
  public void testUpdatePatientMedicalRecord() {
    provideInput("login\nD0001\n123\n2\n1\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 11: View Personal Schedule
   *
   * <ul>
   *   <li>Doctor views their personal appointment schedule.
   *   <li>Verify that the system displays the doctor's upcoming appointments and availability
   *       slots.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 11: View Personal Schedule")
  public void testViewPersonalSchedule() {
    provideInput("login\nD0001\n123\n3\n2\n\n8\nexit\n");
    startApplication();
  }

  /**
   * Test Case 12: Set Availability for Appointments
   *
   * <ul>
   *   <li>Doctor sets or updates their availability for patient appointments.
   *   <li>Verify that the doctor's availability is updated, and patients can see the new slots when
   *       scheduling appointments.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 12: Set Availability for Appointments")
  public void testSetAvailability() {
    provideInput("login\nD0001\n123\n4\n07/11/2024\n1\n\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 13: Accept or Decline Appointment Requests")
  public void testAcceptOrDeclineAppointmentRequests() {
    provideInput("login\nD0001\n123\n5\n");
    startApplication();
  }

  /**
   * Test Case 14: View Upcoming Appointments
   *
   * <ul>
   *   <li>Doctor views all upcoming confirmed appointments.
   *   <li>Verify that the system displays a list of all upcoming appointments with patient details
   *       and appointment times.
   */
  @Test
  @DisplayName("Test Case 14: View Upcoming Appointments")
  public void testViewUpcomingAppointments() {
    provideInput("login\nD0001\n123\n6\n\n8\nexit\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 15: Record Appointment Outcome")
  public void testRecordAppointmentOutcome() {
    provideInput("login\nD0001\n123\n7\n");
    startApplication();
  }
}
