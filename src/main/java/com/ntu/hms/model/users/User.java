package com.ntu.hms.model.users;

/**
 * Represents an abstract user in the hospital management system with basic attributes such
 * as hospital ID, password, name, age, and gender.
 * Subclasses of User must implement the displayMenu method to provide specific menu options.
 */
public abstract class User {
  private String hospitalID;
  private String password;
  private String name;
  private int age;
  private String gender;

  /**
   * Default constructor for the User class. This constructor initializes
   * a new instance of the User class with default values.
   */
  public User() {}

  /**
   * Constructs a new User with the specified details.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   * @param password the user's password for authentication
   * @param name the user's full name
   * @param age the user's age
   * @param gender the user's gender
   */
  public User(String hospitalID, String password, String name, int age, String gender) {
    this.hospitalID = hospitalID;
    this.password = password;
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  /**
   * Retrieves the hospital ID of the user.
   *
   * @return the hospital ID of the user.
   */
  public String getHospitalID() {
    return this.hospitalID;
  }

  /**
   * Sets the hospital ID for the user.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   */
  public void setHospitalID(String hospitalID) {
    this.hospitalID = hospitalID;
  }

  /**
   * Retrieves the user's password for authentication purposes.
   *
   * @return the user's password.
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Sets the user's password for authentication purposes.
   *
   * @param password the new password for the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Retrieves the full name of the user.
   *
   * @return the name of the user.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the full name of the user.
   *
   * @param name the new name to be set for the user.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieves the age of the user.
   *
   * @return the age of the user.
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Sets the age of the user.
   *
   * @param age the new age to be set for the user
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Retrieves the gender of the user.
   *
   * @return the gender of the user.
   */
  public String getGender() {
    return this.gender;
  }

  /**
   * Sets the gender of the user.
   *
   * @param gender the new gender to be set for the user.
   */
  public void setGender(String Gender) {
    this.gender = Gender;
  }

  /**
   * Abstract method to display the menu specific to the user type.
   *
   * <p>Subclasses of User should implement this method to provide a menu with
   * options relevant to the user's role (e.g., Patient, Doctor, Pharmacist).
   */
  public abstract void displayMenu();
}
