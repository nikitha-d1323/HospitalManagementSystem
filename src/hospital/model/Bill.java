package hospital.model;

import hospital.interfaces.Billable;

import java.util.EnumMap;
import java.util.Map;

// Bill implements Billable, so it MUST write calculateBill() and getTaxAmount()
public class Bill implements Billable {

    private static final double GST_RATE = 0.05;    // constant: 5% tax, written in ONE place

    private int billId;
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private double consultationFee;

    // COLLECTION with generics: service -> quantity (e.g. LAB_TEST -> 2)
    private Map<ServiceType, Integer> services = new EnumMap<>(ServiceType.class);

    public Bill(int billId, int appointmentId, int patientId, int doctorId, double consultationFee) {
        this.billId = billId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.consultationFee = consultationFee;
    }

    public int getBillId() { return billId; }
    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }

    // METHOD OVERLOADING: add one unit ...
    public void addService(ServiceType type) {
        addService(type, 1);
    }

    // ... or add a quantity. If the service is already there, the quantities are added together.
    public void addService(ServiceType type, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        int existing = services.containsKey(type) ? services.get(type) : 0;
        services.put(type, existing + quantity);
    }

    // consultation fee + all services, before tax
    public double getSubtotal() {
        double total = consultationFee;
        for (Map.Entry<ServiceType, Integer> entry : services.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    // ----- the 2 methods promised by the Billable interface -----
    @Override
    public double getTaxAmount() {
        return getSubtotal() * GST_RATE;
    }

    @Override
    public double calculateBill() {
        return getSubtotal() + getTaxAmount();
    }

    // prints the bill like a receipt
    public void displayBill(String patientName, String doctorName) {
        System.out.println("\n==================== BILL #" + billId + " ====================");
        System.out.println("Patient : " + patientName + " (ID " + patientId + ")");
        System.out.println("Doctor  : " + doctorName + " (ID " + doctorId + ")");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("----------------------------------------------");
        System.out.printf("%-30s Rs. %10.2f%n", "Consultation Fee", consultationFee);
        for (Map.Entry<ServiceType, Integer> entry : services.entrySet()) {
            ServiceType type = entry.getKey();
            int qty = entry.getValue();
            System.out.printf("%-30s Rs. %10.2f%n", type.getLabel() + " x" + qty, type.getPrice() * qty);
        }
        System.out.println("----------------------------------------------");
        System.out.printf("%-30s Rs. %10.2f%n", "Subtotal", getSubtotal());
        System.out.printf("%-30s Rs. %10.2f%n", "GST (5%)", getTaxAmount());
        System.out.printf("%-30s Rs. %10.2f%n", "TOTAL AMOUNT", calculateBill());
        System.out.println("==============================================");
    }
}