package com.ntu.hms.manager.user;

import com.ntu.hms.CsvDB;
import com.ntu.hms.model.users.*;
import com.ntu.hms.util.ScannerWrapper;
import java.util.List;

/** The type User manager. */
public class UserManager implements UserManagerInterface {
  private final ScannerWrapper scanner;

  private UserManager(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  @Override
  public void addUser() {
    List<User> users = CsvDB.readUsers();
    System.out.println("What type of user would you like to add?");
    System.out.println("1. Patient");
    System.out.println("2. Doctor");
    System.out.println("3. Pharmacist");
    System.out.println("4. Administrator");
    System.out.println("5. Go back to previous menu");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();

    String hospitalID, password, name, gender, dateOfBirth, phoneNumber, email, bloodType;
    int age;

    switch (choice) {
      case 1: // Add Patient
        System.out.print("Enter Hospital ID: ");
        hospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("Enter Age: ");
        age = scanner.nextInt();
        System.out.print("Enter Gender: ");
        gender = scanner.nextLine();
        System.out.print("Enter Date of Birth: ");
        dateOfBirth = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        phoneNumber = scanner.nextLine();
        System.out.print("Enter Email: ");
        email = scanner.nextLine();
        System.out.print("Enter Blood Type: ");
        bloodType = scanner.nextLine();

        // Create Patient and add to list
        Patient newPatient =
            new Patient(
                hospitalID,
                password,
                name,
                age,
                gender,
                dateOfBirth,
                phoneNumber,
                email,
                bloodType);
        users.add(newPatient);
        break;

      case 2: // Add Doctor
        System.out.print("Enter Hospital ID: ");
        hospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("Enter Age: ");
        age = scanner.nextInt();
        System.out.print("Enter Gender: ");
        gender = scanner.nextLine();

        // Doctor and add to list
        Doctor newDoctor = new Doctor(hospitalID, password, name, age, gender);
        users.add(newDoctor);
        break;
      case 3: // Add Pharmacist
        System.out.print("Enter Hospital ID: ");
        hospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("Enter Age: ");
        age = scanner.nextInt();
        System.out.print("Enter Gender: ");
        gender = scanner.nextLine();

        // Create Pharmacist and add to list
        Pharmacist newPharmacist = new Pharmacist(hospitalID, password, name, age, gender);
        users.add(newPharmacist);
        break;
      case 4: // Add Administrator
        System.out.print("Enter Hospital ID: ");
        hospitalID = scanner.nextLine();
        System.out.print("Enter Password: ");
        password = scanner.nextLine();
        System.out.print("Enter Name: ");
        name = scanner.nextLine();
        System.out.print("Enter Age: ");
        age = scanner.nextInt();
        System.out.print("Enter Gender: ");
        gender = scanner.nextLine();

        // Create Administrator and add to list
        Administrator newAdministrator = new Administrator(hospitalID, password, name, age, gender);
        users.add(newAdministrator);
        break;
      case 5:
        break;
      default:
        System.out.println("Invalid choice. Returning to main menu...");
        return;
    }

    // Save the updated user list
    CsvDB.saveUsers(users);
    System.out.println("New user added and saved successfully.");
  }

  @Override
  public void showUsers() {
    List<User> users = CsvDB.readUsers();

    // Ask the user which type of staff they want to view
    System.out.println("\nWhich type of staff do you want to view?");
    System.out.println("1. Patients");
    System.out.println("2. Pharmacists");
    System.out.println("3. Doctors");
    System.out.println("4. Administrators");
    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();

    String selectedStaffType = "";
    switch (choice) {
      case 1:
        selectedStaffType = "Patients";
        break;
      case 2:
        selectedStaffType = "Pharmacists";
        break;
      case 3:
        selectedStaffType = "Doctors";
        break;
      case 4:
        selectedStaffType = "Administrators";
        break;
      default:
        System.out.println("Invalid choice. Returning to main menu...");
        return;
    }

    // Ask the user if they want to filter by gender
    System.out.println("\nWould you like to filter by gender?");
    System.out.println("1. Male");
    System.out.println("2. Female");
    System.out.println("3. No Filter");
    System.out.print("Enter your choice: ");
    int genderChoice = scanner.nextInt();

    String selectedGender = "";
    switch (genderChoice) {
      case 1:
        selectedGender = "Male";
        break;
      case 2:
        selectedGender = "Female";
        break;
      case 3:
        selectedGender = ""; // No gender filter
        break;
      default:
        System.out.println("Invalid choice. Returning to main menu...");
        return;
    }

    // Ask the user if they want to filter by age range
    System.out.print("\nEnter minimum age (or press enter to skip): ");
    String minAgeInput = scanner.nextLine().trim();
    int minAge = minAgeInput.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minAgeInput);

    System.out.print("Enter maximum age (or press enter to skip): ");
    String maxAgeInput = scanner.nextLine().trim();
    int maxAge = maxAgeInput.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxAgeInput);

    // Display filtered users
    System.out.println("\nViewing " + selectedStaffType + " Information:");
    boolean userFound = false;

    for (User user : users) {
      boolean matchesRole = false;
      switch (choice) {
        case 1:
          matchesRole = user instanceof Patient;
          break;
        case 2:
          matchesRole = user instanceof Pharmacist;
          break;
        case 3:
          matchesRole = user instanceof Doctor;
          break;
        case 4:
          matchesRole = user instanceof Administrator;
          break;
      }

      boolean matchesGender =
          selectedGender.isEmpty() || user.getGender().equalsIgnoreCase(selectedGender);
      boolean matchesAge = user.getAge() >= minAge && user.getAge() <= maxAge;

      if (matchesRole && matchesGender && matchesAge) {
        System.out.printf(
            "Hospital ID: %s | Name: %s | Age: %d | Gender: %s\n",
            user.getHospitalID(), user.getName(), user.getAge(), user.getGender());
        userFound = true;
      }
    }

    if (!userFound) {
      System.out.println("No com.ntu.hms.users found matching the specified criteria.");
    }
  }

  @Override
  public void updateUser() {
    List<User> users = CsvDB.readUsers();
    System.out.print(
        "\nEnter the Hospital ID of the staff to update (or press enter to return to main menu): ");
    String staffIDToUpdate = scanner.nextLine();
    boolean userFound = false;

    if (staffIDToUpdate.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    for (User user : users) {
      if (user.getHospitalID().equalsIgnoreCase(staffIDToUpdate)) {
        System.out.println("Found user: " + user);

        // Update common fields: password, name, age, gender
        System.out.print("Enter new password (or press Enter to keep current): ");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
          user.setPassword(newPassword);
        }

        System.out.print("Enter new name (or press Enter to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
          user.setName(newName);
        }

        System.out.print("Enter new age (or press Enter to keep current): ");
        String newAgeInput = scanner.nextLine();
        if (!newAgeInput.trim().isEmpty()) {
          int newAge = Integer.parseInt(newAgeInput);
          user.setAge(newAge);
        }

        System.out.print("Enter new gender (or press Enter to keep current): ");
        String newGender = scanner.nextLine();
        if (!newGender.trim().isEmpty()) {
          user.setGender(newGender);
        }

        // Additional fields for com.ntu.hms.users.Patient only
        if (user instanceof Patient) {
          Patient patient = (Patient) user;

          System.out.print("Enter new date of birth (or press Enter to keep current): ");
          String newDOB = scanner.nextLine();
          if (!newDOB.trim().isEmpty()) {
            patient.setDateOfBirth(newDOB);
          }

          System.out.print("Enter new phone number (or press Enter to keep current): ");
          String newPhoneNumber = scanner.nextLine();
          if (!newPhoneNumber.trim().isEmpty()) {
            patient.setPhoneNumber(newPhoneNumber);
          }

          System.out.print("Enter new email (or press Enter to keep current): ");
          String newEmail = scanner.nextLine();
          if (!newEmail.trim().isEmpty()) {
            patient.setEmail(newEmail);
          }

          System.out.print("Enter new blood type (or press Enter to keep current): ");
          String newBloodType = scanner.nextLine();
          if (!newBloodType.trim().isEmpty()) {
            patient.setBloodType(newBloodType);
          }
        }

        System.out.println("User information updated successfully.");
        userFound = true;
        break;
      }
    }

    if (!userFound) {
      System.out.println("Staff with Hospital ID " + staffIDToUpdate + " not found.");
    }

    // Save the updated com.ntu.hms.users to CSV
    CsvDB.saveUsers(users);
    System.out.println("User data saved successfully.");
  }

  // Method to delete a user
  @Override
  public void deleteUser() {
    List<User> users = CsvDB.readUsers();
    System.out.print("\nEnter the Hospital ID of the staff to delete: ");
    String staffIDToDelete = scanner.nextLine();
    User userToRemove = null;

    if (staffIDToDelete.trim().isEmpty()) {
      System.out.println("Returning to main menu...");
      return;
    }

    for (User user : users) {
      if (user.getHospitalID().equalsIgnoreCase(staffIDToDelete)) {
        userToRemove = user;
        break;
      }
    }

    if (userToRemove != null) {
      users.remove(userToRemove);
      System.out.println("User with Hospital ID " + staffIDToDelete + " has been removed.");

      // Save the updated com.ntu.hms.users to CSV
      CsvDB.saveUsers(users);
      System.out.println("User data saved successfully.");
    } else {
      System.out.println("Staff with Hospital ID " + staffIDToDelete + " not found.");
    }
  }

  /**
   * User manager builder user manager builder.
   *
   * @return the user manager builder
   */
  // Static method to access the builder
  public static UserManagerBuilder userManagerBuilder() {
    return new UserManagerBuilder();
  }

  /** The type User manager builder. */
  // Static inner Builder class
  public static class UserManagerBuilder {
    private ScannerWrapper scanner;

    /**
     * Sets scanner.
     *
     * @param scanner the scanner
     * @return the scanner
     */
    // Setter method for ScannerWrapper
    public UserManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    /**
     * Build user manager.
     *
     * @return the user manager
     */
    // Method to build a UserManager instance
    public UserManager build() {
      // Validation to ensure required fields are set
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must not be null.");
      }
      return new UserManager(scanner);
    }
  }
}
