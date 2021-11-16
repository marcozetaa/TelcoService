package it.polimi.telcoservice;

import jakarta.persistence.*;

public class MobileInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int MobileInternetID;


    @ManyToOne
    @JoinColumn(name = "packageID")
    private ServicePackage servicePackage;

    private int numGB;
    private int extraGB;

}
