package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "product", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "OptionalProduct.findAll", query = "SELECT op FROM OptionalProduct op"),
        @NamedQuery(name = "OptionalProduct.findByName", query = "SELECT op FROM OptionalProduct op WHERE op.name = ?1"),
})
public class OptionalProduct {

    @Id
    private String name;
    private float monthly_fee;

    @ManyToMany(mappedBy = "optionalProducts")
    private List<ServicePackage> servicePackage;

    public OptionalProduct(){
    }

    public OptionalProduct(String name, float monthly_fee){
        this.name = name;
        this.monthly_fee = monthly_fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMonthly_fee() {
        return monthly_fee;
    }

    public void setMonthly_fee(float monthly_fee) {
        this.monthly_fee = monthly_fee;
    }

    public List<ServicePackage> getServicePackage() {
        return servicePackage;
    }

    public void addServicePackage(ServicePackage sp){
        getServicePackage().add(sp);
        sp.getOptionalProducts().add(this);
    }

    public void removeServicePackage(ServicePackage sp){
        getServicePackage().remove(sp);
        sp.getSubscriptions().remove(this);
    }
}
