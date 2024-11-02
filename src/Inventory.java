
import java.util.ArrayList;

public interface Inventory {

    void viewInventory(ArrayList<Medication> inventory);

    void updateMedication(Medication medication, String medicationID, String medicationName, String stockStatus,
            boolean alert, int quantity);
}
