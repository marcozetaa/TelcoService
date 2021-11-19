package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MobilePhone{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobilePhoneID;

    @ManyToMany(mappedBy = "mobilePhones")
    private List<ServicePackage> servicePackage;

    private int num_min;
    private int num_sms;
    private int extra_min;
    private int extra_sms;

}
