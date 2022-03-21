package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OptionalProductException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.UpdateProfileException;
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
    private OptionalProductService optionalProductService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/FixedInternetService")
    private FixedInternetService fiService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobileInternetService")
    private MobileInternetService miService;

    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobilePhoneService")
    private MobilePhoneService mpService;

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

        ServicePackage servicePackage = null;
        int service_package_id = Integer.parseInt(request.getParameter("package_id"));
        try {
            servicePackage = spService.findByID(service_package_id);
        } catch (ServicePackageException e) {
            e.printStackTrace();
        }

        String o_products[] = request.getParameterValues("optional_products");
        List<OptionalProduct> opList = new ArrayList<>();
        for(int i = 0; i < o_products.length; i++) {
            OptionalProduct op = null;
            try {
                op = optionalProductService.findByName(o_products[i]);
            } catch (OptionalProductException e) {
                e.printStackTrace();
            }
            opList.add(op);
        }

        int period = Integer.parseInt("val_period");
        float fee = 0;
        switch (period){
            case 12:
                assert servicePackage != null;
                fee = (float) servicePackage.getFee12();
                break;
            case 24:
                assert servicePackage != null;
                fee = (float) servicePackage.getFee24();
                break;
            case 36:
                assert servicePackage != null;
                fee = (float) servicePackage.getFee36();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + period);
        }

        float tot_value = fee;

        for (int i = 0; i < opList.size(); i++){
            tot_value += opList.get(i).getMonthly_fee();
        }

        LocalDate date = LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("EE, d MMM yyyy hh:mm a"));
        LocalTime time = LocalTime.now();

        Order order = new Order(user,date,time,tot_value);

        user.addOrder(order);

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
        Subscription subscription;
        if(!isValid) {
            userService.incrementsFailedPayments(user);
            try {
                userService.updateProfile(user);
            } catch (UpdateProfileException e) {
                e.printStackTrace();
            }
        }else{
            subscription = new Subscription(period,fee,servicePackage);
            subscription.setOrder(order);
        }

        if(user.getNumFailedPayments() == 3 && user.isInsolvent() == UserStatus.SOLVENT){
            user.setInsolvent(UserStatus.INSOLVENT);
            userService.setNumFailedPayments(user);
            try {
                userService.updateProfile(user);
            } catch (UpdateProfileException e) {
                e.printStackTrace();
            }
        }

        List<ServicePackage> packages = null;
        try{

            packages = spService.findAll();

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Not possible to get services");
        }

        List<FixedInternet> fiList = null;
        try {
            fiList = fiService.findAll();
        } catch (ServicePackageException e) {
            e.printStackTrace();
        }

        List<MobileInternet> miList = null;
        try {
            miList = miService.findAll();
        } catch (ServicePackageException e) {
            e.printStackTrace();
        }

        List<MobilePhone> mpList = null;
        try {
            mpList = mpService.findAll();
        } catch (ServicePackageException e) {
            e.printStackTrace();
        }

        List<OptionalProduct> productList = null;
        try {
            productList = opService.findAll();
        } catch (OrderException e) {
            e.printStackTrace();
        }


        ctx.setVariable("user",user);
        ctx.setVariable("packages", packages);
        ctx.setVariable("FixedInternetList", fiList);
        ctx.setVariable("MobileInternetList", miList);
        ctx.setVariable("MobilePhoneList", mpList);
        ctx.setVariable("OptionalProductList", productList);
        ctx.setVariable("payment",isValid);
        templateEngine.process(path, ctx, response.getWriter());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}

