package hospital.model;

import hospital.util.Constants;

// Staff IS-A Person (inheritance): nurse, receptionist, pharmacist ...
public class Staff extends Person {

    private String designation;
    private double salary;

    public Staff(int id, String name, int age, String gender, String phone,
                 String designation, double salary) {
        super(id, name, age, gender, phone);
        this.designation = designation;
        this.salary = salary;
    }

    public String getDesignation() { return designation; }
    public double getSalary() { return salary; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setSalary(double salary) { this.salary = salary; }

    // Staff's own version of displayDetails (overriding)
    @Override
    public void displayDetails() {
        System.out.println("[STAFF]   " + basicInfo());
        System.out.println("          Designation: " + designation + " | Salary: Rs. " + salary);
    }

    @Override
    public String toFileString() {
        return basicFileString() + Constants.DELIMITER + designation + Constants.DELIMITER + salary;
    }

    public static Staff fromFileString(String line) {
        String[] p = line.split(Constants.DELIMITER_REGEX, -1);
        if (p.length != 7) {
            throw new IllegalArgumentException("A staff line needs 7 fields: " + line);
        }
        return new Staff(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3], p[4],
                p[5], Double.parseDouble(p[6]));
    }
}