package hospital.exception;

// Thrown when we search for a bill that does not exist
public class BillNotFoundException extends HospitalException {
    public BillNotFoundException(String message) {
        super(message);
    }
}