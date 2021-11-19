package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int userID;
    private String username;
    private String password;
    private String email;
    private boolean insolvent;

    @OneToMany(mappedBy ="user", fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    private List<Order> orders;

}
