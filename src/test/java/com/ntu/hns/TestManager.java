package com.ntu.hns;

import java.io.ByteArrayInputStream;

public class TestManager {

  public static void startApplication() {
    App.main(new String[] {});
  }

  public static void stopApplication() {
    App.stopApplication();
  }

  public static void provideInput(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));
  }
}
