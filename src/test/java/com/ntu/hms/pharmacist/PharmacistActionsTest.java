package com.ntu.hms.pharmacist;

import static com.ntu.hms.TestManager.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The type Pharmacist actions test.
 * This class contains test cases for various actions that can be performed by a pharmacist,
 * including viewing appointment outcome records, updating prescription statuses,
 * managing medication inventory, and submitting replenishment requests.
 */
public class PharmacistActionsTest {

  /**
   * Test Case 16: View Appointment Outcome Record.
   * Pharmacist views appointment outcome records to process prescriptions.
   * Verify that the system displays the appointment outcome details, including prescribed medications.
   */
  @Test
  @DisplayName("Test Case 16: View Appointment Outcome Record")
  public void testViewAppointmentOutcomeRecord() {
    provideInput("login\nPH0001\n123\n1\n\n5\nexit\n");
    startApplication();
  }

  /**
   * Test Case 17: Update Prescription Status.
   * Pharmacist updates the status of a prescription to "dispensed."
   * Verify that the prescription status is updated, and the change is reflected in the patient's records.
   */
  @Test
  @DisplayName("Test Case 17: Update Prescription Status")
  public void testUpdatePrescriptionStatus() {
    provideInput("login\nPH0001\n123\n2\n1\n\n5\nlogin\nP0001\n123\n8\n2\n\n9\nexit\n");
    startApplication();
  }

  /**
   * Test Case 18: View Medication Inventory.
   * Pharmacist views the current medication inventory.
   * Verify that the system displays a list of medications, including stock levels.
   */
  @Test
  @DisplayName("Test Case 18: View Medication Inventory")
  public void testViewMedicationInventory() {
    provideInput("login\nPH0001\n123\n3\n\n5\nexit\n");
    startApplication();
  }

  /**
   * Test Case 19: Submit Replenishment Request.
   * Pharmacist submits a replenishment request for low-stock medications.
   * Verify that the replenishment request is submitted successfully, pending approval from the administrator.
   */
  @Test
  @DisplayName("Test Case 19: Submit Replenishment Request")
  public void testSubmitReplenishmentRequest() {
    provideInput("login\nPH0001\n123\n4\ny\n\n5\nexit\n");
    startApplication();
  }
}