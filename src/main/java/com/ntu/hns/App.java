package com.ntu.hns;

import static com.ntu.hns.factory.SingletonFactory.getApplicationManager;

import com.ntu.hns.manager.application.ApplicationManager;

/**
 * The type App.
 */
public class App {
  /**
   * The constant sessionTimings.
   */
  public static final String[] sessionTimings = {
    "09:00 - 10:00",
    "10:00 - 11:00",
    "11:00 - 12:00",
    "12:00 - 13:00",
    "13:00 - 14:00",
    "14:00 - 15:00",
    "15:00 - 16:00",
    "16:00 - 17:00",
    "17:00 - 18:00"
  };

  private static ApplicationManager applicationManager;

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    applicationManager = getApplicationManager();
    applicationManager.start();
  }

  /**
   * Stop application.
   */
  public static void stopApplication() {
    applicationManager.exit();
  }
}
