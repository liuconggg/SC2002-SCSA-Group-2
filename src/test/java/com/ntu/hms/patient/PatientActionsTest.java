package com.ntu.hms.patient;

import static com.ntu.hms.TestManager.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The type Patient actions test.
 * This class contains test cases for various actions that can be performed by a patient,
 * including viewing and updating personal records, managing appointments,
 * and viewing past appointment outcomes.
 */
public class PatientActionsTest {

  /**
   * Sets up.
   * This method is executed before each test case to refresh CSV files.
   */
  @BeforeEach
  public void setUp() {
    refreshCsvFiles();
  }

  /**
   * Tear down.
   * This method is executed after each test case to stop the application.
   */
  @AfterEach
  public void tearDown() {
    stopApplication();
  }

  /**
   * Test Case 1: View Medical Record.
   * Patients view their own medical record.
   * Verify that the system displays the patient's medical record, including Patient ID, Name,
   * Date of Birth, Gender, Contact Information, Blood Type, and Past Diagnoses and Treatments.
   */
  @Test
  @DisplayName("Test Case 1: View Medical Record")
  public void testPatientViewMedicalRecord() {
    provideInput("login\nP0001\n123\n1\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 2: Update Personal Information.
   * Patient updates their email address and contact number.
   * Verify that the patient's contact information is updated successfully, and the changes are
   * reflected in the medical record.
   */
  @Test
  @DisplayName("Test Case 2: Update Personal Information")
  public void testUpdatePersonalInformation() {
    provideInput("login\nP0001\n123\n2\nnew_email@example.com\n9876543210\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 3: View Available Appointment Slots.
   * Patient views available appointment slots with doctors.
   * Verify that the system displays a list of available appointment slots, showing doctors' names,
   * dates, and times.
   */
  @Test
  @DisplayName("Test Case 3: View Available Appointment Slots")
  public void testViewAvailableAppointmentSlots() {
    provideInput("login\nP0001\n123\n3\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 4: Schedule an Appointment.
   * Patient schedules a new appointment with a doctor.
   * Verify that the appointment is scheduled successfully with status "confirmed." The selected time slot
   * becomes unavailable to other patients. The system should prevent the patient from booking a time slot
   * that is unavailable/already booked.
   */
  @Test
  @DisplayName("Test Case 4: Schedule an Appointment")
  public void testScheduleAppointment() {
    provideInput("login\nP0001\n123\n4\nDr. Smith\n2024-11-07 10:00\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 5: Reschedule an Appointment.
   * Patient reschedules an existing appointment to a new slot.
   * Verify that the appointment is rescheduled successfully. The previous time slot becomes available,
   * and the new slot is reserved.
   */
  @Test
  @DisplayName("Test Case 5: Reschedule an Appointment")
  public void testRescheduleAppointment() {
    provideInput("login\nP0001\n123\n5\nDr. Smith\n2024-11-08 11:00\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 6: Cancel an Appointment.
   * Patient cancels an existing appointment.
   * Verify that the appointment is canceled successfully, and the time slot becomes available for others.
   */
  @Test
  @DisplayName("Test Case 6: Cancel an Appointment")
  public void testCancelAppointment() {
    provideInput("login\nP0001\n123\n6\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 7: View Scheduled Appointments.
   * Patient views their list of scheduled appointments.
   * Verify that the system displays all upcoming appointments with details like doctor name,
   * date, time, and status.
   */
  @Test
  @DisplayName("Test Case 7: View Scheduled Appointments")
  public void testViewScheduledAppointments() {
    provideInput("login\nP0001\n123\n7\n\nexit\n");
    startApplication();
  }

  /**
   * Test Case 8: View Past Appointment Outcome Records.
   * Patient views outcome records of past appointments.
   * Verify that the system displays past appointment details, including services provided,
   * prescribed medications, and consultation notes.
   */
  @Test
  @DisplayName("Test Case 8: View Past Appointment Outcome Records")
  public void testViewPastAppointmentOutcomeRecords() {
    provideInput("login\nP0001\n123\n8\n\nexit\n");
    startApplication();
  }
}