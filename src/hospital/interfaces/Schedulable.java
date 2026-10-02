package hospital.interfaces;

// INTERFACE: a contract. Any class that "implements Schedulable" PROMISES to provide these 3 methods.
// An interface has no code inside, only the method names.
public interface Schedulable {

    String getScheduleInfo();   // when is it scheduled?

    boolean isActive();         // is it still going to happen?

    void cancel();              // cancel it
}