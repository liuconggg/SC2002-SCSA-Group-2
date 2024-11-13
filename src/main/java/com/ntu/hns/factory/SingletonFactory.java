package com.ntu.hns.factory;

import static com.ntu.hns.AuthenticationService.authenticationServiceBuilder;
import static com.ntu.hns.enums.Environment.DEV;
import static com.ntu.hns.manager.application.ApplicationManager.applicationManagerBuilder;
import static com.ntu.hns.manager.application.ContextManager.contextManagerBuilder;
import static com.ntu.hns.manager.appointment.AppointmentManager.appointmentManagerBuilder;
import static com.ntu.hns.manager.inventory.InventoryManager.inventoryManagerBuilder;
import static com.ntu.hns.manager.medicalrecord.MedicalRecordManager.medicalRecordManagerBuilder;
import static com.ntu.hns.manager.schedule.ScheduleManager.scheduleManagerBuilder;
import static com.ntu.hns.manager.user.UserManager.userManagerBuilder;

import com.ntu.hns.AuthenticationService;
import com.ntu.hns.enums.Environment;
import com.ntu.hns.manager.application.ApplicationManager;
import com.ntu.hns.manager.application.ContextManager;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.inventory.InventoryManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;
import com.ntu.hns.manager.user.UserManager;
import com.ntu.hns.util.ScannerWrapper;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/** The type Singleton factory. */
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
   * Gets scanner wrapper.
   *
   * @return the scanner wrapper
   */
  public static ScannerWrapper getScannerWrapper() {
    if (scannerWrapper == null) {
      scannerWrapper = new ScannerWrapper(new Scanner(System.in));
    }
    return scannerWrapper;
  }

  /**
   * Gets environment.
   *
   * @return the environment
   */
  public static Environment getEnvironment() {
    if (environment == null) {
      environment = DEV;
    }
    return environment;
  }

  /**
   * Gets date time formatter.
   *
   * @return a singleton date time formatter
   */
  public static DateTimeFormatter getDateTimeFormatter() {
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    return dateTimeFormatter;
  }

  /**
   * Gets authentication service.
   *
   * @return a singleton authentication service
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
   * Gets application manager.
   *
   * @return a singleton application manager
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
   * Gets appointment manager.
   *
   * @return a singleton appointment manager
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
   * Gets context manager.
   *
   * @return a singleton context manager
   */
  public static ContextManager getContextManager() {
    if (contextManager == null) {
      contextManager = contextManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return contextManager;
  }

  /**
   * Gets inventory manager.
   *
   * @return a singleton inventory manager
   */
  public static InventoryManager getInventoryManager() {
    if (inventoryManager == null) {
      inventoryManager = inventoryManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return inventoryManager;
  }

  /**
   * Gets medical record manager.
   *
   * @return a singleton medical record manager
   */
  public static MedicalRecordManager getMedicalRecordManager() {
    if (medicalRecordManager == null) {
      medicalRecordManager = medicalRecordManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return medicalRecordManager;
  }

  /**
   * Gets schedule manager.
   *
   * @return a singleton schedule manager
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
   * Gets user manager.
   *
   * @return a singleton user manager
   */
  public static UserManager getUserManager() {
    if (userManager == null) {
      userManager = userManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return userManager;
  }
}
