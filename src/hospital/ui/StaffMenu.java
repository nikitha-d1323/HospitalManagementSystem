package hospital.ui;

import hospital.exception.HospitalException;
import hospital.model.Staff;
import hospital.service.Hospital;
import hospital.service.StaffService;
import hospital.util.ConsoleInput;

public class StaffMenu {

    private Hospital hospital;
    private StaffService service;
    private ConsoleInput input;

    public StaffMenu(Hospital hospital, ConsoleInput input) {
        this.hospital = hospital;
        this.service = hospital.getStaffService();
        this.input = input;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--------- HOSPITAL STAFF ---------");
            System.out.println("1. Add Staff");
            System.out.println("2. View Staff");
            System.out.println("3. Search Staff by ID");
            System.out.println("4. Update Staff");
            System.out.println("5. Delete Staff");
            System.out.println("6. View ALL People (Patients + Doctors + Staff)");
            System.out.println("7. Back");
            int choice = input.readInt("Enter your choice: ", 1, 7);

            try {
                switch (choice) {
                    case 1:
                        addStaff();
                        break;
                    case 2:
                        service.viewAll();
                        break;
                    case 3:
                        service.searchStaff(input.readInt("Enter Staff ID: ")).displayDetails();
                        break;
                    case 4:
                        updateStaff();
                        break;
                    case 5:
                        service.deleteStaff(input.readInt("Enter Staff ID to delete: "));
                        System.out.println("Staff member deleted successfully.");
                        break;
                    case 6:
                        hospital.showAllPeople();
                        break;
                    default:
                        back = true;
                }
            } catch (HospitalException e) {
                System.out.println("  ! Error: " + e.getMessage());
            }
        }
    }

    private void addStaff() throws HospitalException {
        String name = input.readLine("Name: ");
        int age = input.readInt("Age: ");
        String gender = input.readLine("Gender: ");
        String phone = input.readLine("Phone (10 digits): ");
        String designation = input.readLine("Designation (Nurse, Receptionist ...): ");
        int salary = input.readInt("Monthly salary (Rs.): ");

        Staff staff = service.addStaff(name, age, gender, phone, designation, salary);
        System.out.println("Staff member added successfully with ID " + staff.getId());
    }

    private void updateStaff() throws HospitalException {
        int id = input.readInt("Enter Staff ID to update: ");
        service.searchStaff(id).displayDetails();
        System.out.println("(Press Enter to keep the current value)");
        String phone = input.readLine("New phone: ");
        String designation = input.readLine("New designation: ");
        String salary = input.readLine("New salary: ");

        service.updateStaff(id, phone, designation, salary).displayDetails();
        System.out.println("Staff member updated successfully.");
    }
}