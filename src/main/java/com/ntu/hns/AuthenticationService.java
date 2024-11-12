package com.ntu.hns;

import com.ntu.hns.enums.Environment;
import com.ntu.hns.model.users.User;
import com.ntu.hns.util.ScannerWrapper;
import java.io.Console;
import java.util.List;

public class AuthenticationService {
  private static final String DEFAULT_PASSWORD = "password";

  private final Console console;
  private final Environment environment;
  private final ScannerWrapper scanner;

  private AuthenticationService(Console console, Environment environment, ScannerWrapper scanner) {
    this.console = console;
    this.environment = environment;
    this.scanner = scanner;
  }

  public User authenticate() {
    if (environment == Environment.PROD) {
      if (console == null) {
        System.out.println("No console available. Please run in a terminal. ");
        return null;
      }
      System.out.println("=== Hospital Management System ===");

      String id = console.readLine("Hospital ID: ");
      char[] passwordArray = console.readPassword("Password: ");

      String password = new String(passwordArray);

      List<User> users = CsvDB.readUsers();
      for (User user : users) {
        if (user.getHospitalID().equals(id) && user.getPassword().equals(password)) {
          if (user.getPassword().equals(DEFAULT_PASSWORD)) {
            changePassword(user, users);
          }
          return user;
        }
      }

      System.out.println("Login failed! Please try again!\n");
    } else if (environment == Environment.DEV) {
      System.out.println("=== Hospital Management System ===");
      System.out.println("Hospital ID: ");
      String id = scanner.nextLine();
      System.out.println("Password: ");
      String password = scanner.nextLine();

      List<User> users = CsvDB.readUsers();
      for (User user : users) {
        if (user.getHospitalID().equals(id) && user.getPassword().equals(password)) {
          if (user.getPassword().equals(DEFAULT_PASSWORD)) {
            changePassword(user, users);
          }
          return user;
        }
      }
      System.out.println("Login failed! Please try again!\n");
    }

    return null;
  }

  private void changePassword(User user, List<User> users) {
    System.out.println("=== Please change your password first! ===");
    while (true) {
      char[] newPasswordArray = console.readPassword("New Password: ");
      String newPassword = new String(newPasswordArray);
      char[] confirmPasswordArray = console.readPassword("Confirm New Password: ");
      String confirmPassword = new String(confirmPasswordArray);

      if (newPassword.equals(confirmPassword)) {
        user.setPassword(newPassword);
        CsvDB.saveUsers(users);
        System.out.println("Password changed successfully!");
        break;
      } else {
        System.out.println("Passwords do not match. Please try again.\n");
      }
    }
  }

  public static AuthenticationServiceBuilder authenticationServiceBuilder() {
    return new AuthenticationServiceBuilder();
  }

  public static class AuthenticationServiceBuilder {
    private Console console;
    private Environment environment;
    private ScannerWrapper scanner;

    // Setter method for Console
    public AuthenticationServiceBuilder setConsole(Console console) {
      this.console = console;
      return this; // Return the builder for chaining
    }

    // Setter method for Environment
    public AuthenticationServiceBuilder setEnvironment(Environment environment) {
      this.environment = environment;
      return this; // Return the builder for chaining
    }

    // Setter method for Scanner
    public AuthenticationServiceBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    // Method to build an AuthenticationService instance
    public AuthenticationService build() {
      // Validation can be added here to ensure non-null fields if necessary
      if (environment == null || scanner == null) {
        throw new IllegalArgumentException(
            "CsvDB, Environment, and Scanner are required fields and must not be null.");
      }
      return new AuthenticationService(console, environment, scanner);
    }
  }
}
