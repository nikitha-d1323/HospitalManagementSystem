package hospital.main;

import hospital.service.DoctorService;
import hospital.service.PatientService;
import hospital.ui.DoctorMenu;
import hospital.ui.PatientMenu;
import hospital.util.ConsoleInput;

public class Main {
    public static void main(String[] args) {

        // create the objects once, then share them
        ConsoleInput input = new ConsoleInput();
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        PatientMenu patientMenu = new PatientMenu(patientService, input);
        DoctorMenu doctorMenu = new DoctorMenu(doctorService, input);

        // some starting data so the demo is not empty
        try {
            patientService.addPatient("Arjun Kumar", 34, "Male", "9876543210",
                    "12 Gandhi Street, Coimbatore", "B+", "Diabetes");
            patientService.addPatient("Meena Raj", 28, "Female", "9123456780",
                    "45 Lake View Road, Sulur", "O+", "Migraine");

            doctorService.addDoctor("Dr. Anita Sharma", 42, "Female", "9811122233", "Cardiology", 15, 800);
            doctorService.addDoctor("Dr. Vikram Rao", 38, "Male", "9822233344", "Neurology", 11, 700);
        } catch (Exception e) {
            System.out.println("Could not load sample data: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println("       HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Exit");
            int choice = input.readInt("Enter your choice: ", 1, 3);

            switch (choice) {
                case 1:
                    patientMenu.show();
                    break;
                case 2:
                    doctorMenu.show();
                    break;
                default:
                    running = false;
            }
        }

        System.out.println("Thank you. Goodbye!");
        input.close();
    }
}