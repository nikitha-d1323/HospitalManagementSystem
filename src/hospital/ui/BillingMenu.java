package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Bill;
import hospital.model.ServiceType;
import hospital.service.BillingService;
import hospital.util.ConsoleInput;

public class BillingMenu {

    private BillingService service;
    private ConsoleInput input;

    public BillingMenu(BillingService service, ConsoleInput input) {
        this.service = service;
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------- BILLING MANAGEMENT ---------");
            System.out.println("1. Generate Bill");
            System.out.println("2. View Bill");
            System.out.println("3. View All Bills");
            System.out.println("4. Service Price List");
            System.out.println("5. Back");
            int choice = input.readInt("Enter your choice: ", 1, 5);

            try {
                switch (choice) {
                    case 1:
                        generateBill();
                        break;
                    case 2:
                        service.showBill(service.searchBill(input.readInt("Enter Bill ID: ")));
                        break;
                    case 3:
                        service.viewAll();
                        break;
                    case 4:
                        printPriceList();
                        break;
                    default:
                        back = true;
                }
            } catch (HospitalException e) {
                System.out.println("  ! Error: " + e.getMessage());
            }
        }
    }

    private void generateBill() throws HospitalException {
        Bill bill = service.generateBill(input.readInt("Enter Appointment ID to bill: "));
        System.out.println("Bill #" + bill.getBillId() + " created with the consultation fee.");
        System.out.println("Add extra services (enter 0 when finished):");
        printPriceList();

        ServiceType[] types = ServiceType.values();   // all enum constants as an array
        while (true) {
            int number = input.readInt("Service number (0 = finish): ", 0, types.length);
            if (number == 0) {
                break;
            }
            int quantity = input.readInt("Quantity: ", 1, 100);
            bill.addService(types[number - 1], quantity);
            System.out.println("  Added " + types[number - 1].getLabel() + " x" + quantity);
        }
        service.showBill(bill);
    }

    private void printPriceList() {
        System.out.println("Service price list (consultation fee comes from the doctor):");
        ServiceType[] types = ServiceType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d. %-24s Rs. %.2f%n", i + 1, types[i].getLabel(), types[i].getPrice());
        }
    }
}