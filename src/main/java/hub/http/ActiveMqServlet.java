package hub.http;

import org.apache.activemq.web.MessageServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ActiveMqServlet", urlPatterns = {"/message/*"}, loadOnStartup = 1)
public class ActiveMqServlet extends MessageServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        config.getServletContext().setInitParameter("org.apache.activemq.brokerURL", "vm://localhost");
        super.init(config);
    }

}