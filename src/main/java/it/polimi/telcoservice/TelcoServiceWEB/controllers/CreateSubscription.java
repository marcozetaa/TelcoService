package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Subscription;
import it.polimi.telcoservice.TelcoServiceEJB.services.SubscriptionService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "GoToConfirm", value = "/GoToConfirm")
public class CreateSubscription extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/SubscriptionService")
    private SubscriptionService subService;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get and parse all parameters from request
        boolean isBadRequest = false;
        Integer packageId = null;
        String product_name = null;
        Integer fee = null;
        String val_period = null;
        Date startDate = null;
        try {
            fee = Integer.parseInt(request.getParameter("val_period"));
            packageId = Integer.parseInt((request.getParameter("packageid")));
            product_name = StringEscapeUtils.escapeJava(request.getParameter("productId"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            startDate = (Date) sdf.parse(request.getParameter("date"));
            isBadRequest = product_name.isEmpty() || getMeYesterday().after(startDate);
        } catch (NumberFormatException | NullPointerException | ParseException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        // Create Subscription in DB
        Subscription subscription = null;
        try {
            subService.createSubscription(12, fee, packageId); //TODO: how do get validity period and mohtly fee together
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create mission");
            return;
        }

        String path = getServletContext().getContextPath() + "/Home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("start_date", startDate);
        ctx.setVariable("subscription", subscription);

        templateEngine.process(path, ctx, response.getWriter());
    }
}
