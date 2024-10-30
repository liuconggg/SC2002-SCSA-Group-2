import java.util.ArrayList;

public class Administrator extends User implements Inventory {

	public Administrator(String hospitalID, String password, String name, int age, String gender){
		super(hospitalID, password, name, age, gender);
	}

	public void displayMenu() {
		System.out.println("=== Administrator Menu ===");
		System.out.println("1. View and Manage Hospital Staff");
		System.out.println("2. View Appointment Details");
		System.out.println("3. View and Manage Medication Inventory");
		System.out.println("4. Approve Replenishment Requests");
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

	@Override public void updateInventory(ArrayList<Medication> inventory, String medicationName, int quantity) {

	}

}