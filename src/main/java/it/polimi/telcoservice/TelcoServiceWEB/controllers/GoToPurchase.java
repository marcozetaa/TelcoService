package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.services.OptionalProductService;
import it.polimi.telcoservice.TelcoServiceEJB.services.ServicePackageService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "GoToPurchase", value = "/Purchase")
public class GoToPurchase extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService spService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;

    public GoToPurchase(){ super(); }

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // get and check params
        Integer package_id;
        try {
            package_id = Integer.parseInt(request.getParameter("packageid"));
        } catch (NumberFormatException | NullPointerException e) {
            // only for debugging e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
            return;
        }

        ServicePackage sel_package = null;
        List<OptionalProduct> o_products = null;
        try {
            sel_package = spService.findByID(package_id);
            o_products = opService.findAll();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get Package offer");
        }

        String path = "WEB-INF/Purchase.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("package_id", sel_package.getid());
        ctx.setVariable("package", sel_package);
        ctx.setVariable("products", o_products);

        templateEngine.process(path, ctx, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    public void destroy() {
    }
}
