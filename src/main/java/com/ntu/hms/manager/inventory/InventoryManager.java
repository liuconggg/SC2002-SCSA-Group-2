package com.ntu.hms.manager.inventory;

import com.ntu.hms.CsvDB;
import com.ntu.hms.enums.MedicationStatus;
import com.ntu.hms.enums.ReplenishmentStatus;
import com.ntu.hms.model.Medication;
import com.ntu.hms.model.MedicationItem;
import com.ntu.hms.model.ReplenishmentRequest;
import com.ntu.hms.model.users.Pharmacist;
import com.ntu.hms.util.ScannerWrapper;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager implements InventoryManagerInterface {
  private final ScannerWrapper scanner;

  private InventoryManager(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  @Override
  public void showInventory() {
    System.out.println("\n=== Inventory ===");
    for (Medication medication : CsvDB.readMedications()) {
      System.out.printf(
          "Medication ID:%s\tMedication Name:%s\tStock Status:%s\tQuantity:%d\n",
          medication.getMedicationID(),
          medication.getMedicationName(),
          medication.getStockStatus(),
          medication.getTotalQuantity());
    }
  }

  public void updateInventory(
      Medication medication, String stockStatus, boolean alert, int quantity) {
    medication.setStockStatus(stockStatus);
    medication.setAlert(alert);
    medication.setTotalQuantity(quantity);
  }

  @Override
  public void updateInventory() {
    List<Medication> inventory = CsvDB.readMedications();
    boolean medicationFound = false;

    System.out.print(
        "Enter the name of the medication to update (or press enter to return to main menu): ");
    String medicationName = scanner.nextLine();

    if (medicationName.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    System.out.print("Enter the quantity to add: ");
    while (!scanner.hasNextInt()) {
      System.out.print("Please enter a valid integer for the quantity: ");
      scanner.next(); // Discard invalid input
    }
    int quantity = scanner.nextInt();

    // Loop through the inventory to find if the medication already exists
    for (Medication med : inventory) {
      if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
        // Update the quantity of the existing medication
        int newQuantity = med.getTotalQuantity() + quantity;

        // Determine stock status based on the new quantity
        String stockStatus = MedicationStatus.LOW.name();
        boolean alert = false;
        if (newQuantity < 10) {
          stockStatus = MedicationStatus.LOW.name();
          alert = true;
        } else if (newQuantity <= 50) {
          stockStatus = MedicationStatus.MEDIUM.name();
        } else {
          stockStatus = MedicationStatus.HIGH.name();
        }

        med.setTotalQuantity(newQuantity);
        med.setStockStatus(stockStatus);
        med.setAlert(alert);
        System.out.println("Updated " + medicationName + " quantity to: " + med.getTotalQuantity());
        medicationFound = true;
        break;
      }
    }

    if (!medicationFound) {
      System.out.println(
          "Medication not found in inventory. Please use 'addInventory' to add a new medication.");
      return;
    }

    // Save the updated inventory to the CSV file
    CsvDB.saveMedications(inventory);
    System.out.println("Inventory Item updated successfully.");
  }

  @Override
  public void addInventory() {
    List<Medication> inventory = CsvDB.readMedications();
    System.out.print(
        "Enter the name of the medication to add (or press enter to return to main menu): ");
    String medicationName = scanner.nextLine();

    if (medicationName.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    System.out.print("Enter the quantity to add: ");
    while (!scanner.hasNextInt()) {
      System.out.print("Please enter a valid integer for the quantity: ");
      scanner.next(); // Discard invalid input
    }
    int quantity = scanner.nextInt();

    // Determine stock status based on quantity
    String stockStatus = MedicationStatus.LOW.name();
    boolean alert = false;
    if (quantity < 10) {
      stockStatus = MedicationStatus.LOW.name();
      alert = true;
    } else if (quantity <= 50) {
      stockStatus = MedicationStatus.MEDIUM.name();
    } else {
      stockStatus = MedicationStatus.HIGH.name();
    }

    // Create a new medication and add to inventory
    String medicationID = "M000" + (inventory.size() + 1);
    Medication newMedication =
        new Medication(medicationID, medicationName, stockStatus, alert, quantity);
    inventory.add(newMedication);
    System.out.println(
        "Added new medication: "
            + newMedication.getMedicationName()
            + " with quantity: "
            + newMedication.getTotalQuantity());

    // Save the updated inventory to the CSV file
    CsvDB.saveMedications(inventory);
    System.out.println("Inventory Item added successfully.");
  }

  @Override
  public void deleteInventory() {
    List<Medication> inventory = CsvDB.readMedications();
    boolean medicationFound = false;
    System.out.print(
        "Enter the name of the medication to delete (or press enter to return main menu): ");
    String medicationToDelete = scanner.nextLine();

    if (medicationToDelete.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    // Iterate through inventory and find the medication to delete
    for (int i = 0; i < inventory.size(); i++) {
      if (inventory.get(i).getMedicationName().equalsIgnoreCase(medicationToDelete)) {
        inventory.remove(i); // Remove medication from inventory
        System.out.println(
            "Medication '" + medicationToDelete + "' has been removed from inventory.");
        medicationFound = true;
        break; // Exit loop after deleting the item
      }
    }

    if (!medicationFound) {
      System.out.println("Medication '" + medicationToDelete + "' not found in inventory.");
      return;
    }

    // Save the updated inventory to the CSV file
    CsvDB.saveMedications(inventory);
    System.out.println("Inventory Item deleted successfully.");
  }

  @Override
  public void processReplenishmentRequest(Pharmacist pharmacist) {
    List<Medication> medications = CsvDB.readMedications();
    List<ReplenishmentRequest> replenishmentRequests = CsvDB.readReplenishmentRequests();
    ArrayList<MedicationItem> medicationBatch = new ArrayList<>();

    System.out.println(
        "\nDo you want to create a replenishment request? (y to continue, press Enter to exit): ");
    String userChoice = scanner.nextLine().trim().toLowerCase();

    if (!userChoice.equals("y")) {
      System.out.println("Exiting replenishment request process.");
      return;
    }

    int latestRequestID = replenishmentRequests.size();
    String newRequestID = "R" + String.format("%04d", latestRequestID + 1);

    String choiceToAddMore;
    int lowStockCounter = 0;
    int addedCount = 0;

    ArrayList<Medication> lowStockMedications = new ArrayList<>();
    System.out.println("\nMedications low on stock: ");
    for (Medication medication : medications) {
      if (medication.getAlert()) {
        System.out.printf(
            "Medication ID:%s\tMedication Name:%s\tStock Status:%s\tQuantity:%d\n",
            medication.getMedicationID(),
            medication.getMedicationName(),
            medication.getStockStatus(),
            medication.getTotalQuantity());
        lowStockCounter++;
        lowStockMedications.add(medication);
      }
    }

    ArrayList<String> addedMedicationIDs = new ArrayList<String>();

    if (lowStockCounter == 0) {
      System.out.println("There is no medication that needs replenishment.");
    } else {
      do {
        System.out.println("\nEnter the ID of the medication you want to replenish: ");
        String medicationID = scanner.nextLine();

        // Check if medication has already been added
        if (addedMedicationIDs.contains(medicationID)) {
          System.out.println(
              "This medication has already been added to the replenishment request. Please choose another.");
          choiceToAddMore = "y"; // Continue the loop for re-entry
          continue;
        }

        Medication selectedMedication =
            lowStockMedications
                .stream()
                .filter(medication -> medication.getMedicationID().equals(medicationID))
                .findFirst()
                .orElse(null);
        if (selectedMedication != null) {
          System.out.println("Enter the quantity to replenish: ");
          int quantity = scanner.nextInt();
          MedicationItem medicationItem =
              new MedicationItem(
                  selectedMedication.getMedicationID(),
                  selectedMedication.getMedicationName(),
                  quantity);
          medicationBatch.add(medicationItem);
          addedMedicationIDs.add(medicationItem.getMedicationID());
          addedCount++;
          if (addedCount == lowStockCounter) {
            System.out.println("All low stock medications have been added.");
            break;
          }
          System.out.println("Do you want to add another medication? (y/n): ");
          choiceToAddMore = scanner.nextLine().trim().toLowerCase();
        } else {
          System.out.println("Invalid medication ID. Please try again.");
          choiceToAddMore = "y"; // Continue the loop for re-entry
        }
      } while (choiceToAddMore.equals("y"));

      if (!medicationBatch.isEmpty()) {
        ReplenishmentRequest newRequest =
            new ReplenishmentRequest(
                newRequestID,
                medicationBatch,
                ReplenishmentStatus.PENDING.name(),
                pharmacist.getHospitalID());
        replenishmentRequests.add(newRequest);
        CsvDB.saveReplenishmentRequests(replenishmentRequests);
        System.out.println("Replenishment request created successfully.");
      } else {
        System.out.println("No valid medications were added to the replenishment request.");
      }
    }
  }

  @Override
  public void handleReplenishmentRequest() {
    List<ReplenishmentRequest> replenishmentRequests = CsvDB.readReplenishmentRequests();
    List<Medication> inventory = CsvDB.readMedications();
    boolean requestFound = false;

    System.out.println("\n=== Pending Replenishment Requests ===");
    // Display all pending replenishment requests
    for (ReplenishmentRequest request : replenishmentRequests) {
      if (request.getStatus().equalsIgnoreCase(ReplenishmentStatus.PENDING.name())) {
        System.out.printf(
            "Request ID: %s | Status: %s | Pharmacist ID: %s\n",
            request.getRequestID(), request.getStatus(), request.getPharmacistID());
        requestFound = true;
      }
    }

    if (!requestFound) {
      System.out.println("No pending replenishment requests found.");
      return;
    }

    System.out.print(
        "\nEnter the Request ID to approve or decline (or press enter to return to main menu): ");
    String requestIDToApprove = scanner.nextLine();
    ReplenishmentRequest selectedRequest = null;
    // If user presses "Enter" only, return to main menu
    if (requestIDToApprove.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    // Find the selected request
    for (ReplenishmentRequest request : replenishmentRequests) {
      if (request.getRequestID().equalsIgnoreCase(requestIDToApprove)
          && request.getStatus().equalsIgnoreCase(ReplenishmentStatus.PENDING.name())) {
        selectedRequest = request;
        break;
      }
    }

    if (selectedRequest == null) {
      System.out.println("Invalid Request ID or request is not pending. Please try again.");
      return;
    }

    // Ask the user to approve or decline the request
    System.out.print("\nEnter 'A' to approve or 'D' to decline the request: ");
    String decision = scanner.nextLine().trim().toUpperCase();

    switch (decision) {
      case "A":
        // Set the status to "APPROVED"
        selectedRequest.setStatus(ReplenishmentStatus.APPROVED.name());

        // Update inventory based on the approved replenishment request
        for (MedicationItem item : selectedRequest.getMedicationBatch()) {
          Medication medicationToUpdate = null;

          // Find the medication in the inventory
          for (Medication medication : inventory) {
            if (medication.getMedicationID().equalsIgnoreCase(item.getMedicationID())) {
              medicationToUpdate = medication;
              break;
            }
          }

          if (medicationToUpdate != null) {
            // Update the quantity, stockStatus and alert level of the medication in the inventory
            String stockStatus = MedicationStatus.LOW.name();
            boolean alert = false;
            int newQuantity = medicationToUpdate.getTotalQuantity() + item.getQuantity();

            if (newQuantity < 10) {
              stockStatus = MedicationStatus.LOW.name();
              alert = true;
            } else if (newQuantity <= 50) {
              stockStatus = MedicationStatus.MEDIUM.name();
            } else {
              stockStatus = MedicationStatus.HIGH.name();
            }

            medicationToUpdate.setTotalQuantity(newQuantity);
            medicationToUpdate.setStockStatus(stockStatus);
            medicationToUpdate.setAlert(alert);

            System.out.printf(
                "Medication '%s' is restocked. New quantity: %d\n",
                medicationToUpdate.getMedicationName(), medicationToUpdate.getTotalQuantity());
          } else {
            System.out.println(
                "Medication with ID " + item.getMedicationID() + " not found in inventory.");
          }
        }

        break;

      case "D":
        // Set the status to "DECLINED"
        selectedRequest.setStatus(ReplenishmentStatus.DECLINED.name());
        System.out.println("Replenishment request has been declined.");
        break;

      default:
        System.out.println("Invalid input. Returning to main menu...");
        return;
    }

    // Save the updated replenishment requests to the CSV file
    CsvDB.saveReplenishmentRequests(replenishmentRequests);

    // Save the updated inventory to the CSV file
    if (decision.equals("A")) {
      CsvDB.saveMedications(inventory);
      System.out.println("Inventory updated successfully.");
    }
  }

  public static InventoryManagerBuilder inventoryManagerBuilder() {
    return new InventoryManagerBuilder();
  }

  // Static inner Builder class
  public static class InventoryManagerBuilder {
    private ScannerWrapper scanner;

    // Setter method for ScannerWrapper
    public InventoryManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    // Method to build an InventoryManager instance
    public InventoryManager build() {
      // Add validation to ensure non-null fields if necessary
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must not be null.");
      }
      return new InventoryManager(scanner);
    }
  }
}
