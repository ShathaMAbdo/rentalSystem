package se.bth.rentalSystem_server.Exceptions;

public class NotPayedException extends Throwable {
    public NotPayedException(){
        super("Payment not received ");
    }
    public NotPayedException(String s){
        super(s);
    }
}
