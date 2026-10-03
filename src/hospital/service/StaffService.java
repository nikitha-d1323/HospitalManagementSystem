package hospital.service;

import hospital.exception.HospitalException;
import hospital.exception.InvalidInputException;
import hospital.exception.StaffNotFoundException;
import hospital.model.Staff;
import hospital.util.InputValidator;

import java.util.ArrayList;

public class StaffService {

    private ArrayList<Staff> staffList = new ArrayList<>();
    private int nextId = 301;   // staff IDs: 301, 302, 303 ...

    public Staff addStaff(String name, int age, String gender, String phone,
                          String designation, int salary) throws InvalidInputException {
        String cleanName = InputValidator.validateName(name);
        InputValidator.validateAge(age, 18, 70);
        String cleanPhone = InputValidator.validatePhone(phone);
        String cleanDesignation = InputValidator.validateText(designation, "Designation");
        InputValidator.validatePositive(salary, "Salary");

        Staff staff = new Staff(nextId, cleanName, age, gender, cleanPhone, cleanDesignation, salary);
        nextId++;
        staffList.add(staff);
        return staff;
    }

    public void viewAll() {
        if (staffList.isEmpty()) {
            System.out.println("No staff registered.");
            return;
        }
        for (Staff staff : staffList) {
            staff.displayDetails();
            System.out.println();
        }
        System.out.println("Total staff: " + staffList.size());
    }

    public Staff searchStaff(int id) throws StaffNotFoundException {
        for (Staff staff : staffList) {
            if (staff.getId() == id) {
                return staff;
            }
        }
        throw new StaffNotFoundException("Staff member with ID " + id + " was not found.");
    }

    // UPDATE: blank text means "keep the old value"
    public Staff updateStaff(int id, String phone, String designation, String salaryText) throws HospitalException {
        Staff staff = searchStaff(id);

        // validate everything FIRST, so a bad value never leaves the record half-updated
        String newPhone = phone.isEmpty() ? null : InputValidator.validatePhone(phone);
        String newDesignation = designation.isEmpty() ? null : InputValidator.validateText(designation, "Designation");
        int newSalary = -1;
        if (!salaryText.isEmpty()) {
            try {
                newSalary = Integer.parseInt(salaryText);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Salary must be a whole number.");
            }
            InputValidator.validatePositive(newSalary, "Salary");
        }

        if (newPhone != null) {
            staff.setPhone(newPhone);
        }
        if (newDesignation != null) {
            staff.setDesignation(newDesignation);
        }
        if (newSalary != -1) {
            staff.setSalary(newSalary);
        }
        return staff;
    }

    public void deleteStaff(int id) throws StaffNotFoundException {
        Staff staff = searchStaff(id);
        staffList.remove(staff);
    }

    // ---- used by file handling ----
    public ArrayList<Staff> getAllStaff() {
        return staffList;
    }

    public void addLoaded(Staff staff) {
        staffList.add(staff);
        if (staff.getId() >= nextId) {
            nextId = staff.getId() + 1;
        }
    }
}