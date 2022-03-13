package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.services.OrderService;
import it.polimi.telcoservice.TelcoServiceEJB.services.SubscriptionService;
import it.polimi.telcoservice.TelcoServiceEJB.services.UserService;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "Confirmation", value = "/Confirmation")
public class Confirmation extends HttpServlet {

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OrderService")
    private OrderService oService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/SubscriptionService")
    private SubscriptionService subService;

    Subscription subscription;
    boolean creatingPackage = true;
    String rejectedOrderID;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Order> orders;

        try {
            orders = oService.findByUserNoCache(user.getUserID(), OrderStatus.VALID);
        } catch (OrderException e) {
            e.printStackTrace();
        }

        String result = request.getParameter("result");

        String destServlet;
        Order order;

        boolean isValid;
        switch (result) {
            case "success":
                isValid = true;
                break;
            case "fail":
                isValid = false;
                break;
            case "random":
                isValid = userService.randomPayment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + result);
        }

/*
        if(creatingPackage){
            // get and check params
            Integer package_id;
            try {
                package_id = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException | NullPointerException e) {
                // only for debugging e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
                return;
            }
            try {
                subscription = subService.createSubscription();
                        //userService.createSubscription(Subscription, user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            order = oService.createOrder();
        }
        else {
            //order = oService.findByID(Integer.parseInt(rejectedOrderID)).get();
            //order = userService.updateOrder(order, isValid);
        }

        if(!isValid) user = userService.incrementsFailedPayments(user);

        if(user.getNumFailedPayments()==3){
            AlertEntity alert = new AlertEntity(order.getTotalValueOrder(), order.getDateAndHour(), user);
            userService.createAlert(alert);
            user = userService.setNumFailedPayments(user);
        }

        // if the user has rejected orders we set him to insolvent
        if(userService.findRejectedOrdersByUser(user.getUser_id()).size()>=1) userService.setUserInsolvent(user, true);
        else userService.setUserInsolvent(user, false);

        if(userService.findOrdersToActivate(user.getUser_id()).size()>0) destServlet = "serviceActivationSchedule";
        else destServlet = "homePageCustomer";

        resp.sendRedirect(destServlet);

*/
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        rejectedOrderID = req.getParameter("rejectedOrder");
/*
        if(rejectedOrderID!=null){
            Subscription = userService.findOrderByID(Long.parseLong(rejectedOrderID)).get().getSubscription();
            creatingPackage = false;
        }
        else{
            Subscription = (SubscriptionEntity) req.getSession(false).getAttribute("Subscription");
            creatingPackage = true;
        }

        req.setAttribute("Subscription", Subscription);

        RequestDispatcher dispatcher = req.getRequestDispatcher("confirmationPage.jsp");
        dispatcher.forward(req, resp);
*/
    }



}

