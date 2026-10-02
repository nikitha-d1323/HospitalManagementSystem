package hospital.main;

import hospital.model.Patient;
import hospital.service.PatientService;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        PatientService service = new PatientService();

        // ADD three patients (IDs are given automatically)
        service.addPatient("Arjun Kumar", 34, "Male", "9876543210",
                "12 Gandhi Street, Coimbatore", "B+", "Diabetes");
        service.addPatient("Meena Raj", 28, "Female", "9123456780",
                "45 Lake View Road, Sulur", "O+", "Migraine");
        service.addPatient("Ravi Shankar", 45, "Male", "9988776655",
                "7 Temple Lane, Peelamedu", "A-", "Hypertension");

        System.out.println("===== ALL PATIENTS =====");
        service.viewAll();

        System.out.println("===== SEARCH BY ID 102 =====");
        Patient found = service.searchPatient(102);   // int version
        if (found != null) {
            found.displayDetails();
        }

        System.out.println("\n===== SEARCH BY NAME 'ravi' =====");
        ArrayList<Patient> matches = service.searchPatient("ravi");   // String version
        for (Patient p : matches) {
            p.displayDetails();
        }

        System.out.println("\n===== DELETE ID 101 =====");
        if (service.deletePatient(101)) {
            System.out.println("Patient 101 deleted.");
        }

        System.out.println("\n===== SEARCH ID 999 (does not exist) =====");
        if (service.searchPatient(999) == null) {
            System.out.println("Patient not found.");
        }

        System.out.println("\n===== PATIENTS AFTER DELETE =====");
        service.viewAll();
    }
}