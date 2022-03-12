package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.Employee;
import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.services.EmployeeService;
import it.polimi.telcoservice.TelcoServiceEJB.services.UserService;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.CredentialException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "CheckLogin", value = "/CheckLogin")
public class CheckLogin extends HttpServlet{
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/UserService")
    private UserService usrService;
    @EJB(name = "it.polimi.telcoservice.TelcoServiceEJB.services/EmployeeService")
    private EmployeeService empService;

    private TemplateEngine templateEngine;

    public CheckLogin() { super(); }

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
        String path = "/index.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

        templateEngine.process(path, ctx, response.getWriter());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //obtain and escape params
        String usrn = null;
        String pwd = null;
        String is_emp = null;
        try{
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            is_emp = StringEscapeUtils.escapeJava(request.getParameter("emp"));
            if(usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()){
                throw new Exception("Missing or empty credential value");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
        }

        User user = null;
        Employee employee = null;

        if(is_emp == null){
            try {
                user = usrService.checkCredentials(usrn,pwd);
            } catch (CredentialException | NonUniqueResultException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
                return;
            }
        } else{
            try {
                Integer badge;
                if (isNumeric(usrn))
                    badge = Integer.parseInt(usrn);
                else{
                    throw new Exception("Username not a numeric badge");
                }
                employee = empService.checkCredentials(badge,pwd);
            } catch (CredentialException | NonUniqueResultException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If the user exists, add info to the session and go to home page, otherwise
        // show login page with error message

        String path = "";
        if( user == null && employee == null ){
            path = getServletContext().getContextPath()+"index.html";;
        }
        else {
            if( employee == null) {
                path= getServletContext().getContextPath()+"/Home";
                request.getSession().setAttribute("user", user);
            }
            if( user == null) {
                path   = getServletContext().getContextPath()+"/WorkArea";
                request.getSession().setAttribute("employee", employee);
            }
        }
        response.sendRedirect(path);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public void destroy(){
    }
}
