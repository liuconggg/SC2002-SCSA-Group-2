
import java.util.ArrayList;

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
        System.out.println("=== Patient Menu ===");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment");
        System.out.println("5. Reschedule an Appointment");
        System.out.println("6. Cancel an Appointment");
        System.out.println("7. View Scheduled Appointment");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
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
