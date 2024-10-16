public class Pharmacist extends User {


	public Pharmacist (String hospitalID, String password, String name, String role){
		super(hospitalID, password, name , role);
	}


	public void displayMenu() {
		// TODO - implement Pharmacist.displayMenu
		System.out.println("=== Pharmacist Menu ===");
		System.out.println("1. View Appointment Outcome Record");
		System.out.println("2. Update Prescription Status");
		System.out.println("3. View Medication Inventory");
		System.out.println("4. Submit Replenishment Request");
		System.out.println("5. Logout");
	}

}