package com.ntu.hns.model.users;

import static com.ntu.hns.MenuDisplayer.displayAdministratorMenu;

import com.ntu.hns.manager.appointment.AppointmentManager;
import com.ntu.hns.manager.inventory.InventoryManager;
import com.ntu.hns.manager.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Administrator extends User {
    @Autowired private UserManager userManager;
    @Autowired private InventoryManager inventoryManager;
    @Autowired private AppointmentManager appointmentManager;

    /** Default constructor required for OpenCSV to instantiate object. */
    public Administrator() {}

    public Administrator(String hospitalID, String password, String name, int age, String gender) {
        super(hospitalID, password, name, age, gender);
    }

    public void displayMenu() {
        displayAdministratorMenu();
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

    public void deleteUser() {
        userManager.deleteUser();
    }

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
