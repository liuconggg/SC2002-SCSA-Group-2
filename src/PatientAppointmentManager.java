public interface PatientAppointmentManager extends AppointmentInterface {
    public default void scheduleAppointment() {
    };

    public default void rescheduleAppointment() {
    };

    public default void cancelAppointment() {
    };
}
