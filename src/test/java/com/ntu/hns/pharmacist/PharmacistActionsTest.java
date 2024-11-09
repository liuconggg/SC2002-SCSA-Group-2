package com.ntu.hns.pharmacist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ntu.hns.TestManager.provideInput;
import static com.ntu.hns.TestManager.startApplication;

public class PharmacistActionsTest {

    @Test
    @DisplayName("Test Case 16: View Appointment Outcome Record")
    public void testViewAppointmentOutcomeRecord() {
        provideInput("PH0001\n123\n1\n");
        startApplication();
    }
}