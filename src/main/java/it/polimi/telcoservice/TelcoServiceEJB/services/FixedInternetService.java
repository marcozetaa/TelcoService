package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.FixedInternet;
import it.polimi.telcoservice.TelcoServiceEJB.entities.MobileInternet;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class FixedInternetService {
    @PersistenceContext(unitName = "telcoServiceEJB")
    private EntityManager em;

    public FixedInternetService(){
    }

    public void createFI(int numGB, int extraGB){
        FixedInternet fi = new FixedInternet(numGB,extraGB);

        System.out.println("Method createFI before em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(fi));

        em.persist(fi); // makes also order object managed via cascading

        System.out.println("Method createFI after em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(fi));
    }

    //TODO: remove method must look for packages that contains this service and remove from its list
}
