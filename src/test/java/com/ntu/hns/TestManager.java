package com.ntu.hns;

import java.io.ByteArrayInputStream;

public class TestManager {

    public static void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}
