import java.util.ArrayList;

public class Doctor extends User {

	public Doctor(){}
	public Doctor (String hospitalID, String password, String name, int age, String gender){
		super(hospitalID, password, name, age, gender);
	}



	public void displayMenu() {
		// TODO - implement Doctor.displayMenu
		System.out.println("=== Doctor Menu ===");
		System.out.println("1. View Patient Medical Records");
		System.out.println("2. Update Patient Medical Records");
		System.out.println("3. View Personal Schedule");
		System.out.println("4. Set Availability for Appointments");
		System.out.println("5. Accept or Decline Appointment Requests");
		System.out.println("6. View Upcoming Appointment");
		System.out.println("7. Record Appointment Outcome");
		System.out.println("8. Logout");
	}

	public Doctor getDoctorById(String doctorID, ArrayList<User> users){
		Doctor doctorFound = null;
		for(User doc:users){
			if (doc.getHospitalID().equals(doctorID) && doc instanceof Doctor){
				doctorFound = (Doctor)doc;
				break;
			}
		}

		return doctorFound;
	}


	public ArrayList<Schedule> viewSchedule(String doctorID, ArrayList<Schedule> schedules){
		ArrayList<Schedule> doctorSchedule = new Schedule().getScheduleByDoctorID(doctorID, schedules);
		return doctorSchedule;
	}

	

}