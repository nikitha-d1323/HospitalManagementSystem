package hospital.exception;

// Thrown when we search for a patient that does not exist
public class PatientNotFoundException extends HospitalException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}