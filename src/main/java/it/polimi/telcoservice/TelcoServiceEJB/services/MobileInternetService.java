package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.MobileInternet;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class MobileInternetService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public MobileInternetService(){
    }

    public void createMI(int numGB, int extraGB){
        MobileInternet mi = new MobileInternet(numGB,extraGB);

        System.out.println("Method createMI before em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(mi));

        em.persist(mi); // makes also order object managed via cascading

        System.out.println("Method createMI after em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(mi));
    }

    public List<MobileInternet> findAll() throws ServicePackageException {
        List<MobileInternet> fiList;
        try {
            fiList = em.createNamedQuery("MobileInternet.findAll", MobileInternet.class).getResultList();
        } catch (PersistenceException e){
            throw new ServicePackageException("Could not load Mobile internet");
        }

        return fiList;
    }

    //TODO: remove method must look for packages that contains this service and remove from its list
}
