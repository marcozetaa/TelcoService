package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class SubscriptionException extends Exception{
    private static long serialVersionID = 1L;

    public SubscriptionException( String message ) { super(message); }
}
