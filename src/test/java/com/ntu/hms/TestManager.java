package com.ntu.hms;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestManager {
  private static final Path TARGET_FOLDER_PATH = Paths.get("target/classes/csvdb");
  private static final Path SOURCE_FOLDER_PATH = Paths.get("src/main/resources/csvdb");
  public static final InputStream originalSystemIn = System.in;

  public static void startApplication() {
    App.main(new String[] {});
  }

  public static void stopApplication() {
    App.stopApplication();
  }

  public static void refreshCsvFiles() {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(TARGET_FOLDER_PATH, "*.csv")) {
      stream.forEach(
          file -> {
            try {
              Files.deleteIfExists(file);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(SOURCE_FOLDER_PATH, "*.csv")) {
      for (Path file : stream) {
        Path targetFile = TARGET_FOLDER_PATH.resolve(file.getFileName());
        Files.copy(file, targetFile, REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void provideInput(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    System.out.println(System.in);
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
