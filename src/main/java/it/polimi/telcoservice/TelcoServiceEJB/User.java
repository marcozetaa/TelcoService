package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
        @NamedQuery(name = "User.checkAlreadyRegistered", query = "SELECT r FROM User r WHERE r.email = ?1 or r.username = ?2")
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int userID;
    private String username;
    private String password;
    private String email;
    private boolean insolvent;

    @OneToMany(mappedBy ="user", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<Order> orders;

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

    public boolean isInsolvent() {
        return insolvent;
    }

    public void setInsolvent(boolean insolvent) {
        this.insolvent = insolvent;
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
}
