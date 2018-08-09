package hub.http;

import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import org.apache.activemq.web.MessageServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ActiveMqServlet", urlPatterns = {"/message/*"}, loadOnStartup = 1)
public class ActiveMqServlet extends MessageServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        config.getServletContext().setInitParameter("org.apache.activemq.brokerURL", "vm://localhost");
        super.init(config);
    }

    @Override
    protected void doMessages(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doMessages(request, response);
    }

}