package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.OptionalProduct;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Subscription;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.BadOrderClient;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OptionalProductException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.SubscriptionException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class OptionalProductService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public  OptionalProductService(){
    }

    public void createOptionalProduct(String name, float monthly_fee){
        OptionalProduct op = new OptionalProduct(name,monthly_fee);

        System.out.println("Method createOrder before em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(op));

        em.persist(op); // makes also order object managed via cascading

        System.out.println("Method createOrder after em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(op));
    }

    public List<OptionalProduct> findAll() throws OrderException {
        List<OptionalProduct> opList = null;
        try {
            opList = em.createNamedQuery("OptionalProduct.findAll", OptionalProduct.class).getResultList();
        } catch (PersistenceException e){
            throw new OrderException("Could not load packages");
        }
        return opList;
    }

    public OptionalProduct findByName(String name) throws OptionalProductException {
        OptionalProduct op = null;
        try {
            op = em.createNamedQuery("OptionalProduct.findByName", OptionalProduct.class).setParameter(1,name).getSingleResult();
        } catch (PersistenceException e){
            throw new OptionalProductException("Could not load optional products");
        }
        return op;
    }

    public float getTotValue(String[] o_products){

        float tot_value = 0;

        if(o_products == null)
            return tot_value;

        List<OptionalProduct> opList = new ArrayList<>();

        for(int i = 0; i < o_products.length; i++) {
            OptionalProduct op = null;
            try {
                op = findByName(o_products[i]);
            } catch (OptionalProductException e) {
                e.printStackTrace();
            }
            opList.add(op);
        }

        for (int i = 0; i < opList.size(); i++){
            tot_value += opList.get(i).getMonthly_fee();
        }

        return tot_value;
    }

    public void deleteOptionalProduct(int optionalProductId, int servicePackageId)  {
        ServicePackage sp = em.find(ServicePackage.class, servicePackageId);
        OptionalProduct op = em.find(OptionalProduct.class, optionalProductId);

        sp.removeOptionalProduct(op); // this updates both directions of the associations
        em.remove(op);
    }

}
