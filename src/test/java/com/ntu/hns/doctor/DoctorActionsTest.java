package com.ntu.hns.doctor;

import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DoctorActionsTest {

  @Test
  @DisplayName("Test Case 9: View Patient Medical Record")
  public void testViewPatientMedicalRecord() {
    provideInput("D0001\n123\n1\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 11: View Personal Schedule")
  public void testViewPersonalSchedule() {
    provideInput("D0001\n123\n3\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 12: Set Availability for Appointments")
  public void testSetAvailablity() {
    provideInput("D0001\n123\n4\n07/11/2024\n");
    startApplication();
  }
}
