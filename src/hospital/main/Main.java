package hospital.main;

import hospital.model.Bill;
import hospital.model.ServiceType;
import hospital.service.AppointmentService;
import hospital.service.BillingService;
import hospital.service.DoctorService;
import hospital.service.PatientService;
import hospital.ui.AppointmentMenu;
import hospital.ui.BillingMenu;
import hospital.ui.DoctorMenu;
import hospital.ui.PatientMenu;
import hospital.util.ConsoleInput;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // create the objects once, then share them
        ConsoleInput input = new ConsoleInput();
        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        AppointmentService appointmentService = new AppointmentService(patientService, doctorService);
        BillingService billingService = new BillingService(appointmentService, doctorService, patientService);

        PatientMenu patientMenu = new PatientMenu(patientService, input);
        DoctorMenu doctorMenu = new DoctorMenu(doctorService, input);
        AppointmentMenu appointmentMenu = new AppointmentMenu(appointmentService, input);
        BillingMenu billingMenu = new BillingMenu(billingService, input);

        // some starting data so the demo is not empty
        try {
            patientService.addPatient("Arjun Kumar", 34, "Male", "9876543210",
                    "12 Gandhi Street, Coimbatore", "B+", "Diabetes");
            patientService.addPatient("Meena Raj", 28, "Female", "9123456780",
                    "45 Lake View Road, Sulur", "O+", "Migraine");

            doctorService.addDoctor("Dr. Anita Sharma", 42, "Female", "9811122233", "Cardiology", 15, 800);
            doctorService.addDoctor("Dr. Vikram Rao", 38, "Male", "9822233344", "Neurology", 11, 700);

            // dates are calculated from today, so they are always in the future
            LocalDate soon = LocalDate.now().plusDays(2);
            appointmentService.bookAppointment(101, 201, soon.toString(), "10:00", "Diabetes follow-up");
            appointmentService.bookAppointment(102, 202, soon.toString(), "11:30", "Recurring headaches");
            appointmentService.bookAppointment(101, 202, soon.plusDays(1).toString(), "09:30", "Blood pressure check");

            // two sample bills (appointments 501 and 502); appointment 503 stays free to bill in the demo
            Bill first = billingService.generateBill(501);
            first.addService(ServiceType.LAB_TEST, 2);
            first.addService(ServiceType.ECG);
            Bill second = billingService.generateBill(502);
            second.addService(ServiceType.XRAY);
            second.addService(ServiceType.MEDICINES, 3);
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
            System.out.println("3. Appointment Management");
            System.out.println("4. Billing Management");
            System.out.println("5. Exit");
            int choice = input.readInt("Enter your choice: ", 1, 5);

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
                default:
                    running = false;
            }
        }

        System.out.println("Thank you. Goodbye!");
        input.close();
    }
}