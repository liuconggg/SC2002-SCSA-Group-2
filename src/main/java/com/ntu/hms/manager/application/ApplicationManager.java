package com.ntu.hms.manager.application;

import static com.ntu.hms.enums.Environment.DEV;
import static com.ntu.hms.enums.Environment.PROD;
import static com.ntu.hms.enums.UserAction.EXIT;
import static com.ntu.hms.enums.UserAction.LOGIN;

import com.ntu.hms.AuthenticationService;
import com.ntu.hms.enums.Environment;
import com.ntu.hms.model.users.*;
import com.ntu.hms.util.ScannerWrapper;

public class ApplicationManager {
  private final AuthenticationService authenticationService;
  private final ContextManager contextManager;
  private final Environment environment;
  private final ScannerWrapper scanner;

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

  private String loginOrExit() {
    System.out.println("=== Hospital Management System ===");
    System.out.print("Enter 'login' to login or 'exit' to exit: ");
    String userAction = scanner.nextLine();
    System.out.println();

    return userAction;
  }

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

  public void exit() {
    if (environment == PROD) {
      scanner.close();
      System.exit(0);
    } else if (environment == DEV) {
      scanner.close();
    }
  }

  public static ApplicationManagerBuilder applicationManagerBuilder() {
    return new ApplicationManagerBuilder();
  }

  public static class ApplicationManagerBuilder {
    private AuthenticationService authenticationService;
    private ContextManager contextManager;
    private Environment environment;
    private ScannerWrapper scanner;

    public ApplicationManagerBuilder setAuthenticationService(
        AuthenticationService authenticationService) {
      this.authenticationService = authenticationService;
      return this;
    }

    public ApplicationManagerBuilder setContextManager(ContextManager contextManager) {
      this.contextManager = contextManager;
      return this;
    }

    public ApplicationManagerBuilder setEnvironment(Environment environment) {
      this.environment = environment;
      return this;
    }

    public ApplicationManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this;
    }

    // Method to build an ApplicationManager instance
    public ApplicationManager build() {
      return new ApplicationManager(authenticationService, contextManager, environment, scanner);
    }
  }
}
