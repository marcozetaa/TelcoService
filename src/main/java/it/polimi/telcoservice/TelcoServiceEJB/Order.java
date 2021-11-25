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

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDate getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(LocalDate date_of_creation) {
        this.date_of_creation = date_of_creation;
    }

    public LocalTime getHour_of_creation() {
        return hour_of_creation;
    }

    public void setHour_of_creation(LocalTime hour_of_creation) {
        this.hour_of_creation = hour_of_creation;
    }

    public float getTot_value() {
        return tot_value;
    }

    public void setTot_value(float tot_value) {
        this.tot_value = tot_value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
