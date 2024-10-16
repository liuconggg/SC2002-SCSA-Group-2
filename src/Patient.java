import java.util.ArrayList;

public class Patient extends User {

	public Patient(){}
	public Patient(String hospitalID, String password, String name, String role){
		super(hospitalID, password, name, role);
	}

	public void displayMenu() {
		// TODO - implement Patient.displayMenu
		System.out.println("=== Patient Menu ===");
		System.out.println("1. View Medical Record");
		System.out.println("2. Update Personal Information");
		System.out.println("3. View Available Appointment Slots");
		System.out.println("4. Schedule an Appointment");
		System.out.println("5. Reschedule an Appointment");
		System.out.println("6. Cancel an Appointment");
		System.out.println("7. View Scheduled Appointment");
		System.out.println("8. View Past Appointment Outcome Records");
		System.out.println("9. Logout");
	}


	public ArrayList<Appointment> viewAppointments(String patientID, ArrayList<Appointment> appointments){
		Appointment appts = new Appointment();
		return appts.getAppointmentsByPatientID(patientID, appointments);
	}

	public Patient getPatientById(String patientID, ArrayList<User> users){
		Patient patientFound = null;
		for(User pat:users){
			if (pat.getHospitalID().equals(patientID) && pat instanceof Patient){
				patientFound = (Patient)pat;
				break;
			}
		}

		return patientFound;
	}

}