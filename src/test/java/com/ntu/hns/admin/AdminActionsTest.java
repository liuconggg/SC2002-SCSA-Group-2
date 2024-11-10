package com.ntu.hns.admin;

import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminActionsTest {

  @Test
  @DisplayName("Test Case 21: View Appointment Details")
  public void testViewAppointmentDetails() {
    provideInput("A0001\n123\n2\n");
    startApplication();
  }
}
