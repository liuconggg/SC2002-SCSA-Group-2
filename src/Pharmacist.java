
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Pharmacist extends User implements Inventory, AppointmentOutcomeInterface {

    public Pharmacist(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    public void displayMenu() {
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. View Appointment Outcome Record");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
    }

    @Override
    public void viewAppointmentOutcome(ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords) {
        System.out.println("\n=== Appointment Outcome Records ===");
        for (AppointmentOutcomeRecord record : apptOutcomeRecords) {
            System.out.printf("\nAppointment ID:%s\tPrescription Status:%s\n",
                    record.getAppointmentID(),
                    record.getPrescriptionStatus());

            // Print prescriptions
            System.out.println("Prescriptions:");
            ArrayList<MedicationItem> prescriptions = record.getPrescriptions();
            if (prescriptions.isEmpty()) {
                System.out.println("None");
            } else {
                for (MedicationItem item : prescriptions) {
                    System.out.printf("Medication ID:%s\tMedication Name:%s\tQuantity:%d\n",
                            item.getMedicationID(),
                            item.getMedicationName(),
                            item.getQuantity());
                }
            }
        }
    }

    @Override
    public void updateAppointmentOutcome(AppointmentOutcomeRecord apptOutcomeRecord, String appointmentID,
            String typeOfService, String consultationNotes, ArrayList<MedicationItem> prescriptions,
            String prescriptionStatus) {
        apptOutcomeRecord.setAppointmentID(appointmentID);
        apptOutcomeRecord.setTypeOfService(typeOfService);
        apptOutcomeRecord.setConsultationNotes(consultationNotes);
        apptOutcomeRecord.setPrescriptions(prescriptions);
        apptOutcomeRecord.setPrescriptionStatus(prescriptionStatus);
    }

    @Override
    public void updateMedication(Medication medication, String medicationID, String medicationName, String stockStatus,
            boolean alert, int quantity) {
        medication.setMedicationID(medicationID);
        medication.setMedicationName(medicationName);
        medication.setStockStatus(stockStatus);
        medication.setAlert(alert);
        medication.setTotalQuantity(quantity);
    }

    public void prescribeAndUpdate(ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords, ArrayList<Medication> inventory) {
        Scanner scanner = new Scanner(System.in);
        boolean continueDispensing = true;

        while (continueDispensing) {
            // Display pending appointment outcome records for selection
            System.out.println("\n=== Pending Appointment Outcome Records ===");
            ArrayList<AppointmentOutcomeRecord> pendingRecords = new ArrayList<>();
            int index = 1;
            for (AppointmentOutcomeRecord record : apptOutcomeRecords) {
                if (record.getPrescriptionStatus().equalsIgnoreCase("PENDING")) {
                    System.out.printf("%d. Appointment ID: %s, Prescription: %s\n", index, record.getAppointmentID(), record.getPrescriptions());
                    pendingRecords.add(record);
                    index++;
                }
            }

            // If no pending records are available, inform the user and exit
            if (pendingRecords.isEmpty()) {
                System.out.println("No pending appointment outcome records available for dispensing.");
                break;
            }

            // Prompt the Pharmacist to select an appointment outcome record or press Enter to exit
            System.out.println("\nEnter the number of the Appointment Outcome Record you want to dispense medication for (or press Enter to return): ");
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
                ArrayList<MedicationItem> prescribedItems = selectedRecord.getPrescriptions();
                boolean sufficientStock = true;

                // Check if there is sufficient stock for all prescribed medications for this appointment outcome
                for (MedicationItem item : prescribedItems) {
                    for (Medication medication : inventory) {
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
                    updateAppointmentOutcome(selectedRecord, selectedRecord.getAppointmentID(), selectedRecord.getTypeOfService(),
                            selectedRecord.getConsultationNotes(), selectedRecord.getPrescriptions(), "DISPENSED");
                    // Update inventory for each prescribed medication
                    for (MedicationItem item : prescribedItems) {
                        for (Medication medication : inventory) {
                            if (medication.getMedicationID().equals(item.getMedicationID())) {
                                int newQuantity = medication.getTotalQuantity() - item.getQuantity();
                                boolean alert = false;
                                String stockStatus = "HIGH";
                                if (newQuantity < 10) {
                                    stockStatus = "LOW";
                                    alert = true;
                                } else if (10 <= newQuantity && newQuantity <= 50) {
                                    stockStatus = "MEDIUM";
                                }
                                updateMedication(medication, medication.getMedicationID(),
                                        medication.getMedicationName(),
                                        stockStatus, alert, newQuantity);
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
                    CsvDB.saveAppointmentOutcomeRecords(apptOutcomeRecords);
                    CsvDB.saveMedications(inventory);
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    @Override
    public void viewInventory(ArrayList<Medication> inventory) {
        System.out.println("\n=== Inventory ===");
        for (Medication medication : inventory) {
            System.out.printf("Medication ID:%s\tMedication Name:%s\tStock Status:%s\tQuantity:%d\n",
                    medication.getMedicationID(),
                    medication.getMedicationName(),
                    medication.getStockStatus(),
                    medication.getTotalQuantity());
        }
    }

    public void submitReplenishmentRequest(ArrayList<Medication> inventory,
            ArrayList<ReplenishmentRequest> replenishmentRequests, Pharmacist pharmacist) {
        ArrayList<MedicationItem> medicationBatch = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        System.out.println("\nDo you want to create a replenishment request? (y to continue, press Enter to exit): ");
        String userChoice = sc.nextLine().trim().toLowerCase();

        if (!userChoice.equals("y")) {
            System.out.println("Exiting replenishment request process.");
            return;
        }

        int latestRequestID = replenishmentRequests.size();
        String newRequestID = "R" + String.format("%04d", latestRequestID + 1);

        String medicationID;
        int quantity;
        String choiceToAddMore;

        int lowStockCounter = 0;
        int addedCount = 0;

        ArrayList<Medication> lowStockMedications = new ArrayList<>();
        System.out.println("\nMedications low on stock: ");
        for (Medication medication : inventory) {
            if (medication.getAlert()) {
                System.out.printf("Medication ID:%s\tMedication Name:%s\tStock Status:%s\tQuantity:%d\n",
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
                medicationID = sc.nextLine();

                // Check if medication has already been added
                if (addedMedicationIDs.contains(medicationID)) {
                    System.out.println(
                            "This medication has already been added to the replenishment request. Please choose another.");
                    choiceToAddMore = "y"; // Continue the loop for re-entry
                    continue;
                }

                Medication selectedMedication = null;
                for (Medication medication : lowStockMedications) {
                    if (medication.getMedicationID().equals(medicationID)) {
                        selectedMedication = medication;
                        break;
                    }
                }
                if (selectedMedication != null) {
                    System.out.println("Enter the quantity to replenish: ");
                    quantity = sc.nextInt();
                    sc.nextLine(); // Consume newline
                    MedicationItem medicationItem = new MedicationItem(selectedMedication.getMedicationID(),
                            selectedMedication.getMedicationName(), quantity);
                    medicationBatch.add(medicationItem);
                    addedMedicationIDs.add(medicationItem.getMedicationID());
                    addedCount++;
                    if (addedCount == lowStockCounter) {
                        System.out.println("All low stock medications have been added.");
                        break;
                    }
                    System.out.println("Do you want to add another medication? (y/n): ");
                    choiceToAddMore = sc.nextLine().trim().toLowerCase();
                } else {
                    System.out.println("Invalid medication ID. Please try again.");
                    choiceToAddMore = "y"; // Continue the loop for re-entry
                }
            } while (choiceToAddMore.equals("y"));

            if (!medicationBatch.isEmpty()) {
                ReplenishmentRequest newRequest = new ReplenishmentRequest(newRequestID, medicationBatch, "PENDING",
                        pharmacist.getHospitalID());
                replenishmentRequests.add(newRequest);
                try {
                    CsvDB.saveReplenishmentRequests(replenishmentRequests);
                    System.out.println("Replenishment request created successfully.");
                } catch (IOException e) {
                    System.out.println("Error reading or writing replenishment requests: " + e.getMessage());
                }
            } else {
                System.out.println("No valid medications were added to the replenishment request.");
            }
        }
    }
}
