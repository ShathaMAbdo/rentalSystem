package se.bth.rentalSystem_server.Exceptions;

public class EmptyRentalTime extends Throwable {
    public EmptyRentalTime(){
        super("You must determine date for reservation ");
    }
}
