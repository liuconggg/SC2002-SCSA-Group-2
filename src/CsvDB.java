
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//For all operations in the CSV file
public class CsvDB {

    public static final String DELIMITER = ","; // for CSV file

    // Add cosntant header to filter the header row out
    public static final String HEADER = "ID,Password,Name,Age,Gender"; // Common Header in all user text file

    // The respective files header (to write the header of the file)
    public static final String PATIENT_HEADER = "Patient ID,Password,Name,Age,Gender,Date of Birth,Phone Number,Email,Blood Type";
    public static final String DOCTOR_HEADER = "Doctor ID,Password,Name,Age,Gender";
    public static final String PHARMACIST_HEADER = "Pharmacist ID,Password,Name,Age,Gender";
    public static final String ADMINISTRATOR_HEADER = "Administrator ID,Password,Name,Age,Gender";
    public static final String APPT_HEADER = "Appointment ID,Patient ID,Doctor ID,Date,Session,Status";
    public static final String APPT_OUTCOME_HEADER = "Appointment ID,Type of Service,Consultation Notes,Prescriptions,Prescription Status";
    public static final String SCHEDULE_HEADER = "Doctor ID,Date,Session 1,Session 2,Session 3,Session 4,Session 5,Session 6,Session 7,Session 8";
    public static final String MEDICATION_HEADER = "Medication ID,Medication Name,Stock Status,Alert,Quantity";
    public static final String REQUEST_HEADER = "Request ID,Medication Batch,Status,Pharmacist ID";
    public static final String TREATMENT_HEADER = "Patient ID,Appointment ID, diagnosis";
    public static final String DIAGNOSIS_HEADER = "Patient ID,Appointment ID, treatment";

    // Store the file names
    // public static final String userCSV = "data\\User.csv";
    public static final String patientCSV = "data/Patient.csv";
    public static final String doctorCSV = "data/Doctor.csv";
    public static final String administratorCSV = "data/Administrator.csv";
    public static final String pharmacistCSV = "data/Pharmacist.csv";
    public static final String appointmentCSV = "data/Appointment.csv";
    public static final String appointmentOutcomeRecordCSV = "data/AppointmentOutcomeRecord.csv";
    public static final String scheduleCSV = "data/Schedule.csv";
    public static final String medicationCSV = "data/Medication.csv";
    public static final String requestCSV = "data/ReplenishmentRequest.csv";

    // Read Patient.csv, Doctor.csv, Pharmacist.csv, Administrator.csv files
    public static ArrayList<User> readUsers() throws IOException {
        ArrayList<User> users = new ArrayList<User>();
        String[] csvFiles = {patientCSV, doctorCSV, administratorCSV, pharmacistCSV};
        BufferedReader reader;
        String line;

        for (String file : csvFiles) {
            reader = new BufferedReader(new FileReader(file));

            try {
                while ((line = reader.readLine()) != null) {
                    if (!(line.contains(HEADER))) { // IGNORE HEADER row
                        String[] fields = line.split(DELIMITER);

                        if (file.equals(patientCSV)) {
                            Patient patient = new Patient(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]),
                                    fields[4], fields[5], fields[6], fields[7], fields[8]);
                            users.add(patient);
                        } else if (file.equals(doctorCSV)) {
                            Doctor doctor = new Doctor(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]),
                                    fields[4]);
                            users.add(doctor);
                        } else if (file.equals(pharmacistCSV)) {
                            Pharmacist pharmacist = new Pharmacist(fields[0], fields[1], fields[2],
                                    Integer.parseInt(fields[3]), fields[4]);
                            users.add(pharmacist);
                        } else {
                            // Only administrator
                            Administrator admin = new Administrator(fields[0], fields[1], fields[2],
                                    Integer.parseInt(fields[3]), fields[4]);
                            users.add(admin);
                        }
                    }

                }
            } finally {
                reader.close();
            }
        }

        return users;
    }

    public static void saveUsers(ArrayList<User> users) throws IOException {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
        ArrayList<Administrator> administrators = new ArrayList<Administrator>();

        PrintWriter out;

        for (User user : users) { // Store the users into their respective arrays
            if (user instanceof Patient) {
                patients.add((Patient) user);
            } else if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            } else if (user instanceof Pharmacist) {
                pharmacists.add((Pharmacist) user);
            } else {
                administrators.add((Administrator) user);
            }
        }

        out = new PrintWriter(new FileWriter(patientCSV, false)); // Overwrite the patientCSV
        try {

            // Update patient.csv
            out.println(PATIENT_HEADER);
            for (Patient patient : patients) {
                out.printf("%s,%s,%s,%d,%s,%s,%s,%s,%s\n", patient.getHospitalID(), patient.getPassword(),
                        patient.getName(), patient.getAge(), patient.getGender(), patient.getDateOfBirth(),
                        patient.getPhoneNumber(), patient.getEmail(), patient.getBloodType());
            }

            out.close();

            // Update doctor.csv
            out = new PrintWriter(new FileWriter(doctorCSV, false)); // Overwrite the doctorCSV
            out.println(DOCTOR_HEADER);

            for (Doctor doctor : doctors) {
                out.printf("%s,%s,%s,%d,%s\n", doctor.getHospitalID(), doctor.getPassword(), doctor.getName(),
                        doctor.getAge(), doctor.getGender());
            }

            out.close();

            // Update pharmacist.csv
            out = new PrintWriter(new FileWriter(pharmacistCSV, false)); // Overwrite the pharmacistCSV
            out.println(PHARMACIST_HEADER);
            for (Pharmacist pharmacist : pharmacists) {
                out.printf("%s,%s,%s,%d,%s\n", pharmacist.getHospitalID(), pharmacist.getPassword(),
                        pharmacist.getName(), pharmacist.getAge(), pharmacist.getGender());
            }
            out.close();

            // Update admin.csv
            out = new PrintWriter(new FileWriter(administratorCSV, false)); // Overwrite the administratorCSV
            out.println(ADMINISTRATOR_HEADER);
            for (Administrator admin : administrators) {
                out.printf("%s,%s,%s,%d,%s\n", admin.getHospitalID(), admin.getPassword(), admin.getName(),
                        admin.getAge(), admin.getGender());
            }

        } finally {
            out.close();
        }

    }

    // Read Appointment.csv file
    public static ArrayList<Appointment> readAppointments() throws IOException {
        ArrayList<Appointment> appts = new ArrayList<Appointment>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        BufferedReader reader = new BufferedReader(new FileReader(appointmentCSV));
        String line;

        try {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!(line.contains(APPT_HEADER))) { // Ignore header row
                    String[] fields = line.split(DELIMITER);
                    Appointment appt = new Appointment(fields[0], fields[1], fields[2],
                            LocalDate.parse(fields[3], timeFormatter),
                            Integer.parseInt(fields[4]), fields[5]
                    );

                    appts.add(appt);
                }
            }
        } finally {
            reader.close();
        }

        return appts;
    }

    // Update Appointment.csv file
    public static void saveAppointments(ArrayList<Appointment> appointments) throws IOException {
        PrintWriter out;

        out = new PrintWriter(new FileWriter(appointmentCSV, false));
        out.println(APPT_HEADER);
        try {
            for (Appointment appointment : appointments) {
                out.printf("%s,%s,%s,%s,%s\n", appointment.getAppointmentID(), appointment.getPatientID(),
                        appointment.getDoctorID(),
                        appointment.getDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        appointment.getSession(),
                        appointment.getStatus()
                );
            }
        } finally {
            out.close();
        }
    }

    // Read Schedule.csv file
    public static ArrayList<Schedule> readSchedules() throws IOException {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        BufferedReader reader = new BufferedReader(new FileReader(scheduleCSV));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (!(line.contains(SCHEDULE_HEADER))) {
                    String[] fields = line.split(DELIMITER);

                    Schedule schedule = new Schedule(fields[0], LocalDate.parse(fields[1], timeFormatter),
                            Arrays.copyOfRange(fields, 2, 10)); // Start inclusive, end exclusive for Arrays.copyOfRange

                    schedules.add(schedule);
                }
            }
        } finally {
            reader.close();
        }

        return schedules;
    }

    // Read Medication.csv file
    public static ArrayList<Medication> readMedications() throws IOException {
        ArrayList<Medication> medications = new ArrayList<Medication>();
        BufferedReader reader = new BufferedReader(new FileReader(medicationCSV));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (!(line.contains(MEDICATION_HEADER))) {
                    String[] fields = line.split(DELIMITER);

                    Medication medication = new Medication(fields[0], fields[1], fields[2],
                            Boolean.parseBoolean(fields[3]),
                            Integer.parseInt(fields[4]));
                    medications.add(medication);
                }
            }
        } finally {
            reader.close();
        }

        return medications;
    }

    // Update Medication.csv
    public static void saveMedications(ArrayList<Medication> medications) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(medicationCSV, false));
        out.println(MEDICATION_HEADER);

        try {
            for (Medication medication : medications) {
                out.printf("%s,%s,%s,%b,%d\n", medication.getMedicationID(), medication.getMedicationName(),
                        medication.getStockStatus(), medication.getAlert(), medication.getTotalQuantity());
            }
        } finally {
            out.close();
        }
    }

    // Read ReplenishmentRequest.csv file
    public static ArrayList<ReplenishmentRequest> readRequest() throws IOException {
        ArrayList<ReplenishmentRequest> requests = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(requestCSV));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (!(line.contains(REQUEST_HEADER))) {
                    String[] fields = line.split(DELIMITER);

                    ReplenishmentRequest request = new ReplenishmentRequest(fields[0],
                            parseMedicationBatch(fields[1]),
                            fields[2], fields[3]);
                    requests.add(request);

                }
            }
        } finally {
            reader.close();
        }

        return requests;
    }

    // Helper method to parse medication batch from a string
    private static ArrayList<MedicationItem> parseMedicationBatch(String medicationBatchStr) {
        ArrayList<MedicationItem> medicationBatch = new ArrayList<>();
        String[] items = medicationBatchStr.split(";");

        for (String itemStr : items) {
            String[] itemFields = itemStr.split(":");
            if (itemFields.length == 3) {
                MedicationItem medicationItem = new MedicationItem(itemFields[0], itemFields[1],
                        Integer.parseInt(itemFields[2]));
                medicationBatch.add(medicationItem);
            }
        }

        return medicationBatch;
    }

    // Update ReplenishmentRequest.csv
    public static void saveReplenishmentRequests(ArrayList<ReplenishmentRequest> requests) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(requestCSV, false));
        out.println(REQUEST_HEADER);

        try {
            for (ReplenishmentRequest request : requests) {
                StringBuilder medicationBatchStr = new StringBuilder();
                for (MedicationItem item : request.getMedicationBatch()) {
                    medicationBatchStr.append(item.getMedicationID())
                            .append(":").append(item.getMedicationName())
                            .append(":").append(item.getQuantity())
                            .append(";");
                }
                // Remove trailing semicolon
                if (medicationBatchStr.length() > 0) {
                    medicationBatchStr.setLength(medicationBatchStr.length() - 1);
                }

                out.printf("%s,%s,%s,%s\n", request.getRequestID(), medicationBatchStr.toString(), request.getStatus(),
                        request.getPharmacistID());
            }
        } finally {
            out.close();
        }
    }

    // Read AppointmentOutcomeRecord.csv file
    public static ArrayList<AppointmentOutcomeRecord> readAppointmentOutcomeRecords() throws IOException {
        ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(appointmentOutcomeRecordCSV));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (!(line.contains(APPT_OUTCOME_HEADER))) {
                    String[] fields = line.split(DELIMITER);

                    AppointmentOutcomeRecord apptOutcomeRecord = new AppointmentOutcomeRecord(fields[0], fields[1],
                            fields[2], parseMedicationBatch(fields[3]), fields[4]);
                    apptOutcomeRecords.add(apptOutcomeRecord);
                }
            }
        } finally {
            reader.close();
        }

        return apptOutcomeRecords;
    }

    // Update ReplenishmentRequest.csv
    public static void saveAppointmentOutcomeRecords(ArrayList<AppointmentOutcomeRecord> apptOutcomeRecords)
            throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(appointmentOutcomeRecordCSV, false));
        out.println(APPT_OUTCOME_HEADER);

        try {
            for (AppointmentOutcomeRecord record : apptOutcomeRecords) {
                StringBuilder prescriptionStr = new StringBuilder();
                for (MedicationItem item : record.getPrescriptions()) {
                    prescriptionStr.append(item.getMedicationID())
                            .append(":").append(item.getMedicationName())
                            .append(":").append(item.getQuantity())
                            .append(";");
                }
                // Remove trailing semicolon
                if (prescriptionStr.length() > 0) {
                    prescriptionStr.setLength(prescriptionStr.length() - 1);
                }

                out.printf("%s,%s,%s,%s,%s\n", record.getAppointmentID(), record.getTypeOfService(),
                        record.getConsultationNotes(), prescriptionStr.toString(), record.getPrescriptionStatus());
            }
        } finally {
            out.close();
        }
    }

    public static void saveSchedules(ArrayList<Schedule> schedules) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(scheduleCSV, false))) {
            // Write the header row
            writer.println(SCHEDULE_HEADER);

            // Write each schedule entry
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Schedule schedule : schedules) {
                StringBuilder line = new StringBuilder();
                line.append(schedule.getDoctorID()).append(DELIMITER);
                line.append(schedule.getDate().format(dateFormatter)).append(DELIMITER);

                for (String session : schedule.getSession()) {
                    line.append(session).append(DELIMITER);
                }

                // Remove the trailing delimiter
                line.setLength(line.length() - 1);
                writer.println(line.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving schedules: " + e.getMessage());
        }
    }
}
