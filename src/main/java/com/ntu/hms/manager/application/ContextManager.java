package com.ntu.hms.manager.application;

import com.ntu.hms.model.users.*;
import com.ntu.hms.util.ScannerWrapper;
import java.util.InputMismatchException;

/**
 * ContextManager is a class that handles the main workflow for different types of users in a medical system.
 * It provides methods to manage sessions for Patients, Doctors, Pharmacists, and Administrators.
 *
 * It implements the ContextManagerInterface which defines the methods for user session management.
 */
public class ContextManager implements ContextManagerInterface {
  private final ScannerWrapper scanner;

  /**
   * Constructs a new ContextManager with the provided ScannerWrapper instance.
   *
   * @param scanner the ScannerWrapper instance to be used for input operations
   */
  private ContextManager(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  /**
   * Creates and returns a new instance of ContextManagerBuilder.
   *
   * @return a new ContextManagerBuilder instance
   */
  public static ContextManagerBuilder contextManagerBuilder() {
    return new ContextManagerBuilder();
  }

  /**
   * Begins interaction with the patient, providing a menu for various actions
   * including viewing and updating medical records, managing appointments,
   * and viewing schedules.
   *
   * @param patient the Patient object representing the currently logged-in patient
   */
  // Patient actions
  @Override
  public void beginPatient(Patient patient) {
    while (true) {
      try {
        patient.displayMenu();
        int choice = scanner.nextInt();
        System.out.println();

        switch (choice) {
          case 1:
            patient.viewMedicalRecord();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 2:
            patient.updatePersonalInformation();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 3:
            patient.viewWeeklySchedule();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 4:
            patient.scheduleAppointment();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 5:
            patient.rescheduleAppointment();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 6:
            patient.cancelAppointment();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 7:
            patient.viewScheduledAppointments();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 8:
            patient.viewAppointmentOutcome();
            System.out.println();
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            break;
          case 9:
            System.out.println("You have logged out ");
            System.out.println();
            return;
          default:
            System.out.println("Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
      }
    }
  }

  /**
   * Facilitates the interaction for doctor users by presenting a menu of actions such as
   * viewing and updating medical records, managing schedules, and handling appointments.
   * This method includes a loop that continually displays the menu until the doctor chooses
   * to log out.
   *
   * @param doctor the Doctor object representing the currently logged-in doctor
   */
  // Doctor actions
  @Override
  public void beginDoctor(Doctor doctor) {
    while (true) {
      try {
        doctor.displayMenu();
        int choice = scanner.nextInt();
        System.out.println();

        switch (choice) {
          case 1:
            doctor.viewMedicalRecord();
            break;
          case 2:
            doctor.updateMedicalRecord();
            break;
          case 3:
            doctor.viewWeeklySchedule();
            break;
          case 4:
            doctor.setAvailability();
            break;
          case 5:
            doctor.updateSchedule();
            break;
          case 6:
            doctor.viewUpcomingAppointments();
            break;
          case 7:
            doctor.updateAppointmentOutcome();
            break;
          case 8:
            System.out.println("You have logged out ");
            System.out.println();
            return;
          default:
            System.out.println("Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
      }
    }
  }

  /**
   * Facilitates the interaction for pharmacist users by providing a menu
   * of actions such as viewing appointment outcomes, prescribing medications,
   * viewing inventory, and submitting replenishment requests. This method
   * runs in a continuous loop until the pharmacist chooses to log out.
   *
   * @param pharmacist the Pharmacist object representing the currently logged-in pharmacist
   */
  // Pharmacist actions
  @Override
  public void beginPharmacist(Pharmacist pharmacist) {
    while (true) {
      try {
        pharmacist.displayMenu();
        int choice = scanner.nextInt();
        System.out.println();

        switch (choice) {
          case 1:
            pharmacist.viewAppointmentOutcome();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 2:
            pharmacist.prescribeAndUpdate();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 3:
            pharmacist.viewInventory();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 4:
            pharmacist.submitReplenishmentRequest();
            System.out.println();
            System.out.println("\nPress Enter to continue");
            scanner.nextLine();
            break;
          case 5:
            System.out.println("You have logged out ");
            System.out.println();
            return;
          default:
            System.out.println("Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
      }
    }
  }

  /**
   * Facilitates the interaction for administrator users by presenting
   * a menu of actions such as managing staff, viewing appointments,
   * handling inventory, and managing replenishment requests. This method
   * includes a loop that continually displays the menu until the administrator
   * chooses to log out.
   *
   * @param administrator the Administrator object representing the currently logged-in administrator
   */
  // All Administrator functions and logic
  @Override
  public void beginAdministrator(Administrator administrator) {
    while (true) {
      try {
        administrator.displayMenu();
        int choice = scanner.nextInt();
        System.out.println();

        switch (choice) {
          case 1:
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Add Staff");
            System.out.println("2. View Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Delete Staff");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int staffChoice = scanner.nextInt();
            System.out.println();

            switch (staffChoice) {
              case 1:
                administrator.addUser();
                break;
              case 2:
                administrator.viewUsers();
                break;
              case 3:
                administrator.updateUser();
                break;
              case 4:
                administrator.deleteUser();
                break;
              case 5:
                break;
              default:
                System.out.println("Invalid choice. Returning to main menu...");
                break;
            }

            System.out.println();
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            break;
          case 2:
            administrator.viewAppointment();
            break;
          case 3:
            System.out.println("What would you like to do?");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Inventory Item");
            System.out.println("4. Delete Inventory Item");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int subChoice = scanner.nextInt();

            switch (subChoice) {
              case 1:
                administrator.addInventory();
                break;
              case 2:
                administrator.viewInventory();
                break;
              case 3:
                administrator.updateInventory();
                break;
              case 4:
                administrator.deleteInventory();
                break;
              case 5:
                break;
              default:
                System.out.println("Invalid choice. Returning to main menu...");
                break;
            }

            System.out.println();
            System.out.println("Press Enter to continue");
            scanner.nextLine();
            break;
          case 4:
            administrator.handleReplenishmentRequest();
            break;
          case 5:
            System.out.println("You have logged out ");
            System.out.println();
            return;
          default:
            System.out.println("Invalid input. Please try again.");
            break;
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid number.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
      }
    }
  }

  /**
   * Builder class for constructing an instance of ContextManager.
   */
  public static class ContextManagerBuilder {
    private ScannerWrapper scanner;

    /**
     * Sets the ScannerWrapper instance for the ContextManagerBuilder.
     *
     * @param scanner the ScannerWrapper instance to be set
     * @return the current ContextManagerBuilder instance for method chaining
     */
    // Method to set the ScannerWrapper instance
    public ContextManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for method chaining
    }

    /**
     * Builds the ContextManager instance with the currently set ScannerWrapper instance.
     *
     * @return a new instance of ContextManager configured with the provided ScannerWrapper
     * @throws IllegalArgumentException if the ScannerWrapper is not set
     */
    // Method to build the ContextManager instance
    public ContextManager build() {
      // You can add any validation or default settings here if needed
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must be provided");
      }
      return new ContextManager(scanner);
    }
  }
}
