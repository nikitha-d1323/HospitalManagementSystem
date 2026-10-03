package hospital.main;

import hospital.service.Hospital;
import hospital.ui.AppointmentMenu;
import hospital.ui.BillingMenu;
import hospital.ui.DoctorMenu;
import hospital.ui.PatientMenu;
import hospital.ui.StaffMenu;
import hospital.util.ConsoleInput;
import hospital.util.Constants;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();
        Hospital hospital = new Hospital(Constants.HOSPITAL_NAME);

        try {
            hospital.loadData();      // saved files, or sample data on the very first run

            PatientMenu patientMenu = new PatientMenu(hospital.getPatientService(), input);
            DoctorMenu doctorMenu = new DoctorMenu(hospital.getDoctorService(), input);
            AppointmentMenu appointmentMenu = new AppointmentMenu(hospital.getAppointmentService(), input);
            BillingMenu billingMenu = new BillingMenu(hospital.getBillingService(), input);
            StaffMenu staffMenu = new StaffMenu(hospital, input);

            boolean running = true;
            while (running) {
                System.out.println("\n========================================");
                System.out.println("       HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("       " + hospital.getName());
                System.out.println("========================================");
                System.out.println("1. Patient Management");
                System.out.println("2. Doctor Management");
                System.out.println("3. Appointment Management");
                System.out.println("4. Billing Management");
                System.out.println("5. Hospital Staff");
                System.out.println("6. View Hospital Summary");
                System.out.println("7. Save Data");
                System.out.println("8. Exit");
                int choice = input.readInt("Enter your choice: ", 1, 8);

                switch (choice) {
                    case 1:
                        patientMenu.show();
                        break;
                    case 2:
                        doctorMenu.show();
                        break;
                    case 3:
                        appointmentMenu.show();
                        break;
                    case 4:
                        billingMenu.show();
                        break;
                    case 5:
                        staffMenu.show();
                        break;
                    case 6:
                        hospital.showSummary();
                        break;
                    case 7:
                        saveData(hospital);
                        break;
                    default:
                        saveData(hospital);     // auto-save so no work is lost
                        running = false;
                }
            }
            System.out.println("Thank you for using the Hospital Management System. Goodbye!");
        } finally {
            input.close();            // always release the Scanner
        }
    }

    private static void saveData(Hospital hospital) {
        try {
            hospital.saveData();
        } catch (IOException e) {
            System.out.println("  ! Error: could not save data - " + e.getMessage());
        }
    }
}