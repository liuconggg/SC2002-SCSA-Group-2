package com.ntu.hms;

import com.ntu.hms.enums.Environment;
import com.ntu.hms.model.users.User;
import com.ntu.hms.util.ScannerWrapper;
import java.io.Console;
import java.util.List;

/**
 * The AuthenticationService class provides user authentication functionalities.
 * This service supports both production and development environments.
 * Users in production environment are authenticated via the console,
 * while in development environment, a scanner is used.
 */
public class AuthenticationService {
  private static final String DEFAULT_PASSWORD = "password";

  private final Console console;
  private final Environment environment;
  private final ScannerWrapper scanner;

  /**
   * Constructs a new AuthenticationService instance with the given console, environment,
   * and scanner.
   *
   * @param console the console to be used for input and output
   * @param environment the environment context (PROD or DEV) in which the service operates
   * @param scanner the scanner wrapper for input operations
   */
  private AuthenticationService(Console console, Environment environment, ScannerWrapper scanner) {
    this.console = console;
    this.environment = environment;
    this.scanner = scanner;
  }

  /**
   * Authenticates a user by prompting for their Hospital ID and password.
   * The method behavior changes based on the environment context: PROD or DEV.
   * If the user's password matches the default password, they will be prompted to change it.
   *
   * @return the authenticated User object if authentication succeeds; otherwise null
   */
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

  /**
   * Prompts the user to change their password and updates it if the new password and confirmation match.
   * The method behavior changes based on the environment context: PROD or DEV.
   *
   * @param user The User object whose password needs to be changed.
   * @param users A list of User objects, which will be saved to the database after the password change.
   */
  private void changePassword(User user, List<User> users) {
    System.out.println("=== Please change your password first! ===");
    if (environment == Environment.PROD) {
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
    } else if (environment == Environment.DEV) {
      while (true) {
        System.out.println("New Password: ");
        String newPassword = scanner.nextLine();
        System.out.println("Confirm New Password: ");
        String confirmPassword = scanner.nextLine();

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
  }

  /**
   * Provides a builder for constructing instances of AuthenticationService.
   *
   * @return a new instance of the AuthenticationServiceBuilder
   */
  public static AuthenticationServiceBuilder authenticationServiceBuilder() {
    return new AuthenticationServiceBuilder();
  }

  /**
   * Builder for constructing instances of AuthenticationService.
   */
  public static class AuthenticationServiceBuilder {
    private Console console;
    private Environment environment;
    private ScannerWrapper scanner;

    /**
     * Sets the Console instance to be used by the AuthenticationServiceBuilder.
     *
     * @param console the Console instance to be set
     * @return the current instance of AuthenticationServiceBuilder for method chaining
     */
    // Setter method for Console
    public AuthenticationServiceBuilder setConsole(Console console) {
      this.console = console;
      return this; // Return the builder for chaining
    }

    /**
     * Sets the Environment instance to be used by the AuthenticationServiceBuilder.
     *
     * @param environment the Environment instance to be set
     * @return the current instance of AuthenticationServiceBuilder for method chaining
     */
    // Setter method for Environment
    public AuthenticationServiceBuilder setEnvironment(Environment environment) {
      this.environment = environment;
      return this; // Return the builder for chaining
    }

    /**
     * Sets the ScannerWrapper instance to be used by the AuthenticationServiceBuilder.
     *
     * @param scanner the ScannerWrapper instance to be set
     * @return the current instance of AuthenticationServiceBuilder for method chaining
     */
    // Setter method for Scanner
    public AuthenticationServiceBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    /**
     * Builds and returns an instance of AuthenticationService.
     * Ensures that the required fields (Environment and Scanner) are not null.
     *
     * @return an instance of AuthenticationService
     * @throws IllegalArgumentException if Environment or Scanner are null
     */
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
