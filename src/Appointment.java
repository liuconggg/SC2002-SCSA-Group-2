
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Appointment {

	private String appointmentID;
	private String patientID;
	private String doctorID;
	private LocalDateTime dateTime;
	private String status;

	public Appointment(){}

	public Appointment(String appointmentID, String patientID, String doctorID, LocalDateTime dateTime, String status){
		this.appointmentID = appointmentID;
		this.patientID = patientID;
		this.doctorID = doctorID;
		this.dateTime = dateTime;
		this.status = status;
	}

	public String getAppointmentID() {
		return this.appointmentID;
	}

	/**
	 * 
	 * @param appointmentID
	 */
	public void setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
	}

	public String getPatientID() {
		return this.patientID;
	}

	/**
	 * 
	 * @param patientID
	 */
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public String getDoctorID() {
		return this.doctorID;
	}

	/**
	 * 
	 * @param doctorID
	 */
	public void setDoctorID(String doctorID) {
		this.doctorID = doctorID;
	}

	public String getStatus() {
		return this.status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDateTime() {
		return this.dateTime;
	}

	/**
	 * 
	 * @param dateTime
	 */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public ArrayList<Appointment> getAppointmentsByPatientID(String patientID, ArrayList<Appointment> appointments){
		ArrayList<Appointment> filteredAppt = new ArrayList<Appointment>();

		//Only fetch the confirmed and pending appointments for the patients
		for(Appointment appointment: appointments){
			if(appointment.getPatientID().equals(patientID) && !(appointment.getStatus().equals("Completed")) && !(appointment.getStatus().equals("Cancelled"))) {
				filteredAppt.add(appointment);
			}
		}

		return filteredAppt;
	}

	public ArrayList<Appointment> getAppointmentsByDoctorID(String doctorID, ArrayList<Appointment> appointments){
		ArrayList<Appointment> filteredAppt = new ArrayList<Appointment>();

		//fetch all appointments for the doctor
		for(Appointment appointment: appointments){
			if(appointment.getDoctorID().equals(doctorID)) filteredAppt.add(appointment);
		}

		return filteredAppt;
	} 


	public Appointment getAppointmentByAppointmentID(String appointmentID, ArrayList<Appointment> appointments){
		Appointment found = null;
		for(Appointment appt: appointments){
			if(appt.getAppointmentID().equals(appointmentID)) {
				found = appt;
				break;
			}
		}

		return found;
	}



	// public void createAppointmentOutcomeRecord(User user, String typeOfService, String consultationNotes){
	// 	if(user instanceof Doctor){
	// 		Doctor doctor = (Doctor)user;
	// 		AppointmentOutcomeRecord newRecord = new AppointmentOutcomeRecord(appointmentID, typeOfService, consultationNotes);
	// 	}
	// }

}