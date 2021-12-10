package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.List;

@Entity
public class Employee {

    @Id
    private String username;
    private String password;

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
}
