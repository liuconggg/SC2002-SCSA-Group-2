package com.ntu.hms;

public final class MenuDisplayer {

  public static void displayPatientMenu() {
    System.out.println("\n=== Patient Menu ===");
    System.out.println("1. View Medical Record");
    System.out.println("2. Update Personal Information");
    System.out.println("3. View Available Appointment Slots"); // schedule.csv
    System.out.println("4. Schedule an Appointment"); // schedule.csv
    System.out.println("5. Reschedule an Appointment"); // apointment.csv
    System.out.println("6. Cancel an Appointment"); // appointment.csv
    System.out.println("7. View Scheduled Appointment"); // appointment.csv
    System.out.println("8. View Past Appointment Outcome Records");
    System.out.println("9. Logout");
    System.out.print("Enter your choice: ");
  }

  public static void displayDoctorMenu() {
    System.out.println("\n=== Doctor Menu ===");
    System.out.println("1. View Patient Medical Records");
    System.out.println("2. Update Patient Medical Records");
    System.out.println("3. View Personal Schedule");
    System.out.println("4. Set Availability for Appointments");
    System.out.println("5. Accept or Decline Appointment Requests");
    System.out.println("6. View Upcoming Appointment");
    System.out.println("7. Record Appointment Outcome");
    System.out.println("8. Logout\n");
    System.out.print("Enter your choice: ");
  }

  public static void displayPharmacistMenu() {
    System.out.println("\n=== Pharmacist Menu ===");
    System.out.println("1. View Appointment Outcome Record");
    System.out.println("2. Update Prescription Status");
    System.out.println("3. View Medication Inventory");
    System.out.println("4. Submit Replenishment Request");
    System.out.println("5. Logout");
    System.out.print("Enter your choice: ");
  }

  public static void displayAdministratorMenu() {
    System.out.println("=== Administrator Menu ===");
    System.out.println("1. View and Manage Hospital Staff");
    System.out.println("2. View Appointment Details");
    System.out.println("3. View and Manage Medication Inventory");
    System.out.println("4. Approve Replenishment Requests");
    System.out.println("5. Logout");
    System.out.print("Enter your choice: ");
  }
}