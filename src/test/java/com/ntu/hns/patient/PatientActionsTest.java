package com.ntu.hns.patient;

import static com.ntu.hns.TestManager.parseOutput;
import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;
import static com.ntu.hns.TestManager.stopApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PatientActionsTest {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outputStream));
  }

  @AfterEach
  public void tearDown() {
    stopApplication();
  }

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

    String[] output = parseOutput(outputStream);
    assertEquals("\n==================== Medical Record ====================", output[16]);
    assertEquals(
        "ID            : P0003\n"
            + "Name          : Patient 2\n"
            + "Date of Birth : 24/06/2000\n"
            + "Gender        : Male\n"
            + "Contact       : 98761231\n"
            + "Email         : testing3@email.com\n"
            + "Blood Type    : AB+\n"
            + "=======================================================",
        output[17]);
    assertEquals("\n================= Diagnoses and Treatments =============", output[18]);
  }

  @Test
  @DisplayName("Test Case 2: Update Personal Information")
  public void testUpdatePersonalInformation() {
    provideInput("P0003\n999\n2\n1\n99996666\n2\ntest@gmail.com\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 3: View Available Appointment Slots")
  public void testViewAvailableAppointmentSlots() {
    provideInput("P0001\n123\n3\n6\n2\n\n9\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 4: Schedule an Appointment")
  public void testScheduleAppointment() {
    provideInput("P0001\n123\n4\n6\n11/11/2024\n1\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 5: Reschedule an Appointment")
  public void testRescheduleAppointment() {
    provideInput("P0001\n123\n5\n1\n11/11/2024\n1\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 7: View Scheduled Appointments")
  public void testViewScheduledAppointments() {
    provideInput("P0001\n123\n7\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 8: View Past Appointment Outcome Records")
  public void testViewPastAppointmentOutcomeRecords() {
    provideInput("P0001\n123\n8\n1\n");
    startApplication();
  }
}
