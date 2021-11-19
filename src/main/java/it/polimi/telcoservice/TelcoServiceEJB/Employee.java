package it.polimi.telcoservice.TelcoServiceEJB;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Employee {

    @Id
    private String username;
    private String password;
}
