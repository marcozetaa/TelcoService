package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Order;
import it.polimi.telcoservice.TelcoServiceEJB.entities.ServicePackage;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.OrderException;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.ServicePackageException;
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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "GoToHomepage", value = "/Home")
public class GoToHomepage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/UserService")
    private UserService userService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/ServicePackageService")
    private ServicePackageService pService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/OrderService")
    private OrderService oService;

    public GoToHomepage(){
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

        String path = "WEB-INF/Home.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        String query = request.getQueryString();

        if(query != null) {

            int user_id;
            boolean payment;
            User user;
            List<Order> orderList = null;

            Map<String, String> params = new HashMap<>();
            String[] strParams = query.split("&");
            for (String param : strParams) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                params.put(name, value);
            }


            if(params.containsKey("user")) {
                user_id = Integer.parseInt(params.get("user"));
                user = userService.getUser(user_id);
                try {
                    orderList = oService.findByUserNoCache(user_id);
                } catch (OrderException e) {
                    e.printStackTrace();
                }

                if(params.containsKey("payments")) {
                    payment = Boolean.parseBoolean(params.get("payment"));
                }

                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                ctx.setVariable("my_orders",orderList);
            }

        }

        List<ServicePackage> packages = null;

        try{
            packages = pService.findAll();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Not possible to get services");
        }

        ctx.setVariable("packages", packages);
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
