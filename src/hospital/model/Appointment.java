package hospital.model;

import hospital.interfaces.FileStorable;
import hospital.interfaces.Schedulable;
import hospital.util.Constants;

import java.time.LocalDate;
import java.time.LocalTime;

// One class can implement MANY interfaces (but extend only one class)
public class Appointment implements Schedulable, FileStorable {

    // enum = a fixed list of allowed values (safer than typing "scheduled" as text)
    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED
    }

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private Status status;

    // CONSTRUCTOR (full): used when loading from a file, because the status is already known
    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate date, LocalTime time, String reason, Status status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = status;
    }

    // CONSTRUCTOR OVERLOADING: a new appointment always starts as SCHEDULED
    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate date, LocalTime time, String reason) {
        this(appointmentId, patientId, doctorId, date, time, reason, Status.SCHEDULED);
    }

    public int getAppointmentId() { return appointmentId; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getReason() { return reason; }
    public Status getStatus() { return status; }

    // called by billing: the visit is over
    public void complete() {
        status = Status.COMPLETED;
    }

    // ----- the 3 methods promised by the Schedulable interface -----
    @Override
    public String getScheduleInfo() {
        return date + " " + time;
    }

    @Override
    public boolean isActive() {
        return status == Status.SCHEDULED;
    }

    @Override
    public void cancel() {
        status = Status.CANCELLED;
    }

    // ----- the method promised by the FileStorable interface -----
    @Override
    public String toFileString() {
        return appointmentId + Constants.DELIMITER + patientId + Constants.DELIMITER + doctorId
                + Constants.DELIMITER + date + Constants.DELIMITER + time + Constants.DELIMITER
                + reason + Constants.DELIMITER + status;
    }

    public static Appointment fromFileString(String line) {
        String[] p = line.split(Constants.DELIMITER_REGEX, -1);
        if (p.length != 7) {
            throw new IllegalArgumentException("An appointment line needs 7 fields: " + line);
        }
        return new Appointment(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                LocalDate.parse(p[3]), LocalTime.parse(p[4]), p[5], Status.valueOf(p[6]));
    }
}