import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import java.time.LocalDateTime;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

public class TextDB {
    public static final String DELIMITER = "|";
    
    //Add cosntant header to filter the header row out
    public static final String HEADER = "ID|Password|Name|Age|Gender"; // Common Header in all user text file

    //The respective files header (to write the header of the file)
    public static final String PATIENT_HEADER = "Patient ID|Password|Name|Age|Gender|Date of Birth|Phone Number|Email|Blood Type";
    public static final String DOCTOR_HEADER = "Doctor ID|Password|Name|Age|Gender";
    public static final String APPT_HEADER = "Appointment ID|Patient ID|Doctor ID|Date and Time|Status";
    public static final String SCHEDULE_HEADER = "Doctor ID|Date|Session 1|Session 2|Session 3|Session 4|Session 5|Session 6|Session 7|Session 8";

    // Read all user files (Patients, Doctors, Pharmacists, Administrators)
    public static ArrayList<User> readUsers(String[] fileNames) throws IOException {
        ArrayList<User> data = new ArrayList<User>();
        Scanner sc;

        for (String file : fileNames) {
            sc = new Scanner(new FileInputStream(file));
            try {
                while (sc.hasNextLine()) {
                    String st = sc.nextLine();
                    if (!(st.contains(HEADER))) { // ignore HEADER

                        StringTokenizer star = new StringTokenizer(st, DELIMITER);
                        String hospitalID = star.nextToken().trim();
                        String password = star.nextToken();
                        String name = star.nextToken().trim();
                        int age = Integer.parseInt(star.nextToken().trim());
                        String gender = star.nextToken().trim();

                        if (file.equals("data\\Patients.txt")) { // Create Patient Object

                            String dob = star.nextToken().trim();
                            String phoneNumber = star.nextToken().trim();
                            String email = star.nextToken().trim();
                            String bloodType = star.nextToken().trim();
                            Patient patient = new Patient(hospitalID, password, name, age, gender, dob, phoneNumber,
                                    email, bloodType);
                            data.add(patient);
                        } else if (file.contains("data\\Doctors.txt")) { // Create Doctor Object
                            Doctor doctor = new Doctor(hospitalID, password, name, age, gender);

                            data.add(doctor);
                        }

                        // else if (file.contains("pharmacist")){ //Create Pharmacist Object

                        // }

                        // else { //Create Administrator Object

                        // }
                    }
                }
            } finally {
                sc.close();
            }
        }

        return data;

    }

    // Save or update all user files (Patients, Doctors, Pharmacists,
    // Administrators)
    public static void saveUsers(ArrayList<User> users) throws IOException {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
        ArrayList<Administrator> administrators = new ArrayList<Administrator>();

        PrintWriter out;
        StringBuilder st;
        for (User user : users) { // Store the users into their respective arrays
            if (user instanceof Patient)
                patients.add((Patient) user);
            else if (user instanceof Doctor)
                doctors.add((Doctor) user);
            else if (user instanceof Pharmacist)
                pharmacists.add((Pharmacist) user);
            else
                administrators.add((Administrator) user);
        }

        out = new PrintWriter(new FileWriter("data\\Patients.txt", false)); // Overwrite the users into the respective
                                                                            // txt file

        try {
            out.println(PATIENT_HEADER); // Print header first

            for (Patient patient : patients) {
                st = new StringBuilder();
                st.append(patient.getHospitalID().trim());
                st.append(DELIMITER);
                st.append(patient.getPassword());
                st.append(DELIMITER);
                st.append(patient.getName().trim());
                st.append(DELIMITER);
                st.append(patient.getAge());
                st.append(DELIMITER);
                st.append(patient.getGender());
                st.append(DELIMITER);
                st.append(patient.getDateOfBirth().trim());
                st.append(DELIMITER);
                st.append(patient.getPhoneNumber().trim());
                st.append(DELIMITER);
                st.append(patient.getEmail().trim());
                st.append(DELIMITER);
                st.append(patient.getBloodType().trim());
                out.println(st);
            }

            out.close();

            out = new PrintWriter(new FileWriter("data\\Doctors.txt", false)); // Overwrite the users into the
                                                                               // respective txt file

            out.println(DOCTOR_HEADER); // Print header first
            for (Doctor doctor : doctors) {
                st = new StringBuilder();
                st.append(doctor.getHospitalID().trim());
                st.append(DELIMITER);
                st.append(doctor.getPassword().trim());
                st.append(DELIMITER);
                st.append(doctor.getName().trim());
                st.append(DELIMITER);
                st.append(doctor.getAge());
                st.append(DELIMITER);
                st.append(doctor.getGender());
                out.println(st);
            }
        } finally {
            out.close();
        }

    }

    // Read Appointments.txt file
    public static ArrayList<Appointment> readAppointments(String fileName) throws IOException {
        ArrayList<Appointment> dataAppt = new ArrayList<Appointment>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Scanner sc;

        sc = new Scanner(new FileInputStream(fileName));
        try {
            while (sc.hasNextLine()) {
                String st = sc.nextLine();
                if (!(st.contains(APPT_HEADER))) { // NOT HEADER
                    StringTokenizer star = new StringTokenizer(st, DELIMITER);
                    String appointmentID = star.nextToken().trim();
                    String patientID = star.nextToken().trim();
                    String doctorID = star.nextToken().trim();
                    LocalDateTime dateTime = LocalDateTime.parse(star.nextToken().trim(), timeFormatter);
                    String status = star.nextToken().trim();
                
                    Appointment appt = new Appointment(appointmentID, patientID, doctorID, dateTime, status);
                    dataAppt.add(appt);




                }
            }
        } finally {
            sc.close();
        }

        return dataAppt;

    }
    // Save or update Appointments.txt file
    public static void saveAppointments(ArrayList<Appointment> appointments) throws IOException {
        PrintWriter out;
        StringBuilder st;

        out = new PrintWriter(new FileWriter("data\\Appointments.txt", false)); // Overwrite the appointments
        out.println(APPT_HEADER); // Print header first

        try {
            for (Appointment appt : appointments) {
                st = new StringBuilder();
                st.append(appt.getAppointmentID().trim());
                st.append(DELIMITER);
                st.append(appt.getPatientID().trim());
                st.append(DELIMITER);
                st.append(appt.getDoctorID().trim());
                st.append(DELIMITER);
                st.append(appt.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                st.append(DELIMITER);
                st.append(appt.getStatus());
                out.println(st);
            }

        } finally {
            out.close();
        }
    }


    //Read Doctor Schedule
    public static ArrayList<Schedule> readSchedule(String fileName) throws IOException {
        ArrayList<Schedule> dataSchedule = new ArrayList<Schedule>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner sc;

        sc = new Scanner(new FileInputStream(fileName));

        try {
            while (sc.hasNextLine()) {
                String st = sc.nextLine();
                if (!(st.contains(SCHEDULE_HEADER))) { // NOT HEADER
                    StringTokenizer star = new StringTokenizer(st, DELIMITER);
                    if(star.countTokens() > 0){
                        String doctorID = star.nextToken().trim();
                        LocalDate date = LocalDate.parse(star.nextToken().trim(), timeFormatter);
                        String[] sessions = new String[8];
                        int counter = 0;
                        
                        //Store the sessions availability into 1 String array
                        while(star.hasMoreTokens() && counter <8){
                            sessions[counter] = star.nextToken().trim();
                            counter++;
                        }
                        
                        Schedule schedule = new Schedule(doctorID, date, sessions);
                        dataSchedule.add(schedule);
                    }
                  




                }
            }
        } finally {
            sc.close();
        }

        return dataSchedule;

    }
}
