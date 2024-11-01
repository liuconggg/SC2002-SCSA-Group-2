import java.util.ArrayList;

public class Pharmacist extends User implements Inventory {

	public Pharmacist(String hospitalID, String password, String name, int age, String gender) {
		super(hospitalID, password, name, age, gender);
	}

	public void displayMenu() {
		System.out.println("=== Pharmacist Menu ===");
		System.out.println("1. View Appointment Outcome Record");
		System.out.println("2. Update Prescription Status");
		System.out.println("3. View Medication Inventory");
		System.out.println("4. Submit Replenishment Request");
		System.out.println("5. Logout");
	}

	@Override
	public void viewInventory(ArrayList<Medication> inventory) {
		System.out.println("=== Inventory ===");
		for (Medication medication : inventory) {
			System.out.printf("Medication ID:%s\tMedication Name:%s\tStock Status:%s\tAlert:%b\tQuantity:%d\n",
					medication.getMedicationID(),
					medication.getMedicationName(),
					medication.getStockStatus(),
					medication.getAlert(), medication.getTotalQuantity());
		}
	}

	@Override
	public void updateInventory(ArrayList<Medication> inventory, String medicationName, int quantity) {
		boolean medicationFound = false;

		// Loop through the inventory to find if the medication already exists
		for (Medication med : inventory) {
			if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
				// Update the quantity of the existing medication
				med.setTotalQuantity(med.getTotalQuantity() + quantity);
				System.out.println("Updated " + medicationName + " quantity to: " + med.getTotalQuantity());
				medicationFound = true;
				break; // Exit the loop as the medication has been found and updated
			}
		}

		// If medication was not found in the inventory, optionally add it
		// if (!medicationFound) {
		// 	Medication newMedication = new Medication(medicationName, quantity);
		// 	inventory.add(newMedication);
		// 	System.out.println("Added new medication: " + newMedication.getName() + " with quantity: "
		// 			+ newMedication.getQuantity());
		// }
	}

}