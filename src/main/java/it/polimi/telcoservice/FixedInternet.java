package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class FixedInternet{


    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int fixedInternetID;


    @ManyToMany(mappedBy = "fixedInternets")
    private List<ServicePackage> servicePackage;

    private int numGB;
    private int extraGB;

}
