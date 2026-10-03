package hospital.model;

import hospital.interfaces.Billable;
import hospital.interfaces.FileStorable;
import hospital.util.Constants;

import java.util.EnumMap;
import java.util.Map;

// Bill implements TWO interfaces: Billable (money) and FileStorable (saving)
public class Bill implements Billable, FileStorable {

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

    // ----- the method promised by the FileStorable interface -----
    // services are saved like  LAB_TEST:2;ECG:1   (or a single "-" when there are none)
    @Override
    public String toFileString() {
        StringBuilder items = new StringBuilder();
        for (Map.Entry<ServiceType, Integer> entry : services.entrySet()) {
            if (items.length() > 0) {
                items.append(";");
            }
            items.append(entry.getKey().name()).append(":").append(entry.getValue());
        }
        if (items.length() == 0) {
            items.append("-");
        }
        String d = Constants.DELIMITER;
        return billId + d + appointmentId + d + patientId + d + doctorId + d + consultationFee + d + items;
    }

    public static Bill fromFileString(String line) {
        String[] p = line.split(Constants.DELIMITER_REGEX, -1);
        if (p.length != 6) {
            throw new IllegalArgumentException("A bill line needs 6 fields: " + line);
        }
        Bill bill = new Bill(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                Integer.parseInt(p[3]), Double.parseDouble(p[4]));
        if (!p[5].equals("-")) {
            for (String item : p[5].split(";")) {
                String[] parts = item.split(":");
                bill.addService(ServiceType.valueOf(parts[0]), Integer.parseInt(parts[1]));
            }
        }
        return bill;
    }
}