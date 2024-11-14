
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Administrator extends User
        implements UserManager, AppointmentInterface, AppointmentOutcomeViewer, InventoryManager,
        AdminReplenishmentManager {

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

        // Ask the user if they want to filter by gender
        System.out.println("\nWould you like to filter by gender?");
        System.out.println("1. Male");
        System.out.println("2. Female");
        System.out.println("3. No Filter");
        System.out.print("Enter your choice: ");
        int genderChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

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
        String minAgeInput = sc.nextLine().trim();
        int minAge = minAgeInput.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minAgeInput);

        System.out.print("Enter maximum age (or press enter to skip): ");
        String maxAgeInput = sc.nextLine().trim();
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

            boolean matchesGender = selectedGender.isEmpty() || user.getGender().equalsIgnoreCase(selectedGender);
            boolean matchesAge = user.getAge() >= minAge && user.getAge() <= maxAge;

            if (matchesRole && matchesGender && matchesAge) {
                System.out.printf("Hospital ID: %s | Name: %s | Age: %d | Gender: %s\n", user.getHospitalID(),
                        user.getName(), user.getAge(), user.getGender());
                userFound = true;
            } else if (matchesRole && matchesGender && matchesAge && (user instanceof Patient)) {
                System.out.printf("Hospital ID: %s | Name: %s | Age: %d | Gender: %s\n",
                        user.getHospitalID(), user.getName(), user.getAge(), user.getGender());
                userFound = true;
            }
        }

        if (!userFound) {
            System.out.println("No users found matching the specified criteria.");
        }
    }

    public void updateUser(ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        System.out.print(
                "\nEnter the Hospital ID of the staff to update (or press enter to return to main menu): ");
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
    public void viewAppointment(ArrayList<Appointment> appointments) throws IOException {
        Scanner sc = new Scanner(System.in);

        // Prompt the user for the type of appointment they want to view
        System.out.println("\nWhat type of appointments would you like to view?");
        System.out.println("1. Completed");
        System.out.println("2. Cancelled");
        System.out.println("3. Pending");
        System.out.println("4. No Show");
        System.out.println("5. View All");
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
                filterAndDisplayAppointments(appointments, AppointmentStatus.COMPLETED.name());
                break;
            case 2:
                System.out.println("\n=== Cancelled Appointments ===");
                filterAndDisplayAppointments(appointments, AppointmentStatus.CANCELLED.name());
                break;
            case 3:
                System.out.println("\n=== Pending Appointments ===");
                filterAndDisplayAppointments(appointments, AppointmentStatus.PENDING.name());
                break;
            case 4:
                System.out.println("\n=== No Show Appointments ===");
                filterAndDisplayAppointments(appointments, AppointmentStatus.NO_SHOW.name());
                break;
            case 5:
                System.out.println("\n=== All Appointments ===");
                displayAllAppointments(appointments);
                break;
            default:
                System.out.println("Invalid choice. Returning to the main menu...");
        }
    }

    // Modified Method to filter and display appointments based on their status
    private void filterAndDisplayAppointments(ArrayList<Appointment> appointments, String status) throws IOException {
        boolean found = false;

        // Read Appointment Outcome Records (only if status is Completed)
        ArrayList<AppointmentOutcomeRecord> outcomeRecords = new ArrayList<>();
        if (status.equalsIgnoreCase(AppointmentStatus.COMPLETED.name())) {
            try {
                outcomeRecords = CsvDB.readAppointmentOutcomeRecords();
            } catch (IOException e) {
                System.out.println("Error reading appointment outcome records: " + e.getMessage());
                return;
            }
        }

        for (Appointment appt : appointments) {
            if (appt.getStatus().equalsIgnoreCase(status)) {
                // Display the basic appointment details
                System.out.printf(
                        "Appointment ID: %s | Patient ID: %s | Doctor ID: %s | Date: %s | Session: %d | Status: %s\n",
                        appt.getAppointmentID(), appt.getPatientID(), appt.getDoctorID(), appt.getDate(),
                        appt.getSession(), appt.getStatus());
                found = true;

                // If the appointment is "Completed", display additional outcome details
                if (status.equalsIgnoreCase(AppointmentStatus.COMPLETED.name())) {
                    // Find the corresponding appointment outcome record
                    AppointmentOutcomeRecord outcomeRecord = getOutcomeByAppointmentID(outcomeRecords,
                            appt.getAppointmentID());
                    if (outcomeRecord != null) {
                        // System.out.printf(" Diagnosis: %s\n", outcomeRecord.getDiagnosis());
                        // System.out.printf(" Treatment: %s\n", outcomeRecord.getTreatment());
                        System.out.printf(
                                "Appointment ID: %s | Type of Service: %s | Consultation Notes: %s | Prescriptions: %s | Prescription Status: %s\n",
                                outcomeRecord.getAppointmentID(), outcomeRecord.getTypeOfService(),
                                outcomeRecord.getConsultationNotes(), outcomeRecord.getPrescriptions(),
                                outcomeRecord.getPrescriptionStatus());
                    } else {
                        System.out.println("  No outcome record available for this appointment.");
                    }
                }

                System.out.println("-------------------------------------------------------");
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

    public AppointmentOutcomeRecord getOutcomeByAppointmentID(ArrayList<AppointmentOutcomeRecord> outcomeRecords,
            String appointmentID) {
        for (AppointmentOutcomeRecord outcome : outcomeRecords) {
            if (outcome.getAppointmentID().equals(appointmentID)) {
                return outcome;
            }
        }
        return null;
    }

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

                // Determine stock status based on the new quantity
                String stockStatus = MedicationStatus.LOW.name();
                boolean alert = false;
                if (newQuantity < 10) {
                    stockStatus = MedicationStatus.LOW.name();
                    alert = true;
                } else if (newQuantity >= 10 && newQuantity <= 50) {
                    stockStatus = MedicationStatus.MEDIUM.name();
                } else if (newQuantity > 50) {
                    stockStatus = MedicationStatus.HIGH.name();
                }

                med.setTotalQuantity(newQuantity);
                med.setStockStatus(stockStatus);
                med.setAlert(alert);
                System.out.println("Updated " + medicationName + " quantity to: " + med.getTotalQuantity());
                medicationFound = true;
                break;
            }
        }

        if (!medicationFound) {
            System.out.println("Medication not found in inventory. Please use 'addInventory' to add a new medication.");
            return;
        }

        // Save the updated inventory to the CSV file
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory Item updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void addInventory(ArrayList<Medication> inventory) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the name of the medication to add (or press enter to return to main menu): ");
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

        // Determine stock status based on quantity
        String stockStatus = MedicationStatus.LOW.name();
        boolean alert = false;
        if (quantity < 10) {
            stockStatus = MedicationStatus.LOW.name();
            alert = true;
        } else if (quantity >= 10 && quantity <= 50) {
            stockStatus = MedicationStatus.MEDIUM.name();
        } else if (quantity > 50) {
            stockStatus = MedicationStatus.HIGH.name();
        }

        // Create a new medication and add to inventory
        String medicationID = "M000" + (inventory.size() + 1);
        Medication newMedication = new Medication(medicationID, medicationName, stockStatus, alert, quantity);
        inventory.add(newMedication);
        System.out.println("Added new medication: " + newMedication.getMedicationName() + " with quantity: "
                + newMedication.getTotalQuantity());

        // Save the updated inventory to the CSV file
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory Item added successfully.");
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
            System.out.println("Inventory Item deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    public void approveReplenishmentRequest(ArrayList<ReplenishmentRequest> replenishmentRequests,
                                        ArrayList<Medication> inventory) {
    Scanner sc = new Scanner(System.in);
    boolean requestFound = false;

    System.out.println("\n=== Pending Replenishment Requests ===");
    // Display all pending replenishment requests
    for (ReplenishmentRequest request : replenishmentRequests) {
        if (request.getStatus().equalsIgnoreCase(ReplenishmentStatus.PENDING.name())) {
            System.out.printf("Request ID: %s | Status: %s | Pharmacist ID: %s\n", request.getRequestID(),
                    request.getStatus(), request.getPharmacistID());
            requestFound = true;
        }
    }

    if (!requestFound) {
        System.out.println("No pending replenishment requests found.");
        return;
    }

    System.out.print("\nEnter the Request ID to approve or decline (or press enter to return to main menu): ");
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
                && request.getStatus().equalsIgnoreCase(ReplenishmentStatus.PENDING.name())) {
            selectedRequest = request;
            break;
        }
    }

    if (selectedRequest == null) {
        System.out.println("Invalid Request ID or request is not pending. Please try again.");
        return;
    }

    // Ask the user to approve or decline the request
    System.out.print("\nEnter 'A' to approve or 'D' to decline the request: ");
    String decision = sc.nextLine().trim().toUpperCase();

    switch (decision) {
        case "A":
            // Set the status to "APPROVED"
            selectedRequest.setStatus(ReplenishmentStatus.APPROVED.name());

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
                    // Update the quantity, stockStatus and alert level of the medication in the inventory
                    String stockStatus = MedicationStatus.LOW.name();
                    boolean alert = false;
                    int newQuantity = medicationToUpdate.getTotalQuantity() + item.getQuantity();

                    if (newQuantity < 10) {
                        stockStatus = MedicationStatus.LOW.name();
                        alert = true;
                    } else if (newQuantity >= 10 && newQuantity <= 50) {
                        stockStatus = MedicationStatus.MEDIUM.name();
                    } else if (newQuantity > 50) {
                        stockStatus = MedicationStatus.HIGH.name();
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

            break;

        case "D":
            // Set the status to "DECLINED"
            selectedRequest.setStatus(ReplenishmentStatus.DECLINED.name());
            System.out.println("Replenishment request has been declined.");
            break;

        default:
            System.out.println("Invalid input. Returning to main menu...");
            return;
    }

    // Save the updated replenishment requests to the CSV file
    try {
        CsvDB.saveReplenishmentRequests(replenishmentRequests);
    } catch (IOException e) {
        System.out.println("Error saving replenishment requests: " + e.getMessage());
    }

    // Save the updated inventory to the CSV file
    if (decision.equals("A")) {
        try {
            CsvDB.saveMedications(inventory);
            System.out.println("Inventory updated successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }
}


}
