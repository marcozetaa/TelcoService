package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class ServicePackageException extends Exception{
    private static long serialVersionID = 1L;

    public ServicePackageException( String message ) { super(message); }
}
