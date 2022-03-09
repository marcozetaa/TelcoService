package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
import it.polimi.telcoservice.TelcoServiceEJB.services.FixedInternetService;
import it.polimi.telcoservice.TelcoServiceEJB.services.MobileInternetService;
import it.polimi.telcoservice.TelcoServiceEJB.services.MobilePhoneService;
import it.polimi.telcoservice.TelcoServiceEJB.services.ServicePackageService;
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
import java.util.*;

@WebServlet(name = "WorkArea", value = "/WorkArea")
public class WorkArea extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService pService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/FixedInternetService")
    private FixedInternetService fiService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobileInternetService")
    private MobileInternetService miService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobilePhoneService")
    private MobilePhoneService mpService;


    public WorkArea() { super(); }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "WEB-INF/EmployeeHome.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());


        List<ServicePackage> packages = null;
        try {
            packages = pService.findAll();
        } catch (ServicePackageException e) {
            e.printStackTrace();
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

        ctx.setVariable("packages", packages);
        ctx.setVariable("FixedInternetList", fiList);
        ctx.setVariable("MobileInternetList", miList);
        ctx.setVariable("MobilePhoneList", mpList);

        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
