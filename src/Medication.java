public class Medication {

	private String medicationName;
	private String stockLevel;
	private boolean alert;
	private int totalQuantity;
	private String medicationID;

	public String getMedicationName() {
		return this.medicationName;
	}

	/**
	 * 
	 * @param medicationName
	 */
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getStockLevel() {
		// TODO - implement Medication.getStock
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param stockLevel
	 */
	public void setStockLevel(String stockLevel) {
		// TODO - implement Medication.setStock
		throw new UnsupportedOperationException();
	}

	public boolean getAlert() {
		return this.alert;
	}

	/**
	 * 
	 * @param alert
	 */
	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	public int getTotalQuantity() {
		return this.totalQuantity;
	}

	/**
	 * 
	 * @param totalQuantity
	 */
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getMedicationID() {
		return this.medicationID;
	}

	/**
	 * 
	 * @param medicationID
	 */
	public void setMedicationID(String medicationID) {
		this.medicationID = medicationID;
	}

}