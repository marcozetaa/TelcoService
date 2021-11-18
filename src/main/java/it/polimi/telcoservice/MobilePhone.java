package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MobilePhone{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobilePhoneID;


    @ManyToMany(mappedBy = "offer_package", fetch = FetchType.LAZY)
    private List<ServicePackage> servicePackage;

    private int num_min;
    private int num_sms;
    private int extra_min;
    private int extra_sms;


}
