package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Order;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import it.polimi.telcoservice.TelcoServiceEJB.entities.Subscription;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
import it.polimi.telcoservice.TelcoServiceEJB.services.OptionalProductService;
import it.polimi.telcoservice.TelcoServiceEJB.services.OrderService;
import it.polimi.telcoservice.TelcoServiceEJB.services.ServicePackageService;
import it.polimi.telcoservice.TelcoServiceEJB.services.SubscriptionService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@WebServlet(name = "CreateSubscription", value = "/Subscription")
public class CreateSubscription extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/SubscriptionService")
    private SubscriptionService subService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService spService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OrderService")
    private OrderService oService;

    public CreateSubscription(){
        super();
    }

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    private Date getMeYesterday() {
        return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = "WEB-INF/Home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        // get all parameter names
        Set<String> paramNames = request.getParameterMap().keySet();

        // iterating over parameter names and get its value
        for (String name : paramNames) {
            String value = request.getParameter(name);
        }

        // Get and parse all parameters from request
        //String[] o_products = request.getParameter();
        //int period = Integer.parseInt(urlParams.get("val_period"));
        //int package_id = Integer.parseInt(urlParams.get("package_id"));
        //int order_id = Integer.parseInt(urlParams.get("order_id"));



        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("client");

        // Create Subscription in DB
        try {
           // subService.createSubscription(period,opService.getTotValue(o_products),package_id,order_id);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create subscription");
            return;
        }

        List<ServicePackage> packages = null;
        List<Order> orderList = null;
        try{

            packages = spService.findAll();
            orderList = oService.findByUserNoCache(user.getUserID());

        } catch (OrderException | ServicePackageException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Not possible to get services");
        }

//      ctx.setVariable("subscription", subscription);
        ctx.setVariable("payment",true);
        ctx.setVariable("user",user);
        ctx.setVariable("my_orders",orderList);
        ctx.setVariable("packages", packages);

        templateEngine.process(path, ctx, response.getWriter());
    }

        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
