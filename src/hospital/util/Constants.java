package hospital.util;

// Fixed values used in many places. Written once here, so changing them is easy.
public final class Constants {

    private Constants() {
        // nobody should create a Constants object
    }

    public static final String HOSPITAL_NAME = "CITY CARE HOSPITAL";

    // file handling
    public static final String DATA_DIR = "data";
    public static final String PATIENT_FILE = "patients.txt";
    public static final String DOCTOR_FILE = "doctors.txt";
    public static final String STAFF_FILE = "staff.txt";
    public static final String APPOINTMENT_FILE = "appointments.txt";
    public static final String BILL_FILE = "bills.txt";
    public static final String DELIMITER = "|";          // separates the fields in one line
    public static final String DELIMITER_REGEX = "\\|";  // the same thing, written for split()
}