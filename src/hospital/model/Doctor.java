package hospital.model;

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

    // Doctor's own version of displayDetails (overriding)
    @Override
    public void displayDetails() {
        System.out.println("[DOCTOR]  " + basicInfo());
        System.out.println("          Specialization: " + specialization
                + " | Experience: " + experience + " yrs | Fee: Rs. " + consultationFee);
    }
}