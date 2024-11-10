package com.ntu.hns.manager.medicalrecord;

import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;

public interface MedicalRecordManagerInterface {

  void showMedicalRecord(Patient patient);

  void showMedicalRecord(Doctor doctor);

  void updateMedicalRecord();
}
