package com.ntu.hns.util;

import com.ntu.hns.model.MedicationItem;
import com.opencsv.bean.AbstractBeanField;
import java.util.ArrayList;
import java.util.List;

public class MedicationItemListConverter extends AbstractBeanField<List<MedicationItem>, String> {

  @Override
  protected List<MedicationItem> convert(String value) {
    List<MedicationItem> prescriptions = new ArrayList<>();
    if (value == null || value.trim().isEmpty()) {
      return prescriptions;
    }

    // Split the input value by ';' to get each medication item
    String[] items = value.split(";");
    for (String item : items) {
      if (item.trim().isEmpty()) {
        continue;
      }

      // Split each item by ':' to get medicationID, name, and quantity
      String[] parts = item.split(":");
      if (parts.length == 3) {
        String medicationID = parts[0];
        String medicationName = parts[1];
        int quantity = Integer.parseInt(parts[2]);

        // Create a new MedicationItem object
        MedicationItem medicationItem = new MedicationItem(medicationID, medicationName, quantity);
        prescriptions.add(medicationItem);
      }
    }

    return prescriptions;
  }

  @Override
  protected String convertToWrite(Object value) {
    @SuppressWarnings("unchecked")
    List<MedicationItem> prescriptions = (List<MedicationItem>) value;
    StringBuilder sb = new StringBuilder();

    for (MedicationItem item : prescriptions) {
      sb.append(item.getMedicationID())
          .append(":")
          .append(item.getMedicationName())
          .append(":")
          .append(item.getQuantity())
          .append(";");
    }

    return sb.toString();
  }
}
