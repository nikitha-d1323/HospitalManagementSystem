package hospital.service;

import hospital.model.Patient;

import java.util.ArrayList;

public class PatientService {

    // COLLECTION: a list that grows automatically (better than a fixed array)
    private ArrayList<Patient> patients = new ArrayList<>();

    // next ID to give; every new patient gets 101, 102, 103 ...
    private int nextId = 101;

    // ADD: creates a Patient object and stores it in the list
    public Patient addPatient(String name, int age, String gender, String phone,
                              String address, String bloodGroup, String condition) {
        Patient patient = new Patient(nextId, name, age, gender, phone, address, bloodGroup, condition);
        nextId++;
        patients.add(patient);
        return patient;
    }

    // VIEW: print every patient
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

    // SEARCH by ID (overloaded method 1): returns the patient or null if not found
    public Patient searchPatient(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    // SEARCH by name (overloaded method 2): same name, different parameter type
    public ArrayList<Patient> searchPatient(String name) {
        ArrayList<Patient> matches = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getName().toLowerCase().contains(name.toLowerCase())) {
                matches.add(patient);
            }
        }
        return matches;
    }

    // DELETE: returns true if deleted, false if the ID does not exist
    public boolean deletePatient(int id) {
        Patient patient = searchPatient(id);
        if (patient == null) {
            return false;
        }
        patients.remove(patient);
        return true;
    }
}