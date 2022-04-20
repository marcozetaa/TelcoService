package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
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

@WebServlet(name = "GoToPackageDetails", value = "/PackageDetails")
public class GoToPackageDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService pService;

    public GoToPackageDetails(){
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = "WEB-INF/PackageDetails.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        // get and check params
        int packageId;
        try {
            packageId = Integer.parseInt(request.getParameter("package_id"));
        } catch (NumberFormatException | NullPointerException e) {
            // only for debugging e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
            return;
        }

        ServicePackage service_package = null;
        try{
            service_package = pService.findByID(packageId);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Not possible to get services");
        }


        ctx.setVariable("sp", service_package);

        templateEngine.process(path, ctx, response.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
    }
}
