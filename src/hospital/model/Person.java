package hospital.model;

// "abstract" = we never create a plain Person, only Patient, Doctor, Staff
public abstract class Person {

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
}