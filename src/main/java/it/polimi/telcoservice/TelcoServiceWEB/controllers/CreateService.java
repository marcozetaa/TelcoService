package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.AlertException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.SalesReportException;
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

@WebServlet(name = "CreateService", value = "/CreateService")
public class CreateService extends HttpServlet {
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
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/EmployeeService")
    private EmployeeService emService;

    public CreateService() { super(); }

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

        int production = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("typeOfProd")));

        switch (production){

            case 0:
                createServicePackage(request, response);
                break;
            case 1:
                createMI(request, response);
                break;
            case 2:
                createMP(request, response);
                break;
            case 3:
                createFI(request, response);
                break;
            case 4:
                createOP(request, response);
                break;
        }

        List<ServicePackage> packages = null;
        List<FixedInternet> fiList = null;
        List<MobileInternet> miList = null;
        List<MobilePhone> mpList = null;
        List<OptionalProduct> opList = null;
        List<Alert> alerts = null;
        List<SalesReport> salesReports = null;

        try {
            packages = pService.findAll();
            fiList = fiService.findAll();
            miList = miService.findAll();
            mpList = mpService.findAll();
            opList = opService.findAll();
            alerts = emService.findAllInsolvent();
            salesReports = emService.findAllSalesReport();
        } catch (ServicePackageException | OrderException | AlertException | SalesReportException e) {
            e.printStackTrace();
        }

        ctx.setVariable("packages", packages);
        ctx.setVariable("FixedInternetList", fiList);
        ctx.setVariable("MobileInternetList", miList);
        ctx.setVariable("MobilePhoneList", mpList);
        ctx.setVariable("OptionalProductList", opList);
        ctx.setVariable("AlertList", alerts);
        ctx.setVariable("SalesList", salesReports);

        templateEngine.process(path, ctx, response.getWriter());
    }

    private void createServicePackage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        int fixed_phone = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fixed_phone")));
        int fee12 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee12")));
        int fee24 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee24")));
        int fee36 = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee36")));
        int mobile_phone = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("mobile_phone")));
        int fixed_internet = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fixed_internet")));
        int mobile_internet = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("mobile_internet")));
        String[] o_products = request.getParameterValues("optional_products");

        FixedPhoneStatus status;
        if (fixed_phone == 0)
            status = FixedPhoneStatus.EXCLUDED;
        else
            status = FixedPhoneStatus.INCLUDED;

        try {
            // query db to authenticate for user
            pService.createPackage(name, status, fee12, fee24, fee36, mobile_phone, mobile_internet, fixed_internet, o_products);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create package");
        }

    }

    private void createMI(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int num_gb = 0;
        int extra_fee_gb = 0;

        try {
            num_gb = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_gb")));
            extra_fee_gb = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_gb")));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Mobile Internet detail");
        }

        try {
            miService.createMI(num_gb, extra_fee_gb);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create Mobile Internet");
        }
    }

    private void createMP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer num_min = null;
        Integer num_sms = null;
        Integer extra_fee_min = null;
        Integer extra_fee_sms = null;

        try {
            num_min = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_min")));
            num_sms = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_sms")));
            extra_fee_min = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_min")));
            extra_fee_sms = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_sms")));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Mobile Phone detail");
        }

        try {
            mpService.createMP(num_min, num_sms, extra_fee_min, extra_fee_sms);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create Mobile Phone");
        }
    }

    private void createFI(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer num_gb = null;
        Integer extra_fee_gb = null;

        try{
            num_gb = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("num_gb")));
            extra_fee_gb = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fee_extra_gb")));
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Missing Fixed Internet detail");
        }

        try{
            fiService.createFI(num_gb,extra_fee_gb);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create Fixed Internet");
        }

    }

    private void createOP(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String prod_name = null;
        Integer monthly_fee = null;

        try{
            prod_name = StringEscapeUtils.escapeJava(request.getParameter("prod_name"));
            monthly_fee = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("monthly_fee")));
        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Missing Product detail");
        }

        try{
            opService.createOptionalProduct(prod_name, monthly_fee);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_CREATED, "Could not create Optional Product");
        }
    }

}
