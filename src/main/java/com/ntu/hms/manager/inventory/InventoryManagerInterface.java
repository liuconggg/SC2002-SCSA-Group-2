package com.ntu.hms.manager.inventory;

import com.ntu.hms.model.users.Pharmacist;

public interface InventoryManagerInterface {

  void showInventory();

  void processReplenishmentRequest(Pharmacist pharmacist);

  void addInventory();

  void updateInventory();

  void deleteInventory();

  void handleReplenishmentRequest();
}
