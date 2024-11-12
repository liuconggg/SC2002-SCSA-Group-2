package com.ntu.hns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

  public static String[] parseOutput(ByteArrayOutputStream byteArrayOutputStream) {
    return byteArrayOutputStream.toString().split(System.lineSeparator());
  }

  public static ByteArrayOutputStream getOutputListener() {
    return new ByteArrayOutputStream();
  }

  public static void setSystemOutputConsumer(ByteArrayOutputStream byteArrayOutputStream) {
    System.setOut(new PrintStream(byteArrayOutputStream));
  }
}
