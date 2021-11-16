package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ServicePackage {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private int packageID;
    private String name;
    private boolean fixed_phone;

    @ManyToOne
    @JoinColumn(name = "MobileInternetID")
    private ServicePackage MobileInternet;

    @ManyToOne
    @JoinColumn(name = "mobilePhoneID")
    private ServicePackage mobilePhone;

    @ManyToOne
    @JoinColumn(name = "fixedInternetID")
    private ServicePackage fixedInternet;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "service_product",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "serviceID") }
    )
    private List<OptionalProduct> optional_products;

}
