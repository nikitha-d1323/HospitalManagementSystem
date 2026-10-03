package hospital.model;

import hospital.util.Constants;

// "extends" = INHERITANCE: a Patient IS-A Person
public class Patient extends Person {

    private String address;
    private String bloodGroup;
    private String medicalCondition;

    public Patient(int id, String name, int age, String gender, String phone,
                   String address, String bloodGroup, String medicalCondition) {
        super(id, name, age, gender, phone); // calls the Person constructor first
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.medicalCondition = medicalCondition;
    }

    // OVERRIDING: Patient's own version of the abstract method
    @Override
    public void displayDetails() {
        System.out.println("[PATIENT] " + basicInfo());
        System.out.println("          Blood Group: " + bloodGroup + " | Condition: " + medicalCondition);
        System.out.println("          Address: " + address);
    }

    // FILE HANDLING: turn this object into one line of text
    @Override
    public String toFileString() {
        return basicFileString() + Constants.DELIMITER + address + Constants.DELIMITER
                + bloodGroup + Constants.DELIMITER + medicalCondition;
    }

    // FILE HANDLING: turn one line of text back into a Patient object
    // "static" = we call it as Patient.fromFileString(line) without having a Patient yet
    public static Patient fromFileString(String line) {
        String[] p = line.split(Constants.DELIMITER_REGEX, -1);
        if (p.length != 8) {
            throw new IllegalArgumentException("A patient line needs 8 fields: " + line);
        }
        return new Patient(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3], p[4], p[5], p[6], p[7]);
    }
}