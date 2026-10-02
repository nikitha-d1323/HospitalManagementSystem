package hospital.util;

import hospital.exception.InvalidInputException;

// All the checking rules live here, in one place.
// "static" = we can call InputValidator.validateName(...) without creating an object.
public class InputValidator {

    public static String validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name cannot be empty.");
        }
        if (!name.trim().matches("[A-Za-z .]+")) {
            throw new InvalidInputException("Name can contain only letters, spaces and dots.");
        }
        return name.trim();
    }

    public static int validateAge(int age) throws InvalidInputException {
        if (age < 0 || age > 120) {
            throw new InvalidInputException("Age must be between 0 and 120.");
        }
        return age;
    }

    public static String validatePhone(String phone) throws InvalidInputException {
        // 10 digits, first digit 6-9
        if (phone == null || !phone.trim().matches("[6-9][0-9]{9}")) {
            throw new InvalidInputException("Phone must be 10 digits and start with 6, 7, 8 or 9.");
        }
        return phone.trim();
    }

    public static String validateBloodGroup(String bloodGroup) throws InvalidInputException {
        String bg = (bloodGroup == null) ? "" : bloodGroup.trim().toUpperCase();
        String[] allowed = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String valid : allowed) {
            if (valid.equals(bg)) {
                return bg;
            }
        }
        throw new InvalidInputException("Blood group must be one of A+, A-, B+, B-, AB+, AB-, O+, O-.");
    }
}