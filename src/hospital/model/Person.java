package hospital.model;

import hospital.interfaces.FileStorable;
import hospital.util.Constants;

// "abstract" = we never create a plain Person, only Patient, Doctor, Staff
// It also implements FileStorable, so every person can be saved in a file.
public abstract class Person implements FileStorable {

    // private = ENCAPSULATION: other classes cannot touch these directly
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;

    // CONSTRUCTOR: runs when we write "new ..." and fills the fields
    public Person(int id, String name, int age, String gender, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }

    // getters and setters = the only way to read/change private fields
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // every child class MUST write its own version of this
    public abstract void displayDetails();

    // helper that children can reuse
    protected String basicInfo() {
        return "ID: " + id + " | " + name + " | Age: " + age + " | " + gender + " | Ph: " + phone;
    }

    // helper for files: the common fields as one piece of text, e.g. 101|Arjun Kumar|34|Male|9876543210
    protected String basicFileString() {
        return id + Constants.DELIMITER + name + Constants.DELIMITER + age + Constants.DELIMITER
                + gender + Constants.DELIMITER + phone;
    }
}