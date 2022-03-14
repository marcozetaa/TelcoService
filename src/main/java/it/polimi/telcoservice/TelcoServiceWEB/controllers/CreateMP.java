package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
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
import java.util.List;

@WebServlet(name = "CreateMP", value = "/CreateMP")
public class CreateMP extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService pService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobilePhoneService")
    private MobilePhoneService mpService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/FixedInternetService")
    private FixedInternetService fiService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/MobileInternetService")
    private MobileInternetService miService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;

    public CreateMP() { super(); }

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

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "WEB-INF/EmployeeHome.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());


        Integer num_min = null;
        Integer num_sms = null;
        Integer extra_fee_min = null;
        Integer extra_fee_sms = null;

        try{
            num_min = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_min")));
            num_sms = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_sms")));
            extra_fee_min = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_min")));
            extra_fee_sms = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_sms")));
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Missing Mobile Phone detail");
        }

        try{
            mpService.createMP(num_min,num_sms,extra_fee_min,extra_fee_sms);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create Mobile Phone");
        }

        List<MobilePhone> mpList = null;
        try {
            mpList = mpService.findAll();
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

        List<ServicePackage> packages = null;
        try {
            packages = pService.findAll();
        } catch (ServicePackageException e) {
            e.printStackTrace();
        }

        List<OptionalProduct> opList = null;
        try {
            opList = opService.findAll();
        } catch (OrderException e) {
            e.printStackTrace();
        }

        ctx.setVariable("packages", packages);
        ctx.setVariable("FixedInternetList", fiList);
        ctx.setVariable("MobileInternetList", miList);
        ctx.setVariable("MobilePhoneList", mpList);
        ctx.setVariable("OptionalProductList", opList);
        templateEngine.process(path, ctx, response.getWriter());
    }
}
