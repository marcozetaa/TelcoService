package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

public class BadPackagePhoneChange extends Exception{
    private static long serialVersionID = 1L;

    public BadPackagePhoneChange( String message ){ super(message); }
}
