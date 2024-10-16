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

    // Add cosntant header to filter the header row out
    public static final String USER_HEADER = "ID|Password|Name|Role"; // Common Header in all user text file
    public static final String PATIENT_HEADER = "Patient ID|Name";
    public static final String DOCTOR_HEADER = "Doctor ID|Name";
    public static final String APPT_HEADER = "Appointment ID|Patient ID|Doctor ID|Date and Time|Status";
    public static final String SCHEDULE_HEADER = "Doctor ID|Date|Session 1|Session 2|Session 3|Session 4|Session 5|Session 6|Session 7|Session 8";

    // Read User.txt file
    public static ArrayList<User> readUsers(String fileName) throws IOException {
        ArrayList<User> users = new ArrayList<User>();
        Scanner sc = new Scanner(new FileInputStream(fileName));
        // Read user file
        try {
            while (sc.hasNextLine()) {
                String st = sc.nextLine();
                if (!(st.contains(USER_HEADER))) {
                    StringTokenizer star = new StringTokenizer(st, DELIMITER);
                    String id = star.nextToken().trim();
                    String password = star.nextToken();
                    String name = star.nextToken().trim();
                    String role = star.nextToken().trim();

                    // User user = new User(id, password, name, role);
                    if (role.equals("Patient")) users.add(new Patient(id, password, name, role));
                    else if (role.equals("Doctor")) users.add(new Doctor(id, password, name, role));
                    else if (role.equals("Pharmacist")) users.add(new Pharmacist(id, password, name, role));
                    else users.add(new Administrator(id, password, name, role));

                }
            }
        } finally {
            sc.close();
        }

        return users;

    }


    // Save or update User.txt file
    public static void saveUsers(ArrayList<User> users) throws IOException {
        StringBuilder st;
        PrintWriter out = new PrintWriter(new FileWriter("data\\User.txt", false)); //Allow overwrite of users
        out.println(USER_HEADER);

        try{
            for(User user: users){
                st = new StringBuilder();
                st.append(user.getHospitalID());
                st.append(DELIMITER);
                st.append(user.getPassword());
                st.append(DELIMITER);
                st.append(user.getName());
                st.append(DELIMITER);
                st.append(user.getRole());

                out.println(st);
            }
        } finally{
            out.close();
        }

    }

    // Read Appointments.txt file
    public static ArrayList<Appointment> readAppointments(String fileName) throws IOException {
        ArrayList<Appointment> usersAppt = new ArrayList<Appointment>();
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
                    usersAppt.add(appt);

                }
            }
        } finally {
            sc.close();
        }

        return usersAppt;

    }

    // Save or update Appointments.txt file
    public static void saveAppointments(ArrayList<Appointment> appointments) throws IOException {
        PrintWriter out;
        StringBuilder st;

        out = new PrintWriter(new FileWriter("data\\Appointments.txt", false)); // Overwrite the appointments
        out.println(APPT_HEADER);

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

    // Read Doctor Schedule
    public static ArrayList<Schedule> readSchedule(String fileName) throws IOException {
        ArrayList<Schedule> usersSchedule = new ArrayList<Schedule>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Scanner sc;

        sc = new Scanner(new FileInputStream(fileName));

        try {
            while (sc.hasNextLine()) {
                String st = sc.nextLine();
                if (!(st.contains(SCHEDULE_HEADER))) { // NOT HEADER
                    StringTokenizer star = new StringTokenizer(st, DELIMITER);
                    if (star.countTokens() > 0) {
                        String doctorID = star.nextToken().trim();
                        LocalDateTime date = LocalDateTime.parse(star.nextToken().trim(), timeFormatter); //LocalDate -> LocalDateTime (double check if there is error)
                        String[] sessions = new String[8];
                        int counter = 0;

                        // Store the sessions availability into 1 String array
                        while (star.hasMoreTokens() && counter < 8) {
                            sessions[counter] = star.nextToken().trim();
                            counter++;
                        }

                        Schedule schedule = new Schedule(doctorID, date, sessions);
                        usersSchedule.add(schedule);
                    }

                }
            }
        } finally {
            sc.close();
        }

        return usersSchedule;

    }
}
