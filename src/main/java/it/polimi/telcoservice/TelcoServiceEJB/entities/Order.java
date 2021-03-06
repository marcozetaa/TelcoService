package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "order", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "Order.findByUser", query = "SELECT o FROM Order o WHERE o.client.userID = ?1 ORDER BY o.date_of_creation DESC"),
        @NamedQuery(name = "Order.findByID", query = "SELECT o FROM Order o WHERE o.id = ?1 ORDER BY o.date_of_creation DESC"),
        @NamedQuery(name = "Order.findByStatus", query = "SELECT o FROM Order o WHERE o.client=?1 AND o.status=?2")
})

public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "date_of_creation")
    private LocalDate date_of_creation;

    @JoinColumn(name = "hour_of_creation")
    private LocalTime hour_of_creation;

    @JoinColumn(name = "total_value")
    private float tot_value;

    @JoinColumn(name = "valid")
    private OrderStatus status;

    @JoinColumn(name = "name_package")
    private String name_package;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User client;

    @OneToOne(mappedBy = "order",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Subscription subscription;

    public Order() {
    }

    public Order(User client, LocalDate date_of_creation, LocalTime hour_of_creation, float tot_value, String name_package){
        this.client = client;
        this.date_of_creation = date_of_creation;
        this.hour_of_creation = hour_of_creation;
        this.tot_value = tot_value;
        this.status = OrderStatus.VALID;
        this.name_package = name_package;
    }

    public int getid() {
        return this.id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public LocalDate getDate_of_creation() {
        return this.date_of_creation;
    }

    public void setDate_of_creation(LocalDate date_of_creation) {  this.date_of_creation = date_of_creation; }

    public LocalTime getHour_of_creation() {
        return this.hour_of_creation;
    }

    public void setHour_of_creation(LocalTime hour_of_creation) { this.hour_of_creation = hour_of_creation; }

    public float getTot_value() {
        return this.tot_value;
    }

    public void setTot_value(float tot_value) {
        this.tot_value = tot_value;
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public OrderStatus getStatus() { return this.status; }

    public void setStatus(OrderStatus status) { this.status = status; }

    public User getClient() { return client; }

    public void setClient(User client) { this.client = client; }

    public String getName_package() {
        return name_package;
    }

    public void setName_package(String name_package) {
        this.name_package = name_package;
    }
}
