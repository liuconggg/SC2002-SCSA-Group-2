package com.ntu.hms.manager.medicalrecord;

import static com.ntu.hms.util.UtilProvider.*;

import com.ntu.hms.CsvDB;
import com.ntu.hms.enums.AppointmentStatus;
import com.ntu.hms.enums.ScheduleStatus;
import com.ntu.hms.model.*;
import com.ntu.hms.model.users.Doctor;
import com.ntu.hms.model.users.Patient;
import com.ntu.hms.model.users.User;
import com.ntu.hms.util.ScannerWrapper;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordManager implements MedicalRecordManagerInterface {
  private final ScannerWrapper scanner;

  private MedicalRecordManager(ScannerWrapper scanner) {
    this.scanner = scanner;
  }

  @Override
  public void showMedicalRecord(Patient patient) {
    System.out.println("\n==================== Medical Record ====================");
    System.out.printf("ID            : %s\n", patient.getHospitalID());
    System.out.printf("Name          : %s\n", patient.getName());
    System.out.printf("Date of Birth : %s\n", patient.getDateOfBirth());
    System.out.printf("Gender        : %s\n", patient.getGender());
    System.out.printf("Contact       : %s\n", patient.getPhoneNumber());
    System.out.printf("Email         : %s\n", patient.getEmail());
    System.out.printf("Blood Type    : %s\n", patient.getBloodType());
    System.out.println("=======================================================");

    // Display diagnosis and treatment information with prescriptions only
    System.out.println("\n================= Diagnoses and Treatments =============");
    int recordNo = 1;
    boolean hasRecords = false;

    for (Diagnosis diagnosis : CsvDB.readDiagnoses()) {
      for (Treatment treatment : CsvDB.readTreatments()) {
        if (diagnosis.getPatientId().equals(patient.getHospitalID())
            && treatment.getPatientID().equals(patient.getHospitalID())
            && diagnosis.getAppointmentId().equals(treatment.getAppointmentID())) {

          // Display basic record information
          System.out.printf("Record %d:\n", recordNo++);
          System.out.printf("  Appointment ID : %s\n", diagnosis.getAppointmentId());
          System.out.printf("  Diagnosis      : %s\n", diagnosis.getDiagnosis());
          System.out.printf("  Treatment      : %s\n", treatment.getTreatment());

          // Find and display only the prescriptions
          AppointmentOutcomeRecord outcome =
              getOutcomeByAppointmentID(diagnosis.getAppointmentId());
          if (outcome != null && !outcome.getPrescriptions().isEmpty()) {
            String prescriptions =
                outcome
                    .getPrescriptions()
                    .stream()
                    .map(MedicationItem::toString)
                    .reduce((p1, p2) -> p1 + ", " + p2)
                    .orElse("No prescriptions.");
            System.out.printf("  Prescriptions  : %s\n", prescriptions);
          } else {
            System.out.println("  Prescriptions  : No prescriptions available.");
          }

          System.out.println("-------------------------------------------------------");
          hasRecords = true;
        }
      }
    }

    if (!hasRecords) {
      System.out.println("No diagnoses or treatments found for this patient.");
    }
    System.out.println("=======================================================");
  }

  @Override
  public void showMedicalRecord(Doctor doctor) {
    ArrayList<String> uniquePatientIds = new ArrayList<>();
    ArrayList<User> patientsUnderCare = new ArrayList<>();
    Patient currentPatient = null;

    // Step 1: Filter all confirmed schedules for the logged-in doctor
    for (Schedule schedule : CsvDB.readSchedules()) {
      if (schedule.getDoctorID().equals(doctor.getHospitalID())) {
        for (int i = 0; i < schedule.getSession().length; i++) {
          String sessionInfo = schedule.getSession()[i];
          if (sessionInfo.contains(ScheduleStatus.CONFIRMED.name())) {
            String patientId = sessionInfo.split("-")[0]; // Extract the patient ID
            if (!uniquePatientIds.contains(patientId)) {
              uniquePatientIds.add(patientId);
            }
          }
        }
      }
    }

    // Step 2: Retrieve all user (patient) objects based on unique patient IDs
    for (String patientId : uniquePatientIds) {
      for (Patient patient : CsvDB.readPatients()) {
        if (patient.getHospitalID().equals(patientId)) {
          patientsUnderCare.add(patient);
          break;
        }
      }
    }

    // Step 3: Display the list of patients under the doctor's care
    if (patientsUnderCare.isEmpty()) {
      System.out.println("\nNo patients are currently under your care.");
      return;
    }

    boolean viewingRecords = true;
    while (viewingRecords) {
      System.out.println("\n=== Patients Under Your Care ===");
      int index = 1;
      for (User patient : patientsUnderCare) {
        System.out.printf(
            "%d. Patient Name: %s, Age: %d, Gender: %s\n",
            index, patient.getName(), patient.getAge(), patient.getGender());
        index++;
      }

      // Step 4: Let the doctor select a patient to view their medical records
      System.out.println(
          "\nSelect a patient to view their medical records (or press Enter to return):");
      String input = scanner.nextLine();

      if (input.trim().isEmpty()) {
        System.out.println("\nReturning to previous menu...");
        viewingRecords = false; // Exit the loop
        continue;
      }

      try {
        int choice = Integer.parseInt(input);

        if (choice > 0 && choice <= patientsUnderCare.size()) {
          // Retrieve the chosen patient object
          User selectedPatient = patientsUnderCare.get(choice - 1);

          // Display the patient's medical record information (assuming a method exists)
          System.out.println("\n=== Medical Records for " + selectedPatient.getName() + " ===");
          if (selectedPatient instanceof Patient) {
            currentPatient = (Patient) selectedPatient;
            System.out.println("\n==================== Medical Record ====================");
            System.out.printf("ID            : %s\n", currentPatient.getHospitalID());
            System.out.printf("Name          : %s\n", currentPatient.getName());
            System.out.printf("Date of Birth : %s\n", currentPatient.getDateOfBirth());
            System.out.printf("Gender        : %s\n", currentPatient.getGender());
            System.out.printf("Contact       : %s\n", currentPatient.getPhoneNumber());
            System.out.printf("Email         : %s\n", currentPatient.getEmail());
            System.out.printf("Blood Type    : %s\n", currentPatient.getBloodType());
            System.out.println("=======================================================");

            // Display diagnosis and treatment information with prescriptions only
            System.out.println("\n================= Diagnoses and Treatments =============");
            int recordNo = 1;
            boolean hasRecords = false;

            for (Diagnosis diagnosis : CsvDB.readDiagnoses()) {
              for (Treatment treatment : CsvDB.readTreatments()) {
                if (diagnosis.getPatientId().equals(currentPatient.getHospitalID())
                    && treatment.getPatientID().equals(currentPatient.getHospitalID())
                    && diagnosis.getAppointmentId().equals(treatment.getAppointmentID())) {

                  // Display basic record information
                  System.out.printf("Record %d:\n", recordNo++);
                  System.out.printf("  Appointment ID : %s\n", diagnosis.getAppointmentId());
                  System.out.printf("  Diagnosis      : %s\n", diagnosis.getDiagnosis());
                  System.out.printf("  Treatment      : %s\n", treatment.getTreatment());

                  // Find and display only the prescriptions
                  AppointmentOutcomeRecord outcome =
                      getOutcomeByAppointmentID(diagnosis.getAppointmentId());
                  if (outcome != null && !outcome.getPrescriptions().isEmpty()) {
                    String prescriptions =
                        outcome
                            .getPrescriptions()
                            .stream()
                            .map(MedicationItem::toString)
                            .reduce((p1, p2) -> p1 + ", " + p2)
                            .orElse("No prescriptions.");
                    System.out.printf("  Prescriptions  : %s\n", prescriptions);
                  } else {
                    System.out.println("  Prescriptions  : No prescriptions available.");
                  }

                  System.out.println("-------------------------------------------------------");
                  hasRecords = true;
                }
              }
            }

            if (!hasRecords) {
              System.out.println("\nNo diagnoses or treatments found for this patient.");
            }
            System.out.println("=======================================================");
          }
        } else {
          System.out.println("\nInvalid choice. Please select a valid patient number.");
        }
      } catch (NumberFormatException e) {
        System.out.println("\nInvalid input. Please enter a valid number.");
      }
    }
  }

  @Override
  public void updateMedicalRecord() {
    List<AppointmentOutcomeRecord> appointmentOutcomeRecords =
        CsvDB.readAppointmentOutcomeRecords();
    List<Diagnosis> diagnoses = CsvDB.readDiagnoses();
    List<Treatment> treatments = CsvDB.readTreatments();
    List<Medication> medications = CsvDB.readMedications();

    while (true) {
      // Display the appointment outcomes for user selection
      System.out.println("\nSelect an Appointment Outcome by number:\n");
      for (int i = 0; i < appointmentOutcomeRecords.size(); i++) {
        AppointmentOutcomeRecord outcome = appointmentOutcomeRecords.get(i);
        System.out.println((i + 1) + ". Appointment ID: " + outcome.getAppointmentID());
      }

      // Get user input for appointment outcome selection
      System.out.print(
          "\nEnter the number of the Appointment Outcome (or press Enter to return): ");
      String selectedOutcomeIndexInput = scanner.nextLine();

      if (selectedOutcomeIndexInput.trim().isEmpty()) {
        System.out.println("Returning to previous menu...");
        return;
      }

      int selectedOutcomeIndex;

      try {
        selectedOutcomeIndex = Integer.parseInt(selectedOutcomeIndexInput) - 1;
      } catch (NumberFormatException e) {
        System.out.println("\nInvalid input. Please enter a valid number.");
        continue;
      }

      if (selectedOutcomeIndex < 0 || selectedOutcomeIndex >= appointmentOutcomeRecords.size()) {
        System.out.println("\nInvalid selection. Please select a valid number.");
        continue;
      }

      // Get the selected appointment outcome
      AppointmentOutcomeRecord selectedOutcome =
          appointmentOutcomeRecords.get(selectedOutcomeIndex);
      String appointmentId = selectedOutcome.getAppointmentID();

      // Find the corresponding diagnosis and treatment
      Diagnosis selectedDiagnosis = null;
      for (Diagnosis diagnosis : diagnoses) {
        if (diagnosis.getAppointmentId().equals(appointmentId)) {
          selectedDiagnosis = diagnosis;
          break;
        }
      }

      Treatment selectedTreatment = null;
      for (Treatment treatment : treatments) {
        if (treatment.getAppointmentID().equals(appointmentId)) {
          selectedTreatment = treatment;
          break;
        }
      }

      // Ask the user if they want to update diagnosis
      if (selectedDiagnosis != null) {
        System.out.println("\nCurrent Diagnosis: " + selectedDiagnosis.getDiagnosis());
        String updateDiagnosisResponse;
        while (true) {
          System.out.print(
              "\nDo you want to update the diagnosis? (y/n or press Enter to return to main menu): ");
          updateDiagnosisResponse = scanner.nextLine();

          if (updateDiagnosisResponse.isEmpty()) {
            System.out.println("\nReturning to main menu...");
            return; // Exit the method and return to the main menu
          }

          if (updateDiagnosisResponse.equalsIgnoreCase("y")
              || updateDiagnosisResponse.equalsIgnoreCase("n")) {
            break;
          }

          System.out.println(
              "\nInvalid input. Please enter 'y' to update, 'n' to skip, or press Enter to return to the main menu.");
        }

        if (updateDiagnosisResponse.equalsIgnoreCase("y")) {
          System.out.print("\nEnter new diagnosis details (or press Enter to cancel): ");
          String newDiagnosisDetails = scanner.nextLine();
          if (!newDiagnosisDetails.trim().isEmpty()) {
            String currentDiagnosis = selectedDiagnosis.getDiagnosis();
            String updatedDiagnosis;

            // Check if current diagnosis already contains " - Updated:"
            if (currentDiagnosis.contains(" - Updated:")) {
              // Extract the original part before " - Updated:"
              int updatedIndex = currentDiagnosis.indexOf(" - Updated:");
              String originalDiagnosis = currentDiagnosis.substring(0, updatedIndex);
              updatedDiagnosis = originalDiagnosis + " - Updated: " + newDiagnosisDetails;
            } else {
              // Append " - Updated:" for the first time
              updatedDiagnosis = currentDiagnosis + " - Updated: " + newDiagnosisDetails;
            }

            selectedDiagnosis.setDiagnosis(updatedDiagnosis);
            System.out.println("\nDiagnosis updated.");
          } else {
            System.out.println("\nDiagnosis update cancelled.");
          }
        }
      } else {
        System.out.println("\nNo diagnosis found for this appointment.");
      }

      // Ask the user if they want to update treatment
      if (selectedTreatment != null) {
        System.out.println("\nCurrent Treatment: " + selectedTreatment.getTreatment());
        String updateTreatmentResponse;
        while (true) {
          System.out.print(
              "\nDo you want to update the treatment? (y/n or press Enter to return to main menu): ");
          updateTreatmentResponse = scanner.nextLine();

          if (updateTreatmentResponse.isEmpty()) {
            System.out.println("\nReturning to main menu...");
            return; // Exit the method and return to the main menu
          }

          if (updateTreatmentResponse.equalsIgnoreCase("y")
              || updateTreatmentResponse.equalsIgnoreCase("n")) {
            break;
          }

          System.out.println(
              "\nInvalid input. Please enter 'y' to update, 'n' to skip, or press Enter to return to the main menu.");
        }

        if (updateTreatmentResponse.equalsIgnoreCase("y")) {
          System.out.print("\nEnter new treatment details (or press Enter to cancel): ");
          String newTreatmentDetails = scanner.nextLine();
          if (!newTreatmentDetails.trim().isEmpty()) {
            String currentTreatment = selectedTreatment.getTreatment();
            String updatedTreatment;

            // Check if current treatment already contains " - Updated:"
            if (currentTreatment.contains(" - Updated:")) {
              // Extract the original part before " - Updated:"
              int updatedIndex = currentTreatment.indexOf(" - Updated:");
              String originalTreatment = currentTreatment.substring(0, updatedIndex);
              updatedTreatment = originalTreatment + " - Updated: " + newTreatmentDetails;
            } else {
              // Append " - Updated:" for the first time
              updatedTreatment = currentTreatment + " - Updated: " + newTreatmentDetails;
            }

            selectedTreatment.setTreatment(updatedTreatment);
            System.out.println("\nTreatment updated.");
          } else {
            System.out.println("\nTreatment update cancelled.");
          }
        }
      } else {
        System.out.println("\nNo treatment found for this appointment.");
      }

      if (AppointmentStatus.PENDING
          .name()
          .equalsIgnoreCase(selectedOutcome.getPrescriptionStatus())) {
        System.out.println("\nPrescription Status: " + selectedOutcome.getPrescriptionStatus());
        String editPrescriptionResponse;

        while (true) {
          System.out.print(
              "\nDo you want to edit the prescriptions? (y/n or press Enter to skip): ");
          editPrescriptionResponse = scanner.nextLine();

          if (editPrescriptionResponse.isEmpty()) {
            System.out.println("Skipping prescription edit...");
            return; // Exit to the main menu or continue to the next section
          }

          if (editPrescriptionResponse.equalsIgnoreCase("y")
              || editPrescriptionResponse.equalsIgnoreCase("n")) {
            break;
          }

          System.out.println(
              "\nInvalid input. Please enter 'y' to edit, 'n' to skip, or press Enter to return.");
        }

        if (editPrescriptionResponse.equalsIgnoreCase("y")) {
          System.out.print("\nEnter new prescription details (or press Enter to cancel): ");
          ArrayList<MedicationItem> prescribedMedicines = new ArrayList<>();
          boolean addingMedicines = true;

          while (addingMedicines) {
            System.out.println("\nAvailable Medicines:");
            int medIndex = 1;
            for (Medication med : medications) {
              System.out.printf(
                  "%d. %s (Available: %d units)\n",
                  medIndex, med.getMedicationName(), med.getTotalQuantity());
              medIndex++;
            }

            System.out.print(
                "\nSelect a medicine by number to prescribe (or press Enter to finish): ");
            String medInput = scanner.nextLine();

            if (medInput.trim().isEmpty()) {
              addingMedicines = false;
              continue;
            }

            try {
              int medChoice = Integer.parseInt(medInput);

              if (medChoice > 0 && medChoice <= medications.size()) {
                Medication selectedMed = medications.get(medChoice - 1);
                System.out.printf("\nEnter quantity for %s: ", selectedMed.getMedicationName());
                String quantityInput = scanner.nextLine();
                int quantity = Integer.parseInt(quantityInput);

                if (quantity > 0 && quantity <= selectedMed.getTotalQuantity()) {
                  System.out.println("\nCurrent Prescribing List:");
                  MedicationItem prescribedMed =
                      new MedicationItem(
                          selectedMed.getMedicationID(), selectedMed.getMedicationName(), quantity);
                  prescribedMedicines.add(prescribedMed);
                  for (MedicationItem med : prescribedMedicines) {
                    System.out.printf(
                        "- %s: %d units\n", med.getMedicationName(), med.getQuantity());
                  }
                } else {
                  System.out.println(
                      "\nInvalid quantity. Please enter a valid amount within the available units.");
                }
              } else {
                System.out.println("\nInvalid choice. Please select a valid medicine number.");
              }
            } catch (NumberFormatException e) {
              System.out.println("\nInvalid input. Please enter a valid number.");
            }
          }

          selectedOutcome.setPrescriptions(prescribedMedicines);
        } else {
          System.out.println("\nPlease enter valid input");
        }
      }

      CsvDB.saveAppointmentOutcomeRecords(appointmentOutcomeRecords);
      CsvDB.saveDiagnosis(diagnoses);
      CsvDB.saveTreatment(treatments);
    }
  }

  // Static method to access the builder
  public static MedicalRecordManagerBuilder medicalRecordManagerBuilder() {
    return new MedicalRecordManagerBuilder();
  }

  // Static inner Builder class
  public static class MedicalRecordManagerBuilder {
    private ScannerWrapper scanner;

    // Setter method for ScannerWrapper
    public MedicalRecordManagerBuilder setScanner(ScannerWrapper scanner) {
      this.scanner = scanner;
      return this; // Return the builder for chaining
    }

    // Method to build a MedicalRecordManager instance
    public MedicalRecordManager build() {
      // Validation to ensure required fields are set
      if (scanner == null) {
        throw new IllegalArgumentException("ScannerWrapper must not be null.");
      }
      return new MedicalRecordManager(scanner);
    }
  }
}
