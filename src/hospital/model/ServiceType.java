package hospital.model;

// ENUM with data: each hospital service has a label and a fixed price.
// (The consultation fee is not here, because it comes from the doctor.)
public enum ServiceType {
    LAB_TEST("Lab Test", 500),
    XRAY("X-Ray", 800),
    ECG("ECG", 400),
    MEDICINES("Medicines", 300),
    ROOM_CHARGE("Room Charge (per day)", 1500);

    private String label;
    private double price;

    // enum constructor: runs once for each constant above
    ServiceType(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() { return label; }
    public double getPrice() { return price; }
}