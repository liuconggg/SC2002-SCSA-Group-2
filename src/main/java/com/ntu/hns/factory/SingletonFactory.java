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

  public static ScannerWrapper getScannerWrapper() {
    if (scannerWrapper == null) {
      scannerWrapper = new ScannerWrapper(new Scanner(System.in));
    }
    return scannerWrapper;
  }

  public static Environment getEnvironment() {
    if (environment == null) {
      environment = DEV;
    }
    return environment;
  }

  public static DateTimeFormatter getDateTimeFormatter() {
    if (dateTimeFormatter == null) {
      dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    return dateTimeFormatter;
  }

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

  public static ContextManager getContextManager() {
    if (contextManager == null) {
      contextManager = contextManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return contextManager;
  }

  public static InventoryManager getInventoryManager() {
    if (inventoryManager == null) {
      inventoryManager = inventoryManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return inventoryManager;
  }

  public static MedicalRecordManager getMedicalRecordManager() {
    if (medicalRecordManager == null) {
      medicalRecordManager = medicalRecordManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return medicalRecordManager;
  }

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

  public static UserManager getUserManager() {
    if (userManager == null) {
      userManager = userManagerBuilder().setScanner(getScannerWrapper()).build();
    }
    return userManager;
  }
}
