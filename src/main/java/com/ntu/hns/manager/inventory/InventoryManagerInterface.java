package com.ntu.hns.manager.inventory;

import com.ntu.hns.model.users.Pharmacist;

public interface InventoryManagerInterface {

    void showInventory();

    void processReplenishmentRequest(Pharmacist pharmacist);

    void addInventory();

    void updateInventory();

    void deleteInventory();

    void handleReplenishmentRequest();
}
