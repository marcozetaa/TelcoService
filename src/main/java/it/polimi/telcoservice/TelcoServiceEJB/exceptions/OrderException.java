package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class OrderException extends Exception{
    private static long serialVersionID = 1L;

    public OrderException(String message){ super(message); };
}

