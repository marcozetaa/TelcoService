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

    public int getSubID() {
        return subID;
    }

    public void setSubID(int subID) {
        this.subID = subID;
    }

    public int getValidity_period() {
        return validity_period;
    }

    public void setValidity_period(int validity_period) {
        this.validity_period = validity_period;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
