package com.ntu.hms.util;

import java.util.Scanner;

/** Wrapper class for the java.util.Scanner providing an interface for reading input. */
public class ScannerWrapper {
  private final Scanner scanner;

  /**
   * Constructs a new ScannerWrapper that wraps the provided Scanner instance.
   *
   * @param scanner the Scanner instance to be wrapped
   */
  public ScannerWrapper(Scanner scanner) {
    this.scanner = scanner;
  }

  /**
   * Reads the next token from the input.
   *
   * @return the next token from the input
   */
  public String next() {
    return scanner.next();
  }

  /**
   * Reads the next line of text from the input.
   *
   * @return the next line of text as a String, or null if no line was found
   */
  public String nextLine() {
    return scanner.nextLine();
  }

  /**
   * Reads the next integer from the input and consumes the remaining line.
   *
   * @return the next integer from the input
   */
  public int nextInt() {
    int scannedInt = scanner.nextInt();
    scanner.nextLine();

    return scannedInt;
  }

  /**
   * Checks if the next token in the input can be interpreted as an integer.
   *
   * @return true if the next token can be interpreted as an integer, false otherwise
   */
  public boolean hasNextInt() {
    return scanner.hasNextInt();
  }

  /**
   * Closes the wrapped Scanner instance. This method should be called to release the resources
   * associated with the Scanner.
   */
  public void close() {
    scanner.close();
  }
}
