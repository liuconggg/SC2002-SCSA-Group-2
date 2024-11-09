package com.ntu.hns.manager.schedule;

import com.ntu.hns.model.users.Doctor;

public interface ScheduleManagerInterface {

    void showWeeklySchedule();

    void showWeeklySchedule(Doctor doctor);

    void setAvailability(Doctor doctor);

    void updateSchedule(Doctor doctor);
}
