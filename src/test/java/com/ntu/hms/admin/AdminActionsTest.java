package com.ntu.hms.admin;

import static com.ntu.hms.TestManager.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The type Admin actions test.
 * This class contains test cases for various actions that can be performed by an admin,
 * including viewing and managing hospital staff, viewing appointment details,
 * managing inventory, and approving replenishment requests.
 */
public class AdminActionsTest {

  /**
   * Test view and manage hospital staff.
   * This test case simulates the admin viewing and managing hospital staff details.
   */
  @Test
  @DisplayName("Test Case 20: View and Manage Hospital Staff")
  public void testViewAndManageHospitalStaff() {
    provideInput("login\nA0001\n123\n1\n2\n3\n2\n1\n100\n\n5\nexit\n");
    startApplication();
  }

  /**
   * Test view appointment details.
   * This test case simulates the admin viewing the details of appointments.
   */
  @Test
  @DisplayName("Test Case 21: View Appointment Details")
  public void testViewAppointmentDetails() {
    provideInput("login\nA0001\n123\n2\n1\n2\n2\n2\n3\n2\n4\n2\n5\n5\nexit\n");
    startApplication();
  }

  /**
   * Test view and manage inventory.
   * This test case simulates the admin viewing and managing the medication inventory.
   */
  @Test
  @DisplayName("Test Case 22: View and Manage Medication Inventory")
  public void testViewAndManageInventory() {
    provideInput(
            "login\nA0001\n123\n3\n2\n\n3\n1\nCodeine\n50\n\n3\n3\nCodeine\n10\n\n3\n4\nCodeine\n\n5\nexit\n");
    startApplication();
  }

  /**
   * Test approve replenishment requests.
   * This test case simulates the admin approving replenishment requests.
   */
  @Test
  @DisplayName("Test Case 23: Approve Replenishment Requests")
  public void testApproveReplenishmentRequests() {
    provideInput("login\nA0001\n123\n4\nR0002\nA\n5\nexit\n");
    startApplication();
  }
}