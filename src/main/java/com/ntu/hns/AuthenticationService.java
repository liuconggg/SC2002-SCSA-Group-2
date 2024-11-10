package com.ntu.hns;

import com.ntu.hns.enums.Environment;
import com.ntu.hns.model.users.User;
import java.io.Console;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private static final String DEFAULT_PASSWORD = "password";

  private final Console console;
  private final CsvDB csvDB;
  private final Environment environment;
  private final Scanner scanner;

  @Autowired
  public AuthenticationService(
      @Nullable Console console, CsvDB csvDB, Environment environment, Scanner scanner) {
    this.console = console;
    this.csvDB = csvDB;
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

      List<User> users = csvDB.readUsers();
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

      List<User> users = csvDB.readUsers();
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
        csvDB.saveUsers(users);
        System.out.println("Password changed successfully!");
        break;
      } else {
        System.out.println("Passwords do not match. Please try again.\n");
      }
    }
  }
}
