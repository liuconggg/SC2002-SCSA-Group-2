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

public class Pharmacist extends User implements PharmacistInterface {
  private ScannerWrapper scanner;
  private InventoryManager inventoryManager;
  private AppointmentManager appointmentManager;

  /** Default constructor required for OpenCSV to instantiate object. */
  public Pharmacist() {}

  public Pharmacist(String hospitalID, String password, String name, int age, String gender) {
    super(hospitalID, password, name, age, gender);
  }

  public void displayMenu() {
    displayPharmacistMenu();
  }

  public void viewAppointmentOutcome() {
    appointmentManager.showAppointmentOutcome();
  }

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
        if (record.getPrescriptionStatus().equalsIgnoreCase(PENDING.name())) {
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

  public void viewInventory() {
    inventoryManager.showInventory();
  }

  public void submitReplenishmentRequest() {
    inventoryManager.processReplenishmentRequest(this);
  }

  public void setScanner(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  public void setInventoryManager(InventoryManager inventoryManager) {
    this.inventoryManager = inventoryManager;
  }

  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }
}
