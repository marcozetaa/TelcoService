package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;
    private LocalDate date_of_creation;
    private LocalTime hour_of_creation;
    private float tot_value;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription subscription;

}
