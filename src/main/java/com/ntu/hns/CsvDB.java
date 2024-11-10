package com.ntu.hns;

import com.ntu.hns.model.*;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;
import com.ntu.hns.model.users.Pharmacist;
import com.ntu.hns.model.users.User;
import com.ntu.hns.model.users.Administrator;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//For all operations in the CSV file
@Service
public class CsvDB {
    public static final String currentDir = System.getProperty("user.dir");

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
    public static final String TREATMENT_HEADER = "Patient ID,Appointment ID, Diagnosis";
    public static final String DIAGNOSIS_HEADER = "Patient ID,Appointment ID, Treatment";

    // Store the file names
    // public static final String userCSV = "User.csv";
    public static final String patientCSV = Paths.get(currentDir, "data", "Patient.csv").toString().replaceFirst("/src", "");
    public static final String doctorCSV = Paths.get(currentDir, "data", "Doctor.csv").toString().replaceFirst("/src", "");
    public static final String administratorCSV = Paths.get(currentDir, "data", "Administrator.csv").toString().replaceFirst("/src", "");
    public static final String pharmacistCSV = Paths.get(currentDir, "data", "Pharmacist.csv").toString().replaceFirst("/src", "");
    public static final String appointmentCSV = Paths.get(currentDir, "data", "Appointment.csv").toString().replaceFirst("/src", "");
    public static final String appointmentOutcomeRecordCSV = Paths.get(currentDir, "data", "AppointmentOutcomeRecord.csv").toString().replaceFirst("/src", "");
    public static final String scheduleCSV = Paths.get(currentDir, "data", "Schedule.csv").toString().replaceFirst("/src", "");
    public static final String medicationCSV = Paths.get(currentDir, "data", "Medication.csv").toString().replaceFirst("/src", "");
    public static final String requestCSV = Paths.get(currentDir, "data", "ReplenishmentRequest.csv").toString().replaceFirst("/src", "");
    public static final String diagnosisCSV = Paths.get(currentDir, "data", "Diagnosis.csv").toString().replaceFirst("/src", "");
    public static final String treatmentCSV = Paths.get(currentDir, "data", "Treatment.csv").toString().replaceFirst("/src", "");

    private static final String PATIENT_CSV_PATH = "Patient.csv";
    private static final String DOCTOR_CSV_PATH = "Doctor.csv";
    private static final String PHARMACIST_CSV_PATH = "Pharmacist.csv";
    private static final String ADMINISTRATOR_CSV_PATH = "Administrator.csv";
    private static final String APPOINTMENT_CSV_PATH = "Appointment.csv";
    private static final String DIAGNOSIS_CSV_PATH = "Diagnosis.csv";
    private static final String SCHEDULE_CSV_PATH = "Schedule.csv";
    private static final String TREATMENT_CSV_PATH = "Treatment.csv";
    private static final String MEDICATION_CSV_PATH = "Medication.csv";
    private static final String APPOINTMENT_OUTCOME_RECORD_CSV_PATH = "AppointmentOutcomeRecord.csv";
    private static final String REPLENISHMENT_REQUEST_CSV_PATH = "ReplenishmentRequest.csv";

    private final ApplicationContext context;

    @Autowired
    public CsvDB(ApplicationContext context) {
        this.context = context;
    }

    private static Path createPath(String pathString) {
        try {
            return Paths.get(ClassLoader.getSystemResource(pathString).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> readCsv(String csvPath, Class<T> beanClass) {
        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        try (InputStream inputStream = CsvDB.class.getClassLoader().getResourceAsStream(csvPath);
             InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(beanClass)
                    .withSkipLines(1)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse().stream()
                    .peek(beanFactory::autowireBean)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public List<User> readUsersCsv() {
        List<Patient> patients = readCsv(PATIENT_CSV_PATH, Patient.class);
        List<Doctor> doctors = readCsv(DOCTOR_CSV_PATH, Doctor.class);
        List<Pharmacist> pharmacists = readCsv(PHARMACIST_CSV_PATH, Pharmacist.class);
        List<Administrator> administrators = readCsv(ADMINISTRATOR_CSV_PATH, Administrator.class);

        return Stream.of(patients, doctors, pharmacists, administrators)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Patient> readPatients() {
        return readCsv(PATIENT_CSV_PATH, Patient.class);
    }

    public List<Doctor> readDoctors() {
        return readCsv(DOCTOR_CSV_PATH, Doctor.class);
    }

    // Appointment.csv file
    public List<Appointment> readAppointments() {
        return readCsv(APPOINTMENT_CSV_PATH, Appointment.class);
    }

    public List<Treatment> readTreatments() {
        return readCsv(TREATMENT_CSV_PATH, Treatment.class);
    }

    // ReplenishmentRequest.csv file
    public List<ReplenishmentRequest> readReplenishmentRequests() {
        return readCsv(REPLENISHMENT_REQUEST_CSV_PATH, ReplenishmentRequest.class);
    }

    public List<Diagnosis> readDiagnoses() {
        return readCsv(DIAGNOSIS_CSV_PATH, Diagnosis.class);
    }

    // Read Schedule.csv file
    public List<Schedule> readSchedules() {
        return readCsv(SCHEDULE_CSV_PATH, Schedule.class);
    }

    // Read Medication.csv file
    public List<Medication> readMedications() {
        return readCsv(MEDICATION_CSV_PATH, Medication.class);
    }

    // Read AppointmentOutcomeRecord.csv file
    public List<AppointmentOutcomeRecord> readAppointmentOutcomeRecords() {
        return readCsv(APPOINTMENT_OUTCOME_RECORD_CSV_PATH, AppointmentOutcomeRecord.class);
    }

    private static <T> void writeCsv(Path csvPath, List<T> csvBean) {
        try (Writer writer = new FileWriter(csvPath.toFile())) {
            StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
            sbc.write(csvBean);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveToUsersCsv(List<User> users) {
        List<Patient> patients = new ArrayList<>();
        List<Doctor> doctors = new ArrayList<>();
        List<Pharmacist> pharmacists = new ArrayList<>();
        List<Administrator> administrators = new ArrayList<>();
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
        writeCsv(createPath(PATIENT_CSV_PATH), patients);
        writeCsv(createPath(DOCTOR_CSV_PATH), doctors);
        writeCsv(createPath(PHARMACIST_CSV_PATH), pharmacists);
        writeCsv(createPath(ADMINISTRATOR_CSV_PATH), administrators);
    }

    public static void saveTreatment(List<Treatment> treatments) {
        writeCsv(createPath(TREATMENT_CSV_PATH), treatments);
    }

    public static void saveDiagnosis(List<Diagnosis> diagnoses) {
        writeCsv(createPath(DIAGNOSIS_CSV_PATH), diagnoses);
    }

    public static void saveAppointments(List<Appointment> appointments) {
        writeCsv(createPath(APPOINTMENT_CSV_PATH), appointments);
    }

    public static void saveMedications(List<Medication> medications) {
        writeCsv(createPath(MEDICATION_CSV_PATH), medications);
    }

    public static void saveUsers(List<User> users) {
        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<Doctor> doctors = new ArrayList<>();
        ArrayList<Pharmacist> pharmacists = new ArrayList<>();
        ArrayList<Administrator> administrators = new ArrayList<>();

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

        try {
            out = new PrintWriter(new FileWriter(patientCSV, false)); // Overwrite the patientCSV
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            try {
                out = new PrintWriter(new FileWriter(doctorCSV, false)); // Overwrite the doctorCSV
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(DOCTOR_HEADER);

            for (Doctor doctor : doctors) {
                out.printf("%s,%s,%s,%d,%s\n", doctor.getHospitalID(), doctor.getPassword(), doctor.getName(),
                        doctor.getAge(), doctor.getGender());
            }

            out.close();

            // Update pharmacist.csv
            try {
                out = new PrintWriter(new FileWriter(pharmacistCSV, false)); // Overwrite the pharmacistCSV
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(PHARMACIST_HEADER);
            for (Pharmacist pharmacist : pharmacists) {
                out.printf("%s,%s,%s,%d,%s\n", pharmacist.getHospitalID(), pharmacist.getPassword(),
                        pharmacist.getName(), pharmacist.getAge(), pharmacist.getGender());
            }
            out.close();

            // Update admin.csv
            try {
                out = new PrintWriter(new FileWriter(administratorCSV, false)); // Overwrite the administratorCSV
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            out.println(ADMINISTRATOR_HEADER);
            for (Administrator admin : administrators) {
                out.printf("%s,%s,%s,%d,%s\n", admin.getHospitalID(), admin.getPassword(), admin.getName(),
                        admin.getAge(), admin.getGender());
            }

        } finally {
            out.close();
        }
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

    // Update com.ntu.hms.ReplenishmentRequest.csv
    public static void saveReplenishmentRequests(List<ReplenishmentRequest> requests) throws IOException {
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

    // Update com.ntu.hms.ReplenishmentRequest.csv
    public static void saveAppointmentOutcomeRecords(List<AppointmentOutcomeRecord> apptOutcomeRecords)
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

    public static void saveSchedules(List<Schedule> schedules) {
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
