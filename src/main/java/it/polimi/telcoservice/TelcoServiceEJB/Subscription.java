package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

@Entity
@Table(name = "subscription", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "Subscription.findByPackage", query = "SELECT s FROM Subscription s WHERE s.servicePackage = ?1"),
        @NamedQuery(name = "Subscription.findByPackageAndPeriod", query = "SELECT s FROM Subscription s WHERE s.servicePackage = ?1 and s.validity_period = ?2")
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int subID;

    private int validity_period;
    private float fee;

    @OneToOne
    @JoinColumn(name = "order")
    private Order order;

    @ManyToOne
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }
}
