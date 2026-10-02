package hospital.exception;

// Thrown when the user gives wrong data (bad age, bad phone, empty name ...)
public class InvalidInputException extends HospitalException {
    public InvalidInputException(String message) {
        super(message);
    }
}