package hub.http;

import org.apache.activemq.web.MessageServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "ActiveMqServlet", urlPatterns = {"/message/*"}, loadOnStartup = 1)
public class ActiveMqServlet extends MessageServlet {
}
