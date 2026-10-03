package hospital.service;

import hospital.exception.InvalidInputException;
import hospital.exception.PatientNotFoundException;
import hospital.model.Patient;
import hospital.util.InputValidator;

import java.util.ArrayList;

public class PatientService {

    private ArrayList<Patient> patients = new ArrayList<>();
    private int nextId = 101;

    // "throws" = this method may fail with InvalidInputException; the caller must handle it
    public Patient addPatient(String name, int age, String gender, String phone,
                              String address, String bloodGroup, String condition) throws InvalidInputException {
        // validate FIRST; if anything is wrong an exception is thrown and no patient is created
        String cleanName = InputValidator.validateName(name);
        InputValidator.validateAge(age);
        String cleanPhone = InputValidator.validatePhone(phone);
        String cleanBlood = InputValidator.validateBloodGroup(bloodGroup);

        Patient patient = new Patient(nextId, cleanName, age, gender, cleanPhone, address, cleanBlood, condition);
        nextId++;
        patients.add(patient);
        return patient;
    }

    public void viewAll() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        for (Patient patient : patients) {
            patient.displayDetails();
            System.out.println();
        }
        System.out.println("Total patients: " + patients.size());
    }

    // search by ID: now THROWS an exception instead of returning null
    public Patient searchPatient(int id) throws PatientNotFoundException {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        throw new PatientNotFoundException("Patient with ID " + id + " was not found.");
    }

    // search by name (overloading)
    public ArrayList<Patient> searchPatient(String name) throws PatientNotFoundException {
        ArrayList<Patient> matches = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getName().toLowerCase().contains(name.toLowerCase())) {
                matches.add(patient);
            }
        }
        if (matches.isEmpty()) {
            throw new PatientNotFoundException("No patient found with name containing '" + name + "'.");
        }
        return matches;
    }

    public void deletePatient(int id) throws PatientNotFoundException {
        Patient patient = searchPatient(id); // throws if missing
        patients.remove(patient);
    }
    
    // ---- used by file handling ----
    public ArrayList<Patient> getAllPatients() {
        return patients;
    }

    // adds a patient read from the file and keeps the ID counter correct
    public void addLoaded(Patient patient) {
        patients.add(patient);
        if (patient.getId() >= nextId) {
            nextId = patient.getId() + 1;
        }
    }
}