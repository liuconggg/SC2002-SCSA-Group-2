public class ReplenishRequest {

	private String requestID;
	private String medicationID;
	private int quantity;
	private String status;

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

	public int getQuantity() {
		return this.quantity;
	}

	/**
	 * 
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return this.status;
	}

	public ReplenishRequest() {
		// TODO - implement ReplenishRequest.ReplenishRequest
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param requestID
	 * @param medicationID
	 * @param quantity
	 * @param status
	 */
	public ReplenishRequest(String requestID, String medicationID, int quantity, String status) {
		// TODO - implement ReplenishRequest.ReplenishRequest
		throw new UnsupportedOperationException();
	}

	public String getMedicationID() {
		// TODO - implement ReplenishRequest.getMedicationID
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param medicationID
	 */
	public void setMedicationID(String medicationID) {
		// TODO - implement ReplenishRequest.setMedicationID
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		// TODO - implement ReplenishRequest.setStatus
		throw new UnsupportedOperationException();
	}

}