package se.bth.rentalSystem_server.Exceptions;

public class TimeNotAvailableException extends Exception {
    public TimeNotAvailableException() {
        super("The entered time dose not matching with available Time of borrowed stuff");
    }
}
