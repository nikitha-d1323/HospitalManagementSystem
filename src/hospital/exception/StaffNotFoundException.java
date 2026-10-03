package hospital.exception;

// Thrown when we search for a staff member that does not exist
public class StaffNotFoundException extends HospitalException {
    public StaffNotFoundException(String message) {
        super(message);
    }
}