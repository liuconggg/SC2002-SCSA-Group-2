package com.ntu.hns.util;

import com.ntu.hns.CsvDB;
import com.ntu.hns.model.Schedule;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UtilProvider {
    private final CsvDB csvDB;

    @Autowired
    public UtilProvider(CsvDB csvDB) {
        this.csvDB = csvDB;
    }

    public Patient getPatientById(String patientID, List<Patient> patients) {
        return patients.stream()
                .filter(patient -> patient.getHospitalID().equals(patientID))
                .findFirst()
                .orElse(null);
    }

    public Doctor getDoctorById(String doctorID) {
        return csvDB.readDoctors().stream()
                .filter(doctor -> doctor.getHospitalID().equals(doctorID))
                .findFirst()
                .orElse(null);
    }

    public List<Schedule> getScheduleByDoctorID(String doctorID) {
        return csvDB.readSchedules().stream()
                .filter(schedule -> schedule.getDoctorID().equals(doctorID))
                .collect(Collectors.toList());
    }
}
