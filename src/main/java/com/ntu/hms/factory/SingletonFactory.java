package com.ntu.hms.factory;

import static com.ntu.hms.AuthenticationService.authenticationServiceBuilder;
import static com.ntu.hms.enums.Environment.DEV;
import static com.ntu.hms.manager.application.ApplicationManager.applicationManagerBuilder;
import static com.ntu.hms.manager.application.ContextManager.contextManagerBuilder;
import static com.ntu.hms.manager.appointment.AppointmentManager.appointmentManagerBuilder;
import static com.ntu.hms.manager.inventory.InventoryManager.inventoryManagerBuilder;
import static com.ntu.hms.manager.medicalrecord.MedicalRecordManager.medicalRecordManagerBuilder;
import static com.ntu.hms.manager.schedule.ScheduleManager.scheduleManagerBuilder;
import static com.ntu.hms.manager.user.UserManager.userManagerBuilder;

import com.ntu.hms.AuthenticationService;
import com.ntu.hms.enums.Environment;
import com.ntu.hms.manager.application.ApplicationManager;
import com.ntu.hms.manager.application.ContextManager;
import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.inventory.InventoryManager;
import com.ntu.hms.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hms.manager.schedule.ScheduleManager;
import com.ntu.hms.manager.user.UserManager;
import com.ntu.hms.util.ScannerWrapper;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The SingletonFactory class is responsible for providing singleton instances of various managers
 * and services used throughout the application. This class ensures that only one instance of each
 * component is created and shared.
 */
public class SingletonFactory {
  private static Environment environment;
  private static DateTimeFormatter dateTimeFormatter;
  private static ScannerWrapper scannerWrapper;
  private static AuthenticationService authenticationService;
  private static ApplicationManager applicationManager;
  private static AppointmentManager appointmentManager;
  private static ContextManager contextManager;
  private static InventoryManager inventoryManager;
  private static MedicalRecordManager medicalRecordManager;
  private static ScheduleManager scheduleManager;
  private static UserManager userManager;

  /**
   * Returns a singleton instance of ScannerWrapper. If the instance does not already exist, it
   * initializes a new ScannerWrapper with a Scanner that reads from the standard input.
   *
   * @return a singleton ScannerWrapper instance
   */
  public static ScannerWrapper getScannerWrapper() {
    if (scannerWrapper == null) {
      scannerWrapper = new ScannerWrapper(new Scanner(System.in));
    }
    return scannerWrapper;
  }

  /**
   * Returns the current environment setting. If the environment has not been explicitly set, it
   * defaults to the DEV environment.
   *
   * @return the current environment setting
   */
  public static Environment getEnvironment() {
    if (environment == null) {
      environment = DEV;
    }
    return environment;
  }

  /**
   * Returns a singleton instance of DateTimeFormatter. If the instance does not already exist, it
   * initializes a new DateTimeFormatter with the pattern "dd/MM/yyyy".
   *
   * @return a singleton DateTimeFormatter instance
   */
  public static DateTimeFormatter getDateTimeFormatter() {
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    return dateTimeFormatter;
  }

  /**
   * Returns a singleton instance of AuthenticationService. If the instance does not already exist,
   * it initializes a new AuthenticationService with a console, environment, and scanner.
   *
   * @return a singleton AuthenticationService instance
   */
  public static AuthenticationService getAuthenticationService() {
    if (authenticationService == null) {
      authenticationService =
          authenticationServiceBuilder()
              .setConsole(System.console())
              .setEnvironment(getEnvironment())
              .setScanner(getScannerWrapper())
              .build();
    }
    return authenticationService;
  }

  /**
   * Returns a singleton instance of ApplicationManager. If the instance does not already exist, it
   * initializes a new ApplicationManager by setting the necessary services and context.
   *
   * @return a singleton ApplicationManager instance
   */
  public static ApplicationManager getApplicationManager() {
    if (applicationManager == null) {
      applicationManager =
          applicationManagerBuilder()
              .setAuthenticationService(getAuthenticationService())
              .setContextManager(getContextManager())
              .setEnvironment(getEnvironment())
              .setScanner(getScannerWrapper())
              .build();
    }
    return applicationManager;
  }

  /**
   * Returns a singleton instance of AppointmentManager. If the instance does not already exist, it
   * initializes a new AppointmentManager using the builder pattern, setting up necessary
   * components.
   *
   * @return a singleton AppointmentManager instance
   */
  public static AppointmentManager getAppointmentManager() {
    if (appointmentManager == null) {
      appointmentManager =
          appointmentManagerBuilder()
              .setDateTimeFormatter(getDateTimeFormatter())
              .setScanner(getScannerWrapper())
              .build();
    }
    return appointmentManager;
  }

  /**
   * Returns a singleton instance of ContextManager. If the instance does not already exist, it
   * initializes a new ContextManager using the context manager builder pattern.
   *
   * @return a singleton ContextManager instance
   */
  public static ContextManager getContextManager() {
    if (contextManager == null) {
      contextManager = contextManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return contextManager;
  }

  /**
   * Returns a singleton instance of InventoryManager. If the instance does not already exist, it
   * initializes a new InventoryManager using the InventoryManagerBuilder, setting the scanner
   * obtained from getScannerWrapper.
   *
   * @return a singleton InventoryManager instance
   */
  public static InventoryManager getInventoryManager() {
    if (inventoryManager == null) {
      inventoryManager = inventoryManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return inventoryManager;
  }

  /**
   * Returns a singleton instance of MedicalRecordManager. If the instance does not already exist,
   * it initializes a new MedicalRecordManager using the MedicalRecordManagerBuilder, setting the
   * scanner obtained from getScannerWrapper().
   *
   * @return a singleton MedicalRecordManager instance
   */
  public static MedicalRecordManager getMedicalRecordManager() {
    if (medicalRecordManager == null) {
      medicalRecordManager = medicalRecordManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return medicalRecordManager;
  }

  /**
   * Returns a singleton instance of ScheduleManager. If the instance does not already exist, it
   * initializes a new ScheduleManager using the builder pattern, setting the necessary
   * DateTimeFormatter and ScannerWrapper.
   *
   * @return a singleton ScheduleManager instance
   */
  public static ScheduleManager getScheduleManager() {
    if (scheduleManager == null) {
      scheduleManager =
          scheduleManagerBuilder()
              .setDateTimeFormatter(getDateTimeFormatter())
              .setScanner(getScannerWrapper())
              .build();
    }
    return scheduleManager;
  }

  /**
   * Returns a singleton instance of UserManager. If the instance does not already exist, it
   * initializes a new UserManager using the UserManagerBuilder and sets the Scanner obtained from
   * getScannerWrapper.
   *
   * @return a singleton UserManager instance
   */
  public static UserManager getUserManager() {
    if (userManager == null) {
      userManager = userManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return userManager;
  }

  /**
   * Resets all singleton instances managed by the SingletonFactory to null. This method sets each
   * singleton instance field in the SingletonFactory to null, effectively destroying the current
   * instances and forcing reinitialization when they are next requested.
   *
   * <p>This can be useful in scenarios where it is necessary to reset the state of the application,
   * particularly in development or testing environments where objects need to be reinitialized.
   */
  public static void destroySingletons() {
    environment = null;
    dateTimeFormatter = null;
    scannerWrapper = null;
    authenticationService = null;
    applicationManager = null;
    appointmentManager = null;
    contextManager = null;
    inventoryManager = null;
    medicalRecordManager = null;
    scheduleManager = null;
    userManager = null;
  }
}
