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
}
