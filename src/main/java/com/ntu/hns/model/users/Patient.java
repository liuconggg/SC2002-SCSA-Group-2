package com.ntu.hns.model.users;

import com.ntu.hns.Displayable;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hns.manager.schedule.ScheduleManager;
import com.ntu.hns.CsvDB;
import com.opencsv.bean.CsvBindByPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class Patient extends User implements Displayable {
    @CsvBindByPosition(position = 5) private String dateOfBirth;
    @CsvBindByPosition(position = 6) private String phoneNumber;
    @CsvBindByPosition(position = 7) private String email;
    @CsvBindByPosition(position = 8) private String bloodType;

    @Autowired private CsvDB csvDB;
    @Autowired private Scanner scanner;
    @Autowired private AppointmentManager appointmentManager;
    @Autowired private MedicalRecordManager medicalRecordManager;
    @Autowired private ScheduleManager scheduleManager;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Patient() {}

    public Patient(
            String hospitalID,
            String password,
            String name,
            int age,
            String gender,
            String dateOfBirth,
            String phoneNumber,
            String email,
            String bloodType) {
        super(hospitalID, password, name, age, gender);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bloodType = bloodType;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodType() { return this.bloodType; }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Patient Menu ===");
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots"); // schedule.csv
        System.out.println("4. Schedule an Appointment"); // schedule.csv
        System.out.println("5. Reschedule an Appointment"); // apointment.csv
        System.out.println("6. Cancel an Appointment"); // appointment.csv
        System.out.println("7. View Scheduled Appointment"); // appointment.csv
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
        System.out.print("Enter your choice: ");
    }

    public void updatePersonalInformation() {
        List<User> users = csvDB.readUsersCsv();
        boolean changing = true;
        int action = -1;

        while (changing) {
            System.out.println("Select the information to update:");
            System.out.println("1. Phone Number");
            System.out.println("2. Email Address");
            System.out.println("3. Exit");
            System.out.print("Your Choice: ");
            try {
                action = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (action) {
                    case 1:
                        System.out.print("Enter your new phone number: ");
                        String newPhoneNumber = scanner.nextLine();
                        this.setPhoneNumber(newPhoneNumber);
                        System.out.println("Phone number has been updated");
                        break;
                    case 2:
                        System.out.print("Enter your new email address: ");
                        String newEmail = scanner.nextLine();
                        this.setEmail(newEmail);
                        System.out.println("Email address has been updated");
                        break;
                    case 3:
                        changing = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }

                // Update the Patient object in the ArrayList<User> users
                if ((action > 0) && (action != 3)) {
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getHospitalID().equals(this.getHospitalID())) {
                            users.set(i, this);
                            break;
                        }
                    }

                    // Save all users
                    CsvDB.saveUsers(users);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }

        }
    }

    public void viewWeeklySchedule() {
        scheduleManager.showWeeklySchedule();
    }

    public void viewMedicalRecord() {
        medicalRecordManager.showMedicalRecord(this);
    }

    public void scheduleAppointment() {
        appointmentManager.scheduleAppointment(this);
    }

    public void cancelAppointment() {
        appointmentManager.cancelAppointment(this);
    }

    public void rescheduleAppointment() {
        appointmentManager.rescheduleAppointment(this);
    }

    public void viewScheduledAppointments() {
        appointmentManager.showScheduledAppointments(this);
    }

    public void viewAppointmentOutcome() {
        appointmentManager.showAppointmentOutcome(this);
    }
}
