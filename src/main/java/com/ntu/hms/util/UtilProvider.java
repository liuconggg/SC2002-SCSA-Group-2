package com.ntu.hms.util;

import com.ntu.hms.CsvDB;
import com.ntu.hms.model.AppointmentOutcomeRecord;
import com.ntu.hms.model.Schedule;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import java.util.List;
import java.util.stream.Collectors;

public class UtilProvider {

  public static Patient getPatientById(String patientID, List<Patient> patients) {
    return patients
        .stream()
        .filter(patient -> patient.getHospitalID().equals(patientID))
        .findFirst()
        .orElse(null);
  }

  public static Doctor getDoctorById(String doctorID) {
    return CsvDB.readDoctors()
        .stream()
        .filter(doctor -> doctor.getHospitalID().equals(doctorID))
        .findFirst()
        .orElse(null);
  }

  public static List<Schedule> getScheduleByDoctorID(String doctorID) {
    return CsvDB.readSchedules()
        .stream()
        .filter(schedule -> schedule.getDoctorID().equals(doctorID))
        .collect(Collectors.toList());
  }

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
