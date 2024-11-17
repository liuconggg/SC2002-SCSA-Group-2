package com.ntu.hms.manager.medicalrecord;

import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;

/**
 * MedicalRecordManagerInterface provides methods to manage and display medical records of patients
 * and doctors.
 */
public interface MedicalRecordManagerInterface {

  void showMedicalRecord(Patient patient);

  void showMedicalRecord(Doctor doctor);

  void updateMedicalRecord();
}
