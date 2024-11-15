package com.ntu.hms.model.users;

import static com.ntu.hms.MenuDisplayer.displayAdministratorMenu;

import com.ntu.hms.manager.appointment.AppointmentManager;
import com.ntu.hms.manager.inventory.InventoryManager;
import com.ntu.hms.manager.user.UserManager;

/**
 * Represents an Administrator in the hospital management system.
 * An Administrator has heightened privileges including managing users, inventories, and appointments.
 */
public class Administrator extends User {
  private UserManager userManager;
  private InventoryManager inventoryManager;
  private AppointmentManager appointmentManager;

  /**
   * Default constructor for the Administrator class.
   * This initializes a new instance of an Administrator with default values.
   */
  public Administrator() {}

  /**
   * Constructs a new Administrator with the specified details.
   *
   * @param hospitalID the unique identifier assigned by the hospital
   * @param password the administrator's password for authentication
   * @param name the administrator's full name
   * @param age the administrator's age
   * @param gender the administrator's gender
   */
  public Administrator(String hospitalID, String password, String name, int age, String gender) {
    super(hospitalID, password, name, age, gender);
  }

  /**
   * Displays the administrator menu options.
   * This method will call the displayAdministratorMenu method to show the available options
   * for the administrator to manage users, view appointments, handle inventory, and other tasks.
   */
  public void displayMenu() {
    displayAdministratorMenu();
  }

  /**
   * Adds a new user to the hospital management system.
   * This method delegates the actual user creation process to the `userManager` instance.
   * Depending on the specific implementation of `userManager.addUser()`,
   * this may involve collecting user information, creating a user instance,
   * and saving it to the data store.
   */
  public void addUser() {
    userManager.addUser();
  }

  /**
   * Displays a list of users in the hospital management system.
   * This method delegates the task of showing users to the 'userManager' instance.
   * The users can be filtered based on type, gender, and age range.
   */
  public void viewUsers() {
    userManager.showUsers();
  }

  /**
   * Updates the details of an existing user in the hospital management system.
   * This method prompts for user information to be updated and delegates the actual update process to the `userManager` instance.
   * If the specified user is found, their details are updated; otherwise, an appropriate message is shown.
   */
  public void updateUser() {
    userManager.updateUser();
  }

  /**
   * Deletes an existing user from the hospital management system.
   * This method delegates the user deletion process to the `userManager` instance.
   * It prompts the user for the Hospital ID of the staff to be removed and, if found,
   * deletes the user and saves the updated users list.
   */
  public void deleteUser() {
    userManager.deleteUser();
  }

  /**
   * Displays the appointment information.
   * This method utilizes the `appointmentManager` to show detailed appointment information,
   * potentially filtered by status such as Completed, Cancelled, Pending, No Show, or All.
   */
  public void viewAppointment() {
    appointmentManager.showAppointment();
  }

  /**
   * Displays the inventory details including medication ID, name, stock status, and quantity.
   * This method utilizes the `inventoryManager` to show the current inventory.
   */
  public void viewInventory() {
    inventoryManager.showInventory();
  }

  /**
   * Updates the inventory of medications in the system.
   * This method delegates the actual inventory update process to the `inventoryManager` instance.
   * If a user specifies a medication that already exists in the inventory,
   * its quantity will be updated, and appropriate stock status and alerts will be set.
   * If the medication does not exist, a notification will be displayed,
   * and the user will be prompted to use the `addInventory` method to add new medications.
   */
  public void updateInventory() {
    inventoryManager.updateInventory();
  }

  /**
   * Adds a new inventory item to the hospital management system.
   *
   * This method delegates the inventory addition process to the `inventoryManager` instance.
   * The actual implementation in `inventoryManager.addInventory()` handles the input prompts
   * for medication details, updates the inventory list, and saves the updated list.
   */
  public void addInventory() {
    inventoryManager.addInventory();
  }

  /**
   * Deletes an inventory item from the hospital management system.
   * This method delegates the deletion process to the `inventoryManager` instance,
   * which handles the removal of the specified medication from the inventory.
   */
  public void deleteInventory() {
    inventoryManager.deleteInventory();
  }

  /**
   * Handles the replenishment request process in the hospital management system.
   * This method delegates the task to the `inventoryManager` instance, which handles
   * the reading of pending replenishment requests, displaying them, and updating the
   * inventory based on the approval or denial of requests.
   *
   * It involves the following steps:
   * - Displaying pending replenishment requests.
   * - Allowing the administrator to select a request to approve or decline.
   * - Updating the status of the selected request.
   * - Updating the medication inventory if a request is approved.
   * - Saving the updated replenishment requests and inventory data.
   */
  public void handleReplenishmentRequest() {
    inventoryManager.handleReplenishmentRequest();
  }

  /**
   * Sets the UserManager instance for managing user-related operations.
   *
   * @param userManager the UserManager instance to be assigned
   */
  public void setUserManager(UserManager userManager) {
    this.userManager = userManager;
  }

  /**
   * Sets the InventoryManager instance for managing inventory operations.
   *
   * @param inventoryManager the InventoryManager instance to be assigned
   */
  public void setInventoryManager(InventoryManager inventoryManager) {
    this.inventoryManager = inventoryManager;
  }

  /**
   * Sets the AppointmentManager instance for managing appointment-related operations.
   *
   * @param appointmentManager the AppointmentManager instance to be assigned
   */
  public void setAppointmentManager(AppointmentManager appointmentManager) {
    this.appointmentManager = appointmentManager;
  }
}
