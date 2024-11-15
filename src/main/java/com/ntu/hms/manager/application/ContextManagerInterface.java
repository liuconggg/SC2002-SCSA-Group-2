package com.ntu.hms.manager.application;

import com.ntu.hms.model.users.Administrator;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.model.users.Pharmacist;

/**
 * The ContextManagerInterface defines methods for managing various user
 * interactions in the context of a healthcare management system. Implementations
 * of this interface should handle the specifics of how interactions for each type
 * of user are managed and displayed.
 */
public interface ContextManagerInterface {

  void beginPatient(Patient patient);

  void beginDoctor(Doctor doctor);

  void beginPharmacist(Pharmacist pharmacist);

  void beginAdministrator(Administrator administrator);
}
