package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class BadOrderClient extends Exception{
    private static long serialVersionID = 1L;

    public BadOrderClient(String message){ super(message); };
}
