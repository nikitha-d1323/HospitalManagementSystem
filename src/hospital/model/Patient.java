package hospital.model;

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
}