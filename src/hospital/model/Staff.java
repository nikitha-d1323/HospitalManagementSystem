package hospital.model;

// Staff IS-A Person (inheritance)
public class Staff extends Person {

    private String designation;
    private double salary;

    public Staff(int id, String name, int age, String gender, String phone,
                 String designation, double salary) {
        super(id, name, age, gender, phone);
        this.designation = designation;
        this.salary = salary;
    }

    // Staff's own version of displayDetails (overriding)
    @Override
    public void displayDetails() {
        System.out.println("[STAFF]   " + basicInfo());
        System.out.println("          Designation: " + designation + " | Salary: Rs. " + salary);
    }
}