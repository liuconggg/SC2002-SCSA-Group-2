package com.ntu.hms.manager.inventory;

import com.ntu.hms.model.users.Pharmacist;

/**
 * The InventoryManagerInterface provides a set of methods for managing inventory operations.
 * Implementations of this interface are responsible for handling various tasks related to inventory
 * such as displaying current inventory, processing replenishment requests, and updating or removing
 * inventory items.
 */
public interface InventoryManagerInterface {

  void showInventory();

  void processReplenishmentRequest(Pharmacist pharmacist);

  void addInventory();

  void updateInventory();

  void deleteInventory();

  void handleReplenishmentRequest();
}
