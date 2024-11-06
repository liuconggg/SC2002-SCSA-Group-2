
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Schedule {

    private String doctorID;
    private LocalDate date;
    private String[] session = new String[8];

    public Schedule() {
    }

    public Schedule(String doctorID, LocalDate date, String[] session) {
        this.doctorID = doctorID;
        this.date = date;
        this.session = session;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    /**
     *
     * @param doctorID
     */
    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public LocalDate getDate() {
        return this.date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String[] getSession() {
        return this.session;
    }

    /**
     *
     * @param session
     */
    public void setSession(String[] session) {
        this.session = session;
    }

    public ArrayList<Schedule> getScheduleByDoctorID(String doctorID, ArrayList<Schedule> schedules) {
        ArrayList<Schedule> doctorSchedule = new ArrayList<Schedule>();
        for (Schedule schedule : schedules) {
            if (schedule.getDoctorID().equals(doctorID)) {
                doctorSchedule.add(schedule);
            }
        }

        return doctorSchedule;
    }

    public static Schedule createDefaultSchedule(String doctorID, LocalDate date) {
        String[] defaultSessions = new String[8];
        for (int i = 0; i < defaultSessions.length; i++) {
            defaultSessions[i] = "Available"; // Set all sessions as available by default
        }
        return new Schedule(doctorID, date, defaultSessions);
    }

    // Helper Methods to Handle Session Status and Patient ID
    public String getSessionStatus(int sessionIndex) {
        String sessionInfo = this.session[sessionIndex];
        if (sessionInfo.equalsIgnoreCase("Available") || sessionInfo.equalsIgnoreCase("Unavailable")) {
            return sessionInfo;
        }
        String[] parts = sessionInfo.split("-");
        return parts.length > 1 ? parts[1] : "Unknown";
    }

    public String getPatientIdFromSession(int sessionIndex) {
        String sessionInfo = this.session[sessionIndex];
        if (sessionInfo.contains("-")) {
            return sessionInfo.split("-")[0];
        }
        return null;
    }

    public void setSessionStatus(int sessionIndex, String status) {
        String patientId = getPatientIdFromSession(sessionIndex);
        if (patientId != null) {
            this.session[sessionIndex] = patientId + "-" + status;
        } else if (status.equals("Available")) {
            this.session[sessionIndex] = "Available";
        }
    }

    public void acceptAppointment(int sessionIndex) {
        setSessionStatus(sessionIndex, ScheduleStatus.CONFIRMED.name());
    }

    public void declineAppointment(int sessionIndex) {
        // add logic here
        this.session[sessionIndex] = "Available";
    }

}
