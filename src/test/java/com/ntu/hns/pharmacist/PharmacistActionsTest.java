package com.ntu.hns.pharmacist;

import static com.ntu.hns.TestManager.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PharmacistActionsTest {

  @AfterEach()
  public void tearDown() {
    stopApplication();
  }

  /**
   * Test Case 16: View Appointment Outcome Record
   *
   * <ul>
   *   <li>Pharmacist views appointment outcome records to process prescriptions.
   *   <li>Verify that the system displays the appointment outcome details, including prescribed
   *       medications.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 16: View Appointment Outcome Record")
  public void testViewAppointmentOutcomeRecord() {
    provideInput("PH0001\n123\n1\n");
    startApplication();
  }

  /**
   * Test Case 17: Update Prescription Status
   *
   * <ul>
   *   <li>Pharmacist updates the status of a prescription to "dispensed."
   *   <li>Verify that the prescription status is updated, and the change is reflected in the
   *       patient's records.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 17: Update Prescription Status")
  public void testUpdatePrescriptionStatus() {
    provideInput("PH0001\n123\n2\n1\n\n5\nP0001\n123\n");
    startApplication();
  }

  /**
   * Test Case 18: View Medication Inventory
   *
   * <ul>
   *   <li>Pharmacist views the current medication inventory.
   *   <li>Verify that the system displays a list of medications, including stock levels.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 18: View Medication Inventory")
  public void testViewMedicationInventory() {
    provideInput("PH0001\n123\n3\n\n");
    startApplication();
  }

  /**
   * Test Case 19: Submit Replenishment Request
   *
   * <ul>
   *   <li>Pharmacist submits a replenishment request for low-stock medications.
   *   <li>Verify that the replenishment request is submitted successfully, pending approval from
   *       the administrator.
   * </ul>
   */
  @Test
  @DisplayName("Test Case 19: Submit Replenishment Request")
  public void testSubmitReplenishmentRequest() {
    provideInput("PH0001\n123\n4\ny\n");
    startApplication();
  }
}
