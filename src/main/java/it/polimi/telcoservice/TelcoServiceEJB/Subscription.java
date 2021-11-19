package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int subID;

    private int validity_period;
    private float fee;

    @OneToOne
    @JoinColumn(name = "order")
    private Order order;

    @OneToOne
    @JoinColumn(name = "package")
    private ServicePackage servicePackage;

}
