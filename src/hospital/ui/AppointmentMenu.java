package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Appointment;
import hospital.service.AppointmentService;
import hospital.util.ConsoleInput;

public class AppointmentMenu {

    private AppointmentService service;
    private ConsoleInput input;

    public AppointmentMenu(AppointmentService service, ConsoleInput input) {
        this.service = service;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------- APPOINTMENT MANAGEMENT ---------");
            System.out.println("1. Book Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Search Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Back");
            int choice = input.readInt("Enter your choice: ", 1, 5);

            try {
                switch (choice) {
                    case 1:
                        bookAppointment();
                        break;
                    case 2:
                        service.viewAll();
                        break;
                    case 3:
                        Appointment found = service.searchAppointment(input.readInt("Enter Appointment ID: "));
                        System.out.println("Appointment " + found.getAppointmentId() + ": patient "
                                + found.getPatientId() + ", doctor " + found.getDoctorId() + ", "
                                + found.getScheduleInfo() + ", " + found.getStatus() + ", " + found.getReason());
                        break;
                    case 4:
                        service.cancelAppointment(input.readInt("Enter Appointment ID to cancel: "));
                        System.out.println("Appointment cancelled.");
                        break;
                    default:
                        back = true;
                }
            } catch (HospitalException e) {
                System.out.println("  ! Error: " + e.getMessage());
            }
        }
    }

    private void bookAppointment() throws HospitalException {
        int patientId = input.readInt("Patient ID: ");
        int doctorId = input.readInt("Doctor ID: ");
        String date = input.readLine("Date (yyyy-MM-dd): ");
        String time = input.readLine("Time (HH:mm, 09:00-17:00): ");
        String reason = input.readLine("Reason for visit: ");

        Appointment appointment = service.bookAppointment(patientId, doctorId, date, time, reason);
        System.out.println("Appointment booked. ID: " + appointment.getAppointmentId()
                + " (" + appointment.getScheduleInfo() + ")");
    }
}