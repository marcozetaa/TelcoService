package it.polimi.telcoservice;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;
    private LocalDate date_of_creation;
    private LocalTime hour_of_creation;
    private float tot_value;

    @ManyToOne
    @JoinColumn(name = "User")
    private User user;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "order_product",
            joinColumns = { @JoinColumn(name = "name") },
            inverseJoinColumns = { @JoinColumn(name = "orderID") }
    )
    private List<OptionalProduct> optional_products;


}
