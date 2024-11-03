
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Patient extends User {

    private String dateOfBirth;
    private String phoneNumber;
    private String email;
    private String bloodType;

    public Patient() {
    }

    public Patient(String hospitalID, String password, String name, int age, String gender, String dateOfBirth, String phoneNumber, String email, String bloodType) {
        super(hospitalID, password, name, age, gender);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bloodType = bloodType;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     *
     * @param dateOfBirth
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodType() {
        // TODO - implement Patient.getBloodType
        return this.bloodType;
    }

    // /**
    //  * 
    //  * @param bloodType
    //  */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void displayMenu() {
        // TODO - implement Patient.displayMenu
        System.out.println("\n=== Patient Menu ===");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots"); // schedule.csv
        System.out.println("4. Schedule an Appointment"); //schedule.csv
        System.out.println("5. Reschedule an Appointment"); // apointment.csv
        System.out.println("6. Cancel an Appointment"); // appointment.csv
        System.out.println("7. View Scheduled Appointment"); // appointment.csv
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
    }

    public void viewMedicalRecord(){
		System.out.println("\n=== Medical Record ===");
		System.out.println("ID: " + getHospitalID());
		System.out.println("Name: " + getName());
		System.out.println("Date of Birth: " + getDateOfBirth());
		System.out.println("Gender: " + getGender());
		System.out.println("Blood Type: " + getBloodType());
	}

    public void viewAvailableAppointment(ArrayList<Schedule> schedules, ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);

        // Collect all doctors from the users list
        ArrayList<Doctor> doctors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            }
        }

        // Display the available doctors to the user
        System.out.println("\nAvailable Doctors: ");
        int doctorIndex = 0;
        for (Doctor doc : doctors) {
            System.out.println((++doctorIndex) + ". " + doc.getName());
        }

        // Prompt user to select a doctor by index
        System.out.print("Select a doctor to view available slots: ");
        int selectedDoctorIndex;
        try {
            selectedDoctorIndex = sc.nextInt();
            sc.nextLine();  // Consume the remaining newline
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        // Validate the doctor's selection
        if (selectedDoctorIndex < 1 || selectedDoctorIndex > doctors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // Get the selected doctor
        Doctor selectedDoctor = doctors.get(selectedDoctorIndex - 1);
        String doctorID = selectedDoctor.getHospitalID();

        // Prompt user for number of weeks in advance to view
        System.out.print("Enter the number of weeks you want to view in advance: ");
        String weeksInput = sc.nextLine();

        int numberOfWeeks;
        try {
            numberOfWeeks = Integer.parseInt(weeksInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        // Calculate date range, starting from the next day
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = today.plusWeeks(numberOfWeeks);

        // Define session timings (assuming these are predefined)
        String[] sessionTimings = {
            "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
            "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
            "16:00 - 17:00", "17:00 - 18:00"
        };

        // Filter schedules for the specified doctor
        ArrayList<Schedule> doctorSchedule = new ArrayList<>();
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID)) {
                doctorSchedule.add(schedule);
            }
        }

        LocalDate currentDate = startDate;

        // Check each date within the range for availability or assume availability if not in schedule
        while (!currentDate.isAfter(endDate)) {
            boolean dateInSchedule = false;

            for (Schedule schedule : doctorSchedule) {
                if (schedule.getDate().equals(currentDate)) {
                    dateInSchedule = true;

                    // Display available sessions with timings for this date
                    System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n", 
                            selectedDoctor.getName(), currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    for (int i = 0; i < schedule.getSession().length; i++) {
                        if (schedule.getSession()[i].equals("Available")) {
                            System.out.printf("Session %d (%s)\n", i + 1, sessionTimings[i]);
                        }
                    }
                    break;
                }
            }

            // If date not found in schedule, assume all sessions are available
            if (!dateInSchedule) {
                System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n", 
                        selectedDoctor.getName(), currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                for (int i = 0; i < sessionTimings.length; i++) {
                    System.out.printf("Session %d (%s)\n", i + 1, sessionTimings[i]);
                }
            }

            // Move to the next day, skipping weekends
            currentDate = currentDate.plusDays(1);
            if (currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7) {
                currentDate = currentDate.plusDays(2);  // Skip Saturday and Sunday
            }
        }

        System.out.println("\nPress Enter to return to the main menu.");
        sc.nextLine();  // Pause to allow the user to review output
    }

    public void scheduleAppointment(ArrayList<Schedule> schedules, ArrayList<User> users, String patientID) {
        Scanner sc = new Scanner(System.in);

        // Collect all doctors from the users list
        ArrayList<Doctor> doctors = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            }
        }

        // Display the available doctors to the user
        System.out.println("\nAvailable Doctors: ");
        int doctorIndex = 0;
        for (Doctor doc : doctors) {
            System.out.println((++doctorIndex) + ". " + doc.getName());
        }

        // Prompt user to select a doctor by index
        System.out.print("\nSelect a doctor to schedule an appointment (Press Enter to return): ");
        String doctorInput = sc.nextLine().trim();
        
        // Allow patient to exit scheduling process
        if (doctorInput.isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }
        
        int selectedDoctorIndex;
        try {
            selectedDoctorIndex = Integer.parseInt(doctorInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        if (selectedDoctorIndex < 1 || selectedDoctorIndex > doctors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        // Get the selected doctor
        Doctor selectedDoctor = doctors.get(selectedDoctorIndex - 1);
        String doctorID = selectedDoctor.getHospitalID();

        // Prompt user for appointment date
        System.out.print("Enter the appointment date (dd/MM/yyyy) (Press Enter to return): ");
        String dateInput = sc.nextLine().trim();
        
        // Allow patient to exit if they choose
        if (dateInput.isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }
        
        LocalDate appointmentDate;
        try {
            appointmentDate = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            return;
        }

        // Find or create a schedule for the chosen date
        Schedule scheduleForDate = null;
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID) && schedule.getDate().equals(appointmentDate)) {
                scheduleForDate = schedule;
                break;
            }
        }

        if (scheduleForDate == null) {
            scheduleForDate = Schedule.createDefaultSchedule(doctorID, appointmentDate);
            schedules.add(scheduleForDate);
        }

        // Display available sessions and allow patient to select
        System.out.println("\nAvailable sessions:");
        String[] session = scheduleForDate.getSession();
        boolean hasAvailableSession = false;
        for (int i = 0; i < session.length; i++) {
            if (session[i].equals("Available")) {
                System.out.printf("Session %d: Available\n", i + 1);
                hasAvailableSession = true;
            }
        }

        if (!hasAvailableSession) {
            System.out.println("No available sessions on this date.");
            return;
        }

        // Allow user to return without booking a session
        System.out.print("Select a session number to book (Press Enter to return): ");
        String sessionInput = sc.nextLine().trim();
        
        if (sessionInput.isEmpty()) {
            System.out.println("Returning to main menu...");
            return;
        }

        int sessionNumber;
        try {
            sessionNumber = Integer.parseInt(sessionInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid session number.");
            return;
        }

        if (sessionNumber < 1 || sessionNumber > session.length || !session[sessionNumber - 1].equals("Available")) {
            System.out.println("Invalid session selection or session not available.");
            return;
        }

        // Mark the session as booked with patient ID and Pending status
        session[sessionNumber - 1] = patientID + "-Pending";
        scheduleForDate.setSession(session);

        // Save updated schedules to the CSV file
        CsvDB.saveSchedules(schedules);

        System.out.printf("Appointment booked for Dr. %s on %s, Session %d.\n", 
                          selectedDoctor.getName(), appointmentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), sessionNumber);
    }
    
    public void viewScheduledAppointments(ArrayList<Appointment> appointments, ArrayList<User> users) {
        ArrayList<Appointment> patientAppointments = new ArrayList<>();

        String[] sessionTimings = {
            "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
            "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
            "16:00 - 17:00", "17:00 - 18:00"
        };

        // Filter confirmed appointments for this patient
        for (Appointment appt : appointments) {
            if (appt.getPatientID().equals(this.getHospitalID())) {
                patientAppointments.add(appt);
            }
        }

        if (patientAppointments.isEmpty()) {
            System.out.println("\nYou have no scheduled appointments.");
        } else {
            System.out.println("\nYour scheduled appointments:");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            int apptCounter = 0;

            // Display each appointment's details
            for (Appointment appt : patientAppointments) {
                Doctor doctor = (Doctor) getDoctorById(appt.getDoctorID(), users);

                if (doctor != null) {
                    System.out.printf("%d. Appointment with Dr. %s on %s at %s\n", 
                                      ++apptCounter,
                                      doctor.getName(),
                                      appt.getDate().format(dateFormatter),
                                      sessionTimings[appt.getSession() - 1]);
                }
            }
        }
    }

    private Doctor getDoctorById(String doctorID, ArrayList<User> users) {
        for (User user : users) {
            if (user instanceof Doctor && user.getHospitalID().equals(doctorID)) {
                return (Doctor) user;
            }
        }
        return null;
    }

    public ArrayList<Appointment> viewAppointments(String patientID, ArrayList<Appointment> appointments) {
        Appointment appts = new Appointment();
        return appts.getAppointmentsByPatientID(patientID, appointments);
    }

    public static Patient getPatientById(String patientID, ArrayList<User> users) {
        Patient patientFound = null;
        for (User pat : users) {
            if (pat.getHospitalID().equals(patientID) && pat instanceof Patient) {
                patientFound = (Patient) pat;
                break;
            }
        }

        return patientFound;
    }

}
