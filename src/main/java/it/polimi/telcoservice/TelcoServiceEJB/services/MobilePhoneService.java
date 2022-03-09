package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.MobilePhone;
import it.polimi.telcoservice.TelcoServiceEJB.entities.MobileInternet;
import it.polimi.telcoservice.TelcoServiceEJB.entities.MobilePhone;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class MobilePhoneService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public MobilePhoneService(){
    }

    public void createMP(int num_min, int num_sms, int extra_min, int extra_sms){
        MobilePhone mp = new MobilePhone(num_min, num_sms, extra_min, extra_sms);

        System.out.println("Method createMP before em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(mp));

        em.persist(mp); // makes also order object managed via cascading

        System.out.println("Method createMP after em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(mp));
    }

    public List<MobilePhone> findAll() throws ServicePackageException {
        List<MobilePhone> fiList;
        try {
            fiList = em.createNamedQuery("MobilePhone.findAll", MobilePhone.class).getResultList();
        } catch (PersistenceException e){
            throw new ServicePackageException("Could not load Mobile Phone");
        }

        return fiList;
    }

    //TODO: remove method must look for packages that contains this service and remove from its list
}
