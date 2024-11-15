package com.ntu.hms.util;

import com.ntu.hms.model.MedicationItem;
import com.opencsv.bean.AbstractBeanField;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicationItemListConverter is a custom converter that extends AbstractBeanField
 * to handle the conversion between a List of MedicationItem objects and a single
 * String representation. The converter is designed to work with OpenCSV for reading
 * and writing data.
 *
 * The String format expected for conversion to a List of MedicationItem is:
 * "medicationID1:medicationName1:quantity1;medicationID2:medicationName2:quantity2;..."
 * Each medication item is split by a semicolon (';') and each part of the medication
 * item is separated by a colon (':').
 *
 * The conversion from a List of MedicationItem to String generates a format where
 * each medication item is represented as "medicationID:medicationName:quantity"
 * separated by semicolons.
 */
public class MedicationItemListConverter extends AbstractBeanField<List<MedicationItem>, String> {

  /**
   * Converts a String representation of medication items to a list of MedicationItem objects.
   *
   * @param value the String containing medication items formatted as "medicationID:medicationName:quantity;...".
   * @return a List of MedicationItem objects parsed from the input String.
   */
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

  /**
   * Converts a list of MedicationItem objects into a single String representation.
   *
   * Each MedicationItem in the list is converted to the format "medicationID:medicationName:quantity"
   * and all items are concatenated together, separated by semicolons.
   *
   * @param value the list of MedicationItem objects to be converted.
   * @return a String representation of the list formatted as "medicationID:medicationName:quantity;...".
   */
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
