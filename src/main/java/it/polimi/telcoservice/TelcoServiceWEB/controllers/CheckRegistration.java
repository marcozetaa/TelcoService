package it.polimi.telcoservice.TelcoServiceWEB.controllers;

import it.polimi.telcoservice.TelcoServiceEJB.entities.User;
import it.polimi.telcoservice.TelcoServiceEJB.exceptions.CredentialException;
import it.polimi.telcoservice.TelcoServiceEJB.services.UserService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.persistence.PersistenceException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CheckRegistration", value = "/CheckRegistration")
public class CheckRegistration extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.questionnaire.services/UserService")
    private UserService usrService;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
// obtain and escape params
        String usrn = null;
        String pwd = null;
        String email = null;
        String adm = "N";

        try {

            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            email = StringEscapeUtils.escapeJava(request.getParameter("email"));

            if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty() || email == null || email.isEmpty()) { throw new Exception("Missing or empty credential value");}

        }catch (Exception e) {
            // for debugging only e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        User user;
        String path = null;
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        
        try{
            user = usrService.checkAlreadyRegistered(email, usrn);

            if( user != null) {
                ctx.setVariable("errorMsg", "Username or email already in use");
                path = "/Registration.html";
            }
        } catch (CredentialException e) {
            e.printStackTrace();
        }

        try {
            usrService.registrateUser(usrn, pwd, email);
            path = "/index.html";

        }catch (CredentialException e){
            e.printStackTrace();
        }

        templateEngine.process(path, ctx, response.getWriter());
    }

    @Override
    public void destroy() {
    }
}
