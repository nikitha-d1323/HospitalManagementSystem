package hospital.interfaces;

// INTERFACE: anything that can produce an amount to pay promises these two methods.
public interface Billable {

    double calculateBill();     // final amount to pay (with tax)

    double getTaxAmount();      // only the tax part
}