package it.polimi.telcoservice.TelcoServiceEJB.exceptions;

import it.polimi.telcoservice.TelcoServiceEJB.entities.SalesReport;

public class SalesReportException extends Exception{
    private static long serialVersionID = 1L;
    public SalesReportException(String message ) { super(message); }
}
