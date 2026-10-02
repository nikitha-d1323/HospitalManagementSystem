package hospital.main;

import hospital.model.Patient;

public class Main {
    public static void main(String[] args) {
        // OBJECT creation: calls the Patient constructor
        Patient p = new Patient(101, "Arjun Kumar", 34, "Male", "9876543210",
                "12 Gandhi Street, Coimbatore", "B+", "Diabetes");

        p.displayDetails();

        p.setPhone("9000000000"); // change a private field through the setter
        System.out.println("After update:");
        p.displayDetails();
    }
}