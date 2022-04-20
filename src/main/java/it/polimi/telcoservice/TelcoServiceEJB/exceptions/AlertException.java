package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class AlertException extends Exception{
    private static long serialVersionID = 1L;
    public AlertException( String message ) { super(message); }
}
