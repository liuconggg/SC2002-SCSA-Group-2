package com.ntu.hns.manager.application;

import com.ntu.hns.model.users.*;
import com.ntu.hns.util.ScannerWrapper;
import java.util.InputMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextManager implements ContextManagerInterface {
  private final ScannerWrapper scanner;

  @Autowired
  public ContextManager(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  // Patient actions
  @Override
  public void beginPatient(Patient patient) {
    //    int choice = -1;

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

  // Doctor actions
  @Override
  public void beginDoctor(Doctor doctor) {
    int choice = -1;

    while (choice != 8) {
      try {
        doctor.displayMenu();
        choice = scanner.nextInt();
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
            break;
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

  // Pharmacist actions
  @Override
  public void beginPharmacist(Pharmacist pharmacist) {
    int choice = -1;

    while (choice != 5) {
      try {
        pharmacist.displayMenu();
        choice = scanner.nextInt();
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
            break;
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

  // All Administrator functions and logic
  @Override
  public void beginAdministrator(Administrator administrator) {
    int choice = -1;

    while (choice != 5) {
      try {
        administrator.displayMenu();
        choice = scanner.nextInt();
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
            scanner.nextLine();

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
            break;
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
}
