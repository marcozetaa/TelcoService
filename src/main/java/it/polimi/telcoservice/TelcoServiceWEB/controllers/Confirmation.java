package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.*;
import it.polimi.telcoservice.TelcoServiceEJB.services.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.Provider;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "Confirmation", value = "/Confirmation")
public class Confirmation extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/UserService")
    private UserService userService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OrderService")
    private OrderService oService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/SubscriptionService")
    private SubscriptionService subService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService spService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "WEB-INF/Home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        //In case of explicit buying commands
        //String result = request.getParameter("result");
        int package_id = Integer.parseInt(request.getParameter("package_id"));
        int period = Integer.parseInt(request.getParameter("val_period"));
        String[] o_products = request.getParameterValues("optional_products");

        LocalDate date = LocalDate.parse(request.getParameter("date"));
        LocalTime time = LocalTime.now();

        float tot_value = 0;

        try {
            tot_value = spService.getFee(package_id, period);
        } catch (BadPackagePhoneChange e) {
            e.printStackTrace();
        }

        tot_value += opService.getTotValue(o_products);

        int order_id = oService.createOrder(user,date,time,tot_value);

        boolean isValid = true;
        /* IN CASE OF EXPLICIT COMMANDS
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
*/

        List<ServicePackage> packages = null;
        List<Order> orderList = null;
        try{

            packages = spService.findAll();
            orderList = oService.findByUserNoCache(user.getUserID());

        } catch (OrderException | ServicePackageException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Not possible to get services");
        }

        if(isValid){

            // Create Subscription in DB

            int sub_id;

            try {
                sub_id = subService.createSubscription(period,opService.getTotValue(o_products),package_id,order_id);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create subscription");
                return;
            }

            //Connect order to subscription

            try {
                oService.updateOrder(order_id);
            } catch (UpdateProfileException e) {
                e.printStackTrace();
            }

            ctx.setVariable("user",user);
            ctx.setVariable("my_orders",orderList);
            ctx.setVariable("packages", packages);
            ctx.setVariable("payment",true);
            templateEngine.process(path, ctx, response.getWriter());

        } else{

            //Order did not went well, control of insolvency
            try {
                oService.changeOrderStatus(order_id,user.getUserID(),OrderStatus.INVALID);
            } catch (BadOrderStatusChange | BadOrderClient | InvalidStatusChange e) {
                e.printStackTrace();
            }

            userService.incrementsFailedPayments(user);

            try {
                userService.updateProfile(user);
                oService.updateOrder(order_id);
            } catch (UpdateProfileException e) {
                e.printStackTrace();
            }

            if(user.getNumFailedPayments() == 3 && user.isInsolvent() == UserStatus.SOLVENT){
                userService.setInsolvent(user,UserStatus.INSOLVENT);
                userService.setNumFailedPayments(user);
                try {
                    userService.updateProfile(user);
                } catch (UpdateProfileException e) {
                    e.printStackTrace();
                }
            }

            ctx.setVariable("user",user);
            ctx.setVariable("my_orders",orderList);
            ctx.setVariable("packages", packages);
            ctx.setVariable("payment",false);
            templateEngine.process(path, ctx, response.getWriter());

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

