package hospital.model;

import hospital.util.Constants;

// Doctor IS-A Person (inheritance)
public class Doctor extends Person {

    private String specialization;
    private int experience;
    private double consultationFee;

    public Doctor(int id, String name, int age, String gender, String phone,
                  String specialization, int experience, double consultationFee) {
        super(id, name, age, gender, phone);
        this.specialization = specialization;
        this.experience = experience;
        this.consultationFee = consultationFee;
    }

    // getters (the services need these)
    public String getSpecialization() { return specialization; }
    public int getExperience() { return experience; }
    public double getConsultationFee() { return consultationFee; }

    // Doctor's own version of displayDetails (overriding)
    @Override
    public void displayDetails() {
        System.out.println("[DOCTOR]  " + basicInfo());
        System.out.println("          Specialization: " + specialization
                + " | Experience: " + experience + " yrs | Fee: Rs. " + consultationFee);
    }

    @Override
    public String toFileString() {
        return basicFileString() + Constants.DELIMITER + specialization + Constants.DELIMITER
                + experience + Constants.DELIMITER + consultationFee;
    }

    public static Doctor fromFileString(String line) {
        String[] p = line.split(Constants.DELIMITER_REGEX, -1);
        if (p.length != 8) {
            throw new IllegalArgumentException("A doctor line needs 8 fields: " + line);
        }
        return new Doctor(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3], p[4],
                p[5], Integer.parseInt(p[6]), Double.parseDouble(p[7]));
    }
}