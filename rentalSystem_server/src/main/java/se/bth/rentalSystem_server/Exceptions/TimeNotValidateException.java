package se.bth.rentalSystem_server.Exceptions;

public class TimeNotValidateException extends Throwable {
    public TimeNotValidateException(){
        super("The selected material is reserved in this entered time");
    }
}
