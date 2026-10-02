package hospital.exception;

// Thrown when an appointment rule is broken (past date, doctor busy, not found ...)
public class AppointmentException extends HospitalException {
    public AppointmentException(String message) {
        super(message);
    }
}