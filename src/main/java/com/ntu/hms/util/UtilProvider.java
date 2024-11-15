package com.ntu.hms.util;

import com.ntu.hms.CsvDB;
import com.ntu.hms.model.AppointmentOutcomeRecord;
import com.ntu.hms.model.Schedule;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class providing various methods to retrieve information about Patients, Doctors, and Appointments.
 */
public class UtilProvider {

  /**
   * Retrieves a Patient object from a list of patients based on the provided patient ID.
   *
   * @param patientID The ID of the patient to be retrieved.
   * @param patients A list of patients to search within.
   * @return The Patient object with the matching ID, or null if no match is found.
   */
  public static Patient getPatientById(String patientID, List<Patient> patients) {
    return patients
        .stream()
        .filter(patient -> patient.getHospitalID().equals(patientID))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves a Doctor object from a list of doctors based on the provided doctor ID.
   *
   * @param doctorID The ID of the doctor to be retrieved.
   * @return The Doctor object with the matching ID, or null if no match is found.
   */
  public static Doctor getDoctorById(String doctorID) {
    return CsvDB.readDoctors()
        .stream()
        .filter(doctor -> doctor.getHospitalID().equals(doctorID))
        .findFirst()
        .orElse(null);
  }

  /**
   * Retrieves a list of schedules for a given doctor based on the provided doctor ID.
   *
   * @param doctorID The ID of the doctor for whom to retrieve schedules.
   * @return A list of Schedule objects that belong to the doctor with the specified ID.
   */
  public static List<Schedule> getScheduleByDoctorID(String doctorID) {
    return CsvDB.readSchedules()
        .stream()
        .filter(schedule -> schedule.getDoctorID().equals(doctorID))
        .collect(Collectors.toList());
  }

  /**
   * Retrieves an AppointmentOutcomeRecord based on the provided appointment ID.
   *
   * @param appointmentID The ID of the appointment whose outcome record to retrieve.
   * @return The AppointmentOutcomeRecord with the matching appointment ID, or null if no match is found.
   */
  public static AppointmentOutcomeRecord getOutcomeByAppointmentID(String appointmentID) {
    return CsvDB.readAppointmentOutcomeRecords()
        .stream()
        .filter(
            appointmentOutcomeRecord ->
                appointmentOutcomeRecord.getAppointmentID().equals(appointmentID))
        .findFirst()
        .orElse(null);
  }
}
