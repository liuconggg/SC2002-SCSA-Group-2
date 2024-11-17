package com.ntu.hms;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Test manager. This class provides utility methods for managing tests, such as starting
 * and stopping the application, refreshing CSV files, and capturing or providing system
 * input/output.
 */
public class TestManager {
  private static final Path TARGET_FOLDER_PATH = Paths.get("target/classes/csvdb");
  private static final Path SOURCE_FOLDER_PATH = Paths.get("src/main/resources/csvdb");

  /** Start the application. Invokes the main method of the App class to start the application. */
  public static void startApplication() {
    App.main(new String[] {});
  }

  /**
   * Stop the application. Invokes the stopApplication method of the App class to stop the
   * application.
   */
  public static void stopApplication() {
    App.stopApplication();
  }

  /**
   * Refresh CSV files. Deletes existing CSV files in the target folder and copies new CSV files
   * from the source folder.
   */
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

  /**
   * Provide input to the system. Sets the System input stream to the provided string.
   *
   * @param input the input string to be provided.
   */
  public static void provideInput(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));
  }

  /**
   * Parse the output of the system. Splits the output captured in the ByteArrayOutputStream by the
   * system line separator.
   *
   * @param byteArrayOutputStream the output stream from which the output is parsed.
   * @return an array of strings, where each string is a line of the output.
   */
  public static String[] parseOutput(ByteArrayOutputStream byteArrayOutputStream) {
    return byteArrayOutputStream.toString().split(System.lineSeparator());
  }

  /**
   * Get an output listener. Creates and returns a new ByteArrayOutputStream to capture system
   * output.
   *
   * @return a ByteArrayOutputStream for capturing system output.
   */
  public static ByteArrayOutputStream getOutputListener() {
    return new ByteArrayOutputStream();
  }

  /**
   * Set the system output consumer. Sets the system output stream to the provided
   * ByteArrayOutputStream.
   *
   * @param byteArrayOutputStream the output stream to be set as the system output stream.
   */
  public static void setSystemOutputConsumer(ByteArrayOutputStream byteArrayOutputStream) {
    System.setOut(new PrintStream(byteArrayOutputStream));
  }
}
