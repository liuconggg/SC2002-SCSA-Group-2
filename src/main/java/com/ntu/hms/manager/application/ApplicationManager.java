package com.ntu.hms.manager.application;

import static com.ntu.hms.enums.Environment.DEV;
import static com.ntu.hms.enums.Environment.PROD;
import static com.ntu.hms.enums.UserAction.EXIT;
import static com.ntu.hms.enums.UserAction.LOGIN;
import static com.ntu.hms.factory.SingletonFactory.destroySingletons;

import com.ntu.hms.AuthenticationService;
import com.ntu.hms.enums.Environment;
import com.ntu.hms.model.users.*;
import com.ntu.hms.util.ScannerWrapper;

/**
 * The ApplicationManager class is the core component that manages the application lifecycle
 * including user authentication, user context handling, menu interactions, and application exit.
 * It interacts with various services like AuthenticationService and ContextManager to ensure that
 * users can authenticate and perform their respective operations based on their roles.
 */
public class ApplicationManager {
  private final AuthenticationService authenticationService;
  private final ContextManager contextManager;
  private final Environment environment;
  private final ScannerWrapper scanner;

  /**
   * Constructs an instance of ApplicationManager.
   *
   * @param authenticationService the service used for user authentication
   * @param contextManager the manager for handling user contexts
   * @param environment the current environment (e.g., PROD or DEV)
   * @param scanner the wrapper for scanner input operations
   */
  private ApplicationManager(
      AuthenticationService authenticationService,
      ContextManager contextManager,
      Environment environment,
      ScannerWrapper scanner) {
    this.authenticationService = authenticationService;
    this.contextManager = contextManager;
    this.environment = environment;
    this.scanner = scanner;
  }

  /**
   * Starts the main loop of the application, prompting the user to either log in or exit.
   *
   * The method continuously asks the user for input to either log in or exit the application.
   * If the user chooses to log in, the handleLogin method is invoked to manage the login process.
   * If the user chooses to exit, the exit method is called, and the loop is terminated.
   */
  public void start() {
    while (true) {
      String userAction = loginOrExit();
      if (userAction.equalsIgnoreCase(LOGIN.name())) {
        handleLogin(); // Handle login and user context flow
      } else if (userAction.equalsIgnoreCase(EXIT.name())) {
        exit(); // Exit the program
        break; // This line ensures that if exit() doesn't terminate the program, the loop ends.
      }
    }
  }

  /**
   * Manages the login process for the application.
   *
   * This method is invoked as part of the main loop to handle user authentication.
   * It uses the `authenticationService` to authenticate the user. If authentication
   * succeeds, the user's context is managed via the `handleUserContext` method. If
   * authentication fails, a message is printed, and the method returns.
   */
  private void handleLogin() {
    // Load all CSV data
    User user = authenticationService.authenticate();

    if (user == null) {
      // Authentication failed, return from the method
      System.out.println("Authentication failed. Returning to main menu...");
      return;
    }

    // Authentication passed, handle user context
    handleUserContext(user);
  }

  /**
   * Prompts the user to enter either 'login' to proceed with login or 'exit' to terminate the application.
   *
   * This method displays the main menu message and reads an input line from the user through the scanner.
   * The user's input is then returned for further processing.
   *
   * @return the user's input, either 'login' or 'exit'
   */
  private String loginOrExit() {
    System.out.println("=== Hospital Management System ===");
    System.out.print("Enter 'login' to login or 'exit' to exit: ");
    String userAction = scanner.nextLine();
    System.out.println();

    return userAction;
  }

  /**
   * Handles the context for different types of users (Patient, Doctor, Pharmacist, Administrator).
   *
   * @param user the user whose context needs to be managed; the user can be an instance of Patient, Doctor, Pharmacist, or Administrator
   */
  private void handleUserContext(User user) {
    if (user instanceof Patient) {
      contextManager.beginPatient((Patient) user);
    } else if (user instanceof Doctor) {
      contextManager.beginDoctor((Doctor) user);
    } else if (user instanceof Pharmacist) {
      contextManager.beginPharmacist((Pharmacist) user);
    } else if (user instanceof Administrator) {
      contextManager.beginAdministrator((Administrator) user);
    }
  }

  /**
   * Terminates the application based on the current environment.
   *
   * In the production environment (PROD), it closes the scanner and exits the program.
   * In the development environment (DEV), it destroys singleton instances.
   */
  public void exit() {
    if (environment == PROD) {
      scanner.close();
      System.exit(0);
    } else if (environment == DEV) {
      destroySingletons();
    }
  }

  /**
   * Creates a new instance of ApplicationManagerBuilder.
   *
   * @return a new ApplicationManagerBuilder instance
   */
  public static ApplicationManagerBuilder applicationManagerBuilder() {
    return new ApplicationManagerBuilder();
  }

  public static class ApplicationManagerBuilder {
    private AuthenticationService authenticationService;
    private ContextManager contextManager;
    private Environment environment;
    private ScannerWrapper scanner;

    /**
     * Sets the AuthenticationService instance to be used by the ApplicationManagerBuilder.
     *
     * @param authenticationService the AuthenticationService instance to be set
     * @return the current instance of ApplicationManagerBuilder for method chaining
     */
    public ApplicationManagerBuilder setAuthenticationService(
        AuthenticationService authenticationService) {
      this.authenticationService = authenticationService;
      return this;
    }

    /**
     * Sets the ContextManager instance to be used by the ApplicationManagerBuilder.
     *
     * @param contextManager the ContextManager instance to be set
     * @return the current instance of ApplicationManagerBuilder for method chaining
     */
    public ApplicationManagerBuilder setContextManager(ContextManager contextManager) {
      this.contextManager = contextManager;
      return this;
    }

    /**
     * Sets the Environment instance to be used by the ApplicationManagerBuilder.
     *
     * @param environment the Environment instance to be set
     * @return the current instance of ApplicationManagerBuilder for method chaining
     */
    public ApplicationManagerBuilder setEnvironment(Environment environment) {
      this.environment = environment;
      return this;
    }

    /**
     * Sets the ScannerWrapper instance to be used by the ApplicationManagerBuilder.
     *
     * @param scanner the ScannerWrapper instance to be set
     * @return the current instance of ApplicationManagerBuilder for method chaining
     */
    public ApplicationManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this;
    }

    /**
     * Builds and returns an instance of ApplicationManager configured with the specified services,
     * context manager, environment, and scanner wrapped by this builder.
     *
     * @return a new instance of ApplicationManager with the configured components
     */
    // Method to build an ApplicationManager instance
    public ApplicationManager build() {
      return new ApplicationManager(authenticationService, contextManager, environment, scanner);
    }
  }
}
