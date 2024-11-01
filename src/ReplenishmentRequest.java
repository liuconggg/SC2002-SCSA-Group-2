import java.util.ArrayList;

public class ReplenishmentRequest {

	private String requestID;
	private ArrayList<MedicationItem> medicationBatch;
	private String status;
	private String pharmacistID;

	public ReplenishmentRequest() {

	}

	/**
	 * 
	 * @param requestID
	 * @param medication
	 * @param status
	 * @param pharmacistID
	 */
	public ReplenishmentRequest(String requestID, ArrayList<MedicationItem> medicationBatch, String status,
			String pharmacistID) {
		this.requestID = requestID;
		this.medicationBatch = medicationBatch;
		this.status = status;
		this.pharmacistID = pharmacistID;
	}

	public String getRequestID() {
		return this.requestID;
	}

	/**
	 * 
	 * @param requestID
	 */
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public ArrayList<MedicationItem> getMedicationBatch() {
		return this.medicationBatch;
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

	public String getPharmacistID() {
		return this.pharmacistID;
	}

}