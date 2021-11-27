package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.MobileInternet;
import it.polimi.telcoservice.TelcoServiceEJB.entities.OptionalProduct;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MobileInternetService {
    @PersistenceContext(unitName = "telcoServiceEJB")
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

    //TODO: remove method must look for packages that contains this service and remove from its list
}
