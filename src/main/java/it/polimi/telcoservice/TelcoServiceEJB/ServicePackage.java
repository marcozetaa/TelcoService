package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ServicePackage {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private int packageID;
    private String name;
    private boolean fixed_phone;


    //aggiungere in name il nome della tabella nel db
    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "fixed_internet_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "fixed_internetID") }
    )
    private List<FixedInternet> fixedInternets;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "mobile_internet_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "mobile_InternetID") }
    )
    private List<MobileInternet> mobileInternets;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "mobile_phone_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "mobile_PhoneID") }
    )
    private List<MobilePhone> mobilePhones;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "optional_package",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "optional_name") }
    )
    private List<OptionalProduct> optionalProducts;

}
