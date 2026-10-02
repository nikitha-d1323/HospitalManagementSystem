package hospital.exception;

// Thrown when we search for a doctor that does not exist
public class DoctorNotFoundException extends HospitalException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}