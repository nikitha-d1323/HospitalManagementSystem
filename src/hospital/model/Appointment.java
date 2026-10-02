package hospital.model;

import hospital.interfaces.Schedulable;

import java.time.LocalDate;
import java.time.LocalTime;

// "implements" = this class keeps the promise made by the Schedulable interface
public class Appointment implements Schedulable {

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

    // a new appointment always starts as SCHEDULED
    public Appointment(int appointmentId, int patientId, int doctorId,
                       LocalDate date, LocalTime time, String reason) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.reason = reason;
        this.status = Status.SCHEDULED;
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
}