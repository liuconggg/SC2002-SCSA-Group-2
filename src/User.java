public abstract class User {

	private String hospitalID;
	private String password;
	private String name;
	private String role;

	/**
	 * 
	 * @param hospitalID
	 * @param password
	 */

	 public User(){}

	 public User(String hospitalID, String password, String name, String role){
		this.hospitalID = hospitalID;
		this.password = password;
		this.name = name;
		this.role = role;
	 }


	public String getHospitalID() {
		return this.hospitalID;
	}

	/**
	 * 
	 * @param hospitalID
	 */
	public void setHospitalID(String hospitalID) {
		this.hospitalID = hospitalID;
	}

	public String getPassword() {
		return this.password;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getRole(){
		return this.role;
	}

	public void setRole(String role){
		this.role = role;
	}


	public abstract void displayMenu();

}