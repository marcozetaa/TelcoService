package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscription", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "Subscription.findByPackage", query = "SELECT s FROM Subscription s WHERE s.servicePackage = ?1"),
        @NamedQuery(name = "Subscription.findByOrder", query = "SELECT s FROM Subscription s WHERE s.order = ?1"),
        @NamedQuery(name = "Subscription.findByPackageAndPeriod", query = "SELECT s FROM Subscription s WHERE s.servicePackage = ?1 and s.validity_period = ?2")
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "validity_period")
    private int validity_period;

    @JoinColumn(name = "fee")
    private float fee;

    @OneToOne
    @JoinColumn(name = "id_order")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "id_package")
    private ServicePackage servicePackage;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "subscribe_product",
            joinColumns = { @JoinColumn(name = "id_subscribe") },
            inverseJoinColumns = { @JoinColumn(name = "name_product") }
    )
    private ArrayList<OptionalProduct> optionalProductsSub = new ArrayList<>();

    public Subscription(){
    }

    public Subscription(int validity_period, float fee, ServicePackage servicePackage){
        this.validity_period = validity_period;
        this.fee = fee;
        this.servicePackage = servicePackage;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
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
    public void addOptionalProduct(OptionalProduct op){
        getOptionalProducts().add(op);
        op.getSubscriptions().add(this);
    }

    public List<OptionalProduct> getOptionalProducts() {
        return optionalProductsSub;
    }

    public ServicePackage getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(ServicePackage servicePackage) {
        this.servicePackage = servicePackage;
    }
}
