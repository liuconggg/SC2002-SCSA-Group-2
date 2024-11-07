package com.ntu.hns.model.users;

import com.ntu.hns.*;
import com.ntu.hns.enums.MedicationStatus;
import com.ntu.hns.enums.ReplenishmentStatus;
import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.inventory.InventoryManager;
import com.ntu.hns.manager.user.UserManager;
import com.ntu.hns.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class Administrator extends User implements Displayable {
    @Autowired private UserManager userManager;
    @Autowired private InventoryManager inventoryManager;
    @Autowired private AppointmentManager appointmentManager;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Administrator() {}

    public Administrator(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    @Override
    public void displayMenu() {
        System.out.println("=== Administrator Menu ===");
        System.out.println("1. View and Manage Hospital Staff");
        System.out.println("2. View Appointment Details");
        System.out.println("3. View and Manage Medication Inventory");
        System.out.println("4. Approve Replenishment Requests");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }

    public void addUser() {
        userManager.addUser();
    }

    public void viewUsers() {
        userManager.showUsers();
    }

    public void updateUser() {
        userManager.updateUser();
    }

    // Method to delete a user
    public void deleteUser() {
        userManager.deleteUser();
    }

    // Method to view appointments, now accepting appointments as a parameter
    public void viewAppointment() {
        appointmentManager.showAppointment();
    }

    public void viewInventory() {
        inventoryManager.showInventory();
    }

    public void updateInventory() {
        inventoryManager.updateInventory();
    }

    public void addInventory() {
        inventoryManager.addInventory();
    }

    public void deleteInventory() {
        inventoryManager.deleteInventory();
    }

    public void handleReplenishmentRequest() {
        inventoryManager.handleReplenishmentRequest();
    }
}
