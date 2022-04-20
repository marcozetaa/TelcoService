package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Alert;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Employee;
import it.polimi.telcoservice.TelcoServiceEJB.entities.SalesReport;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.AlertException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.CredentialException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.SalesReportException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;

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

    public List<Alert> findAllInsolvent() throws AlertException {
        List<Alert> aList;
        try {
            aList = em.createNamedQuery("Alert.findAll", Alert.class).getResultList();
        } catch (PersistenceException e){
            e.printStackTrace();
            throw new AlertException("Could not load alerts");
        }

        return aList;
    }

    public List<SalesReport> findAllSalesReport() throws SalesReportException {
        List<SalesReport> reportList;
        try {
            reportList = em.createNamedQuery("SalesReport.findAll", SalesReport.class).getResultList();
        } catch (PersistenceException e){
            throw new SalesReportException("Could not load reports");
        }

        return reportList;
    }
}
