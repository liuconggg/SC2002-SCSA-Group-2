package com.ntu.hns.model.users;

import static com.ntu.hns.enums.ReplenishmentStatus.PENDING;

import com.ntu.hns.Displayable;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.model.AppointmentOutcomeRecord;
import com.ntu.hns.model.Medication;
import com.ntu.hns.CsvDB;
import com.ntu.hns.model.MedicationItem;
import com.ntu.hns.enums.AppointmentOutcomeStatus;
import com.ntu.hns.enums.MedicationStatus;
import com.ntu.hns.manager.inventory.InventoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Pharmacist extends User implements Displayable {
    @Autowired private CsvDB csvDB;
    @Autowired private Scanner scanner;
    @Autowired private InventoryManager inventoryManager;
    @Autowired private AppointmentManager appointmentManager;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Pharmacist() {}

    public Pharmacist(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }

    public void viewAppointmentOutcome() {
        appointmentManager.showAppointmentOutcome();
    }

    public void prescribeAndUpdate() {
        List<AppointmentOutcomeRecord> appointmentOutcomeRecords = csvDB.readAppointmentOutcomeRecords();
        List<Medication> medications = csvDB.readMedications();
        boolean continueDispensing = true;

        while (continueDispensing) {
            // Display pending appointment outcome records for selection
            System.out.println("\n=== Pending Appointment Outcome Records ===");
            ArrayList<AppointmentOutcomeRecord> pendingRecords = new ArrayList<>();
            int index = 1;
            for (AppointmentOutcomeRecord record : appointmentOutcomeRecords) {
                if (record.getPrescriptionStatus().equalsIgnoreCase(PENDING.name())) {
                    System.out.printf("%d. Appointment ID: %s, Prescription: %s\n", index, record.getAppointmentID(),
                            record.getPrescriptions());
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
                                        medication.getMedicationID(), medication.getTotalQuantity(),
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
                    System.out.println("Appointment Outcome ID: " + selectedRecord.getAppointmentID()
                            + " has been dispensed successfully.");
                } else {
                    System.out.println("Appointment Outcome ID: " + selectedRecord.getAppointmentID()
                            + " could not be dispensed due to insufficient stock.");
                }

                // Save the updated appointment outcome records and inventory
                try {
                    CsvDB.saveAppointmentOutcomeRecords(appointmentOutcomeRecords);
                    CsvDB.saveMedications(medications);
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }

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
}
