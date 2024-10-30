import java.util.ArrayList;

public interface Inventory {
    void viewInventory(ArrayList<Medication> inventory);
    void updateInventory(ArrayList<Medication> inventory, String medicationName, int quantity);
}
