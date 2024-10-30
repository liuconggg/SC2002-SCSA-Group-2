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

}