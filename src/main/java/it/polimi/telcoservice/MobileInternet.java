package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MobileInternet{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobileInternetID;


    @ManyToMany(mappedBy = "mobileInternets")
    private List<ServicePackage> servicePackage;

    private int numGB;
    private int extraGB;

}
