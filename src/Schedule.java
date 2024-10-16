import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Schedule {

	private String doctorID;
	private LocalDateTime date;
	private String[] session = new String[8];

	public Schedule(){}
	public Schedule(String doctorID, LocalDateTime date, String[] session){
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

	public LocalDateTime getDate() {
		return this.date;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(LocalDateTime date) {
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

	public ArrayList<Schedule> getScheduleByDoctorID(String doctorID, ArrayList<Schedule> schedules){
		ArrayList<Schedule> doctorSchedule = new ArrayList<Schedule>();
		for(Schedule schedule: schedules){
			if(schedule.getDoctorID().equals(doctorID)){
				doctorSchedule.add(schedule);
			}
		}

		return doctorSchedule;
	}


}