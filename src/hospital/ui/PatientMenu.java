package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Patient;
import hospital.service.PatientService;
import hospital.util.ConsoleInput;

import java.util.ArrayList;

// The screen for patient management. It talks to the user and calls PatientService.
public class PatientMenu {

    private PatientService service;
    private ConsoleInput input;

    // CONSTRUCTOR: receives the objects it needs
    public PatientMenu(PatientService service, ConsoleInput input) {
        this.service = service;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------- PATIENT MANAGEMENT ---------");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Back");
            int choice = input.readInt("Enter your choice: ", 1, 5);

            try {
                switch (choice) {
                    case 1:
                        addPatient();
                        break;
                    case 2:
                        service.viewAll();
                        break;
                    case 3:
                        searchPatient();
                        break;
                    case 4:
                        service.deletePatient(input.readInt("Enter Patient ID to delete: "));
                        System.out.println("Patient deleted successfully.");
                        break;
                    default:
                        back = true;
                }
            } catch (HospitalException e) {
                // every error from the service arrives here as a friendly message
                System.out.println("  ! Error: " + e.getMessage());
            }
        }
    }

    private void addPatient() throws HospitalException {
        String name = input.readLine("Name: ");
        int age = input.readInt("Age: ");
        String gender = input.readLine("Gender: ");
        String phone = input.readLine("Phone (10 digits): ");
        String address = input.readLine("Address: ");
        String bloodGroup = input.readLine("Blood group (e.g. O+): ");
        String condition = input.readLine("Medical condition: ");

        Patient patient = service.addPatient(name, age, gender, phone, address, bloodGroup, condition);
        System.out.println("Patient added successfully with ID " + patient.getId());
    }

    private void searchPatient() throws HospitalException {
        System.out.println("1. Search by ID   2. Search by Name");
        int mode = input.readInt("Choose: ", 1, 2);
        if (mode == 1) {
            service.searchPatient(input.readInt("Enter Patient ID: ")).displayDetails();  // int version
        } else {
            ArrayList<Patient> matches = service.searchPatient(input.readLine("Enter name: ")); // String version
            for (Patient patient : matches) {
                patient.displayDetails();
            }
        }
    }
}