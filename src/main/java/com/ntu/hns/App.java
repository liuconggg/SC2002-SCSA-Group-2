package com.ntu.hns;

import com.ntu.hns.manager.application.ApplicationManager;
import com.ntu.hns.spring.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
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

  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    applicationManager = context.getBean(ApplicationManager.class);
    applicationManager.start();
  }

  public static void stopApplication() {
    applicationManager.exit();
  }
}
