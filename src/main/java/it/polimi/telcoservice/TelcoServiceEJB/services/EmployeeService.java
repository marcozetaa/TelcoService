package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Employee;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Employee;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.CredentialException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class EmployeeService {

    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public EmployeeService() {
    }

    public Employee checkCredentials(Integer id, String pwd) throws CredentialException {

        List<Employee> eList = null;
        try{
            eList = em.createNamedQuery("Employee.checkCredentials", Employee.class)
                    .setParameter(1,id).setParameter(2,pwd).getResultList();
        } catch (PersistenceException e) {
            throw new CredentialException("Could not verify credentials");
        }

        if (eList.isEmpty()) {
            return null;
        } else if (eList.size() == 1)
            return eList.get(0);
        throw  new NonUniqueResultException("More than one Employee registered");
    }


}
