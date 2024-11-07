package com.ntu.hns;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ntu.hns.TestManager.provideInput;

public class AppTest {
    private App app;

    @BeforeEach
    public void setUp() {
        app = new App();
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testPatientViewMedicalRecord() {
        provideInput("P0001\n123\n1\n\n");
        app.main(new String[] {});
    }
}
