package hospital.service;

import hospital.exception.AppointmentException;
import hospital.exception.BillNotFoundException;
import hospital.exception.HospitalException;
import hospital.model.Appointment;
import hospital.model.Bill;
import hospital.model.Doctor;

import java.util.ArrayList;

public class BillingService {

    private ArrayList<Bill> bills = new ArrayList<>();
    private int nextId = 701;   // bill IDs: 701, 702, 703 ...

    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;

    public BillingService(AppointmentService appointmentService, DoctorService doctorService,
                          PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // Creates a bill that contains only the doctor's consultation fee.
    // Extra services are added afterwards with bill.addService(...)
    public Bill generateBill(int appointmentId) throws HospitalException {
        Appointment appointment = appointmentService.searchAppointment(appointmentId);

        if (appointment.getStatus() == Appointment.Status.CANCELLED) {
            throw new AppointmentException("Cannot bill a cancelled appointment.");
        }
        for (Bill existing : bills) {
            if (existing.getAppointmentId() == appointmentId) {
                throw new AppointmentException("Bill #" + existing.getBillId()
                        + " already exists for this appointment.");
            }
        }

        // the consultation fee comes from the doctor
        Doctor doctor = doctorService.searchDoctor(appointment.getDoctorId());
        Bill bill = new Bill(nextId, appointmentId, appointment.getPatientId(),
                appointment.getDoctorId(), doctor.getConsultationFee());
        nextId++;
        bills.add(bill);

        appointment.complete();     // billing finishes the visit
        return bill;
    }

    public Bill searchBill(int billId) throws BillNotFoundException {
        for (Bill bill : bills) {
            if (bill.getBillId() == billId) {
                return bill;
            }
        }
        throw new BillNotFoundException("Bill with ID " + billId + " was not found.");
    }

    public void showBill(Bill bill) {
        bill.displayBill(patientName(bill.getPatientId()), doctorName(bill.getDoctorId()));
    }

    public void viewAll() {
        if (bills.isEmpty()) {
            System.out.println("No bills generated yet.");
            return;
        }
        System.out.println(String.format("%-8s %-14s %-18s %s", "Bill ID", "Appointment", "Patient", "Total"));
        System.out.println("--------------------------------------------------------");
        double grandTotal = 0;
        for (Bill bill : bills) {
            // "Billable" works here too: Bill IS-A Billable
            System.out.println(String.format("%-8d %-14d %-18s Rs. %.2f", bill.getBillId(),
                    bill.getAppointmentId(), patientName(bill.getPatientId()), bill.calculateBill()));
            grandTotal += bill.calculateBill();
        }
        System.out.println("--------------------------------------------------------");
        System.out.println(String.format("Total billed: Rs. %.2f", grandTotal));
    }

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
    public ArrayList<Bill> getAllBills() {
        return bills;
    }

    // adds a bill read from the file and keeps the ID counter correct
    public void addLoaded(Bill bill) {
        bills.add(bill);
        if (bill.getBillId() >= nextId) {
            nextId = bill.getBillId() + 1;
        }
    }
}