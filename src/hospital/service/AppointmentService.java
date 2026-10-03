package hospital.service;

import hospital.exception.AppointmentException;
import hospital.exception.HospitalException;
import hospital.exception.InvalidInputException;
import hospital.model.Appointment;
import hospital.util.InputValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AppointmentService {

    private ArrayList<Appointment> appointments = new ArrayList<>();
    private int nextId = 501;   // appointment IDs: 501, 502, 503 ...

    // the appointment service USES the other two services to check that the IDs really exist
    private PatientService patientService;
    private DoctorService doctorService;

    public AppointmentService(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public Appointment bookAppointment(int patientId, int doctorId, String dateText,
                                       String timeText, String reason) throws HospitalException {
        // Rule 1: patient and doctor must exist (these throw "not found" exceptions if missing)
        patientService.searchPatient(patientId);
        doctorService.searchDoctor(doctorId);

        // Rule 2: date and time must be written correctly
        LocalDate date;
        LocalTime time;
        try {
            date = LocalDate.parse(dateText);      // format yyyy-MM-dd, e.g. 2026-12-15
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Date must be a real date like 2026-12-15 (yyyy-MM-dd).");
        }
        try {
            time = LocalTime.parse(timeText);      // format HH:mm, e.g. 14:30
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Time must be like 14:30 (24-hour HH:mm).");
        }
        String cleanReason = InputValidator.validateText(reason, "Reason");

        // Rule 3: not in the past
        if (date.isBefore(LocalDate.now())) {
            throw new AppointmentException("Appointment date cannot be in the past.");
        }
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new AppointmentException("That time has already passed today.");
        }

        // Rule 4: working hours only
        if (time.isBefore(LocalTime.of(9, 0)) || time.isAfter(LocalTime.of(17, 0))) {
            throw new AppointmentException("Appointments are only between 09:00 and 17:00.");
        }

        // Rule 5: the doctor must be free at that time
        for (Appointment a : appointments) {
            if (a.isActive() && a.getDoctorId() == doctorId
                    && a.getDate().equals(date) && a.getTime().equals(time)) {
                throw new AppointmentException("Doctor " + doctorId + " already has an appointment at "
                        + date + " " + time + ".");
            }
        }

        Appointment appointment = new Appointment(nextId, patientId, doctorId, date, time, cleanReason);
        nextId++;
        appointments.add(appointment);
        return appointment;
    }

    public void viewAll() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.");
            return;
        }
        System.out.println(String.format("%-5s %-16s %-18s %-11s %-6s %-10s %s",
                "ID", "Patient", "Doctor", "Date", "Time", "Status", "Reason"));
        System.out.println("-------------------------------------------------------------------------------------");
        for (Appointment a : appointments) {
            System.out.println(String.format("%-5d %-16s %-18s %-11s %-6s %-10s %s",
                    a.getAppointmentId(), patientName(a.getPatientId()), doctorName(a.getDoctorId()),
                    a.getDate(), a.getTime(), a.getStatus(), a.getReason()));
        }
        System.out.println("Total appointments: " + appointments.size());
    }

    public Appointment searchAppointment(int id) throws AppointmentException {
        for (Appointment a : appointments) {
            if (a.getAppointmentId() == id) {
                return a;
            }
        }
        throw new AppointmentException("Appointment with ID " + id + " was not found.");
    }

    public void cancelAppointment(int id) throws AppointmentException {
        Appointment appointment = searchAppointment(id);
                if (!appointment.isActive()) {
            throw new AppointmentException("Appointment " + id + " cannot be cancelled (status: "
                    + appointment.getStatus() + ").");
        }
        appointment.cancel();     // method from the Schedulable interface
    }

    // helpers: show names instead of IDs
    private String patientName(int patientId) {
        try {
            return patientService.searchPatient(patientId).getName();
        } catch (HospitalException e) {
            return "(removed)";
        }
    }

    private String doctorName(int doctorId) {
        try {
            return doctorService.searchDoctor(doctorId).getName();
        } catch (HospitalException e) {
            return "(removed)";
        }
    }
    
    // ---- used by file handling ----
    public ArrayList<Appointment> getAllAppointments() {
        return appointments;
    }

    // adds an appointment read from the file and keeps the ID counter correct
    public void addLoaded(Appointment appointment) {
        appointments.add(appointment);
        if (appointment.getAppointmentId() >= nextId) {
            nextId = appointment.getAppointmentId() + 1;
        }
    }
}