package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.OptionalProduct;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Order;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Subscription;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.SubscriptionException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.List;

@Stateless
public class SubscriptionService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public SubscriptionService(){
    }

    public void createSubscription(int validity_period, float fee, int package_id, int order_id, String[] o_products) {
        ServicePackage servicePackage = em.find(ServicePackage.class, package_id);
        Order order = em.find(Order.class, order_id);

        Subscription subscription = new Subscription(validity_period, fee, servicePackage);
        if(o_products.length != 0){
            for (String o_product : o_products){
                subscription.addOptionalProduct(em.find(OptionalProduct.class, o_product));
            }
        }

        em.persist(subscription);

        // for debugging: let's check if subscription is managed
        System.out.println("Method createSubscription before servicePackage.addSubscription(subscription)");
        System.out.println("Is subscription object managed?  " + em.contains(subscription));

        subscription.setOrder(order);
        order.setSubscription(subscription);

        em.persist(subscription);
        em.merge(order);

        System.out.println("Method createSubscription after servicePackage.addSubscription(subscription)");
        System.out.println("Is subscription object managed?  " + em.contains(subscription));

    }

    public List<Subscription> findByPackage(int id) throws SubscriptionException {
        List<Subscription> sList = null;
        try {
            sList = em.createNamedQuery("Subscription.findByPackage", Subscription.class).setParameter(1,id).getResultList();
        } catch (PersistenceException e){
            throw new SubscriptionException("Could not load subscription");
        }
        return sList;
    }

    public List<Subscription> findByOrder(int id) throws SubscriptionException {
        List<Subscription> sList = null;
        try {
            sList = em.createNamedQuery("Subscription.findByOrder", Subscription.class).setParameter(1,id).getResultList();
        } catch (PersistenceException e){
            throw new SubscriptionException("Could not load subscription");
        }
        return sList;
    }

    public List<Subscription> findByPackageAndPeriod(int id, int validity_period) throws SubscriptionException {
        List<Subscription> sList = null;
        try{
            sList = em.createNamedQuery("Subscription.findByPackageAndPeriod", Subscription.class).setParameter(1,id).setParameter(2,validity_period).getResultList();
        } catch (PersistenceException e){
            throw new SubscriptionException("could not load subscription");
        }
        return sList;
    }

    //TODO: Shall we do also "deleteSubscription"?
}
