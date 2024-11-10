package com.ntu.hns.admin;

import static com.ntu.hns.TestManager.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminActionsTest {

  @AfterEach()
  public void tearDown() {
    stopApplication();
  }

  @Test
  @DisplayName("Test Case 21: View Appointment Details")
  public void testViewAppointmentDetails() {
    provideInput("A0001\n123\n2\n");
    startApplication();
  }
}
