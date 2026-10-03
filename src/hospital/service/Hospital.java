package hospital.service;

import hospital.interfaces.Billable;
import hospital.model.Appointment;
import hospital.model.Bill;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.Person;
import hospital.model.ServiceType;
import hospital.model.Staff;
import hospital.util.Constants;
import hospital.util.FileHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

// The CENTRAL class. It creates all the services once, shares them, and handles load / save / summary.
public class Hospital {

    private String name;
    private PatientService patientService;
    private DoctorService doctorService;
    private StaffService staffService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private FileHandler fileHandler = new FileHandler();

    public Hospital(String name) {
        this.name = name;
        patientService = new PatientService();
        doctorService = new DoctorService();
        staffService = new StaffService();
        appointmentService = new AppointmentService(patientService, doctorService);
        billingService = new BillingService(appointmentService, doctorService, patientService);
    }

    public String getName() { return name; }
    public PatientService getPatientService() { return patientService; }
    public DoctorService getDoctorService() { return doctorService; }
    public StaffService getStaffService() { return staffService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public BillingService getBillingService() { return billingService; }

    // POLYMORPHISM: one list of Person holds Patient, Doctor and Staff objects.
    // person.displayDetails() runs the correct overridden version for each object at run time.
    public void showAllPeople() {
        ArrayList<Person> everyone = new ArrayList<>();
        everyone.addAll(patientService.getAllPatients());
        everyone.addAll(doctorService.getAllDoctors());
        everyone.addAll(staffService.getAllStaff());

        if (everyone.isEmpty()) {
            System.out.println("No people registered yet.");
            return;
        }
        for (Person person : everyone) {
            person.displayDetails();         // same call, different output for each kind of person
            System.out.println();
        }
        System.out.println("Total people associated with the hospital: " + everyone.size());
    }

    public void showSummary() {
        int scheduled = 0;
        int completed = 0;
        int cancelled = 0;
        for (Appointment appointment : appointmentService.getAllAppointments()) {
            switch (appointment.getStatus()) {
                case SCHEDULED:
                    scheduled++;
                    break;
                case COMPLETED:
                    completed++;
                    break;
                default:
                    cancelled++;
            }
        }

        double totalBilled = 0;
        for (Billable bill : billingService.getAllBills()) {   // the INTERFACE type is used as a reference
            totalBilled += bill.calculateBill();
        }

        System.out.println("\n========== " + name + " - SUMMARY ==========");
        System.out.println("Patients             : " + patientService.getAllPatients().size());
        System.out.println("Doctors              : " + doctorService.getAllDoctors().size());
        System.out.println("Staff members        : " + staffService.getAllStaff().size());
        System.out.println("Appointments (total) : " + appointmentService.getAllAppointments().size());
        System.out.println("   Scheduled         : " + scheduled);
        System.out.println("   Completed         : " + completed);
        System.out.println("   Cancelled         : " + cancelled);
        System.out.println("Bills generated      : " + billingService.getAllBills().size());
        System.out.println(String.format("Total billed amount  : Rs. %.2f", totalBilled));
    }

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
        for (Staff staff : fileHandler.loadStaff()) {
            staffService.addLoaded(staff);
        }
        for (Appointment appointment : fileHandler.loadAppointments()) {
            appointmentService.addLoaded(appointment);
        }
        for (Bill bill : fileHandler.loadBills()) {
            billingService.addLoaded(bill);
        }
        System.out.println("Saved data loaded: " + patientService.getAllPatients().size() + " patients, "
                + doctorService.getAllDoctors().size() + " doctors, "
                + staffService.getAllStaff().size() + " staff, "
                + appointmentService.getAllAppointments().size() + " appointments, "
                + billingService.getAllBills().size() + " bills.");
    }

    // Called by "Save Data" and when the program exits.
    public void saveData() throws IOException {
        fileHandler.saveRecords(Constants.PATIENT_FILE, patientService.getAllPatients());
        fileHandler.saveRecords(Constants.DOCTOR_FILE, doctorService.getAllDoctors());
        fileHandler.saveRecords(Constants.STAFF_FILE, staffService.getAllStaff());
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

            staffService.addStaff("Sunita Devi", 30, "Female", "9844455566", "Nurse", 28000);
            staffService.addStaff("Karthik S", 26, "Male", "9855566677", "Receptionist", 22000);

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