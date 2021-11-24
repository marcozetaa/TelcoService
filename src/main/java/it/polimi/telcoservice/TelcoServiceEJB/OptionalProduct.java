package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OptionalProduct {

    @Id
    private String name;
    private float monthly_fee;

    @ManyToMany(mappedBy = "optionalProducts")
    private List<ServicePackage> servicePackage;

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
}
