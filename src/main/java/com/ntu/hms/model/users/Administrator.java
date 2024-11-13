package com.ntu.hms.model.users;

import static com.ntu.hms.MenuDisplayer.displayAdministratorMenu;

import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.inventory.InventoryManager;
import com.ntu.hms.manager.user.UserManager;

public class Administrator extends User {
  private UserManager userManager;
  private InventoryManager inventoryManager;
  private AppointmentManager appointmentManager;

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

  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

  public void setInventoryManager(InventoryManager inventoryManager) {
    this.inventoryManager = inventoryManager;
  }

  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }
}
