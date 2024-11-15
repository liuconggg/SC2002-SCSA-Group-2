package com.ntu.hms.manager.application;

import com.ntu.hms.model.users.Administrator;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.model.users.Pharmacist;

public interface ContextManagerInterface {

  void beginPatient(Patient patient);

  void beginDoctor(Doctor doctor);

  void beginPharmacist(Pharmacist pharmacist);

  void beginAdministrator(Administrator administrator);
}
