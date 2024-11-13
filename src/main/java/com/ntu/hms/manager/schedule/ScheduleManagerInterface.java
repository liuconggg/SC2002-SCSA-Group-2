package com.ntu.hms.manager.schedule;

import com.ntu.hms.model.users.Doctor;

public interface ScheduleManagerInterface {

  void showWeeklySchedule();

  void showWeeklySchedule(Doctor doctor);

  void setAvailability(Doctor doctor);

  void updateSchedule(Doctor doctor);
}
