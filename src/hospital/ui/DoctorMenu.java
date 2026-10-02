package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Doctor;
import hospital.service.DoctorService;
import hospital.util.ConsoleInput;

import java.util.ArrayList;

// The screen for doctor management (same pattern as PatientMenu)
public class DoctorMenu {

    private DoctorService service;
    private ConsoleInput input;

    public DoctorMenu(DoctorService service, ConsoleInput input) {
        this.service = service;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------- DOCTOR MANAGEMENT ---------");
            System.out.println("1. Add Doctor");
            System.out.println("2. View Doctors");
            System.out.println("3. Search Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Back");
            int choice = input.readInt("Enter your choice: ", 1, 5);

            try {
                switch (choice) {
                    case 1:
                        addDoctor();
                        break;
                    case 2:
                        service.viewAll();
                        break;
                    case 3:
                        searchDoctor();
                        break;
                    case 4:
                        service.deleteDoctor(input.readInt("Enter Doctor ID to delete: "));
                        System.out.println("Doctor deleted successfully.");
                        break;
                    default:
                        back = true;
                }
            } catch (HospitalException e) {
                System.out.println("  ! Error: " + e.getMessage());
            }
        }
    }

    private void addDoctor() throws HospitalException {
        String name = input.readLine("Name: ");
        int age = input.readInt("Age: ");
        String gender = input.readLine("Gender: ");
        String phone = input.readLine("Phone (10 digits): ");
        String specialization = input.readLine("Specialization: ");
        int experience = input.readInt("Experience (years): ");
        int fee = input.readInt("Consultation fee (Rs.): ");

        Doctor doctor = service.addDoctor(name, age, gender, phone, specialization, experience, fee);
        System.out.println("Doctor added successfully with ID " + doctor.getId());
    }

    private void searchDoctor() throws HospitalException {
        System.out.println("1. Search by ID   2. Search by Specialization");
        int mode = input.readInt("Choose: ", 1, 2);
        if (mode == 1) {
            service.searchDoctor(input.readInt("Enter Doctor ID: ")).displayDetails();
        } else {
            ArrayList<Doctor> matches = service.searchDoctor(input.readLine("Enter specialization: "));
            for (Doctor doctor : matches) {
                doctor.displayDetails();
            }
        }
    }
}