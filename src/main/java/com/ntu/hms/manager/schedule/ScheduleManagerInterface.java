package com.ntu.hms.manager.schedule;

import com.ntu.hms.model.users.Doctor;

/**
 * Interface for managing schedules in a healthcare system. Provides methods to display weekly
 * schedules and set or update availability.
 */
public interface ScheduleManagerInterface {

  void showWeeklySchedule();

  void showWeeklySchedule(Doctor doctor);

  void setAvailability(Doctor doctor);

  void updateSchedule(Doctor doctor);
}
