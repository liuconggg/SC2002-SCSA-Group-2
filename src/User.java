public class User {

	private String hospitalID;
	private String password;
	private String name;
	private int age;
	private String gender;

	/**
	 * 
	 * @param hospitalID
	 * @param password
	 */

	 public User(){}

	 public User(String hospitalID, String password, String name, int age, String gender){
		this.hospitalID = hospitalID;
		this.password = password;
		this.name = name;
		this.age = age;
		this.gender = gender;
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

	public int getAge() {
		return this.age;
	}

	/**
	 * 
	 * @param age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return this.gender;
	}

	/**
	 * 
	 * @param Gender
	 */
	public void setGender(String Gender) {
		this.gender = Gender;
	}


	public void displayMenu(){
		System.out.println("Please log in!");
	}

}