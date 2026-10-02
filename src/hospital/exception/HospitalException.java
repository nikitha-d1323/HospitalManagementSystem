package hospital.exception;

// Base class for all our own exceptions.
// "extends Exception" makes it a CHECKED exception: Java forces us to handle it.
public class HospitalException extends Exception {
    public HospitalException(String message) {
        super(message);
    }
}