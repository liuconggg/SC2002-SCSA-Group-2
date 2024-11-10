package com.ntu.hns.doctor;

import static com.ntu.hns.TestManager.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DoctorActionsTest {

  @AfterEach()
  public void tearDown() {
    stopApplication();
  }

  @Test
  @DisplayName("Test Case 9: View Patient Medical Record")
  public void testViewPatientMedicalRecord() {
    provideInput("D0001\n123\n1\n1\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 10: Update Patient Medical Records")
  public void testUpdatePatientMedicalRecord() {
    provideInput("D0001\n123\n2\n1\n");
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
    provideInput("D0001\n123\n4\n07/11/2024\n1\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 13: Accept or Decline Appointment Requests")
  public void testAcceptOrDeclineAppointmentRequests() {
    provideInput("D0001\n123\n5\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 14: View Upcoming Appointments")
  public void testViewUpcomingAppointments() {
    provideInput("D0001\n123\n6\n");
    startApplication();
  }

  @Test
  @DisplayName("Test Case 15: Record Appointment Outcome")
  public void testRecordAppointmentOutcome() {
    provideInput("D0001\n123\n7\n");
    startApplication();
  }
}
