package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.List;

@Stateless
public class ServicePackageService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    private ServicePackageService(){
    }

    private void createPackage(String name, FixedPhoneStatus status, int  fee12, int fee24, int fee36){

        ServicePackage servicePackage = new ServicePackage(name,status,fee12, fee24, fee36);

        //TODO: Should we add also optional product and all other services lists?

        em.persist(servicePackage);

        System.out.println("Method createPackage after em.persist()");
        System.out.println("Is servicePackage object managed?  " + em.contains(servicePackage));
    }

    public List<ServicePackage> findByID(String id) throws OrderException {
        List<ServicePackage> spList = null;
        try {
            spList = em.createNamedQuery("ServicePackage.findByID", ServicePackage.class).setParameter(1,id).getResultList();
        } catch (PersistenceException e){
            throw new OrderException("Could not load packages");
        }
        return spList;
    }

    public List<ServicePackage> findByOptionalProduct(OptionalProduct sp) throws OrderException {
        List<ServicePackage> spList = null;
        try {
            spList = em.createNamedQuery("ServicePackage.findByOptionalProduct", ServicePackage.class).setParameter(1,sp).getResultList();
        } catch (PersistenceException e){
            throw new OrderException("Could not load packages");
        }
        return spList;
    }

    public List<ServicePackage> findByFI(FixedInternet fi) throws ServicePackageException {
        List<ServicePackage> spList = null;
        try {
            spList = em.createNamedQuery("ServicePackage.findByFI", ServicePackage.class).setParameter(1,fi).getResultList();
        } catch (PersistenceException e){
            throw new ServicePackageException("Could not load packages");
        }
        return spList;
    }

    public List<ServicePackage> findByMI(MobileInternet mi) throws ServicePackageException {
        List<ServicePackage> spList = null;
        try {
            spList = em.createNamedQuery("ServicePackage.findByMI", ServicePackage.class).setParameter(1,mi).getResultList();
        } catch (PersistenceException e){
            throw new ServicePackageException("Could not load packages");
        }
        return spList;
    }

    public List<ServicePackage> findByMI(MobilePhone mp) throws ServicePackageException {
        List<ServicePackage> spList = null;
        try {
            spList = em.createNamedQuery("ServicePackage.findByMP", ServicePackage.class).setParameter(1,mp).getResultList();
        } catch (PersistenceException e){
            throw new ServicePackageException("Could not load packages");
        }
        return spList;
    }

    public void changeFixedPhoneStatus(int packageId, FixedPhoneStatus status) throws BadPackagePhoneChange, BadOrderClient, InvalidStatusChange {
        System.out.println("Entering changeOrderStatus() method of OrderService component");

        ServicePackage servicePackage = null;
        try {
            servicePackage = em.find(ServicePackage.class, packageId);
        } catch (PersistenceException e) {
            throw new BadPackagePhoneChange("Could not fetch the package");
        }

        System.out.println("Method changeFixedPhoneStatus: Change the phone status in the package");

        servicePackage.setFixed_phone(status); // this could be encapsulated into a method

        try {
            em.flush(); // ensures status updated in the database before expenseReport addition
        } catch (PersistenceException e) {
            throw new InvalidStatusChange("Status update failed");
        }
        System.out.println("Exiting changeFixedPhoneStatus() method of ServicePackageService component");

    }

    //TODO: deleteServicePackage should include to eliminate also in Optional Product, FI, MP and MI?

}
