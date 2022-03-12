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
import java.util.List;

@WebServlet(name = "CreatePackage", value = "/CreatePackage")
public class CreatePackage extends HttpServlet {
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

    public CreatePackage() { super(); }

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "WEB-INF/EmployeeHome.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        // obtain and escape params

        String name;
        Integer fixed_phone;
        Integer fee12;
        Integer fee24;
        Integer fee36;
        Integer mobile_phone;
        Integer fixed_internet;
        Integer mobile_internet;

        try {

            name = StringEscapeUtils.escapeJava(request.getParameter("name"));
            fixed_phone = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fixed_phone")));
            fee12 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee12")));
            fee24 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee24")));
            fee36 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee36")));
            mobile_phone = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("mobile_phone")));
            fixed_internet = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fixed_internet")));
            mobile_internet = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("mobile_internet")));

/*
            if (name == null || fixed_phone == null || mobile_phone == null || fixed_internet == null || mobile_internet == null ||
                    name.isEmpty() || fixed_phone.isEmpty() ||  mobile_phone.isEmpty() || fixed_internet.isEmpty() || mobile_internet.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }
*/
        }catch (Exception e) {
            // for debugging only e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Package Details");
            return;
        }

        FixedPhoneStatus status;
        if(fixed_phone == 0)
            status = FixedPhoneStatus.EXCLUDED;
        else
            status = FixedPhoneStatus.INCLUDED;

        List<ServicePackage> packages;
        try {
            // query db to authenticate for user
            pService.createPackage(name,status,fee12,fee24,fee36,mobile_phone,mobile_internet,fixed_internet);
            packages =  pService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create package");
            return;
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
}
