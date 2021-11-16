package it.polimi.telcoservice;

import jakarta.persistence.*;

public class MobilePhone{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private int mobilePhoneID;


    @ManyToOne
    @JoinColumn(name = "packageID")
    private ServicePackage servicePackage;

    private int num_min;
    private int num_sms;
    private int extra_min;
    private int extra_sms;


}
