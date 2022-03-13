package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "user", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
        @NamedQuery(name = "User.checkAlreadyRegistered", query = "SELECT COUNT(r) FROM User r WHERE r.email = ?1 or r.username = ?2")
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @JoinColumn(name = "username")
    private String username;

    @JoinColumn(name = "password")
    private String password;

    @JoinColumn(name = "email")
    private String email;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "surname")
    private String surname;

    @JoinColumn(name = "status")
    private UserStatus status;

    @JoinColumn(name = "num_failed_payments")
    private int num_failed_payments;

    @OneToMany(mappedBy ="client", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<Order> orders;

    public User() {
    }

    public User(String username, String password, String email, String name, String surname){
        this.orders = orders;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.status = UserStatus.SOLVENT;
        this.num_failed_payments = 0;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus isInsolvent() {
        return status;
    }

    public void setInsolvent(UserStatus status) {
        this.status = status;
    }

    public List<Order> getOrders(){
        return orders;
    }

    public void addOrder(Order order){
        getOrders().add((order));
        order.setUser(this);
    }

    public void deleteOrder(Order order){
        getOrders().remove(order);
    }

    public int getNumFailedPayments() {
        return num_failed_payments;
    }

    public void setNumFailedPayments(int num_failed_payments) {
        this.num_failed_payments = num_failed_payments;
    }

    public void incrementFailedPayments() {
        this.num_failed_payments++;
    }

}
