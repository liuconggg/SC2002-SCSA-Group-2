package com.ntu.hms;

import static com.ntu.hms.factory.SingletonFactory.*;

import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.inventory.InventoryManager;
import com.ntu.hms.manager.medicalrecord.MedicalRecordManager;
import com.ntu.hms.manager.schedule.ScheduleManager;
import com.ntu.hms.manager.user.UserManager;
import com.ntu.hms.model.*;
import com.ntu.hms.model.users.Administrator;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.model.users.Pharmacist;
import com.ntu.hms.model.users.User;
import com.ntu.hms.util.ScannerWrapper;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The CsvDB class provides methods for reading and writing data to and from various CSV files.
 * The class includes methods for handling different entities such as Users, Patients, Doctors,
 * Pharmacists, Administrators, Appointments, Treatments, Diagnoses, Schedules, Medications,
 * AppointmentOutcomeRecords, and ReplenishmentRequests.
 */
public class CsvDB {
  private static final String CSV_DELIMITER = ",";

  private static final String APPOINTMENT_HEADER =
      "Appointment ID,Patient ID,Doctor ID,Date,Session,Status";
  private static final String PATIENT_HEADER =
      "Patient ID,Password,Name,Age,Gender,Date of Birth,Phone Number,Email,Blood Type";
  private static final String DOCTOR_HEADER = "Doctor ID,Password,Name,Age,Gender";
  private static final String PHARMACIST_HEADER = "Pharmacist ID,Password,Name,Age,Gender";
  private static final String ADMINISTRATOR_HEADER = "Administrator ID,Password,Name,Age,Gender";
  private static final String SCHEDULE_HEADER =
      "Doctor ID,Date,Session 1,Session 2,Session 3,Session 4,Session 5,Session 6,Session 7,Session 8";
  private static final String MEDICATION_HEADER =
      "Medication ID,Medication Name,Stock Status,Alert,Quantity";
  private static final String DIAGNOSIS_HEADER = "Appointment ID,Patient ID,Diagnosis";
  private static final String TREATMENT_HEADER = "Appointment ID,Patient ID,Treatment";
  private static final String APPOINTMENT_OUTCOME_RECORD_HEADER = "Appointment ID,Type of Service,Consultation Notes,Prescriptions,Prescription Status";
  private static final String REPLENISHMENT_REQUEST_HEADER = "Request ID,Medication Batch,Status,Pharmacist ID";


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

  /**
   * Creates a Path instance from the specified path string.
   *
   * @param pathString the path string, typically a relative path to a resource.
   * @return a Path instance corresponding to the provided path string.
   * @throws RuntimeException if the URI syntax is incorrect.
   */
  private static Path createPath(String pathString) {
    try {
      return Paths.get(ClassLoader.getSystemResource(pathString).toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Reads a CSV file from the given path and maps each line to an instance of the specified class.
   *
   * @param <T> the type of the class to which each line of the CSV should be mapped
   * @param csvPath the path to the CSV file
   * @param clazz the class type to which each line of the CSV should be mapped
   * @return a list of instances of the specified class, where each instance represents a line in the CSV, excluding the header
   * @throws RuntimeException if the specified CSV file cannot be read
   */
  private static <T> List<T> readCsv(String csvPath, Class<T> clazz) {
    ClassLoader classLoader = CsvDB.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(csvPath);
    try (BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {

      // Use streams to read the CSV data
      return bufferedReader
          .lines()
          .skip(1) // Skip the header line
          .map(line -> mapToInstance(line, clazz)) // Map each line to an instance of T
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Maps a CSV line to an instance of the specified class type by parsing the values and assigning
   * them to the fields of the newly created instance.
   *
   * @param <T> the type of the class to be instantiated
   * @param line the CSV line containing the data
   * @param clazz the class type to which the CSV line should be mapped
   * @return an instance of the specified class type, populated with data from the CSV line,
   *         or null if an error occurs during instantiation or data mapping
   */
  private static <T> T mapToInstance(String line, Class<T> clazz) {
    try {
      // Create an instance of T using reflection
      T instance = clazz.getDeclaredConstructor().newInstance();

      String[] data = line.split(CSV_DELIMITER);
      Field[] csvFields = getCsvFields(clazz);
      IntStream.range(0, csvFields.length)
          .forEach(
              index -> {
                try {
                  Field field = csvFields[index];
                  field.setAccessible(true);

                  if (field.getType().equals(int.class)) {
                    field.set(instance, Integer.parseInt(data[index].trim()));
                  } else if (field.getType().equals(boolean.class)) {
                    field.set(instance, Boolean.parseBoolean(data[index].trim()));
                  } else if (field.getType().equals(String.class)) {
                    field.set(instance, data[index].trim());
                  } else if (field.getType().equals(LocalDate.class)) {
                    field.set(instance, LocalDate.parse(data[index], getDateTimeFormatter()));
                  } else if (field.getType().equals(List.class)
                      && field
                          .getGenericType()
                          .getTypeName()
                          .contains(MedicationItem.class.getSimpleName())) {
                    List<MedicationItem> medicationItems = parseMedicationItems(data[index]);
                    field.set(instance, medicationItems);
                  } else if (field.getName().equals("session")) {
                    String[] sessionsData = Arrays.copyOfRange(data, 2, data.length);
                    field.set(instance, sessionsData);
                  }
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });

      Field[] nonCsvFields = getNonCsvFields(clazz);
      IntStream.range(0, nonCsvFields.length)
          .forEach(
              index -> {
                try {
                  Field field = nonCsvFields[index];
                  field.setAccessible(true);

                  if (field.getType().equals(AppointmentManager.class)) {
                    field.set(instance, getAppointmentManager());
                  } else if (field.getType().equals(InventoryManager.class)) {
                    field.set(instance, getInventoryManager());
                  } else if (field.getType().equals(MedicalRecordManager.class)) {
                    field.set(instance, getMedicalRecordManager());
                  } else if (field.getType().equals(ScheduleManager.class)) {
                    field.set(instance, getScheduleManager());
                  } else if (field.getType().equals(UserManager.class)) {
                    field.set(instance, getUserManager());
                  } else if (field.getType().equals(ScannerWrapper.class)) {
                    field.set(instance, getScannerWrapper());
                  }
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });

      return instance;
    } catch (ReflectiveOperationException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Retrieves all fields marked as CSV fields from the specified class and its superclasses.
   *
   * @param clazz the class from which to retrieve CSV fields
   * @return an array of Fields that are designated as CSV fields
   */
  private static Field[] getCsvFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null) {
      List<Field> currentFields =
          Arrays.stream(clazz.getDeclaredFields())
              .filter(CsvDB::isCsvField)
              .collect(Collectors.toList());
      fields.addAll(0, currentFields);
      clazz = clazz.getSuperclass();
    }

    return fields.toArray(new Field[0]);
  }

  /**
   * Retrieves all fields that are not marked as CSV fields from the specified class and its superclasses.
   *
   * @param clazz the class from which to retrieve non-CSV fields
   * @return an array of Fields that are not designated as CSV fields
   */
  private static Field[] getNonCsvFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null) {
      List<Field> currentFields =
          Arrays.stream(clazz.getDeclaredFields())
              .filter(field -> !isCsvField(field))
              .collect(Collectors.toList());
      fields.addAll(0, currentFields);
      clazz = clazz.getSuperclass();
    }

    return fields.toArray(new Field[0]);
  }

  /**
   * Determines if a given field is a CSV-compatible field.
   *
   * @param field the field to be checked
   * @return true if the field is compatible with CSV processing, false otherwise
   */
  private static boolean isCsvField(Field field) {
    return !field.getType().equals(AppointmentManager.class)
        && !field.getType().equals(InventoryManager.class)
        && !field.getType().equals(MedicalRecordManager.class)
        && !field.getType().equals(ScheduleManager.class)
        && !field.getType().equals(UserManager.class)
        && !field.getType().equals(ScannerWrapper.class);
  }

  /**
   * Parses a CSV formatted string containing medication data into a list of MedicationItem objects.
   *
   * @param medicationData the CSV string containing medication data, where each medication entry is separated by a semicolon (;) and fields within each entry are separated by a colon
   *  (:)
   * @return a list of MedicationItem objects parsed from the CSV string
   */
  // Helper method to parse a CSV value into a list of Medication objects
  private static List<MedicationItem> parseMedicationItems(String medicationData) {
    List<MedicationItem> medicationItems = new ArrayList<>();

    // Split each medication entry by semicolon (;)
    String[] medicationEntries = medicationData.split(";");
    for (String entry : medicationEntries) {
      String[] fields = entry.split(":");

      // Assuming the fields are: medicationID, medicationName, quantity
      if (fields.length == 3) {
        String medicationID = fields[0].trim();
        String medicationName = fields[1].trim();
        int quantity = Integer.parseInt(fields[2].trim());

        // Create a Medication object
        MedicationItem medicationItem = new MedicationItem(medicationID, medicationName, quantity);
        medicationItems.add(medicationItem);
      }
    }

    return medicationItems;
  }

  /**
   * Reads and aggregates users from various sources (patients, doctors, pharmacists, and administrators).
   *
   * @return a list of all users, including patients, doctors, pharmacists, and administrators.
   */
  public static List<User> readUsers() {
    List<Patient> patients = readPatients();
    List<Doctor> doctors = readDoctors();
    List<Pharmacist> pharmacists = readPharmacists();
    List<Administrator> administrators = readAdministrators();

    return Stream.of(patients, doctors, pharmacists, administrators)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  /**
   * Reads and parses the patient data from the specified CSV file.
   *
   * @return a list of Patient instances populated with the data from the CSV file.
   */
  public static List<Patient> readPatients() {
    return readCsv(PATIENT_CSV_PATH, Patient.class);
  }

  /**
   * Reads and parses the doctor data from the specified CSV file.
   *
   * @return a list of Doctor instances populated with the data from the CSV file.
   */
  public static List<Doctor> readDoctors() {
    return readCsv(DOCTOR_CSV_PATH, Doctor.class);
  }

  /**
   * Reads and parses the pharmacist data from the specified CSV file.
   *
   * @return a list of Pharmacist instances populated with the data from the CSV file.
   */
  public static List<Pharmacist> readPharmacists() {
    return readCsv(PHARMACIST_CSV_PATH, Pharmacist.class);
  }

  /**
   * Reads and parses the administrator data from the specified CSV file.
   *
   * @return a list of Administrator instances populated with the data from the CSV file.
   */
  public static List<Administrator> readAdministrators() {
    return readCsv(ADMINISTRATOR_CSV_PATH, Administrator.class);
  }

  /**
   * Reads and parses the appointment data from the specified CSV file.
   *
   * @return a list of Appointment instances populated with the data from the CSV file.
   */
  // Appointment.csv file
  public static List<Appointment> readAppointments() {
    return readCsv(APPOINTMENT_CSV_PATH, Appointment.class);
  }

  /**
   * Reads and parses the treatment data from the specified CSV file.
   *
   * @return a list of Treatment instances populated with the data from the CSV file.
   */
  public static List<Treatment> readTreatments() {
    return readCsv(TREATMENT_CSV_PATH, Treatment.class);
  }

  /**
   * Reads and parses the replenishment request data from the specified CSV file.
   *
   * @return a list of ReplenishmentRequest instances populated with the data from the CSV file.
   */
  // ReplenishmentRequest.csv file
  public static List<ReplenishmentRequest> readReplenishmentRequests() {
    return readCsv(REPLENISHMENT_REQUEST_CSV_PATH, ReplenishmentRequest.class);
  }

  /**
   * Reads and parses the diagnosis data from the specified CSV file.
   *
   * @return a list of Diagnosis instances populated with the data from the CSV file.
   */
  public static List<Diagnosis> readDiagnoses() {
    return readCsv(DIAGNOSIS_CSV_PATH, Diagnosis.class);
  }

  /**
   * Reads and parses the schedule data from the specified CSV file.
   *
   * @return a list of Schedule instances populated with the data from the CSV file.
   */
  // Read Schedule.csv file
  public static List<Schedule> readSchedules() {
    return readCsv(SCHEDULE_CSV_PATH, Schedule.class);
  }

  /**
   * Reads and parses medication data from the specified CSV file.
   *
   * @return a list of Medication instances populated with the data from the CSV file.
   */
  // Read Medication.csv file
  public static List<Medication> readMedications() {
    return readCsv(MEDICATION_CSV_PATH, Medication.class);
  }

  /**
   * Reads and parses the appointment outcome data from the specified CSV file.
   *
   * @return a list of AppointmentOutcomeRecord instances populated with the data from the CSV file.
   */
  // Read AppointmentOutcomeRecord.csv file
  public static List<AppointmentOutcomeRecord> readAppointmentOutcomeRecords() {
    return readCsv(APPOINTMENT_OUTCOME_RECORD_CSV_PATH, AppointmentOutcomeRecord.class);
  }

  /**
   * Writes a list of objects to a CSV file.
   *
   * @param <T>        the type of objects in the modelList
   * @param csvPathString the path to the CSV file as a string
   * @param header     the header row for the CSV file
   * @param modelList  the list of objects to write to the CSV file
   */
  private static <T> void writeCsv(String csvPathString, String header, List<T> modelList) {
    Path csvPath = Paths.get("target", "classes", csvPathString);

    try (Writer writer = new FileWriter(csvPath.toFile())) {
      Class<?> clazz = modelList.get(0).getClass();

      // Write the CSV header
      writer.write(header);
      writer.write("\n");

      // Get fields that should be used for CSV output
      Field[] csvFields = getCsvFields(clazz);

      // Write each object's data as a CSV row using Streams
      String csvData =
          modelList
              .stream()
              .map(
                  item ->
                      Stream.of(csvFields)
                          .map(
                              field -> {
                                try {
                                  field.setAccessible(true);
                                  Object value = field.get(item);

                                  String csvValue = null;
                                  if (value instanceof String) {
                                    csvValue = (String) value;
                                  } else if (value instanceof Integer) {
                                    csvValue = Integer.toString((int) value);
                                  } else if (value instanceof LocalDate) {
                                    csvValue = ((LocalDate) value).format(getDateTimeFormatter());
                                  } else if (value instanceof Boolean) {
                                    csvValue = Boolean.toString((boolean) value);
                                  } else if (value instanceof List) {
                                    List<?> list = (List<?>) value;
                                    if (!list.isEmpty() && list.get(0) instanceof MedicationItem) {
                                      csvValue =
                                          list.stream()
                                              .map(
                                                  medicationItem -> {
                                                    MedicationItem medItem =
                                                        (MedicationItem) medicationItem;
                                                    return medItem.getMedicationID()
                                                        + ":"
                                                        + medItem.getMedicationName()
                                                        + ":"
                                                        + medItem.getQuantity();
                                                  })
                                              .collect(Collectors.joining(";"));
                                    }
                                  } else if (field.getName().equals("session")) {
                                    csvValue = String.join(CSV_DELIMITER, (String[]) value);
                                  }
                                  return csvValue;
                                } catch (IllegalAccessException e) {
                                  throw new RuntimeException("Failed to access field value", e);
                                }
                              })
                          .collect(Collectors.joining(CSV_DELIMITER)))
              .collect(Collectors.joining("\n"));

      writer.write(csvData);
      writer.write("\n");

    } catch (IOException e) {
      throw new RuntimeException("Error writing CSV file", e);
    }
  }

  /**
   * Saves a list of users by categorizing them into their respective roles
   * (Patient, Doctor, Pharmacist, Administrator) and then saving each
   * category separately.
   *
   * @param users the list of users to be saved, where users can be of types
   *              Patient, Doctor, Pharmacist, or Administrator.
   */
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

  /**
   * Saves a list of treatments to a CSV file by writing them to the specified
   * path with the appropriate CSV header.
   *
   * @param treatments the list of Treatment objects to be saved
   */
  public static void saveTreatment(List<Treatment> treatments) {
    writeCsv(TREATMENT_CSV_PATH, TREATMENT_HEADER, treatments);
  }

  /**
   * Saves a list of Diagnosis objects to a CSV file by writing them to the specified path with the appropriate CSV header.
   *
   * @param diagnoses the list of Diagnosis objects to be saved
   */
  public static void saveDiagnosis(List<Diagnosis> diagnoses) {
    writeCsv(DIAGNOSIS_CSV_PATH, DIAGNOSIS_HEADER, diagnoses);
  }

  /**
   * Saves a list of Appointment objects to a CSV file.
   *
   * @param appointments the list of Appointment objects to be saved
   */
  public static void saveAppointments(List<Appointment> appointments) {
    writeCsv(APPOINTMENT_CSV_PATH, APPOINTMENT_HEADER, appointments);
  }

  /**
   * Saves a list of Medication objects to a CSV file.
   *
   * @param medications the list of Medication objects to be saved
   */
  public static void saveMedications(List<Medication> medications) {
    writeCsv(MEDICATION_CSV_PATH, MEDICATION_HEADER, medications);
  }

  /**
   * Saves a list of Patient objects to a CSV file using the specified path and header.
   *
   * @param patients the list of Patient objects to be saved
   */
  public static void savePatients(List<Patient> patients) {
    writeCsv(PATIENT_CSV_PATH, PATIENT_HEADER, patients);
  }

  /**
   * Saves a list of Doctor objects to a CSV file using the specified path and header.
   *
   * @param doctors the list of Doctor objects to be saved
   */
  public static void saveDoctors(List<Doctor> doctors) {
    writeCsv(DOCTOR_CSV_PATH, DOCTOR_HEADER, doctors);
  }

  /**
   * Saves a list of pharmacists to a CSV file.
   *
   * @param pharmacists a list of Pharmacist objects to be saved.
   */
  public static void savePharmacists(List<Pharmacist> pharmacists) {
    writeCsv(PHARMACIST_CSV_PATH, PHARMACIST_HEADER, pharmacists);
  }

  /**
   * Saves a list of Administrator objects to a CSV file.
   *
   * @param administrators the list of Administrator objects to be saved to the CSV file
   */
  public static void saveAdministrators(List<Administrator> administrators) {
    writeCsv(ADMINISTRATOR_CSV_PATH, ADMINISTRATOR_HEADER, administrators);
  }

  /**
   * Saves a list of schedules to a CSV file.
   *
   * @param schedules the list of schedules to be saved
   */
  public static void saveSchedules(List<Schedule> schedules) {
    writeCsv(SCHEDULE_CSV_PATH, SCHEDULE_HEADER, schedules);
  }

  /**
   * Saves the given list of appointment outcome records to a CSV file.
   *
   * @param appointmentOutcomeRecords the list of appointment outcome records to be saved.
   */
  public static void saveAppointmentOutcomeRecords(
      List<AppointmentOutcomeRecord> appointmentOutcomeRecords) {
    writeCsv(
        APPOINTMENT_OUTCOME_RECORD_CSV_PATH,
        APPOINTMENT_OUTCOME_RECORD_HEADER,
        appointmentOutcomeRecords);
  }

  /**
   * Saves a list of replenishment requests to a CSV file.
   *
   * @param replenishmentRequests the list of ReplenishmentRequest objects to be saved
   */
  public static void saveReplenishmentRequests(List<ReplenishmentRequest> replenishmentRequests) {
    writeCsv(REPLENISHMENT_REQUEST_CSV_PATH, REPLENISHMENT_REQUEST_HEADER, replenishmentRequests);
  }
}
