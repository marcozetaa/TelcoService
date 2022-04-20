package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.*;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
import it.polimi.telcoservice.TelcoServiceEJB.services.OptionalProductService;
import it.polimi.telcoservice.TelcoServiceEJB.services.OrderService;
import it.polimi.telcoservice.TelcoServiceEJB.services.ServicePackageService;
import it.polimi.telcoservice.TelcoServiceEJB.services.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "GoToPurchase", value = "/Purchase")
public class GoToPurchase extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService spService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OrderService")
    private OrderService oService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OptionalProductService")
    private OptionalProductService opService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/UserService")
    private UserService userService;

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

        String path = "WEB-INF/Purchase.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        // get and check params
        int package_id;
        int order_id;

        boolean is_reorder = false;
        boolean redirect = false;

        User user;
        HttpSession session = request.getSession();

        // get all parameter names
        Set<String> paramNames = request.getParameterMap().keySet();

        ServicePackage sel_package = null;
        Order reorder = null;
        List<OptionalProduct> o_products = null;

        try {
            user = (User) session.getAttribute("user");
        } catch (NumberFormatException | NullPointerException e) {
            // only for debugging e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
            return;
        }

        if (paramNames.contains("order_id")) {
            is_reorder = true;
            order_id = Integer.parseInt(request.getParameter("order_id"));
            try {
                reorder = oService.findByID(order_id);
                sel_package = spService.findByName(reorder.getName_package());
            } catch (OrderException | ServicePackageException e) {
                e.printStackTrace();
            }
        } else if (paramNames.contains("package_id")){
            package_id = Integer.parseInt(request.getParameter("package_id"));
            try {
                sel_package = spService.findByID(package_id);
            } catch (ServicePackageException e) {
                e.printStackTrace();
            }
            if(paramNames.contains("redirect"))
                redirect = true;
        }

        try {
            o_products = opService.findAll();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get Package offer");
        }

        session.setAttribute("user",user);
        ctx.setVariable("package_id", sel_package.getid());
        ctx.setVariable("package", sel_package);
        ctx.setVariable("products", o_products);
        ctx.setVariable("is_reorder",is_reorder);
        ctx.setVariable("redirect",redirect);
        ctx.setVariable("order", reorder);

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
