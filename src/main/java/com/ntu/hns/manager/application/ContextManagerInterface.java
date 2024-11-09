package com.ntu.hns.manager.application;

import com.ntu.hns.model.users.Administrator;
import com.ntu.hns.model.users.Doctor;
import com.ntu.hns.model.users.Patient;
import com.ntu.hns.model.users.Pharmacist;

public interface ContextManagerInterface {

    void beginPatient(Patient patient);

    void beginDoctor(Doctor patient);

    void beginPharmacist(Pharmacist patient);

    void beginAdministrator(Administrator patient);
}
