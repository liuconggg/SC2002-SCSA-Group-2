package com.ntu.hms.model.users;

import static com.ntu.hms.MenuDisplayer.displayPharmacistMenu;
import static com.ntu.hms.enums.ReplenishmentStatus.PENDING;

import com.ntu.hms.CsvDB;
import com.ntu.hms.PharmacistInterface;
import com.ntu.hms.enums.AppointmentOutcomeStatus;
import com.ntu.hms.enums.MedicationStatus;
import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.inventory.InventoryManager;
import com.ntu.hms.model.AppointmentOutcomeRecord;
import com.ntu.hms.model.Medication;
import com.ntu.hms.model.MedicationItem;
import com.ntu.hms.util.ScannerWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * The Pharmacist class represents a user with the role of a pharmacist in the hospital management system.
 * It extends the User class and implements the PharmacistInterface.
 * A Pharmacist can prescribe and update medications, manage inventory, and view appointment outcomes.
 */
public class Pharmacist extends User implements PharmacistInterface {
  private ScannerWrapper scanner;
  private InventoryManager inventoryManager;
  private AppointmentManager appointmentManager;

  /**
   * Default constructor for the Pharmacist class.
   * This constructor initializes a new instance of the Pharmacist
   * class with default values and dependencies.
   */
  public Pharmacist() {}

  /**
   * Constructs a new Pharmacist with the specified details.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   * @param password the user's password for authentication
   * @param name the user's full name
   * @param age the user's age
   * @param gender the user's gender
   */
  public Pharmacist(String hospitalID, String password, String name, int age, String gender) {
    super(hospitalID, password, name, age, gender);
  }

  /**
   * Displays the menu options available to a pharmacist.
   * Delegates the task to the static method displayPharmacistMenu
   * which includes options for viewing appointment outcomes,
   * updating prescription statuses, viewing medication inventory,
   * submitting replenishment requests, and logging out.
   */
  public void displayMenu() {
    displayPharmacistMenu();
  }

  /**
   * Views the outcome of appointments.
   *
   * This method initiates the process of displaying the appointment outcomes
   * by utilizing the appointment manager. The outcomes include details such
   * as appointment IDs, prescription statuses, and a list of prescribed medications.
   */
  public void viewAppointmentOutcome() {
    appointmentManager.showAppointmentOutcome();
  }

  /**
   * Prescribes and updates the inventory based on pending appointment outcome records.
   *
   * This method performs the following tasks:
   * 1. Reads appointment outcome records and medication data from the CSV database.
   * 2. Displays pending appointment outcome records for the pharmacist to select.
   * 3. Prompts the pharmacist to select an appointment outcome record to dispense medication for.
   * 4. Validates the selected record and checks if sufficient stock is available for the prescribed medications.
   * 5. Updates the prescription status to dispensed if sufficient stock is available.
   * 6. Updates the inventory quantities and statuses for the dispensed medications.
   * 7. Saves the updated appointment outcome records and medication data back to the CSV database.
   *
   * If there are no pending appointment outcome records or if the pharmacist decides to exit,
   * the method will return to the main menu.
   */
  @Override
  public void prescribeAndUpdate() {
    List<AppointmentOutcomeRecord> appointmentOutcomeRecords =
        CsvDB.readAppointmentOutcomeRecords();
    List<Medication> medications = CsvDB.readMedications();
    boolean continueDispensing = true;

    while (continueDispensing) {
      // Display pending appointment outcome records for selection
      System.out.println("\n=== Pending Appointment Outcome Records ===");
      ArrayList<AppointmentOutcomeRecord> pendingRecords = new ArrayList<>();
      int index = 1;
      for (AppointmentOutcomeRecord record : appointmentOutcomeRecords) {
        if (record.getPrescriptionStatus().equalsIgnoreCase(PENDING.name()) && record.getPrescriptions() != null && !record.getPrescriptions().isEmpty()){
          System.out.printf(
              "%d. Appointment ID: %s, Prescription: %s\n",
              index, record.getAppointmentID(), record.getPrescriptions());
          pendingRecords.add(record);
          index++;
        }
      }

      // If no pending records are available, inform the user and exit
      if (pendingRecords.isEmpty()) {
        System.out.println("No pending appointment outcome records available for dispensing.");
        break;
      }

      // Prompt the Pharmacist to select an appointment outcome record or press Enter
      // to exit
      System.out.println(
          "\nEnter the number of the Appointment Outcome Record you want to dispense medication for (or press Enter to return): ");
      String input = scanner.nextLine();

      if (input.trim().isEmpty()) {
        System.out.println("Returning to main menu...");
        continueDispensing = false; // Exit the loop
        continue;
      }

      try {
        int choice = Integer.parseInt(input);

        // Validate the choice
        if (choice < 1 || choice > pendingRecords.size()) {
          System.out.println("Invalid selection. Please try again.");
          continue;
        }

        // Get the selected record
        AppointmentOutcomeRecord selectedRecord = pendingRecords.get(choice - 1);
        List<MedicationItem> prescribedItems = selectedRecord.getPrescriptions();
        boolean sufficientStock = true;

        // Check if there is sufficient stock for all prescribed medications for this
        // appointment outcome
        for (MedicationItem item : prescribedItems) {
          for (Medication medication : medications) {
            if (medication.getMedicationID().equals(item.getMedicationID())) {
              if (medication.getTotalQuantity() < item.getQuantity()) {
                System.out.printf(
                    "Insufficient stock for Medication ID: %s. Available: %d, Required: %d\n",
                    medication.getMedicationID(),
                    medication.getTotalQuantity(),
                    item.getQuantity());
                sufficientStock = false;
                break;
              }
            }
          }
          if (!sufficientStock) {
            break;
          }
        }

        // If sufficient stock for this appointment outcome, proceed with dispensing
        if (sufficientStock) {
          selectedRecord.setPrescriptionStatus(AppointmentOutcomeStatus.DISPENSED.name());
          // Update inventory for each prescribed medication
          for (MedicationItem item : prescribedItems) {
            for (Medication medication : medications) {
              if (medication.getMedicationID().equals(item.getMedicationID())) {
                int newQuantity = medication.getTotalQuantity() - item.getQuantity();
                boolean alert = false;
                String stockStatus = MedicationStatus.HIGH.name();
                if (newQuantity < 10) {
                  stockStatus = MedicationStatus.LOW.name();
                  alert = true;
                } else if (newQuantity <= 50) {
                  stockStatus = MedicationStatus.MEDIUM.name();
                }
                inventoryManager.updateInventory(medication, stockStatus, alert, newQuantity);
              }
            }
          }
          System.out.println(
              "Appointment Outcome ID: "
                  + selectedRecord.getAppointmentID()
                  + " has been dispensed successfully.");
        } else {
          System.out.println(
              "Appointment Outcome ID: "
                  + selectedRecord.getAppointmentID()
                  + " could not be dispensed due to insufficient stock.");
        }

        // Save the updated appointment outcome records and inventory
        CsvDB.saveAppointmentOutcomeRecords(appointmentOutcomeRecords);
        CsvDB.saveMedications(medications);

      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }

  /**
   * Displays the current medication inventory information.
   *
   * This method delegates the task of showing the inventory
   * details to the inventory manager. It provides a listing
   * of all medications including their IDs, names, stock statuses,
   * and quantities.
   */
  public void viewInventory() {
    inventoryManager.showInventory();
  }

  /**
   * Submits a request to replenish medication inventory.
   *
   * This method is responsible for initiating the process of creating a replenishment
   * request for medications that are low in stock. It interacts with the inventory manager
   * to handle the specifics of the replenishment request.
   */
  public void submitReplenishmentRequest() {
    inventoryManager.processReplenishmentRequest(this);
  }

  /**
   * Sets the scanner instance for the Pharmacist.
   *
   * @param scanner the ScannerWrapper instance used for reading input
   */
  public void setScanner(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  /**
   * Sets the InventoryManager instance for the Pharmacist.
   *
   * @param inventoryManager the InventoryManager responsible for managing medication inventory
   */
  public void setInventoryManager(InventoryManager inventoryManager) {
    this.inventoryManager = inventoryManager;
  }

  /**
   * Sets the AppointmentManager instance for the Pharmacist.
   *
   * @param appointmentManager the AppointmentManager instance used for managing appointments
   */
  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }
}
