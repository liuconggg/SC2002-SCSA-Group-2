package com.ntu.hns;

import static com.ntu.hns.factory.SingletonFactory.*;

import com.ntu.hns.model.*;
import com.ntu.hns.model.users.Administrator;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;
import com.ntu.hns.model.users.Pharmacist;
import com.ntu.hns.model.users.User;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvDB {
  private static final String[] APPOINTMENT_HEADER = {
    "Appointment ID", "Patient ID", "Doctor ID", "Date", "Session", "Status"
  };
  private static final String[] PATIENT_HEADER = {
    "Patient ID",
    "Password",
    "Name",
    "Age",
    "Gender",
    "Date of Birth",
    "Phone Number",
    "Email",
    "Blood Type"
  };
  private static final String[] DOCTOR_HEADER = {"Doctor ID", "Password", "Name", "Age", "Gender"};
  private static final String[] PHARMACIST_HEADER = {
    "Pharmacist ID", "Password", "Name", "Age", "Gender"
  };
  private static final String[] ADMINISTRATOR_HEADER = {
    "Administrator ID", "Password", "Name", "Age", "Gender"
  };
  private static final String[] SCHEDULE_HEADER = {
    "Doctor ID",
    "Date",
    "Session 1",
    "Session 2",
    "Session 3",
    "Session 4",
    "Session 5",
    "Session 6",
    "Session 7",
    "Session 8"
  };
  private static final String[] MEDICATION_HEADER = {
    "Medication ID", "Medication Name", "Stock Status", "Alert", "Quantity"
  };
  private static final String[] DIAGNOSIS_HEADER = {"Appointment ID", "Patient ID", "Diagnosis"};
  private static final String[] TREATMENT_HEADER = {"Appointment ID", "Patient ID", "Treatment"};
  private static final String[] APPOINTMENT_OUTCOME_RECORD_HEADER = {
    "Request ID", "Medication Batch", "Status", "Pharmacist ID"
  };
  private static final String[] REPLENISHMENT_REQUEST_HEADER = {
    "Appointment ID",
    "Type of Service",
    "Consultation Notes",
    "Prescriptions",
    "Prescription Status"
  };

  private static final String PATIENT_CSV_PATH = "csvdb/Patient.csv";
  private static final String DOCTOR_CSV_PATH = "csvdb/Doctor.csv";
  private static final String PHARMACIST_CSV_PATH = "csvdb/Pharmacist.csv";
  private static final String ADMINISTRATOR_CSV_PATH = "csvdb/Administrator.csv";
  private static final String APPOINTMENT_CSV_PATH = "csvdb/Appointment.csv";
  private static final String DIAGNOSIS_CSV_PATH = "csvdb/Diagnosis.csv";
  private static final String SCHEDULE_CSV_PATH = "csvdb/Schedule.csv";
  private static final String TREATMENT_CSV_PATH = "csvdb/Treatment.csv";
  private static final String MEDICATION_CSV_PATH = "csvdb/Medication.csv";
  private static final String APPOINTMENT_OUTCOME_RECORD_CSV_PATH =
      "csvdb/AppointmentOutcomeRecord.csv";
  private static final String REPLENISHMENT_REQUEST_CSV_PATH = "csvdb/ReplenishmentRequest.csv";

  private static Path createPath(String pathString) {
    try {
      return Paths.get(ClassLoader.getSystemResource(pathString).toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> List<T> readCsv(String csvPath, Class<T> beanClass) {
    try (InputStream inputStream = CsvDB.class.getClassLoader().getResourceAsStream(csvPath);
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
      CsvToBean<T> csvToBean =
          new CsvToBeanBuilder<T>(reader)
              .withType(beanClass)
              .withSkipLines(1)
              .withIgnoreLeadingWhiteSpace(true)
              .build();

      return csvToBean.parse();
    } catch (IOException e) {
      e.printStackTrace();

      return null;
    }
  }

  public static List<User> readUsers() {
    List<Patient> patients = readPatients();
    List<Doctor> doctors = readDoctors();
    List<Pharmacist> pharmacists = readPharmacists();
    List<Administrator> administrators = readAdministrators();

    return Stream.of(patients, doctors, pharmacists, administrators)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  public static List<Patient> readPatients() {
    List<Patient> patients = readCsv(PATIENT_CSV_PATH, Patient.class);

    return Objects.requireNonNull(patients)
        .stream()
        .peek(
            patient -> {
              patient.setScanner(getScannerWrapper());
              patient.setAppointmentManager(getAppointmentManager());
              patient.setMedicalRecordManager(getMedicalRecordManager());
              patient.setScheduleManager(getScheduleManager());
            })
        .collect(Collectors.toList());
  }

  public static List<Doctor> readDoctors() {
    List<Doctor> doctors = readCsv(DOCTOR_CSV_PATH, Doctor.class);

    return Objects.requireNonNull(doctors)
        .stream()
        .peek(
            doctor -> {
              doctor.setAppointmentManager(getAppointmentManager());
              doctor.setMedicalRecordManager(getMedicalRecordManager());
              doctor.setScheduleManager(getScheduleManager());
            })
        .collect(Collectors.toList());
  }

  public static List<Pharmacist> readPharmacists() {
    List<Pharmacist> pharmacists = readCsv(PHARMACIST_CSV_PATH, Pharmacist.class);

    return Objects.requireNonNull(pharmacists)
        .stream()
        .peek(
            pharmacist -> {
              pharmacist.setScanner(getScannerWrapper());
              pharmacist.setInventoryManager(getInventoryManager());
              pharmacist.setAppointmentManager(getAppointmentManager());
            })
        .collect(Collectors.toList());
  }

  public static List<Administrator> readAdministrators() {
    List<Administrator> administrators = readCsv(ADMINISTRATOR_CSV_PATH, Administrator.class);

    return Objects.requireNonNull(administrators)
        .stream()
        .peek(
            administrator -> {
              administrator.setUserManager(getUserManager());
              administrator.setInventoryManager(getInventoryManager());
              administrator.setAppointmentManager(getAppointmentManager());
            })
        .collect(Collectors.toList());
  }

  // Appointment.csv file
  public static List<Appointment> readAppointments() {
    return readCsv(APPOINTMENT_CSV_PATH, Appointment.class);
  }

  public static List<Treatment> readTreatments() {
    return readCsv(TREATMENT_CSV_PATH, Treatment.class);
  }

  // ReplenishmentRequest.csv file
  public static List<ReplenishmentRequest> readReplenishmentRequests() {
    return readCsv(REPLENISHMENT_REQUEST_CSV_PATH, ReplenishmentRequest.class);
  }

  public static List<Diagnosis> readDiagnoses() {
    return readCsv(DIAGNOSIS_CSV_PATH, Diagnosis.class);
  }

  // Read Schedule.csv file
  public static List<Schedule> readSchedules() {
    return readCsv(SCHEDULE_CSV_PATH, Schedule.class);
  }

  // Read Medication.csv file
  public static List<Medication> readMedications() {
    return readCsv(MEDICATION_CSV_PATH, Medication.class);
  }

  // Read AppointmentOutcomeRecord.csv file
  public static List<AppointmentOutcomeRecord> readAppointmentOutcomeRecords() {
    return readCsv(APPOINTMENT_OUTCOME_RECORD_CSV_PATH, AppointmentOutcomeRecord.class);
  }

  private static <T> void writeCsv(String pathString, String[] header, List<T> csvBean) {
    Path csvPath = createPath(pathString);
    try (Writer writer = new FileWriter(csvPath.toFile())) {
      CSVWriter csvWriter =
          new CSVWriter(
              writer,
              CSVWriter.DEFAULT_SEPARATOR,
              CSVWriter.NO_QUOTE_CHARACTER,
              CSVWriter.DEFAULT_ESCAPE_CHARACTER,
              CSVWriter.DEFAULT_LINE_END);
      csvWriter.writeNext(header);

      StatefulBeanToCsv<T> sbc =
          new StatefulBeanToCsvBuilder<T>(writer)
              .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
              .build();
      sbc.write(csvBean);
    } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveUsers(List<User> users) {
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
    savePatients(patients);
    saveDoctors(doctors);
    savePharmacists(pharmacists);
    saveAdministrators(administrators);
  }

  public static void saveTreatment(List<Treatment> treatments) {
    writeCsv(TREATMENT_CSV_PATH, TREATMENT_HEADER, treatments);
  }

  public static void saveDiagnosis(List<Diagnosis> diagnoses) {
    writeCsv(DIAGNOSIS_CSV_PATH, DIAGNOSIS_HEADER, diagnoses);
  }

  public static void saveAppointments(List<Appointment> appointments) {
    writeCsv(APPOINTMENT_CSV_PATH, APPOINTMENT_HEADER, appointments);
  }

  public static void saveMedications(List<Medication> medications) {
    writeCsv(MEDICATION_CSV_PATH, MEDICATION_HEADER, medications);
  }

  public static void savePatients(List<Patient> patients) {
    writeCsv(PATIENT_CSV_PATH, PATIENT_HEADER, patients);
  }

  public static void saveDoctors(List<Doctor> doctors) {
    writeCsv(DOCTOR_CSV_PATH, DOCTOR_HEADER, doctors);
  }

  public static void savePharmacists(List<Pharmacist> pharmacists) {
    writeCsv(PHARMACIST_CSV_PATH, PHARMACIST_HEADER, pharmacists);
  }

  public static void saveAdministrators(List<Administrator> administrators) {
    writeCsv(ADMINISTRATOR_CSV_PATH, ADMINISTRATOR_HEADER, administrators);
  }

  public static void saveSchedules(List<Schedule> schedules) {
    writeCsv(SCHEDULE_CSV_PATH, SCHEDULE_HEADER, schedules);
  }

  public static void saveAppointmentOutcomeRecords(
      List<AppointmentOutcomeRecord> appointmentOutcomeRecords) {
    writeCsv(
        APPOINTMENT_OUTCOME_RECORD_CSV_PATH,
        APPOINTMENT_OUTCOME_RECORD_HEADER,
        appointmentOutcomeRecords);
  }

  public static void saveReplenishmentRequests(List<ReplenishmentRequest> replenishmentRequests) {
    writeCsv(REPLENISHMENT_REQUEST_CSV_PATH, REPLENISHMENT_REQUEST_HEADER, replenishmentRequests);
  }
}
