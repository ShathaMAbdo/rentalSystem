package se.bth.rentalSystem_server.Exceptions;


public class DeneyCancelException extends Throwable {
    public DeneyCancelException(){
        super("Reservation can cancel before start time but not after ");
    }
}
