package it.polimi.telcoservice.TelcoServiceEJB.entities;

import javax.persistence.*;

@Entity
@Table(name = "employee", schema = "telco_service_db")
@NamedQueries({
        @NamedQuery(name = "Employee.checkCredentials", query = "SELECT e FROM Employee e  WHERE e.email = ?1 and e.password = ?2"),
})
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int employeeID;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
}
