package hospital.service;

import hospital.model.Appointment;
import hospital.model.Bill;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.ServiceType;
import hospital.util.Constants;
import hospital.util.FileHandler;

import java.io.IOException;
import java.time.LocalDate;

// The CENTRAL class. It creates all the services once, shares them, and handles load / save.
public class Hospital {

    private String name;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private FileHandler fileHandler = new FileHandler();

    public Hospital(String name) {
        this.name = name;
        patientService = new PatientService();
        doctorService = new DoctorService();
        appointmentService = new AppointmentService(patientService, doctorService);
        billingService = new BillingService(appointmentService, doctorService, patientService);
    }

    public String getName() { return name; }
    public PatientService getPatientService() { return patientService; }
    public DoctorService getDoctorService() { return doctorService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public BillingService getBillingService() { return billingService; }

    // Called when the program starts.
    // First run (no data folder yet) -> sample data. Later runs -> read the saved files.
    public void loadData() {
        if (!fileHandler.dataFilesExist()) {
            System.out.println("No saved data found - loading sample data for demonstration.");
            loadSampleData();
            return;
        }
        // the ORDER matters: patients and doctors first, because appointments refer to them
        for (Patient patient : fileHandler.loadPatients()) {
            patientService.addLoaded(patient);
        }
        for (Doctor doctor : fileHandler.loadDoctors()) {
            doctorService.addLoaded(doctor);
        }
        for (Appointment appointment : fileHandler.loadAppointments()) {
            appointmentService.addLoaded(appointment);
        }
        for (Bill bill : fileHandler.loadBills()) {
            billingService.addLoaded(bill);
        }
        System.out.println("Saved data loaded: " + patientService.getAllPatients().size() + " patients, "
                + doctorService.getAllDoctors().size() + " doctors, "
                + appointmentService.getAllAppointments().size() + " appointments, "
                + billingService.getAllBills().size() + " bills.");
    }

    // Called by "Save Data" and when the program exits.
    public void saveData() throws IOException {
        fileHandler.saveRecords(Constants.PATIENT_FILE, patientService.getAllPatients());
        fileHandler.saveRecords(Constants.DOCTOR_FILE, doctorService.getAllDoctors());
        fileHandler.saveRecords(Constants.APPOINTMENT_FILE, appointmentService.getAllAppointments());
        fileHandler.saveRecords(Constants.BILL_FILE, billingService.getAllBills());
        System.out.println("Data saved to folder: " + fileHandler.getDataFolderPath());
    }

    // fictional demo data, created through the same validated methods the menus use
    private void loadSampleData() {
        try {
            patientService.addPatient("Arjun Kumar", 34, "Male", "9876543210",
                    "12 Gandhi Street, Coimbatore", "B+", "Diabetes");
            patientService.addPatient("Meena Raj", 28, "Female", "9123456780",
                    "45 Lake View Road, Sulur", "O+", "Migraine");

            doctorService.addDoctor("Dr. Anita Sharma", 42, "Female", "9811122233", "Cardiology", 15, 800);
            doctorService.addDoctor("Dr. Vikram Rao", 38, "Male", "9822233344", "Neurology", 11, 700);

            // dates are calculated from today, so they are always in the future
            LocalDate soon = LocalDate.now().plusDays(2);
            appointmentService.bookAppointment(101, 201, soon.toString(), "10:00", "Diabetes follow-up");
            appointmentService.bookAppointment(102, 202, soon.toString(), "11:30", "Recurring headaches");
            appointmentService.bookAppointment(101, 202, soon.plusDays(1).toString(), "09:30", "Blood pressure check");

            // two sample bills; appointment 503 stays free to bill in the demo
            Bill first = billingService.generateBill(501);
            first.addService(ServiceType.LAB_TEST, 2);
            first.addService(ServiceType.ECG);
            Bill second = billingService.generateBill(502);
            second.addService(ServiceType.XRAY);
            second.addService(ServiceType.MEDICINES, 3);
        } catch (Exception e) {
            System.out.println("Could not load sample data: " + e.getMessage());
        }
    }
}