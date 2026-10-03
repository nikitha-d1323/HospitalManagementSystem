package hospital.util;

import hospital.interfaces.FileStorable;
import hospital.model.Appointment;
import hospital.model.Bill;
import hospital.model.Doctor;
import hospital.model.Patient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

// FILE HANDLING: every record is saved as ONE line of text, fields separated by "|".
public class FileHandler {

    // GENERICS: <T extends FileStorable> = "any type that can be stored in a file".
    // One method saves patients, doctors, appointments AND bills.
    // "throws IOException": the caller must handle a failure to write.
    public <T extends FileStorable> void saveRecords(String fileName, List<T> records) throws IOException {
        File folder = new File(Constants.DATA_DIR);
        if (!folder.exists()) {
            folder.mkdirs();                       // create the "data" folder if it is missing
        }
        File file = new File(folder, fileName);

        // try-with-resources: the writer is closed automatically, even if an error happens
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (T record : records) {
                writer.write(record.toFileString());
                writer.newLine();
            }
        }
    }

    public boolean dataFilesExist() {
        return new File(Constants.DATA_DIR, Constants.PATIENT_FILE).exists();
    }

    public String getDataFolderPath() {
        return new File(Constants.DATA_DIR).getAbsolutePath();
    }

    // reads all non-empty lines of a file; uses try / catch / finally
    private List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(Constants.DATA_DIR, fileName);
        if (!file.exists()) {
            return lines;                          // nothing saved yet
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("  ! Could not read " + fileName + ": " + e.getMessage());
        } finally {
            // finally ALWAYS runs: we close the file whether reading worked or failed
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("  ! Could not close " + fileName);
                }
            }
        }
        return lines;
    }

    // A damaged line is skipped with a warning; the program still starts.
    public List<Patient> loadPatients() {
        List<Patient> list = new ArrayList<>();
        for (String line : readLines(Constants.PATIENT_FILE)) {
            try {
                list.add(Patient.fromFileString(line));
            } catch (IllegalArgumentException e) {     // NumberFormatException is a kind of this
                System.out.println("  ! Skipped a damaged line in " + Constants.PATIENT_FILE + ": " + line);
            }
        }
        return list;
    }

    public List<Doctor> loadDoctors() {
        List<Doctor> list = new ArrayList<>();
        for (String line : readLines(Constants.DOCTOR_FILE)) {
            try {
                list.add(Doctor.fromFileString(line));
            } catch (IllegalArgumentException e) {
                System.out.println("  ! Skipped a damaged line in " + Constants.DOCTOR_FILE + ": " + line);
            }
        }
        return list;
    }

    public List<Appointment> loadAppointments() {
        List<Appointment> list = new ArrayList<>();
        for (String line : readLines(Constants.APPOINTMENT_FILE)) {
            try {
                list.add(Appointment.fromFileString(line));
            } catch (IllegalArgumentException | DateTimeException e) {   // bad number, status or date
                System.out.println("  ! Skipped a damaged line in " + Constants.APPOINTMENT_FILE + ": " + line);
            }
        }
        return list;
    }

    public List<Bill> loadBills() {
        List<Bill> list = new ArrayList<>();
        for (String line : readLines(Constants.BILL_FILE)) {
            try {
                list.add(Bill.fromFileString(line));
            } catch (IllegalArgumentException e) {
                System.out.println("  ! Skipped a damaged line in " + Constants.BILL_FILE + ": " + line);
            }
        }
        return list;
    }
}