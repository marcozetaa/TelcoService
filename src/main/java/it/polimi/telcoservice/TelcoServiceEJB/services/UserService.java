package it.polimi.telcoservice.TelcoServiceEJB.services;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Order;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.entities.UserStatus;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.CredentialException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.UpdateProfileException;
import javax.ejb.Stateless;
import javax.persistence.*;

import java.util.List;
import java.util.Random;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "TelcoServiceEJB")
    private EntityManager em;

    public UserService() {
    }

    public User checkCredentials(String usrn, String pwd) throws CredentialException {
        List<User> uList = null;
        try{
            uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1,usrn).setParameter(2,pwd).getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new CredentialException("Could not verify credentials");
        }

        if (uList.isEmpty()) {
            return null;
        } else if (uList.size() == 1)
            return uList.get(0);
        throw  new NonUniqueResultException("More than one user registered");
    }

    public User registrateUser(String username, String password, String email, String name, String surname) throws CredentialException {
        long userInstances = 0;

        try {
            userInstances = (long) em.createNamedQuery("User.checkAlreadyRegistered").setParameter(1, email).setParameter(2, username).getSingleResult();
        }catch (PersistenceException e) {
            throw new CredentialException("Could not verify credentials");
        }

        if(userInstances != 0) {
            throw new CredentialException("username or email already in use");
        }
        else {
            User newU = new User(username, password, email, name, surname);
            em.persist(newU);
            em.flush(); //to ensure that the new inserted user can be used in the next login, even though the login uses a (SELECT) JPQL query which by rule force a flush before beign executed
            return newU;
        }
    }

    public void updateProfile(User u) throws UpdateProfileException{
        try {
            em.merge(u);
        } catch (PersistenceException e){
            throw new UpdateProfileException("Could not change profile");
        }
    }

    public void incrementsFailedPayments(User u){
        User user = em.find(User.class, u.getUserID());
        user.incrementFailedPayments();
    }

    public void setNumFailedPayments(User u){
        User user = em.find(User.class, u.getUserID());
        user.setNumFailedPayments(0);
    }

    public boolean randomPayment(){
        Random rd = new Random();
        return rd.nextBoolean();
    }

    public void setInsolvent(User u, UserStatus status){
        User user = em.find(User.class,u.getUserID());
        user.setInsolvent(status);
    }

    public User getUser(int user_id){


        User user = em.find(User.class,user_id);

        return user;
    }
}
