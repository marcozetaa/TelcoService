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
import java.util.*;

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

        String path = getServletContext().getContextPath()+"/Home?";

        int user_id;
        int package_id = 0;
        int period;
        int order_id = 0;

        float tot_value = 0;

        boolean isValid;

        String result;
        String name_package = "";

        String[] o_products;

        LocalDate date;
        LocalTime time;

        User user;

        // get all parameter names
        Set<String> paramNames = request.getParameterMap().keySet();

        user_id = Integer.parseInt(request.getParameter("user"));

        user = userService.getUser(user_id);

        path += "user=" + user.getUserID() + "&";

        result = StringEscapeUtils.escapeJava(request.getParameter("purchase"));
        period = Integer.parseInt(request.getParameter("val_period"));
        o_products = request.getParameterValues("optional_products");
        date = LocalDate.parse(request.getParameter("date"));
        time = LocalTime.now();

        if (paramNames.contains("order_id")) {
            //Reorder submit
            name_package = StringEscapeUtils.escapeJava(request.getParameter("name_package"));
            try {
                package_id = spService.findByName(name_package).getid();
            } catch (ServicePackageException e) {
                e.printStackTrace();
            }

            tot_value = computeOrderValue(package_id, period, o_products);

            order_id = Integer.parseInt(request.getParameter("order_id"));

            try {
                Order order = oService.findByID(order_id);
                oService.updateOrder(order_id, date, time, tot_value);
            } catch (OrderException | UpdateProfileException e) {
                e.printStackTrace();
            }
        } else if(paramNames.contains("package_id")){
            //Purchase submit
            package_id = Integer.parseInt(request.getParameter("package_id"));
            try {
                name_package = spService.findByID(package_id).getName();
            } catch (ServicePackageException e) {
                e.printStackTrace();
            }
            tot_value = computeOrderValue(package_id, period, o_products);
            order_id = oService.createOrder(user_id, date, time, tot_value, name_package);
        }

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

        if (isValid) {

            // Create Subscription in DB
            try {
                oService.changeOrderStatus(order_id,user_id,OrderStatus.VALID);
            } catch (BadOrderStatusChange | BadOrderClient | InvalidStatusChange e) {
                e.printStackTrace();
            }

            try {
                subService.createSubscription(period, tot_value, package_id, order_id, o_products);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create subscription");
                return;
            }

            path += "payment=" + true;

            response.sendRedirect(path);
        } else {

            //Order did not went well, control of insolvency
            try {
                oService.changeOrderStatus(order_id, user.getUserID(),OrderStatus.INVALID);
            } catch (BadOrderStatusChange | BadOrderClient | InvalidStatusChange e) {
                e.printStackTrace();
            }

            path += "payment=" + false;

            response.sendRedirect(path);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    public void destroy() {
    }

    public float computeOrderValue(int package_id, int period, String[] o_products){

        float tot_value = 0;

        try {
            tot_value = spService.getFee(package_id, period);
        } catch (BadPackagePhoneChange e) {
            e.printStackTrace();
        }

        tot_value += opService.getTotValue(o_products);

        return tot_value;
    }
}

