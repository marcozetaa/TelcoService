package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OptionalProduct {

    @Id
    private String name;
    private float monthly_fee;

    @ManyToMany( mappedBy = "optional_products", fetch = FetchType.EAGER)
    private List<Order> orders;

}
