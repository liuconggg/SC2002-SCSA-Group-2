package com.ntu.hns.patient;

import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PatientActionsTest {

    @Test
    @DisplayName("Test Case 1: View Medical Record")
    public void testPatientViewMedicalRecord() {
        provideInput("P0001\n123\n1\n");
        startApplication();
    }

    @Test
    @DisplayName("Test Case 2: Update Personal Information")
    public void testUpdatePersonalInformation() {
        provideInput("P0001\n123\n2\n2\ntest@gmail.com\n");
        startApplication();
    }

    @Test
    @DisplayName("Test Case 3: View Available Appointment Slots")
    public void testViewAvailableAppointmentSlots() {
        provideInput("P0001\n123\n3\n6\n");
        startApplication();
    }

    @Test
    @DisplayName("Test Case 7: View Scheduled Appointments")
    public void testViewScheduledAppointments() {
        provideInput("P0001\n123\n7\n");
        startApplication();
    }
}
