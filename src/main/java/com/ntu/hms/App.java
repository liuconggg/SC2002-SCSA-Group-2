package com.ntu.hms;

import static com.ntu.hms.factory.SingletonFactory.getApplicationManager;

import com.ntu.hms.manager.application.ApplicationManager;

/** The App class serves as the main entry point for the hospital management system application. */
public class App {
  /**
   * Defines the time slots for sessions during the hospital's operational hours. Each element in
   * the array represents a one-hour session, starting from 09:00 and ending at 17:00.
   */
  public static final String[] sessionTimings = {
    "09:00 - 10:00",
    "10:00 - 11:00",
    "11:00 - 12:00",
    "12:00 - 13:00",
    "13:00 - 14:00",
    "14:00 - 15:00",
    "15:00 - 16:00",
    "16:00 - 17:00"
  };

  /**
   * Singleton instance of ApplicationManager responsible for managing the overall application
   * lifecycle.
   */
  private static ApplicationManager applicationManager;

  /**
   * The main method serves as the entry point for starting the hospital management system
   * application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    applicationManager = getApplicationManager();
    applicationManager.start();
  }

  /**
   * Terminates the application by invoking the exit method of the ApplicationManager.
   *
   * <p>This method ensures that all application resources are properly disposed of and the runtime
   * environment is appropriately shut down. Depending on the environment (PROD or DEV), the
   * application will either close all open scanners and exit, or simply destroy singleton
   * instances.
   */
  public static void stopApplication() {
    applicationManager.exit();
  }
}
