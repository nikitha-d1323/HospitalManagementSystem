package hospital.service;

import hospital.exception.DoctorNotFoundException;
import hospital.exception.InvalidInputException;
import hospital.model.Doctor;
import hospital.util.InputValidator;

import java.util.ArrayList;

public class DoctorService {

    private ArrayList<Doctor> doctors = new ArrayList<>();
    private int nextId = 201;   // doctor IDs: 201, 202, 203 ...

    public Doctor addDoctor(String name, int age, String gender, String phone,
                            String specialization, int experience, int fee) throws InvalidInputException {
        String cleanName = InputValidator.validateName(name);
        InputValidator.validateAge(age, 22, 80);            // overloaded version with a range
        String cleanPhone = InputValidator.validatePhone(phone);
        String cleanSpec = InputValidator.validateText(specialization, "Specialization");
        if (experience < 0 || experience > age - 21) {
            throw new InvalidInputException("Experience must be between 0 and " + (age - 21) + " years for this age.");
        }
        InputValidator.validatePositive(fee, "Consultation fee");

        Doctor doctor = new Doctor(nextId, cleanName, age, gender, cleanPhone, cleanSpec, experience, fee);
        nextId++;
        doctors.add(doctor);
        return doctor;
    }

    public void viewAll() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }
        for (Doctor doctor : doctors) {
            doctor.displayDetails();
            System.out.println();
        }
        System.out.println("Total doctors: " + doctors.size());
    }

    // search by ID (overloading 1)
    public Doctor searchDoctor(int id) throws DoctorNotFoundException {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        throw new DoctorNotFoundException("Doctor with ID " + id + " was not found.");
    }

    // search by specialization (overloading 2)
    public ArrayList<Doctor> searchDoctor(String specialization) throws DoctorNotFoundException {
        ArrayList<Doctor> matches = new ArrayList<>();
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialization().toLowerCase().contains(specialization.toLowerCase())) {
                matches.add(doctor);
            }
        }
        if (matches.isEmpty()) {
            throw new DoctorNotFoundException("No doctor found for specialization '" + specialization + "'.");
        }
        return matches;
    }

    public void deleteDoctor(int id) throws DoctorNotFoundException {
        Doctor doctor = searchDoctor(id);
        doctors.remove(doctor);
    }
}