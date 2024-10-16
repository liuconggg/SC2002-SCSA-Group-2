public class AppointmentOutcomeRecord {

	private String appointmentID;
	private String typeOfService;
	private String consultationNotes;


	public AppointmentOutcomeRecord(String appointmentID, String typeOfService, String consultationNotes){
		this.appointmentID = appointmentID;
		this.typeOfService = typeOfService;
		this.consultationNotes = consultationNotes;
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

	public String getTypeOfService() {
		return this.typeOfService;
	}

	/**
	 * 
	 * @param typeOfService
	 */
	public void setTypeOfService(String typeOfService) {
		this.typeOfService = typeOfService;
	}

	public String getConsultationNotes() {
		return this.consultationNotes;
	}

	/**
	 * 
	 * @param consultationNotes
	 */
	public void setConsultationNotes(String consultationNotes) {
		this.consultationNotes = consultationNotes;
	}

	// public PrescribedMedication[] getPrescribedMedication() {

	// }

	// /**
	//  * 
	//  * @param prescribedMedication
	//  */
	// public void setPrescribedMedication(PrescribedMedication[] prescribedMedication) {

	// }

}