package com.ntu.hns.patient;

import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PatientActionsTest {

  /**
   * Test Case 1: View Medical Record
   *
   * <ul>
   *   <li>Patients view their own medical record.
   *   <li>Verify that the system displays the patient's medical record, including Patient ID, Name,
   *       Date of Birth, Gender, Contact Information, Blood Type, and Past Diagnoses and
   *       Treatments.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 1: View Medical Record")
  public void testPatientViewMedicalRecord() {
    provideInput("login\nP0003\n999\n1\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 2: Update Personal Information
   *
   * <ul>
   *   <li>Patient updates their email address and contact number.
   *   <li>Verify that patient's contact information is updated successfully, and the changes are
   *       reflected in the medical record.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 2: Update Personal Information")
  public void testUpdatePersonalInformation() {
    provideInput("login\nP0003\n999\n2\n1\n99996666\n2\ntest@gmail.com\n3\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 3: View Available Appointment Slots
   *
   * <ul>
   *   <li>Patient views available appointment slots with doctors.
   *   <li>Verify that the system displays a list of available appointment slots, showing doctors'
   *       names, dates, and times.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 3: View Available Appointment Slots")
  public void testViewAvailableAppointmentSlots() {
    provideInput("login\nP0001\n123\n3\n6\n2\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 4: Schedule an Appointment
   *
   * <ul>
   *   <li>Patient schedules a new appointment with a doctor.
   *   <li>Verify that the appointment is scheduled successfully with status "confirmed". The
   *       selected time slot becomes unavailable to other patients. The system should prevent the
   *       patient from booking a time slot that is unavailable/already booked.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 4: Schedule an Appointment")
  public void testScheduleAppointment() {
    provideInput("login\nP0001\n123\n4\n6\n11/11/2024\n1\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 5: Reschedule an Appointment
   *
   * <ul>
   *   <li>Patient reschedules an existing appointment to a new slot.
   *   <li>Verify that the appointment is rescheduled successfully. The previous time slot becomes
   *       available, and the new slot is reserved.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 5: Reschedule an Appointment")
  public void testRescheduleAppointment() {
    provideInput("login\nP0001\n123\n5\n1\n11/11/2024\n1\n");
    startApplication();
  }

  /**
   * Test Case 6: Cancel an Appointment
   *
   * <ul>
   *   <li>Patient cancels an existing appointment.
   *   <li>Verify that the appointment is canceled successfully, and the time slot becomes available
   *       for others.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 6: Cancel an Appointment")
  public void testCancelAppointment() {
    provideInput("login\nP0001\n123\n6\n1\n");
    startApplication();
  }

  /**
   * Test Case 7: View Scheduled Appointments
   *
   * <ul>
   *   <li>Patient views their list of scheduled appointments.
   *   <li>Verify that the system displays all upcoming appointments with details like doctor name,
   *       date, time, and status.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 7: View Scheduled Appointments")
  public void testViewScheduledAppointments() {
    provideInput("login\nP0001\n123\n7\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 8: View Past Appointment Outcome Records
   *
   * <ul>
   *   <li>Patient views outcome records of past appointments.
   *   <li>Verify that the system displays past appointment details, including services provided,
   *       prescribed medications, and consultation notes.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 8: View Past Appointment Outcome Records")
  public void testViewPastAppointmentOutcomeRecords() {
    provideInput("login\nP0001\n123\n8\n1\n\n9\nexit\n");
    startApplication();
  }
}
