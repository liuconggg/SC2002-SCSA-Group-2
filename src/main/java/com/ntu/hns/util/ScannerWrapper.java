package com.ntu.hns.util;

import java.util.Scanner;

public class ScannerWrapper {
  private final Scanner scanner;

  public ScannerWrapper(Scanner scanner) {
    this.scanner = scanner;
  }

  public String next() {
    return scanner.next();
  }

  public String nextLine() {
    return scanner.nextLine();
  }

  public int nextInt() {
    int scannedInt = scanner.nextInt();
    scanner.nextLine();

    return scannedInt;
  }

  public boolean hasNextInt() {
    return scanner.hasNextInt();
  }

  public void close() {
    scanner.close();
  }
}
