
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Administrator extends User implements Inventory {

    public Administrator(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    public void displayMenu() {
        System.out.println("=== Administrator Menu ===");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointment Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
    }

    @Override
    public String toString() {
        String hospitalID = this.getHospitalID();
        String name = this.getName();
        int age = this.getAge();
        String gender = this.getGender();
        return String.format("Admin ID: %s | Name: %s | Age: %d | Gender: %s", hospitalID, name, age, gender);
    }

    public void addUser(ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        System.out.println("What type of user would you like to add?");
        System.out.println("1. Patient");
        System.out.println("2. Doctor");
        System.out.println("3. Pharmacist");
        System.out.println("4. Administrator");
        System.out.println("5. Go back to previous menu");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String hospitalID, password, name, gender, dateOfBirth, phoneNumber, email, bloodType;
        int age;

        switch (choice) {
            case 1: // Add Patient
                System.out.print("Enter Hospital ID: ");
                hospitalID = sc.nextLine();
                System.out.print("Enter Password: ");
                password = sc.nextLine();
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                System.out.print("Enter Age: ");
                age = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Gender: ");
                gender = sc.nextLine();
                System.out.print("Enter Date of Birth: ");
                dateOfBirth = sc.nextLine();
                System.out.print("Enter Phone Number: ");
                phoneNumber = sc.nextLine();
                System.out.print("Enter Email: ");
                email = sc.nextLine();
                System.out.print("Enter Blood Type: ");
                bloodType = sc.nextLine();

                // Create Patient and add to list
                Patient newPatient = new Patient(hospitalID, password, name, age, gender, dateOfBirth, phoneNumber,
                        email, bloodType);
                users.add(newPatient);
                break;

            case 2: // Add Doctor
                System.out.print("Enter Hospital ID: ");
                hospitalID = sc.nextLine();
                System.out.print("Enter Password: ");
                password = sc.nextLine();
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                System.out.print("Enter Age: ");
                age = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Gender: ");
                gender = sc.nextLine();

                // Create Doctor and add to list
                Doctor newDoctor = new Doctor(hospitalID, password, name, age, gender);
                users.add(newDoctor);
                break;

            case 3: // Add Pharmacist
                System.out.print("Enter Hospital ID: ");
                hospitalID = sc.nextLine();
                System.out.print("Enter Password: ");
                password = sc.nextLine();
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                System.out.print("Enter Age: ");
                age = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Gender: ");
                gender = sc.nextLine();

                // Create Pharmacist and add to list
                Pharmacist newPharmacist = new Pharmacist(hospitalID, password, name, age, gender);
                users.add(newPharmacist);
                break;

            case 4: // Add Administrator
                System.out.print("Enter Hospital ID: ");
                hospitalID = sc.nextLine();
                System.out.print("Enter Password: ");
                password = sc.nextLine();
                System.out.print("Enter Name: ");
                name = sc.nextLine();
                System.out.print("Enter Age: ");
                age = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Gender: ");
                gender = sc.nextLine();

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
        try {
            CsvDB.saveUsers(users);
            System.out.println("New user added and saved successfully.");
        } catch (IOException e) {
            System.out.println("Error creating user " + e.getMessage());
        }
    }

    public void viewUsers(ArrayList<User> users) {
            Scanner sc = new Scanner(System.in);

            // Ask the user which type of staff they want to view
            System.out.println("\nWhich type of staff do you want to view?");
            System.out.println("1. Patients");
            System.out.println("2. Pharmacists");
            System.out.println("3. Doctors");
            System.out.println("4. Administrators");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

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

            System.out.println("\nViewing " + selectedStaffType + " Information:");

            for (User user : users) {
                switch (choice) {
                    case 1: // View Patients
                        if (user instanceof Patient) {
                            System.out.println(user);
                        }
                        break;
                    case 2: // View Pharmacists
                        if (user instanceof Pharmacist) {
                            System.out.println(user);
                        }
                        break;
                    case 3: // View Doctors
                        if (user instanceof Doctor) {
                            System.out.println(user);
                        }
                        break;
                    case 4: // View Administrators
                        if (user instanceof Administrator) {
                            System.out.println(user);
                        }
                        break;
                    default:
                        break;
                }
            }
    }

    public void updateUser(ArrayList<User> users) {
            Scanner sc = new Scanner(System.in);
            System.out.print(
                    "\nEnter the Hospital ID of the staff to update (or press enter to return to main menu: ");
            String staffIDToUpdate = sc.nextLine();
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
                    String newPassword = sc.nextLine();
                    if (!newPassword.trim().isEmpty()) {
                        user.setPassword(newPassword);
                    }

                    System.out.print("Enter new name (or press Enter to keep current): ");
                    String newName = sc.nextLine();
                    if (!newName.trim().isEmpty()) {
                        user.setName(newName);
                    }

                    System.out.print("Enter new age (or press Enter to keep current): ");
                    String newAgeInput = sc.nextLine();
                    if (!newAgeInput.trim().isEmpty()) {
                        int newAge = Integer.parseInt(newAgeInput);
                        user.setAge(newAge);
                    }

                    System.out.print("Enter new gender (or press Enter to keep current): ");
                    String newGender = sc.nextLine();
                    if (!newGender.trim().isEmpty()) {
                        user.setGender(newGender);
                    }

                    // Additional fields for Patient only
                    if (user instanceof Patient) {
                        Patient patient = (Patient) user;

                        System.out.print("Enter new date of birth (or press Enter to keep current): ");
                        String newDOB = sc.nextLine();
                        if (!newDOB.trim().isEmpty()) {
                            patient.setDateOfBirth(newDOB);
                        }

                        System.out.print("Enter new phone number (or press Enter to keep current): ");
                        String newPhoneNumber = sc.nextLine();
                        if (!newPhoneNumber.trim().isEmpty()) {
                            patient.setPhoneNumber(newPhoneNumber);
                        }

                        System.out.print("Enter new email (or press Enter to keep current): ");
                        String newEmail = sc.nextLine();
                        if (!newEmail.trim().isEmpty()) {
                            patient.setEmail(newEmail);
                        }

                        System.out.print("Enter new blood type (or press Enter to keep current): ");
                        String newBloodType = sc.nextLine();
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

            try {
                // Save the updated users to CSV
                CsvDB.saveUsers(users);
                System.out.println("User data saved successfully.");
            } catch (IOException e) {
                System.out.println("Error updating user " + e.getMessage());
            }
    }

    // Method to delete a user
    public void deleteUser(ArrayList<User> users) {

            Scanner sc = new Scanner(System.in);
            System.out.print("\nEnter the Hospital ID of the staff to delete: ");
            String staffIDToDelete = sc.nextLine();
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

                try {
                    // Save the updated users to CSV
                    CsvDB.saveUsers(users);
                    System.out.println("User data saved successfully.");
                } catch (IOException e) {
                    System.out.println("Error deleting user " + e.getMessage());
                }
            } else {
                System.out.println("Staff with Hospital ID " + staffIDToDelete + " not found.");
            }
    }

    // Method to view appointments, now accepting appointments as a parameter
    public void viewAppointments(ArrayList<Appointment> appointments) {
        Scanner sc = new Scanner(System.in);

        // Prompt the user for the type of appointment they want to view
        System.out.println("\nWhat type of appointments would you like to view?");
        System.out.println("1. Completed");
        System.out.println("2. Cancelled");
        System.out.println("3. Pending");
        System.out.println("4. View All");
        System.out.print("Enter your choice (or press enter to return to main menu): ");

        String input = sc.nextLine(); // Capture the input as a string
        // If user presses "Enter" only, return to main menu
        if (input.trim().isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        // If input is not empty, try to parse it as an integer
        int choice;
        try {
            choice = Integer.parseInt(input); // Convert input to integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Please enter a valid number.");
            return;
        }

        // Filter and display appointments based on user's choice
        switch (choice) {
            case 1:
                System.out.println("\n=== Completed Appointments ===");
                filterAndDisplayAppointments(appointments, "Completed");
                break;
            case 2:
                System.out.println("\n=== Cancelled Appointments ===");
                filterAndDisplayAppointments(appointments, "Cancelled");
                break;
            case 3:
                System.out.println("\n=== Pending Appointments ===");
                filterAndDisplayAppointments(appointments, "Pending");
                break;
            case 4:
                System.out.println("\n=== All Appointments ===");
                displayAllAppointments(appointments);
                break;
            default:
                System.out.println("Invalid choice. Returning to the main menu...");
        }
    }

    // Method to filter and display appointments based on their status
    private void filterAndDisplayAppointments(ArrayList<Appointment> appointments, String status) {
        boolean found = false;
        for (Appointment appt : appointments) {
            if (appt.getStatus().equalsIgnoreCase(status)) {
                System.out.printf(
                        "Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Session: %d | Status: %s\n",
                        appt.getAppointmentID(), appt.getPatientID(), appt.getDoctorID(), appt.getDate(),
                        appt.getSession(), appt.getStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointments found with status: " + status);
        }
    }

    // Method to display all appointments
    private void displayAllAppointments(ArrayList<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        for (Appointment appt : appointments) {
            System.out.printf(
                    "Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Session: %d | Status: %s\n",
                    appt.getAppointmentID(), appt.getPatientID(), appt.getDoctorID(), appt.getDate(), appt.getSession(),
                    appt.getStatus());
        }
    }

    @Override
    public void viewInventory(ArrayList<Medication> inventory) {
        System.out.println("\n=== Inventory ===");
        for (Medication medication : inventory) {
            System.out.printf("Medication ID:%s\tMedication Name:%s\tStock Status:%s\tQuantity:%d\n",
                    medication.getMedicationID(),
                    medication.getMedicationName(),
                    medication.getStockStatus(),
                    medication.getTotalQuantity());
        }
    }

    public void updateInventory(ArrayList<Medication> inventory) {
        Scanner sc = new Scanner(System.in);
        boolean medicationFound = false;
        String stockStatus = "LOW";
        boolean alert = false;

        System.out.print("Enter the name of the medication to update (or press enter to return to main menu): ");
        String medicationName = sc.nextLine();

        if (medicationName.trim().isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        System.out.print("Enter the quantity to add: ");
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer for the quantity: ");
            sc.next(); // Discard invalid input
        }
        int quantity = sc.nextInt();
        sc.nextLine(); // Consume newline left after nextInt()

        // Loop through the inventory to find if the medication already exists
        for (Medication med : inventory) {
            if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
                // Update the quantity of the existing medication
                int newQuantity = med.getTotalQuantity() + quantity;
                if (newQuantity < 10) {
                    stockStatus = "LOW";
                    alert = true;
                } else if (newQuantity >= 10 && newQuantity <= 50) {
                    stockStatus = "MEDIUM";
                } else if (newQuantity > 50) {
                    stockStatus = "HIGH";
                }
                med.setTotalQuantity(newQuantity);
                med.setStockStatus(stockStatus);
                med.setAlert(alert);
                System.out.println("Updated " + medicationName + " quantity to: " + med.getTotalQuantity());
                medicationFound = true;
                break;
            }
        }

        // If medication was not found in the inventory, add it
        if (!medicationFound) {
            String medicationID = "M000" + (inventory.size() + 1);
            if (quantity < 10) {
                stockStatus = "LOW";
                alert = true;
            } else if (quantity >= 10 && quantity <= 50) {
                stockStatus = "MEDIUM";
            } else if (quantity > 50) {
                stockStatus = "HIGH";
            }
            Medication newMedication = new Medication(medicationID, medicationName, stockStatus, alert, quantity);
            inventory.add(newMedication);
            System.out.println("Added new medication: " + newMedication.getMedicationName() + " with quantity: "
                    + newMedication.getTotalQuantity());
        }

        // Save the updated inventory to the CSV file
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void deleteInventory(ArrayList<Medication> inventory) {
        Scanner sc = new Scanner(System.in);
        boolean medicationFound = false;
        System.out.print("Enter the name of the medication to delete (or press enter to return main menu): ");
        String medicationToDelete = sc.nextLine();

        if (medicationToDelete.trim().isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        // Iterate through inventory and find the medication to delete
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getMedicationName().equalsIgnoreCase(medicationToDelete)) {
                inventory.remove(i); // Remove medication from inventory
                System.out.println("Medication '" + medicationToDelete + "' has been removed from inventory.");
                medicationFound = true;
                break; // Exit loop after deleting the item
            }
        }

        if (!medicationFound) {
            System.out.println("Medication '" + medicationToDelete + "' not found in inventory.");
            return;
        }

        // Save the updated inventory to the CSV file
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    @Override
    public void updateMedication(Medication medication, String medicationID, String medicationName, String stockStatus,
            boolean alert, int quantity) {
        medication.setMedicationID(medicationID);
        medication.setMedicationName(medicationName);
        medication.setStockStatus(stockStatus);
        medication.setAlert(alert);
        medication.setTotalQuantity(quantity);
    }

    public void approveReplenishmentRequests(ArrayList<ReplenishmentRequest> replenishmentRequests,
            ArrayList<Medication> inventory) {
        Scanner sc = new Scanner(System.in);
        boolean requestFound = false;

        System.out.println("\n=== Pending Replenishment Requests ===");
        // Display all pending replenishment requests
        for (ReplenishmentRequest request : replenishmentRequests) {
            if (request.getStatus().equalsIgnoreCase("PENDING")) {
                System.out.printf("Request ID: %s | Status: %s | Pharmacist ID: %s\n", request.getRequestID(),
                        request.getStatus(), request.getPharmacistID());
                requestFound = true;
            }
        }

        if (!requestFound) {
            System.out.println("No pending replenishment requests found.");
            return;
        }

        System.out.print("\nEnter the Request ID to approve (or press enter to return to main menu): ");
        String requestIDToApprove = sc.nextLine();
        ReplenishmentRequest selectedRequest = null;
        // If user presses "Enter" only, return to main menu
        if (requestIDToApprove.trim().isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        // Find the selected request
        for (ReplenishmentRequest request : replenishmentRequests) {
            if (request.getRequestID().equalsIgnoreCase(requestIDToApprove)
                    && request.getStatus().equalsIgnoreCase("PENDING")) {
                selectedRequest = request;
                break;
            }
        }

        if (selectedRequest == null) {
            System.out.println("Invalid Request ID or request is not pending. Please try again.");
            return;
        }

        // Set the status to "APPROVED"
        selectedRequest.setStatus("APPROVED");

        // Update inventory based on the approved replenishment request
        for (MedicationItem item : selectedRequest.getMedicationBatch()) {
            Medication medicationToUpdate = null;

            // Find the medication in the inventory
            for (Medication medication : inventory) {
                if (medication.getMedicationID().equalsIgnoreCase(item.getMedicationID())) {
                    medicationToUpdate = medication;
                    break;
                }
            }

            if (medicationToUpdate != null) {
                // Update the quantity, stockStatus and alert level of the medication in the
                // inventory
                String stockStatus = "LOW";
                boolean alert = false;
                int newQuantity = medicationToUpdate.getTotalQuantity() + item.getQuantity();

                if (newQuantity < 10) {
                    stockStatus = "LOW";
                    alert = true;
                } else if (newQuantity >= 10 && newQuantity <= 50) {
                    stockStatus = "MEDIUM";
                } else if (newQuantity > 50) {
                    stockStatus = "HIGH";
                }

                medicationToUpdate.setTotalQuantity(newQuantity);
                medicationToUpdate.setStockStatus(stockStatus);
                medicationToUpdate.setAlert(alert);

                System.out.printf("Medication '%s' is restocked. New quantity: %d\n",
                        medicationToUpdate.getMedicationName(),
                        medicationToUpdate.getTotalQuantity());
            } else {
                System.out.println("Medication with ID " + item.getMedicationID() + " not found in inventory.");
            }
        }

        // Save the updated replenishment requests to the CSV file
        try {
            CsvDB.saveReplenishmentRequests(replenishmentRequests);
            System.out.println("Replenishment request updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving replenishment requests: " + e.getMessage());
        }

        // Save the updated inventory to the CSV file
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

}
