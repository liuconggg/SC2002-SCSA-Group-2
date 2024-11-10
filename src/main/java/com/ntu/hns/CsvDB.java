package com.ntu.hns;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CsvDB {
  private static final String[] appointment_header = {
    "Appointment ID", "Patient ID", "Doctor ID", "Date", "Session", "Status"
  };
  private static final String[] patient_header = {
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
  private static final String[] doctor_header = {"Doctor ID", "Password", "Name", "Age", "Gender"};
  private static final String[] pharmacist_header = {
    "Pharmacist ID", "Password", "Name", "Age", "Gender"
  };
  private static final String[] administrator_header = {
    "Administrator ID", "Password", "Name", "Age", "Gender"
  };
  private static final String[] schedule_header = {
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
  private static final String[] medication_header = {
    "Medication ID", "Medication Name", "Stock Status", "Alert", "Quantity"
  };
  private static final String[] diagnosis_header = {"Appointment ID", "Patient ID", "Diagnosis"};
  private static final String[] treatment_header = {"Appointment ID", "Patient ID", "Treatment"};
  private static final String[] appointment_outcome_record_header = {
    "Request ID", "Medication Batch", "Status", "Pharmacist ID"
  };
  private static final String[] replenishment_request_header = {
    "Appointment ID",
    "Type of Service",
    "Consultation Notes",
    "Prescriptions",
    "Prescription Status"
  };

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
      CsvToBean<T> csvToBean =
          new CsvToBeanBuilder<T>(reader)
              .withType(beanClass)
              .withSkipLines(1)
              .withIgnoreLeadingWhiteSpace(true)
              .build();

      return csvToBean
          .parse()
          .stream()
          .peek(beanFactory::autowireBean)
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();

      return null;
    }
  }

  public List<User> readUsers() {
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

  private static <T> void writeCsv(Path csvPath, String[] header, List<T> csvBean) {
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

  public void saveUsers(List<User> users) {
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
    writeCsv(createPath(TREATMENT_CSV_PATH), treatment_header, treatments);
  }

  public static void saveDiagnosis(List<Diagnosis> diagnoses) {
    writeCsv(createPath(DIAGNOSIS_CSV_PATH), diagnosis_header, diagnoses);
  }

  public static void saveAppointments(List<Appointment> appointments) {
    writeCsv(createPath(APPOINTMENT_CSV_PATH), appointment_header, appointments);
  }

  public static void saveMedications(List<Medication> medications) {
    writeCsv(createPath(MEDICATION_CSV_PATH), medication_header, medications);
  }

  public static void savePatients(List<Patient> patients) {
    writeCsv(createPath(PATIENT_CSV_PATH), patient_header, patients);
  }

  public static void saveDoctors(List<Doctor> doctors) {
    writeCsv(createPath(DOCTOR_CSV_PATH), doctor_header, doctors);
  }

  public static void savePharmacists(List<Pharmacist> pharmacists) {
    writeCsv(createPath(PHARMACIST_CSV_PATH), pharmacist_header, pharmacists);
  }

  public static void saveAdministrators(List<Administrator> administrators) {
    writeCsv(createPath(ADMINISTRATOR_CSV_PATH), administrator_header, administrators);
  }

  public static void saveSchedules(List<Schedule> schedules) {
    writeCsv(createPath(SCHEDULE_CSV_PATH), schedule_header, schedules);
  }

  public static void saveAppointmentOutcomeRecords(
      List<AppointmentOutcomeRecord> appointmentOutcomeRecords) {
    writeCsv(
        createPath(APPOINTMENT_OUTCOME_RECORD_CSV_PATH),
        appointment_outcome_record_header,
        appointmentOutcomeRecords);
  }

  public static void saveReplenishmentRequests(List<ReplenishmentRequest> replenishmentRequests) {
    writeCsv(
        createPath(REPLENISHMENT_REQUEST_CSV_PATH),
        replenishment_request_header,
        replenishmentRequests);
  }
}
