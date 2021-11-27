package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class BadOrderStatusChange extends Exception {
    private static final long serialVersionUID = 1L;

    public BadOrderStatusChange(String message) {
        super(message);
    }
}

