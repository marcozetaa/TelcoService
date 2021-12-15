package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "order", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "Order.findByUser", query = "SELECT o FROM Order o WHERE o.client = ?1"),
        @NamedQuery(name = "Order.findByID", query = "SELECT o FROM Order o WHERE o.orderID = ?1")
})

public class Order {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;
    private LocalDate date_of_creation;
    private LocalTime hour_of_creation;
    private float tot_value;

    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user")
    private User client;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription subscription;

    public Order() {
    }

    public Order(User client, LocalDate date_of_creation, LocalTime hour_of_creation, float tot_value){
        this.client = client;
        this.date_of_creation = date_of_creation;
        this.hour_of_creation = hour_of_creation;
        this.tot_value = tot_value;
        this.status = OrderStatus.VALID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDate getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(LocalDate date_of_creation) {  this.date_of_creation = date_of_creation; }

    public LocalTime getHour_of_creation() {
        return hour_of_creation;
    }

    public void setHour_of_creation(LocalTime hour_of_creation) { this.hour_of_creation = hour_of_creation; }

    public float getTot_value() {
        return tot_value;
    }

    public void setTot_value(float tot_value) {
        this.tot_value = tot_value;
    }

    public User getUser() {
        return client;
    }

    public void setUser(User client){
        this.client = client;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public OrderStatus getStatus() { return this.status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public User getClient() { return client; }

    public void setClient(User client) { this.client = client; }
}
