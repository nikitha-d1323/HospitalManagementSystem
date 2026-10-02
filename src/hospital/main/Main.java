package hospital.main;

import hospital.exception.HospitalException;
import hospital.model.Patient;
import hospital.service.PatientService;

public class Main {
    public static void main(String[] args) {

        PatientService service = new PatientService();

        // Each test is inside try/catch so one error does not stop the program.
        // catch (HospitalException e) catches InvalidInput AND PatientNotFound (inheritance!)

        System.out.println("--- Test 1: valid patient ---");
        try {
            Patient p = service.addPatient("Arjun Kumar", 34, "Male", "9876543210",
                    "12 Gandhi Street, Coimbatore", "B+", "Diabetes");
            System.out.println("Added with ID " + p.getId());
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 2: age -5 ---");
        try {
            service.addPatient("Meena Raj", -5, "Female", "9123456780",
                    "45 Lake View Road", "O+", "Migraine");
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 3: phone 12345 ---");
        try {
            service.addPatient("Meena Raj", 28, "Female", "12345",
                    "45 Lake View Road", "O+", "Migraine");
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 4: empty name ---");
        try {
            service.addPatient("   ", 28, "Female", "9123456780",
                    "45 Lake View Road", "O+", "Migraine");
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 5: blood group Z+ ---");
        try {
            service.addPatient("Meena Raj", 28, "Female", "9123456780",
                    "45 Lake View Road", "Z+", "Migraine");
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 6: search ID 999 ---");
        try {
            service.searchPatient(999).displayDetails();
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Test 7: delete ID 101 then search it ---");
        try {
            service.deletePatient(101);
            System.out.println("Deleted patient 101.");
            service.searchPatient(101);
        } catch (HospitalException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // finally ALWAYS runs, whether there was an error or not
            System.out.println("(finally block: this test is finished)");
        }

        System.out.println("\n--- Final list ---");
        service.viewAll();
    }
}