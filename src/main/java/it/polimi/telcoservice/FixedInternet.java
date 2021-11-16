package it.polimi.telcoservice;

import jakarta.persistence.*;

public class FixedInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int fixedInternetID;


    @ManyToOne
    @JoinColumn(name = "packageID")
    private ServicePackage servicePackage;

    private int numGB;
    private int extraGB;

}
