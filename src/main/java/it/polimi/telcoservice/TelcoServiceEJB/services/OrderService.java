package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Order;
import it.polimi.telcoservice.TelcoServiceEJB.entities.OrderStatus;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.BadOrderClient;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.BadOrderStatusChange;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.InvalidStatusChange;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Stateless
public class OrderService {
    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public OrderService() {
    }

    public void createOrder(int userID, int subscriptionID, LocalDate date_of_creation, LocalTime hour_of_creation, float tot_value){

        User client = em.find(User.class, userID);
        Order order = new Order(client, date_of_creation, hour_of_creation, tot_value);

        // for debugging: let's check if order is managed
        System.out.println("Method createOrder before client.addorder(order)");
        System.out.println("Is order object managed?  " + em.contains(order));

        client.addOrder(order); // updates both sides of the relationship

        System.out.println("Method createOrder AFTER client.addOrder(order)");
        System.out.println("Is mission object managed?  " + em.contains(order));


        em.persist(order); // makes also order object managed via cascading

        System.out.println("Method createOrder after em.persist()");
        System.out.println("Is mission object managed?  " + em.contains(order));
    }

    public List<Order> findByUserNoCache(int userid, OrderStatus status) throws OrderException {
        List<Order> oList = null;
        try {
            oList = em.createNamedQuery("Order.findByUser", Order.class).setHint("javax.persistence.cache.storeMode", "REFRESH").setParameter(1,userid).setParameter(2, status).getResultList();
        } catch (PersistenceException e){
            throw new OrderException("Could not load orders");
        }
        return oList;
    }

    public List<Order> findByID(String id) throws OrderException {
        List<Order> oList = null;
        try {
            oList = em.createNamedQuery("Order.findByUser", Order.class).setParameter(1,id).getResultList();
        } catch (PersistenceException e){
            throw new OrderException("Could not load orders");
        }
        return oList;
    }

    public void changeOrderStatus(int orderId, int clientId, OrderStatus status) throws BadOrderStatusChange, BadOrderClient, InvalidStatusChange {
        System.out.println("Entering changeOrderStatus() method of OrderService component");

        Order order = null;
        try {
            order = em.find(Order.class, orderId);
        } catch (PersistenceException e) {
            throw new BadOrderStatusChange("Could not fetch the mission");
        }

        if (order.getClient().getUserID() != clientId) {
            throw new BadOrderClient("Client not authorized to change the status of the order");
        }

        System.out.println("Method changeOrderStatus: Change the orders status");

        order.setStatus(status); // this could be encapsulated into a method

        try {
            em.flush(); // ensures status updated in the database before expenseReport addition
        } catch (PersistenceException e) {
            throw new InvalidStatusChange("Status update failed");
        }
        System.out.println("Exiting changeOrderStatus() method of OrderService component");

    }

    public void deleteOrder(int orderId, int clientId) throws BadOrderClient {
        Order order = em.find(Order.class, orderId);
        User owner = em.find(User.class, clientId);
        if (order.getClient() != owner) {
            throw new BadOrderClient("Client not authorized to delete this order");
        }
        owner.deleteOrder(order); // this updates both directions of the associations
        em.remove(order);
    }
}
