package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Alert", schema = "telco_service_db")
@NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_Client")
    private int id_client;
    @JoinColumn(name = "Username")
    private String username;
    @JoinColumn(name = "Email")
    private String email;
    @JoinColumn(name = "TotalAmount")
    private float total;
    @JoinColumn(name = "DateOfCreation")
    private LocalDate date_of_creation;
    @JoinColumn(name = "HourOfCreation")
    private LocalTime hour_of_creation;

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
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
}


