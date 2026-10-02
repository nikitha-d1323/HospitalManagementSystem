package hospital.main;

import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.Person;
import hospital.model.Staff;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        // A list that can hold ANY kind of Person
        ArrayList<Person> people = new ArrayList<>();

        people.add(new Patient(101, "Arjun Kumar", 34, "Male", "9876543210",
                "12 Gandhi Street, Coimbatore", "B+", "Diabetes"));
        people.add(new Doctor(201, "Dr. Anita Sharma", 42, "Female", "9811122233",
                "Cardiology", 15, 800));
        people.add(new Staff(301, "Sunita Devi", 30, "Female", "9844455566",
                "Nurse", 28000));

        // POLYMORPHISM: same call, different result for each object
        for (Person person : people) {
            person.displayDetails();
            System.out.println();
        }
    }
}